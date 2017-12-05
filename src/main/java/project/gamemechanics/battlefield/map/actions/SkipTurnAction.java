package project.gamemechanics.battlefield.map.actions;

import project.gamemechanics.battlefield.actionresults.ActionResult;
import project.gamemechanics.battlefield.actionresults.BattleActionResult;
import project.gamemechanics.battlefield.actionresults.events.EventsFactory;
import project.gamemechanics.interfaces.MapNode;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;

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

    @Override
    public ActionResult execute() {
        final ActionResult result = new BattleActionResult(getID(), sender, null, null, new ArrayList<>());
        result.addEvent(EventsFactory.makeEndTurnEvent());
        return result;
    }
}
