package project.gamemechanics.interfaces;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.jetbrains.annotations.Nullable;
import project.gamemechanics.battlefield.Tile;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * interface for tile-based battle maps' nodes.
 *
 * @see Countable
 * @see project.gamemechanics.battlefield.map.BattleMap
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(Tile.class),
})
public interface MapNode extends Countable {
    /**
     * check if the node is passable.
     *
     * @return true if it is or false otherwise
     */
    @NotNull Boolean getIsPassable();

    /**
     * set the node's passability.
     *
     * @param isPassable new node passability value
     */
    void setIsPassable(@NotNull Boolean isPassable);

    /**
     * get {@link AliveEntity} standing on the node.
     *
     * @return null if the node isn't occupied or node's inhabitant otherwise
     * @see AliveEntity
     */
    @Nullable AliveEntity getInhabitant();

    /**
     * check if the node's occupied.
     *
     * @return true if it is or false otherwise
     */
    @NotNull Boolean isOccupied();

    /**
     * try to occupy the node.
     *
     * @param stander {@link AliveEntity} trying to occupy the node
     * @return true if the node was empty and passable or false otherwise
     */
    @NotNull Boolean occupy(@NotNull AliveEntity stander);

    /**
     * remove node's inhabitant if there's one.
     */
    void free();

    /**
     * get one of the nodes adjacent to this node.
     *
     * @param direction node's side from where to take an adjacent node
     * @return null if there's no adjacent node from that side or {@link MapNode} otherwise
     */
    @Nullable MapNode getAdjacent(@NotNull Integer direction);

    /**
     * get all nodes adjacent to this one.
     *
     * @return list of adjacent nodes from each side (may be nulls in the list
     *     where's no adjacent nodes)
     */
    @NotNull List<MapNode> getAdjacentTiles();

    /**
     * check if the node is adjacent to another node.
     *
     * @param node node to check adjacency with
     * @return true if nodes're adjacent or false otherwise
     */
    @NotNull Boolean isAdjacentTo(@NotNull MapNode node);

    /**
     * set nodes adjacent to this node.
     *
     * @param node list of adjacent nodes
     */
    void setAdjacentTiles(@NotNull List<MapNode> node);

    /**
     * get node's coordinates.
     *
     * @return node's coordinates, [rowCoordinate, colCoordinate]
     */
    @NotNull List<Integer> getCoordinates();

    /**
     * get one of the node's coordinates by index.
     *
     * @param coordinateIndex index of the coordinate to get
     * @return special constant if the index is invalid
     *     or either nodes rowCoordinate or colCoordinate value otherwise
     */
    @NotNull Integer getCoordinate(@NotNull Integer coordinateIndex);

    /**
     * calculate heuristic value for the given node to this node.
     *
     * @param node node to calculate heuristic value to
     * @return heuristic value
     */
    @NotNull Integer getH(@NotNull MapNode node);
}
