package project.gamemechanics.battlefield.map.helpers;

import org.jetbrains.annotations.Nullable;
import project.gamemechanics.battlefield.map.BattleMap;
import project.gamemechanics.globals.Constants;
import project.gamemechanics.globals.DigitsPairIndices;
import project.gamemechanics.interfaces.MapNode;

import javax.validation.constraints.NotNull;
import java.util.*;

// pathfinding helper class
// pathfinding is based on A* algorithm
@SuppressWarnings("RedundantSuppression")
public final class Pathfinder implements PathfindingAlgorithm {
    private static final Integer DEFAULT_OPEN_LIST_CAPACITY = 16;

    private final BattleMap map;

    public Pathfinder(@NotNull BattleMap map) {
        this.map = map;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public @Nullable Route getPath(@NotNull List<Integer> fromPos, @NotNull List<Integer> toPos) {
        final MapNode fromTile = map.getTile(fromPos.get(DigitsPairIndices.ROW_COORD_INDEX),
                fromPos.get(DigitsPairIndices.COL_COORD_INDEX));
        final MapNode toTile = map.getTile(toPos.get(DigitsPairIndices.ROW_COORD_INDEX),
                toPos.get(DigitsPairIndices.COL_COORD_INDEX));
        return getPath(fromTile, toTile);
    }

    @SuppressWarnings({"ConstantConditions", "OverlyComplexMethod"})
    private @Nullable Route getPath(@Nullable MapNode start, @Nullable MapNode goal) {
        if (goal == null || start == null || !goal.getIsPassable()) {
            return null;
        }

        final Map<MapNode, Integer> fScore = new HashMap<>();
        final PriorityQueue<MapNode> open = new PriorityQueue<>(DEFAULT_OPEN_LIST_CAPACITY,
                Comparator.comparingInt(fScore::get));

        final Map<MapNode, Integer> gScore = new HashMap<>();
        gScore.put(start, 0);
        fScore.put(start, start.getH(goal));
        open.offer(start);

        final Set<MapNode> closed = new HashSet<>();
        // map containing parent tiles for each tile in the route
        final Map<MapNode, MapNode> routeMap = new HashMap<>();
        final List<MapNode> route = new ArrayList<>();
        while (!open.isEmpty()) {
            MapNode current = open.poll();

            if (current.equals(goal)) {
                while (current != null) {
                    route.add(0, current);
                    current = routeMap.get(current);
                }
                return new BattleMapRoute(route);
            }

            closed.add(current);

            for (MapNode adjacent : current.getAdjacentTiles()) {
                if (adjacent == null || closed.contains(adjacent)) {
                    continue;
                }
                if (!adjacent.getIsPassable()) {
                    closed.add(adjacent);
                    continue;
                }

                final Integer tentativeG = gScore.get(current)
                        + getG(current, adjacent, gScore.get(current));
                final Boolean containsOpen = open.contains(adjacent);
                if (!containsOpen || tentativeG < gScore.getOrDefault(adjacent, 0)) {
                    if (!gScore.containsKey(adjacent)) {
                        gScore.put(adjacent, tentativeG);
                    } else {
                        gScore.replace(adjacent, tentativeG);
                    }
                    if (!fScore.containsKey(adjacent)) {
                        fScore.put(adjacent, tentativeG + adjacent.getH(goal));
                    } else {
                        fScore.replace(adjacent, tentativeG + adjacent.getH(goal));
                    }
                    if (containsOpen) {
                        open.remove(adjacent);
                    }
                    open.offer(adjacent);
                    routeMap.put(adjacent, current);
                }
            }
        }

        return null;
    }

    private @NotNull Integer getG(@NotNull MapNode from, @NotNull MapNode to, @NotNull Integer fromCost) {
        if (!from.isAdjacentTo(to)) {
            return 0;
        }
        return Constants.DEFAULT_MOVEMENT_COST + fromCost;
    }
}