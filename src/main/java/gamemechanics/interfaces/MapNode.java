package gamemechanics.interfaces;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface MapNode extends Countable {
    Boolean getIsPassable();
    void setIsPassable(Boolean isPassable);

    AliveEntity getInhabitant();
    Boolean isOccupied();
    Boolean occupy(AliveEntity stander);
    Boolean free();

    MapNode getAdjacent(Integer direction);
    List<MapNode> getAdjacentTiles();
    Boolean isAdjacentTo(MapNode node);
    void setAdjacentTiles(List<MapNode> node);

    List<Integer> getCoordinates();
    Integer getCoordinate(Integer coordinateIndex);

    Integer getH(@NotNull MapNode node);
}
