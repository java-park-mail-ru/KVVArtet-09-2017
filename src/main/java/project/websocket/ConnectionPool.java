package project.websocket;

import project.gamemechanics.smartcontroller.SmartController;

import java.util.ArrayDeque;
import java.util.Deque;

public class ConnectionPool implements ObjectivePool<SmartController>{

    private Deque<SmartController> connectionPool = new ArrayDeque<>();
    private Integer startCapacity = 8;

    public ConnectionPool() {
         initializeNewElements();
    }

    private void initializeNewElements() {
        for(int i = 0; i < startCapacity; i++ ) {
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
        Integer capacityMultiplier = 2;
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
