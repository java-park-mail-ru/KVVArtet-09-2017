package project.websocket;

import project.gamemechanics.smartcontroller.SmartController;

import javax.validation.constraints.NotNull;
import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;


public class ConnectionPool implements ObjectivePool.SmartControllersPool {
    private static final int CAPACITY_MULTIPLIER = 2;
    private static final int START_CAPACITY = 8;

    private Deque<SmartController> connectionPool = new ConcurrentLinkedDeque<>();
    private Integer capacity = START_CAPACITY;

    public ConnectionPool() {
         initializeNewElements();
    }

    private void initializeNewElements() {
        Integer fixedSize = connectionPool.size();
        for(int i = 0; i < capacity - fixedSize; ++i ) {
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

    private void addMore() {
        initializeNewElements();
        capacity *= CAPACITY_MULTIPLIER;
    }

    @Override
    public void addElement(@NotNull SmartController smartController) {
        connectionPool.add(smartController);
    }

    @Override
    public Boolean isEmpty() {
        return connectionPool.isEmpty();
    }
}
