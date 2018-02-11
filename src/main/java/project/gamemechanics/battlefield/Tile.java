package project.gamemechanics.battlefield;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.jetbrains.annotations.Nullable;
import project.gamemechanics.components.properties.PropertiesFactory;
import project.gamemechanics.components.properties.Property;
import project.gamemechanics.components.properties.PropertyCategories;
import project.gamemechanics.globals.DigitsPairIndices;
import project.gamemechanics.globals.Directions;
import project.gamemechanics.interfaces.AliveEntity;
import project.gamemechanics.interfaces.MapNode;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
        this.coordinates.add(DigitsPairIndices.ROW_COORD_INDEX, coordX);
        this.coordinates.add(DigitsPairIndices.COL_COORD_INDEX, coordY);
    }

    @Override
    @JsonIgnore
    public @NotNull Integer getInstancesCount() {
        return INSTANCE_COUNTER.get();
    }

    @Override
    @JsonIgnore
    public @NotNull Integer getID() {
        return tileID;
    }

    @Override
    @JsonIgnore
    public @NotNull Boolean getIsPassable() {
        return isPassable;
    }

    @Override
    public void setIsPassable(@NotNull Boolean isPassable) {
        this.isPassable = isPassable;
    }

    @Override
    @JsonIgnore
    public @Nullable AliveEntity getInhabitant() {
        return inhabitant;
    }

    @Override
    @JsonIgnore
    public @NotNull Boolean isOccupied() {
        return inhabitant != null;
    }

    @Override
    public @NotNull Boolean occupy(@NotNull AliveEntity stander) {
        Boolean isSuccessful = true;
        if (inhabitant == null && isPassable) {
            inhabitant = stander;
            if (stander.hasProperty(PropertyCategories.PC_COORDINATES)) {
                stander.setProperty(PropertyCategories.PC_COORDINATES, coordinates);
            } else {
                final Property coordinatesProperty =
                        PropertiesFactory.makeProperty(PropertyCategories.PC_COORDINATES);
                Objects.requireNonNull(coordinatesProperty).setPropertyList(coordinates);
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
    public @Nullable MapNode getAdjacent(@NotNull Integer direction) {
        if (direction < Directions.UP || direction >= adjacentTiles.size()) {
            return null;
        }
        return adjacentTiles.get(direction);
    }

    @Override
    @JsonIgnore
    public @NotNull List<MapNode> getAdjacentTiles() {
        return adjacentTiles;
    }

    @Override
    public @NotNull Boolean isAdjacentTo(@NotNull MapNode tile) {
        return adjacentTiles.contains(tile);
    }

    @Override
    public void setAdjacentTiles(@NotNull List<MapNode> adjacencyList) {
        Integer adjacencyIndex = Directions.UP;
        for (MapNode tile : adjacencyList) {
            adjacentTiles.add(adjacencyIndex++, tile);
            if (adjacencyIndex == Directions.DIRECTIONS_COUNT) {
                break;
            }
        }
    }

    @Override
    public @NotNull List<Integer> getCoordinates() {
        return coordinates;
    }

    @Override
    public @NotNull Integer getCoordinate(@NotNull Integer coordinateIndex) {
        if (coordinateIndex < 0 || coordinateIndex >= coordinates.size()) {
            return Integer.MIN_VALUE;
        }
        return coordinates.get(coordinateIndex);
    }

    @Override
    public @NotNull Integer getH(@NotNull MapNode goal) {
        return manhattanDistance(coordinates, goal.getCoordinates());
    }

    private @NotNull Integer manhattanDistance(@NotNull List<Integer> fromCoords,
                                               @NotNull List<Integer> toCoords) {
        return Math.abs(fromCoords.get(DigitsPairIndices.ROW_COORD_INDEX)
                - toCoords.get(DigitsPairIndices.ROW_COORD_INDEX))
                + Math.abs(fromCoords.get(DigitsPairIndices.COL_COORD_INDEX)
                - toCoords.get(DigitsPairIndices.COL_COORD_INDEX));
    }
}
