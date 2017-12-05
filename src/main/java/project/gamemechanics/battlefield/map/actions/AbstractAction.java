package project.gamemechanics.battlefield.map.actions;

import project.gamemechanics.interfaces.Action;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractAction implements Action {
    private final AtomicInteger instanceCounter = new AtomicInteger(0);
    private final Integer actionID = instanceCounter.getAndIncrement();

    @Override
    public Integer getInstancesCount() {
        return instanceCounter.get();
    }

    @Override
    public Integer getID() {
        return actionID;
    }
}
