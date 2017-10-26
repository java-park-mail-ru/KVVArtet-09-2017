package gamemechanics.battlefield;

import gamemechanics.globals.AdjacencyIndices;
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
    private final List<Tile> adjacentTiles = new ArrayList<>(AdjacencyIndices.AX_SIZE);

    public Tile() {
        this(false);
    }

    public Tile(Boolean isPassable) {
        this.isPassable = isPassable;
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
        if (direction < AdjacencyIndices.AX_UP || direction >= adjacentTiles.size()) {
            return null;
        }
        return adjacentTiles.get(direction);
    }

    public void setAdjacentTiles(List<Tile> adjacencyList) {
        Integer adjacencyIndex = AdjacencyIndices.AX_UP;
        for (Tile tile : adjacencyList) {
            adjacentTiles.set(adjacencyIndex++, tile);
            if (adjacencyIndex == AdjacencyIndices.AX_SIZE) {
                break;
            }
        }
    }
}
