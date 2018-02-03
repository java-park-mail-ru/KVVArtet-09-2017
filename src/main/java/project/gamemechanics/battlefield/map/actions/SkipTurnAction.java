package project.gamemechanics.battlefield.map.actions;

import org.jetbrains.annotations.Nullable;
import project.gamemechanics.battlefield.actionresults.ActionResult;
import project.gamemechanics.battlefield.actionresults.BattleActionResult;
import project.gamemechanics.battlefield.actionresults.events.EventsFactory;
import project.gamemechanics.interfaces.MapNode;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;

@SuppressWarnings("RedundantSuppression")
public class SkipTurnAction extends AbstractAction {
    private final MapNode sender;

    public SkipTurnAction(@NotNull MapNode sender) {
        this.sender = sender;
    }

    @Override
    public @NotNull Boolean isSkip() {
        return true;
    }

    @Override
    public @NotNull MapNode getSender() {
        return sender;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public @Nullable MapNode getTarget() {
        return null;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public @NotNull ActionResult execute() {
        final ActionResult result = new BattleActionResult(getID(), sender, null, null, new ArrayList<>());
        result.addEvent(EventsFactory.makeEndTurnEvent());
        return result;
    }
}
