package gamemechanics.battlefield.map.helpers;

import gamemechanics.interfaces.AliveEntity;
import gamemechanics.interfaces.MapNode;

import java.util.List;

public class BattleMapRoute implements Route {
    private static final int SOURCE_TILE_INDEX = 0;

    private final List<MapNode> route;

    public BattleMapRoute(List<MapNode> route) {
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
    public void walkThrough(Integer distance) {
        AliveEntity pedestrian = route.get(SOURCE_TILE_INDEX).getInhabitant();
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
        walkThrough(route.size() -1);
    }
}
