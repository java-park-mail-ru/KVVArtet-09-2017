package gamemechanics.battlefield;

import gamemechanics.components.properties.PropertiesFactory;
import gamemechanics.components.properties.Property;
import gamemechanics.components.properties.PropertyCategories;
import gamemechanics.globals.DigitsPairIndices;
import gamemechanics.globals.Directions;
import gamemechanics.interfaces.AliveEntity;
import gamemechanics.interfaces.MapNode;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Tile implements MapNode {
    private static final AtomicInteger instanceCounter = new AtomicInteger(0);
    private final Integer tileID = instanceCounter.getAndIncrement();

    private Boolean isPassable;
    private AliveEntity inhabitant = null;
    private final List<MapNode> adjacentTiles = new ArrayList<>(Directions.DIRECTIONS_COUNT);
    private final List<Integer> coordinates = new ArrayList<>(DigitsPairIndices.PAIR_SIZE);

    public Tile(Integer coordX, Integer coordY) {
        this(false, coordX, coordY);
    }

    public Tile(Boolean isPassable, Integer coordX, Integer coordY) {
        this.isPassable = isPassable;
        this.coordinates.set(DigitsPairIndices.ROW_COORD_INDEX, coordX);
        this.coordinates.set(DigitsPairIndices.COL_COORD_INDEX, coordY);
    }

    @Override
    public Integer getInstancesCount() {
        return instanceCounter.get();
    }

    @Override
    public Integer getID() {
        return tileID;
    }

    @Override
    public Boolean getIsPassable() {
        return isPassable;
    }

    @Override
    public void setIsPassable(Boolean isPassable) {
        this.isPassable = isPassable;
    }

    @Override
    public AliveEntity getInhabitant() {
        return inhabitant;
    }

    @Override
    public Boolean isOccupied() {
        return inhabitant == null;
    }

    @Override
    public Boolean occupy(AliveEntity stander) {
        Boolean isSuccessful = true;
        if (this.inhabitant == null) {
            this.inhabitant = stander;
            if (stander.hasProperty(PropertyCategories.PC_COORDINATES)) {
                stander.setProperty(PropertyCategories.PC_COORDINATES, coordinates);
            } else {
                Property coordinatesProperty = PropertiesFactory.makeProperty(PropertyCategories.PC_COORDINATES);
                coordinatesProperty.setPropertyList(coordinates);
                stander.addProperty(PropertyCategories.PC_COORDINATES, coordinatesProperty);
            }
        } else {
            isSuccessful = false;
        }
        return isSuccessful;
    }

    @Override
    public Boolean free() {
        Boolean isSuccessful = true;
        if (inhabitant != null) {
            inhabitant = null;
        } else {
            isSuccessful = false;
        }
        return isSuccessful;
    }

    @Override
    public MapNode getAdjacent(Integer direction) {
        if (direction < Directions.UP || direction >= adjacentTiles.size()) {
            return null;
        }
        return adjacentTiles.get(direction);
    }

    @Override
    public List<MapNode> getAdjacentTiles() {
        return adjacentTiles;
    }

    @Override
    public Boolean isAdjacentTo(MapNode tile) {
        return adjacentTiles.contains(tile);
    }

    @Override
    public void setAdjacentTiles(List<MapNode> adjacencyList) {
        Integer adjacencyIndex = Directions.UP;
        for (MapNode tile : adjacencyList) {
            adjacentTiles.set(adjacencyIndex++, tile);
            if (adjacencyIndex == Directions.DIRECTIONS_COUNT) {
                break;
            }
        }
    }

    public List<Integer> getCoordinates() {
        return coordinates;
    }

    public Integer getCoordinate(Integer coordinateIndex) {
        if (coordinateIndex < 0 || coordinateIndex >= coordinates.size()) {
            return Integer.MIN_VALUE;
        }
        return coordinates.get(coordinateIndex);
    }

    @Override
    public Integer getH(@NotNull MapNode goal) {
        return manhattanDistance(coordinates, goal.getCoordinates());
    }

    private Integer manhattanDistance(List<Integer> fromCoords, List<Integer> toCoords) {
        return Math.abs(fromCoords.get(DigitsPairIndices.ROW_COORD_INDEX)
                + toCoords.get(DigitsPairIndices.ROW_COORD_INDEX))
                + Math.abs(fromCoords.get(DigitsPairIndices.COL_COORD_INDEX)
                + toCoords.get(DigitsPairIndices.COL_COORD_INDEX));
    }
}
