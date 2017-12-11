package gamemechanics.battlefield;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    private static final AtomicInteger INSTANCE_COUNTER = new AtomicInteger(0);
    private final Integer tileID = INSTANCE_COUNTER.getAndIncrement();

    private Boolean isPassable;
    private AliveEntity inhabitant = null;
    private final List<MapNode> adjacentTiles = new ArrayList<>(Directions.DIRECTIONS_COUNT);
    private final List<Integer> coordinates = new ArrayList<>(DigitsPairIndices.PAIR_SIZE);

    public Tile(@NotNull Integer coordX, @NotNull Integer coordY) {
        this(false, coordX, coordY);
    }

    public Tile(@NotNull Boolean isPassable, @NotNull Integer coordX, @NotNull Integer coordY) {
        this.isPassable = isPassable;
        this.coordinates.set(DigitsPairIndices.ROW_COORD_INDEX, coordX);
        this.coordinates.set(DigitsPairIndices.COL_COORD_INDEX, coordY);
    }

    @Override
    @JsonIgnore
    public Integer getInstancesCount() {
        return INSTANCE_COUNTER.get();
    }

    @Override
    @JsonProperty("id")
    public Integer getID() {
        return tileID;
    }

    @Override
    public Boolean getIsPassable() {
        return isPassable;
    }

    @Override
    public void setIsPassable(@NotNull Boolean isPassable) {
        this.isPassable = isPassable;
    }

    @Override
    @JsonIgnore
    public AliveEntity getInhabitant() {
        return inhabitant;
    }

    @Override
    public Boolean isOccupied() {
        return inhabitant == null;
    }

    @Override
    public Boolean occupy(@NotNull AliveEntity stander) {
        Boolean isSuccessful = true;
        if (this.inhabitant == null) {
            this.inhabitant = stander;
            if (stander.hasProperty(PropertyCategories.PC_COORDINATES)) {
                stander.setProperty(PropertyCategories.PC_COORDINATES, coordinates);
            } else {
                final Property coordinatesProperty = PropertiesFactory.makeProperty(PropertyCategories.PC_COORDINATES);
                coordinatesProperty.setPropertyList(coordinates);
                stander.addProperty(PropertyCategories.PC_COORDINATES, coordinatesProperty);
            }
        } else {
            isSuccessful = false;
        }
        return isSuccessful;
    }

    @Override
    public void free() {
        if (inhabitant != null) {
            inhabitant = null;
        }
    }

    @Override
    public MapNode getAdjacent(@NotNull Integer direction) {
        if (direction < Directions.UP || direction >= adjacentTiles.size()) {
            return null;
        }
        return adjacentTiles.get(direction);
    }

    @Override
    @JsonIgnore
    public List<MapNode> getAdjacentTiles() {
        return adjacentTiles;
    }

    @Override
    public Boolean isAdjacentTo(@NotNull MapNode tile) {
        return adjacentTiles.contains(tile);
    }

    @Override
    public void setAdjacentTiles(@NotNull List<MapNode> adjacencyList) {
        Integer adjacencyIndex = Directions.UP;
        for (MapNode tile : adjacencyList) {
            adjacentTiles.set(adjacencyIndex++, tile);
            if (adjacencyIndex == Directions.DIRECTIONS_COUNT) {
                break;
            }
        }
    }

    @Override
    public List<Integer> getCoordinates() {
        return coordinates;
    }

    @Override
    public Integer getCoordinate(@NotNull Integer coordinateIndex) {
        if (coordinateIndex < 0 || coordinateIndex >= coordinates.size()) {
            return Integer.MIN_VALUE;
        }
        return coordinates.get(coordinateIndex);
    }

    @Override
    public Integer getH(@NotNull MapNode goal) {
        return manhattanDistance(coordinates, goal.getCoordinates());
    }

    private Integer manhattanDistance(@NotNull List<Integer> fromCoords, @NotNull List<Integer> toCoords) {
        return Math.abs(fromCoords.get(DigitsPairIndices.ROW_COORD_INDEX)
                + toCoords.get(DigitsPairIndices.ROW_COORD_INDEX))
                + Math.abs(fromCoords.get(DigitsPairIndices.COL_COORD_INDEX)
                + toCoords.get(DigitsPairIndices.COL_COORD_INDEX));
    }
}
