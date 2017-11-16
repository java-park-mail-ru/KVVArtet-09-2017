package gamemechanics.battlefield.map.actions;

import com.sun.istack.internal.NotNull;
import gamemechanics.battlefield.actionresults.ActionResult;
import gamemechanics.battlefield.actionresults.BattleActionResult;
import gamemechanics.battlefield.actionresults.events.EventsFactory;
import gamemechanics.battlefield.map.helpers.PathfindingAlgorithm;
import gamemechanics.battlefield.map.helpers.Route;
import gamemechanics.components.properties.PropertyCategories;
import gamemechanics.interfaces.MapNode;

import java.util.ArrayList;

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
    public MapNode getSender() {
        return sender;
    }

    @Override
    public MapNode getTarget() {
        return target;
    }

    @Override
    public Boolean isMovement() {
        return true;
    }

    @Override
    public ActionResult execute() {
        ActionResult result = new BattleActionResult(getID(), sender, target, null, new ArrayList<>());
        if (!sender.isOccupied()) {
            result.addEvent(EventsFactory.makeRollbackEvent());
            return result;
        }
        Route route = pathfinder.getPath(sender.getCoordinates(), target.getCoordinates());
        if (route.getLength() <= sender.getInhabitant().getProperty(PropertyCategories.PC_SPEED)) {
            route.walkThrough();
        } else {
            route.walkThrough(sender.getInhabitant().getProperty(PropertyCategories.PC_SPEED));
        }
        result.addEvent(EventsFactory.makeMovementEvent(route));
        return result;
    }
}
