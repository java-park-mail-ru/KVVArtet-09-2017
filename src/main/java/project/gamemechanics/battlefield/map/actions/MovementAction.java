package project.gamemechanics.battlefield.map.actions;

import project.gamemechanics.battlefield.actionresults.ActionResult;
import project.gamemechanics.battlefield.actionresults.BattleActionResult;
import project.gamemechanics.battlefield.actionresults.events.EventsFactory;
import project.gamemechanics.battlefield.map.helpers.PathfindingAlgorithm;
import project.gamemechanics.battlefield.map.helpers.Route;
import project.gamemechanics.components.properties.PropertyCategories;
import project.gamemechanics.interfaces.MapNode;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Objects;

public class MovementAction extends AbstractAction {
    private final MapNode sender;
    private final MapNode target;
    private final PathfindingAlgorithm pathfinder;

    public MovementAction(@NotNull MapNode sender, @NotNull MapNode target,
                          @NotNull PathfindingAlgorithm pathfinder) {
        this.sender = sender;
        this.target = target;
        this.pathfinder = pathfinder;
    }

    @Override
    public @NotNull MapNode getSender() {
        return sender;
    }

    @Override
    public @NotNull MapNode getTarget() {
        return target;
    }

    @Override
    public @NotNull Boolean isMovement() {
        return true;
    }

    @Override
    public @NotNull ActionResult execute() {
        final ActionResult result = new BattleActionResult(getID(), sender, target, null, new ArrayList<>());
        if (!sender.isOccupied()) {
            result.addEvent(EventsFactory.makeRollbackEvent());
            return result;
        }
        final Route route = pathfinder.getPath(sender.getCoordinates(), target.getCoordinates());
        if (Objects.requireNonNull(route).getLength()
                <= Objects.requireNonNull(sender.getInhabitant())
                .getProperty(PropertyCategories.PC_SPEED)) {
            route.walkThrough();
        } else {
            route.walkThrough(sender.getInhabitant().getProperty(PropertyCategories.PC_SPEED));
        }
        result.addEvent(EventsFactory.makeMovementEvent(route));
        return result;
    }
}
