package project.gamemechanics.battlefield.map.helpers;

import project.gamemechanics.interfaces.AliveEntity;
import project.gamemechanics.interfaces.MapNode;

import javax.validation.constraints.NotNull;
import java.util.List;

public class BattleMapRoute implements Route {
    private static final int SOURCE_TILE_INDEX = 0;

    private final List<MapNode> route;

    public BattleMapRoute(@NotNull List<MapNode> route) {
        this.route = route;
    }

    @Override
    public Integer getLength() {
        return route.size() - 1;
    }

    @Override
    public Integer getTravelCost() {
        // actually that's not useful info at the moment
        // as every tile has similar travel cost = 1,
        // so travel cost will always be equal to the route length
        return route.size() - 1;
    }

    @Override
    public void walkThrough(@NotNull Integer distance) {
        final AliveEntity pedestrian = route.get(SOURCE_TILE_INDEX).getInhabitant();
        if (pedestrian == null) {
            return;
        }
        route.get(SOURCE_TILE_INDEX).free();
        Integer tileToMoveAt = distance;
        if (tileToMoveAt > route.size()) {
            tileToMoveAt = route.size() - 1;
        }
        if (!route.get(tileToMoveAt).occupy(pedestrian)) {
            route.get(SOURCE_TILE_INDEX).occupy(pedestrian);
        }
    }

    @Override
    public void walkThrough() {
        walkThrough(route.size() - 1);
    }

    @Override
    public List<Integer> getStartCoordinates() {
        return route.get(0).getCoordinates();
    }

    @Override
    public List<Integer> getGoalCoordinates(@NotNull Integer distance) {
        return route.get(distance).getCoordinates();
    }

    @Override
    public List<Integer> getGoalCoordinates() {
        return getGoalCoordinates(route.size() - 1);
    }

    public List<MapNode> getRoute() {
        return route;
    }
}
