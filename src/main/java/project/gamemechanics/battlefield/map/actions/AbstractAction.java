package project.gamemechanics.battlefield.map.actions;

import project.gamemechanics.interfaces.Action;

import javax.validation.constraints.NotNull;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractAction implements Action {
    private final AtomicInteger instanceCounter = new AtomicInteger(0);
    private final Integer actionID = instanceCounter.getAndIncrement();

    @Override
    public @NotNull Integer getInstancesCount() {
        return instanceCounter.get();
    }

    @Override
    public @NotNull Integer getID() {
        return actionID;
    }
}
