package project.gamemechanics.battlefield.map.tilesets;

import java.util.List;

/**
 * interface for classes providing field of vision (FoV) calculations for tile-based battle maps.
 */
public interface FieldOfVision {
    /**
     * re-calculate FoV for new position
     *
     * @param newPosition new point of view (PoV) coordinates [rowCoordinate, colCoordinate]
     */
    void refresh(List<Integer> newPosition);

    /**
     * check the visibility of the map node
     *
     * @param position coordinates of the point to check, [rowCoordinate, colCoordinate]
     * @return true is the node is visible from the current PoV or false otherwise
     */
    Boolean isVisible(List<Integer> position);
}
