package project.gamemechanics.battlefield.aliveentitiescontainers;

import project.gamemechanics.globals.DigitsPairIndices;
import project.gamemechanics.globals.Directions;
import project.gamemechanics.interfaces.AliveEntity;
import project.gamemechanics.interfaces.MapNode;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SuppressWarnings("ConstantConditions")
public class SpawnPoint {
    private final MapNode center;
    private final Integer spawnAreaSide;

    private final Squad squad;

    public SpawnPoint(@NotNull MapNode center, @NotNull Integer spawnAreaSide, @NotNull Squad squad) {
        this.center = center;
        this.spawnAreaSide = spawnAreaSide;
        this.squad = squad;
    }

    public @NotNull Squad getSquad() {
        return squad;
    }

    @SuppressWarnings("OverlyComplexMethod")
    public void emplaceSquad() {
        final List<MapNode> rowCenters = new ArrayList<>();
        final Integer halfSide = (spawnAreaSide - 1) / 2;
        // get all spawn area row centers
        MapNode upperRow = center.getAdjacent(Directions.UP);
        while (center.getCoordinate(DigitsPairIndices.ROW_COORD_INDEX)
                - upperRow.getCoordinate(DigitsPairIndices.ROW_COORD_INDEX) < halfSide) {
            if (upperRow == null) {
                break;
            }
            rowCenters.add(upperRow);
            upperRow = upperRow.getAdjacent(Directions.UP);
        }
        rowCenters.add(center);
        MapNode lowerRow = center.getAdjacent(Directions.DOWN);
        while (lowerRow.getCoordinate(DigitsPairIndices.ROW_COORD_INDEX)
                - center.getCoordinate(DigitsPairIndices.ROW_COORD_INDEX) < halfSide) {
            if (lowerRow == null) {
                break;
            }
            rowCenters.add(lowerRow);
            lowerRow = lowerRow.getAdjacent(Directions.DOWN);
        }
        final List<MapNode> spawnArea = new ArrayList<>();
        // via each row center get a spawn area row
        for (MapNode tile : rowCenters) {
            MapNode leftCol = tile.getAdjacent(Directions.LEFT);
            while (tile.getCoordinate(DigitsPairIndices.COL_COORD_INDEX)
                    - leftCol.getCoordinate(DigitsPairIndices.COL_COORD_INDEX) < halfSide) {
                if (leftCol == null) {
                    break;
                }
                spawnArea.add(leftCol);
                leftCol = leftCol.getAdjacent(Directions.LEFT);
            }
            spawnArea.add(tile);
            MapNode rightCol = tile.getAdjacent(Directions.RIGHT);
            while (rightCol.getCoordinate(DigitsPairIndices.COL_COORD_INDEX)
                    - tile.getCoordinate(DigitsPairIndices.COL_COORD_INDEX) < halfSide) {
                if (rightCol == null) {
                    break;
                }
                spawnArea.add(rightCol);
                rightCol = rightCol.getAdjacent(Directions.RIGHT);
            }
        }
        // after obtaining a spawn area tileset, emplace the squad
        for (Integer i = 0; i < squad.getSquadSize(); ++i) {
            final AliveEntity member = squad.getMember(i);
            if (member != null) {
                final Random random = new Random(System.currentTimeMillis());
                Integer randomTileIndex = random.nextInt(spawnArea.size());
                while (spawnArea.get(randomTileIndex).isOccupied()
                        || spawnArea.get(randomTileIndex).getIsPassable()) {
                    randomTileIndex = random.nextInt(spawnArea.size());
                }
                // placing battler on the tile and give him its coordinates
                spawnArea.get(randomTileIndex).occupy(member);
            }
        }
    }
}
