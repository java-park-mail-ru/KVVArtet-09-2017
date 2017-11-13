package gamemechanics.battlefield.map.actions;

import gamemechanics.interfaces.MapNode;

import javax.validation.constraints.NotNull;

public class SkipTurnAction extends AbstractAction {
    private final MapNode sender;

    public SkipTurnAction(@NotNull MapNode sender) {
        this.sender = sender;
    }

    @Override
    public Boolean isSkip() {
        return true;
    }

    @Override
    public MapNode getSender() {
        return sender;
    }

    @Override
    public MapNode getTarget() {
        return null;
    }

    public Boolean execute() {
        return true;
    }
}
