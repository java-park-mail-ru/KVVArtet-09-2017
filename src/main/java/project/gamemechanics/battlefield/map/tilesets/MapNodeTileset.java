package project.gamemechanics.battlefield.map.tilesets;

import project.gamemechanics.globals.DigitsPairIndices;
import project.gamemechanics.globals.Directions;
import project.gamemechanics.interfaces.MapNode;

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

    public MapNodeTileset(@NotNull MapNode center, Integer size, Boolean isCircle) {
        this(center, isCircle ? TilesetShapes.TS_CIRCLE : TilesetShapes.TS_SQUARE,
                Directions.DIRECTIONS_COUNT, size);
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
                makeSquareTileset(center, size);
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
                break;
            default:
                break;
        }
    }

    private void makeSquareTileset(@NotNull MapNode center, Integer size) {
        final Integer halfSide = (size - 1) / 2;
        // grabbing row centers, first upper rows, then lower ones
        final List<MapNode> rowCenters = new ArrayList<>();
        MapNode rowCenter = center.getAdjacent(Directions.UP);
        while (rowCenter != null) {
            if (center.getH(rowCenter) > halfSide) {
                break;
            }
            rowCenters.add(0, rowCenter);
            rowCenter = rowCenter.getAdjacent(Directions.UP);
        }
        rowCenters.add(center);
        rowCenter = center.getAdjacent(Directions.DOWN);
        while (rowCenter != null) {
            if (center.getH(rowCenter) > halfSide) {
                break;
            }
            rowCenters.add(rowCenter);
            rowCenter = rowCenter.getAdjacent(Directions.DOWN);
        }
        // for each row center grab a row by two halves and put it into the tileset
        for (MapNode node : rowCenters) {
            getTileRowFromRowCenter(node, size, true);
        }
    }

    private void makeConeTileset(@NotNull MapNode center, Integer direction, Integer size, Boolean isReverse) {
        final List<MapNode> centralCol = getTilesetLine(center, direction, size);
        if (centralCol == null) {
            return;
        }
        tileset.addAll(centralCol);
        if (isReverse) {
            for (MapNode colCenter : centralCol) {
                if (center != colCenter) {
                    final Boolean isHorizontal = direction == Directions.LEFT || direction == Directions.RIGHT;
                    final MapNode colHeadLeft = getReverseConeColumnHead(colCenter,
                            isHorizontal ? Directions.UP : Directions.LEFT);
                    final MapNode colHeadRight = getReverseConeColumnHead(colCenter,
                            isHorizontal ? Directions.DOWN : Directions.RIGHT);
                    getReverseConeColumn(colHeadLeft, direction, size - colCenter.getH(colHeadLeft));
                    getReverseConeColumn(colHeadRight, direction, size - colCenter.getH(colHeadRight));
                }
            }
        } else {
            Integer colWidth = size;
            for (MapNode rowCenter : centralCol) {
                getTileRowFromRowCenter(rowCenter, colWidth--,
                        direction == Directions.UP || direction == Directions.DOWN);
            }
        }
    }

    private void makeLineTileset(@NotNull MapNode center, Integer direction, Integer size) {
        tileset.add(center);
        if (direction < 0 || direction >= Directions.DIRECTIONS_COUNT) {
            return;
        }
        MapNode nextNode = center.getAdjacent(direction);
        while (nextNode != null) {
            if (center.getH(nextNode) > size) {
                break;
            }
            if (nextNode.getIsPassable()) {
                tileset.add(nextNode);
            } else {
                break;
            }
            nextNode = nextNode.getAdjacent(direction);
        }
    }

    private void makeCircleTileset(@NotNull MapNode center, Integer size) {
        for (Integer direction = Directions.UP; direction < Directions.DIRECTIONS_COUNT; ++direction) {
            makeConeTileset(center, direction, size, false);
        }
    }

    private void makePointTileset(@NotNull MapNode center) {
        tileset.add(center);
    }

    private List<MapNode> getTilesetLine(@NotNull MapNode startPoint, Integer direction, Integer distance) {
        if (direction >= Directions.DIRECTIONS_COUNT || direction < 0) {
            return null;
        }
        final List<MapNode> line = new ArrayList<>();
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

    private MapNode getReverseConeColumnHead(@NotNull MapNode rowCenter, Integer direction) {
        MapNode colHead = rowCenter.getAdjacent(direction);
        while (tileset.contains(colHead)) {
            if (colHead == null) {
                break;
            }
            if (!colHead.getIsPassable()) {
                break;
            }
            colHead = colHead.getAdjacent(direction);
        }
        if (colHead != null) {
            if (!colHead.getIsPassable()) {
                return null;
            }
        }
        return colHead;
    }

    private void getReverseConeColumn(@NotNull MapNode colHead, Integer direction, Integer size) {
        if (colHead != null) {
            if (size <= 0) {
                if (!tileset.contains(colHead)) {
                    tileset.add(colHead);
                }
                return;
            }
            final List<MapNode> column = getTilesetLine(colHead, direction,
                    size);
            if (column != null) {
                for (MapNode node : column) {
                    if (!tileset.contains(node)) {
                        tileset.add(node);
                    }
                }
            }
        }
    }

    private void getTileRowFromRowCenter(@NotNull MapNode rowCenter, Integer size, Boolean isHorizontal) {
        final Integer halfSize = (size - 1) / 2;
        if (halfSize <= 0) {
            return;
        }
        Integer sideDirection = isHorizontal ? Directions.LEFT : Directions.UP;
        List<MapNode> halfRow = getTilesetLine(rowCenter, sideDirection, halfSize);
        if (halfRow != null) {
            for (MapNode node : halfRow) {
                if (!tileset.contains(node)) {
                    tileset.add(node);
                }
            }
            halfRow.clear();
        }
        sideDirection = sideDirection == Directions.LEFT ? Directions.RIGHT : Directions.DOWN;
        halfRow = getTilesetLine(rowCenter, sideDirection, halfSize);
        if (halfRow != null) {
            for (MapNode node : halfRow) {
                if (!tileset.contains(node)) {
                    tileset.add(node);
                }
            }
        }
    }

    Set<MapNode> getTileset() {
        return tileset;
    }

    static Integer calculateDirection(@NotNull MapNode sender, @NotNull MapNode target) {
        if (sender.equals(target)) {
            return Directions.DIRECTIONS_COUNT;
        }
        final List<Integer> coordinatesDiff = new ArrayList<>(DigitsPairIndices.PAIR_SIZE);
        for (Integer i = DigitsPairIndices.ROW_COORD_INDEX; i < DigitsPairIndices.PAIR_SIZE; ++i) {
            coordinatesDiff.set(i, target.getCoordinate(i) - sender.getCoordinate(i));
        }
        final Integer majorDiffIndex = coordinatesDiff.get(DigitsPairIndices.ROW_COORD_INDEX)
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
