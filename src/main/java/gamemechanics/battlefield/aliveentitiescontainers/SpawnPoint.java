package gamemechanics.battlefield.aliveentitiescontainers;

import gamemechanics.battlefield.Tile;
import gamemechanics.components.properties.PropertiesFactory;
import gamemechanics.components.properties.PropertyCategories;
import gamemechanics.globals.DigitsPairIndices;
import gamemechanics.globals.Directions;
import gamemechanics.interfaces.AliveEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SpawnPoint {
    private final Tile center;
    private final Integer spawnAreaSide;

    private final Squad squad;

    public SpawnPoint(Tile center, Integer spawnAreaSide, Squad squad) {
        this.center = center;
        this.spawnAreaSide = spawnAreaSide;
        this.squad = squad;
    }

    public Squad getSquad() {
        return squad;
    }

    public Boolean emplaceSquad() {
        List<Tile> rowCenters = new ArrayList<>();
        Integer halfSide = (spawnAreaSide - 1) / 2;
        // get all spawn area row centers
        Tile upperRow = center.getAdjacent(Directions.UP);
        while (center.getCoordinate(DigitsPairIndices.X_COORD_INDEX)
                - upperRow.getCoordinate(DigitsPairIndices.X_COORD_INDEX) < halfSide) {
            if (upperRow == null) {
                break;
            }
            rowCenters.add(upperRow);
            upperRow = upperRow.getAdjacent(Directions.UP);
        }
        rowCenters.add(center);
        Tile lowerRow = center.getAdjacent(Directions.DOWN);
        while (lowerRow.getCoordinate(DigitsPairIndices.X_COORD_INDEX)
                - center.getCoordinate(DigitsPairIndices.X_COORD_INDEX) < halfSide) {
            if (lowerRow == null) {
                break;
            }
            rowCenters.add(lowerRow);
            lowerRow = lowerRow.getAdjacent(Directions.DOWN);
        }
        List<Tile> spawnArea = new ArrayList<>();
        // via each row center get a spawn area row
        for (Tile tile : rowCenters) {
            Tile leftCol = tile.getAdjacent(Directions.LEFT);
            while (tile.getCoordinate(DigitsPairIndices.Y_COORD_INDEX)
                    - leftCol.getCoordinate(DigitsPairIndices.Y_COORD_INDEX) < halfSide) {
                if (leftCol == null) {
                    break;
                }
                spawnArea.add(leftCol);
                leftCol = leftCol.getAdjacent(Directions.LEFT);
            }
            spawnArea.add(tile);
            Tile rightCol = tile.getAdjacent(Directions.RIGHT);
            while (rightCol.getCoordinate(DigitsPairIndices.Y_COORD_INDEX)
                    - tile.getCoordinate(DigitsPairIndices.Y_COORD_INDEX) < halfSide) {
                if (rightCol == null) {
                    break;
                }
                spawnArea.add(rightCol);
                rightCol = rightCol.getAdjacent(Directions.RIGHT);
            }
        }
        // after obtaining a spawn area tileset, emplace the squad
        for (Integer i = 0; i < squad.getSquadSize(); ++i) {
            AliveEntity member = squad.getMember(i);
            if (member != null) {
                Random random = new Random(System.currentTimeMillis());
                Integer randomTileIndex = random.nextInt(spawnArea.size());
                while (spawnArea.get(randomTileIndex).isOccupied()
                        || spawnArea.get(randomTileIndex).getIsPassable()) {
                    randomTileIndex = random.nextInt(spawnArea.size());
                }
                // placing battler on the tile and give him its coordinates
                spawnArea.get(randomTileIndex).occupy(member);
                if (!member.hasProperty(PropertyCategories.PC_COORDINATES)) {
                    member.addProperty(PropertyCategories.PC_COORDINATES,
                            PropertiesFactory.makeProperty(PropertyCategories.PC_COORDINATES));
                }
                member.setProperty(PropertyCategories.PC_COORDINATES,
                        spawnArea.get(randomTileIndex).getCoordinates());
            }
        }
        return true;
    }
}
