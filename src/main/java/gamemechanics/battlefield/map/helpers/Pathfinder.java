package gamemechanics.battlefield.map.helpers;

import gamemechanics.battlefield.map.BattleMap;
import gamemechanics.globals.Constants;
import gamemechanics.globals.DigitsPairIndices;
import gamemechanics.interfaces.MapNode;

import javax.validation.constraints.NotNull;
import java.util.*;

// pathfinding helper class
// pathfinding is based on A* algorithm
public final class Pathfinder {
    private static final Integer DEFAULT_OPEN_LIST_CAPACITY = 16;

    private final BattleMap map;

    public Pathfinder(BattleMap map) {
        this.map = map;
    }

    public List<MapNode> getPath(@NotNull MapNode start, @NotNull MapNode goal) {

        final Set<MapNode> closed = new HashSet<>();

        final Map<MapNode, Integer> gScore = new HashMap<>();
        final Map<MapNode, Integer> fScore = new HashMap<>();

        // map containing parent tiles for each tile in the route
        final Map<MapNode, MapNode> routeMap = new HashMap<>();

        final List<MapNode> route = new LinkedList<>();

        final PriorityQueue<MapNode> open = new PriorityQueue<>(DEFAULT_OPEN_LIST_CAPACITY,
                Comparator.comparingInt(fScore::get));

        gScore.put(start, 0);
        fScore.put(start, start.getH(goal));

        while (!open.isEmpty()) {
            MapNode current = open.poll();

            if (current.equals(goal)) {
                while (current != null) {
                    route.add(0, current);
                    current = routeMap.get(current);
                }
                return route;
            }

            closed.add(current);

            for (MapNode adjacent : current.getAdjacentTiles()) {
                if (closed.contains(adjacent)) {
                    continue;
                }
                if (!adjacent.getIsPassable()) {
                    closed.add(adjacent);
                    continue;
                }
                Integer tentativeG = gScore.get(current)
                        + getG(current, adjacent, gScore.get(current));
                Boolean contains = open.contains(adjacent);
                if (!contains || tentativeG < gScore.get(adjacent)) {
                    gScore.put(adjacent, tentativeG);
                    fScore.put(adjacent, tentativeG + adjacent.getH(goal));
                    if (contains) {
                        open.remove(adjacent);
                    }
                    open.offer(adjacent);
                    routeMap.put(adjacent, current);
                }
            }
        }

        return null;
    }

    public List<MapNode> getPath(List<Integer> fromPos, List<Integer> toPos) {
        MapNode fromTile = map.getTile(fromPos.get(DigitsPairIndices.ROW_COORD_INDEX),
                fromPos.get(DigitsPairIndices.COL_COORD_INDEX));
        MapNode toTile = map.getTile(toPos.get(DigitsPairIndices.ROW_COORD_INDEX),
                toPos.get(DigitsPairIndices.COL_COORD_INDEX));
        return getPath(fromTile, toTile);
    }

    private Integer getG(@NotNull MapNode from, @NotNull MapNode to, Integer fromCost) {
        if (!from.isAdjacentTo(to)) {
            return Integer.MIN_VALUE;
        }
        return Constants.DEFAULT_MOVEMENT_COST + fromCost;
    }
}