package websocket;

import gamemechanics.smartcontroller.SmartController;

import java.util.PriorityQueue;
import java.util.Queue;

public class ConnectionPool implements ObjectivePool<SmartController> {

    private Queue<SmartController> connectionPool = new PriorityQueue<>();
    private Integer startCapacity = 8;
    private Integer capacityMultiplier = 2;

    public ConnectionPool() {
        initializeNewElements();
    }

    private void initializeNewElements() {
        for (int i = 0; i < startCapacity; i++) {
            connectionPool.add(new SmartController());
        }
    }

    @Override
    public SmartController getElement() {
        if (isEmpty()) {
            addMore();
        }
        return connectionPool.remove();
    }

    @Override
    public void addMore() {
        initializeNewElements();
        startCapacity *= capacityMultiplier;
    }

    @Override
    public void addElement(SmartController smartController) {
        connectionPool.add(smartController);
    }

    @Override
    public Boolean isEmpty() {
        return connectionPool.isEmpty();
    }
}
