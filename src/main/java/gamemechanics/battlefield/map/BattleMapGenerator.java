package gamemechanics.battlefield.map;

import gamemechanics.battlefield.Tile;
import gamemechanics.globals.DigitsPairIndices;
import gamemechanics.globals.Directions;
import gamemechanics.interfaces.MapNode;

import java.util.*;

public final class BattleMapGenerator {
    private static final Map<Integer, List<Integer>> movementsMap = initDirectionsMap();

    private BattleMapGenerator() {}

    public static List<List<MapNode>> generateBattleMap(Integer width, Integer height, Integer passableTilesCount) {
        final List<Integer> mapSize = new ArrayList<>(DigitsPairIndices.PAIR_SIZE);
        mapSize.set(DigitsPairIndices.ROW_COORD_INDEX, height);
        mapSize.set(DigitsPairIndices.COL_COORD_INDEX, width);
        List<List<MapNode>> map = new ArrayList<>(height);
        for (Integer i = 0; i < map.size(); ++i) {
            map.set(i, new ArrayList<>(width));
            for (Integer j = 0; j < map.get(i).size(); ++j) {
                map.get(i).set(j, new Tile(i, j));
            }
        }
        List<Integer> coords = new ArrayList<>(DigitsPairIndices.PAIR_SIZE);
        Random random = new Random(System.currentTimeMillis());
        coords.set(DigitsPairIndices.ROW_COORD_INDEX, random.nextInt() % height);
        coords.set(DigitsPairIndices.COL_COORD_INDEX, random.nextInt() % width);
        map.get(coords.get(DigitsPairIndices.ROW_COORD_INDEX)).get(DigitsPairIndices.COL_COORD_INDEX).setIsPassable(true);
        Integer tilesMadePassable = 1;
        while (tilesMadePassable < passableTilesCount) {
            Integer direction = Directions.DIRECTIONS_COUNT;
            while (!isDirectionValid(coords, mapSize, direction)) {
                direction = random.nextInt() % Directions.DIRECTIONS_COUNT;
            }
            List<Integer> movement = movementsMap.get(direction);
            for (Integer i = 0; i < coords.size(); ++i) {
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

    private static Boolean isDirectionValid(List<Integer> coords, List<Integer> mapSize, Integer direction) {
        switch (direction) {
            case Directions.UP:
                return coords.get(DigitsPairIndices.COL_COORD_INDEX) > 0;
            case Directions.RIGHT:
                return (coords.get(DigitsPairIndices.ROW_COORD_INDEX) + 1)
                        < mapSize.get(DigitsPairIndices.ROW_COORD_INDEX);
            case Directions.DOWN:
                return (coords.get(DigitsPairIndices.COL_COORD_INDEX) + 1)
                        < mapSize.get(DigitsPairIndices.COL_COORD_INDEX);
            case Directions.LEFT:
                return coords.get(DigitsPairIndices.ROW_COORD_INDEX) > 0;
        }
        return false;
    }

    private static Map<Integer, List<Integer>> initDirectionsMap() {
        Map<Integer, List<Integer>> directionsMap = new HashMap<>();

        List<Integer> upMovement = new ArrayList<>(DigitsPairIndices.PAIR_SIZE);
        upMovement.set(DigitsPairIndices.ROW_COORD_INDEX, 0);
        upMovement.set(DigitsPairIndices.COL_COORD_INDEX, -1);
        directionsMap.put(Directions.UP, upMovement);

        List<Integer> rightMovement = new ArrayList<>(DigitsPairIndices.PAIR_SIZE);
        rightMovement.set(DigitsPairIndices.ROW_COORD_INDEX, 1);
        rightMovement.set(DigitsPairIndices.COL_COORD_INDEX, 0);
        directionsMap.put(Directions.RIGHT, rightMovement);

        List<Integer> downMovement = new ArrayList<>(DigitsPairIndices.PAIR_SIZE);
        downMovement.set(DigitsPairIndices.ROW_COORD_INDEX, 0);
        downMovement.set(DigitsPairIndices.COL_COORD_INDEX, 1);
        directionsMap.put(Directions.DOWN, downMovement);

        List<Integer> leftMovement = new ArrayList<>(DigitsPairIndices.PAIR_SIZE);
        leftMovement.set(DigitsPairIndices.ROW_COORD_INDEX, -1);
        leftMovement.set(DigitsPairIndices.COL_COORD_INDEX, 0);
        directionsMap.put(Directions.LEFT, leftMovement);

        return directionsMap;
    }

    private static void setAdjacencies(List<List<MapNode>> map) {
        for (Integer i = 0; i < map.size(); ++i) {
            for (Integer j = 0; j < map.get(i).size(); ++j) {
                List<MapNode> adjacencies = new ArrayList<>(Directions.DIRECTIONS_COUNT);
                for (Integer k = 0; k < Directions.DIRECTIONS_COUNT; ++k) {
                    List<Integer> coordinates = new ArrayList<>(DigitsPairIndices.PAIR_SIZE);
                    coordinates.set(DigitsPairIndices.ROW_COORD_INDEX,
                            i + movementsMap.get(k).get(DigitsPairIndices.ROW_COORD_INDEX));
                    coordinates.set(DigitsPairIndices.COL_COORD_INDEX,
                            j + movementsMap.get(k).get(DigitsPairIndices.COL_COORD_INDEX));
                    if (coordinates.get(DigitsPairIndices.ROW_COORD_INDEX) < 0
                            || coordinates.get(DigitsPairIndices.ROW_COORD_INDEX) >= map.size()) {
                        adjacencies.set(k, null);
                    } else {
                        if (coordinates.get(DigitsPairIndices.COL_COORD_INDEX) < 0
                                || coordinates.get(DigitsPairIndices.COL_COORD_INDEX)
                                >= map.get(coordinates.get(DigitsPairIndices.ROW_COORD_INDEX)).size()) {
                            adjacencies.set(k, null);
                        } else {
                            adjacencies.set(k, map.get(coordinates.get(DigitsPairIndices.ROW_COORD_INDEX))
                                    .get(coordinates.get(DigitsPairIndices.COL_COORD_INDEX)));
                        }
                    }
                }
                map.get(i).get(j).setAdjacentTiles(adjacencies);
            }
        }
    }
}
