package gamemechanics.battlefield.map.tilesets;

import gamemechanics.globals.DigitsPairIndices;
import gamemechanics.globals.Directions;
import gamemechanics.interfaces.MapNode;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class MapNodeTileset implements Tileset {
    private final Set<MapNode> tileset = new HashSet<>();

    public MapNodeTileset(@NotNull MapNode center, Integer shape, Integer direction, Integer size) {
        makeTileset(center, shape, direction, size);
    }

    public MapNodeTileset(@NotNull MapNode center) {
        this(center, TilesetShapes.TS_POINT, Directions.DIRECTIONS_COUNT, 0);
    }

    public MapNodeTileset(@NotNull MapNode center, Integer size) {
        this(center, TilesetShapes.TS_CIRCLE, Directions.DIRECTIONS_COUNT, size);
    }

    @Override
    public Boolean isValid() {
        return !tileset.isEmpty();
    }

    @Override
    public Integer getTilesCount() {
        return tileset.size();
    }

    private void makeTileset(@NotNull MapNode center, Integer shape, Integer direction, Integer size) {
        switch (shape) {
            case TilesetShapes.TS_SQUARE:
                makeSquareTileset(center, direction, size);
                break;
            case TilesetShapes.TS_CONE:
                makeConeTileset(center, direction, size, false);
                break;
            case TilesetShapes.TS_REVERSE_CONE:
                makeConeTileset(center, direction, size, true);
                break;
            case TilesetShapes.TS_LINE:
                makeLineTileset(center, direction, size);
                break;
            case TilesetShapes.TS_CIRCLE:
                makeCircleTileset(center, size);
                break;
            case TilesetShapes.TS_POINT:
                makePointTileset(center);
        }
    }

    private void makeSquareTileset(@NotNull MapNode center, Integer direction, Integer size) {
        Integer halfSide = (size - 1) / 2;

    }

    private void makeConeTileset(@NotNull MapNode center, Integer direction, Integer size, Boolean isReverse) {

    }

    private void makeLineTileset(@NotNull MapNode center, Integer direction, Integer size) {

    }

    private void makeCircleTileset(@NotNull MapNode center, Integer size) {

    }

    private void makePointTileset(@NotNull MapNode center) {
        tileset.add(center);
    }

    private List<MapNode> getTilesetLine(@NotNull MapNode startPoint, Integer direction, Integer distance) {
        if (direction >= Directions.DIRECTIONS_COUNT || direction < 0) {
            return null;
        }
        List<MapNode> line = new ArrayList<>();
        if (startPoint.getIsPassable()) {
            line.add(startPoint);
        } else {
            return null;
        }
        MapNode node = startPoint.getAdjacent(direction);
        while (node != null && startPoint.getH(node) <= distance) {
            if (!node.getIsPassable()) {
                break;
            }
            line.add(node);
            node = node.getAdjacent(direction);
        }
        return line;
    }

    protected Set<MapNode> getTileset() {
        return tileset;
    }

    protected static Integer calculateDirection(@NotNull MapNode sender, @NotNull MapNode target) {
        if (sender.equals(target)) {
            return Directions.DIRECTIONS_COUNT;
        }
        List<Integer> coordinatesDiff = new ArrayList<>(DigitsPairIndices.PAIR_SIZE);
        for (Integer i = DigitsPairIndices.ROW_COORD_INDEX; i < DigitsPairIndices.PAIR_SIZE; ++i) {
            coordinatesDiff.set(i, target.getCoordinate(i) - sender.getCoordinate(i));
        }
        Integer majorDiffIndex = coordinatesDiff.get(DigitsPairIndices.ROW_COORD_INDEX)
                >= coordinatesDiff.get(DigitsPairIndices.COL_COORD_INDEX) ? DigitsPairIndices.ROW_COORD_INDEX
                : DigitsPairIndices.COL_COORD_INDEX;
        if (majorDiffIndex == DigitsPairIndices.ROW_COORD_INDEX) {
            if (coordinatesDiff.get(majorDiffIndex) > 0) {
                return Directions.DOWN;
            } else {
                return Directions.UP;
            }
        } else {
            if (coordinatesDiff.get(majorDiffIndex) > 0) {
                return Directions.RIGHT;
            } else {
                return Directions.LEFT;
            }
        }
    }
}
