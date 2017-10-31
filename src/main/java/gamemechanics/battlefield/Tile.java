package gamemechanics.battlefield;

import gamemechanics.globals.DigitsPairIndices;
import gamemechanics.globals.Directions;
import gamemechanics.interfaces.AliveEntity;
import gamemechanics.interfaces.Countable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Tile implements Countable {
    private static final AtomicInteger instanceCounter = new AtomicInteger(0);
    private final Integer tileID = instanceCounter.getAndIncrement();

    private Boolean isPassable;
    private AliveEntity inhabitant = null;
    private final List<Tile> adjacentTiles = new ArrayList<>(Directions.DIRECTIONS_COUNT);
    private final List<Integer> coordinates = new ArrayList<>(DigitsPairIndices.PAIR_SIZE);

    public Tile(Integer coordX, Integer coordY) {
        this(false, coordX, coordY);
    }

    public Tile(Boolean isPassable, Integer coordX, Integer coordY) {
        this.isPassable = isPassable;
        this.coordinates.set(DigitsPairIndices.X_COORD_INDEX, coordX);
        this.coordinates.set(DigitsPairIndices.Y_COORD_INDEX, coordY);
    }

    @Override
    public Integer getInstancesCount() {
        return instanceCounter.get();
    }

    @Override
    public Integer getID() {
        return tileID;
    }

    public Boolean getIsPassable() {
        return isPassable;
    }

    public void setIsPassable(Boolean isPassable) {
        this.isPassable = isPassable;
    }

    public AliveEntity getInhabitant() {
        return inhabitant;
    }

    public Boolean isOccupied() {
        return inhabitant == null;
    }

    public Boolean occupy(AliveEntity stander) {
        Boolean isSuccessful = true;
        if (this.inhabitant == null) {
            this.inhabitant = stander;
        } else {
            isSuccessful = false;
        }
        return isSuccessful;
    }

    public Boolean free() {
        Boolean isSuccessful = true;
        if (inhabitant != null) {
            inhabitant = null;
        } else {
            isSuccessful = false;
        }
        return isSuccessful;
    }

    public Tile getAdjacent(Integer direction) {
        if (direction < Directions.UP || direction >= adjacentTiles.size()) {
            return null;
        }
        return adjacentTiles.get(direction);
    }

    public void setAdjacentTiles(List<Tile> adjacencyList) {
        Integer adjacencyIndex = Directions.UP;
        for (Tile tile : adjacencyList) {
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
}
