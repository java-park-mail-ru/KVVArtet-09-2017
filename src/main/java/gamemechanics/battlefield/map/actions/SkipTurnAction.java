package gamemechanics.battlefield.map.actions;

import gamemechanics.battlefield.actionresults.ActionResult;
import gamemechanics.battlefield.actionresults.BattleActionResult;
import gamemechanics.battlefield.actionresults.events.EventsFactory;
import gamemechanics.interfaces.MapNode;

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
        ActionResult result = new BattleActionResult(getID(), sender, null, null, new ArrayList<>());
        result.addEvent(EventsFactory.makeEndTurnEvent());
        return result;
    }
}
