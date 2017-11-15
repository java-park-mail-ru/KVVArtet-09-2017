package gamemechanics.battlefield.map.actions;

import com.sun.istack.internal.NotNull;
import gamemechanics.battlefield.map.helpers.PathfindingAlgorithm;
import gamemechanics.battlefield.map.helpers.Route;
import gamemechanics.components.properties.PropertyCategories;
import gamemechanics.interfaces.MapNode;

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
    public Boolean execute() {
        if (!sender.isOccupied()) {
            return false;
        }
        Route route = pathfinder.getPath(sender.getCoordinates(), target.getCoordinates());
        if (route.getLength() <= sender.getInhabitant().getProperty(PropertyCategories.PC_SPEED)) {
            route.walkThrough();
        } else {
            route.walkThrough(sender.getInhabitant().getProperty(PropertyCategories.PC_SPEED));
        }
        return true;
    }
}
