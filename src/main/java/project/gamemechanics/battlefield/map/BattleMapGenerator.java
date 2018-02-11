package project.gamemechanics.battlefield.map;

import project.gamemechanics.battlefield.Tile;
import project.gamemechanics.globals.DigitsPairIndices;
import project.gamemechanics.globals.Directions;
import project.gamemechanics.interfaces.MapNode;

import javax.validation.constraints.NotNull;
import java.util.*;

public final class BattleMapGenerator {
    private static final Map<Integer, List<Integer>> MOVEMENTS_MAP = initDirectionsMap();

    private BattleMapGenerator() {
    }

    public static @NotNull List<List<MapNode>> generateBattleMap(@NotNull Integer width,
                                                                 @NotNull Integer height,
                                                                 @NotNull Integer passableTilesCount) {
        final List<Integer> mapSize = new ArrayList<>(DigitsPairIndices.PAIR_SIZE);
        mapSize.add(DigitsPairIndices.ROW_COORD_INDEX, height);
        mapSize.add(DigitsPairIndices.COL_COORD_INDEX, width);
        final List<List<MapNode>> map = new ArrayList<>();
        for (Integer i = 0; i < height; ++i) {
            map.add(i, new ArrayList<>());
            for (Integer j = 0; j < width; ++j) {
                map.get(i).add(new Tile(i, j));
            }
        }
        final List<Integer> coords = new ArrayList<>();
        for (Integer i = 0; i < DigitsPairIndices.PAIR_SIZE; ++i) {
            coords.add(0);
        }
        final Random random = new Random(System.currentTimeMillis());
        coords.set(DigitsPairIndices.ROW_COORD_INDEX, random.nextInt(height));
        coords.set(DigitsPairIndices.COL_COORD_INDEX, random.nextInt(width));
        map.get(coords.get(DigitsPairIndices.ROW_COORD_INDEX))
                .get(DigitsPairIndices.COL_COORD_INDEX).setIsPassable(true);
        Integer tilesMadePassable = 1;

        while (tilesMadePassable < passableTilesCount) {
            Integer direction = Directions.DIRECTIONS_COUNT;
            while (!isDirectionValid(coords, mapSize, direction)) {
                direction = random.nextInt(Directions.DIRECTIONS_COUNT);
            }
            final List<Integer> movement = MOVEMENTS_MAP.get(direction);
            for (Integer i = 0; i < DigitsPairIndices.PAIR_SIZE; ++i) {
                coords.set(i, coords.get(i) + movement.get(i));
            }
            if (!map.get(coords.get(DigitsPairIndices.ROW_COORD_INDEX))
                    .get(coords.get(DigitsPairIndices.COL_COORD_INDEX)).getIsPassable()) {
                map.get(coords.get(DigitsPairIndices.ROW_COORD_INDEX))
                        .get(coords.get(DigitsPairIndices.COL_COORD_INDEX)).setIsPassable(true);
                ++tilesMadePassable;
            }
        }
        setAdjacencies(map);
        return map;
    }

    private static @NotNull Boolean isDirectionValid(@NotNull List<Integer> coords,
                                                     @NotNull List<Integer> mapSize,
                                                     @NotNull Integer direction) {
        switch (direction) {
            case Directions.UP:
                return coords.get(DigitsPairIndices.ROW_COORD_INDEX) > 0;
            case Directions.RIGHT:
                return (coords.get(DigitsPairIndices.COL_COORD_INDEX) + 1)
                        < mapSize.get(DigitsPairIndices.COL_COORD_INDEX);
            case Directions.DOWN:
                return (coords.get(DigitsPairIndices.ROW_COORD_INDEX) + 1)
                        < mapSize.get(DigitsPairIndices.ROW_COORD_INDEX);
            case Directions.LEFT:
                return coords.get(DigitsPairIndices.COL_COORD_INDEX) > 0;
            default: break;
        }
        return false;
    }

    private static @NotNull Map<Integer, List<Integer>> initDirectionsMap() {
        final List<Integer> upMovement = new ArrayList<>(DigitsPairIndices.PAIR_SIZE);
        upMovement.add(DigitsPairIndices.ROW_COORD_INDEX, -1);
        upMovement.add(DigitsPairIndices.COL_COORD_INDEX, 0);
        final Map<Integer, List<Integer>> directionsMap = new HashMap<>();
        directionsMap.put(Directions.UP, upMovement);

        final List<Integer> rightMovement = new ArrayList<>(DigitsPairIndices.PAIR_SIZE);
        rightMovement.add(DigitsPairIndices.ROW_COORD_INDEX, 0);
        rightMovement.add(DigitsPairIndices.COL_COORD_INDEX, 1);
        directionsMap.put(Directions.RIGHT, rightMovement);

        final List<Integer> downMovement = new ArrayList<>(DigitsPairIndices.PAIR_SIZE);
        downMovement.add(DigitsPairIndices.ROW_COORD_INDEX, 1);
        downMovement.add(DigitsPairIndices.COL_COORD_INDEX, 0);
        directionsMap.put(Directions.DOWN, downMovement);

        final List<Integer> leftMovement = new ArrayList<>(DigitsPairIndices.PAIR_SIZE);
        leftMovement.add(DigitsPairIndices.ROW_COORD_INDEX, 0);
        leftMovement.add(DigitsPairIndices.COL_COORD_INDEX, -1);
        directionsMap.put(Directions.LEFT, leftMovement);

        return directionsMap;
    }

    private static void setAdjacencies(@NotNull List<List<MapNode>> map) {
        for (Integer i = 0; i < map.size(); ++i) {
            for (Integer j = 0; j < map.get(i).size(); ++j) {
                final List<MapNode> adjacencies = new ArrayList<>(Directions.DIRECTIONS_COUNT);
                for (Integer k = 0; k < Directions.DIRECTIONS_COUNT; ++k) {
                    final List<Integer> coordinates = new ArrayList<>(DigitsPairIndices.PAIR_SIZE);
                    coordinates.add(DigitsPairIndices.ROW_COORD_INDEX,
                            i + MOVEMENTS_MAP.get(k).get(DigitsPairIndices.ROW_COORD_INDEX));
                    coordinates.add(DigitsPairIndices.COL_COORD_INDEX,
                            j + MOVEMENTS_MAP.get(k).get(DigitsPairIndices.COL_COORD_INDEX));
                    if (coordinates.get(DigitsPairIndices.ROW_COORD_INDEX) < 0
                            || coordinates.get(DigitsPairIndices.ROW_COORD_INDEX) >= map.size()) {
                        adjacencies.add(k, null);
                    } else {
                        if (coordinates.get(DigitsPairIndices.COL_COORD_INDEX) < 0
                                || coordinates.get(DigitsPairIndices.COL_COORD_INDEX)
                                >= map.get(coordinates.get(DigitsPairIndices.ROW_COORD_INDEX)).size()) {
                            adjacencies.add(k, null);
                        } else {
                            adjacencies.add(k, map.get(coordinates.get(DigitsPairIndices.ROW_COORD_INDEX))
                                    .get(coordinates.get(DigitsPairIndices.COL_COORD_INDEX)));
                        }
                    }
                }
                map.get(i).get(j).setAdjacentTiles(adjacencies);
            }
        }
    }
}
