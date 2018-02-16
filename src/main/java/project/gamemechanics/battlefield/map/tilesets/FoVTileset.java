package project.gamemechanics.battlefield.map.tilesets;

import project.gamemechanics.battlefield.map.BattleMap;
import project.gamemechanics.globals.DigitsPairIndices;
import project.gamemechanics.globals.Directions;
import project.gamemechanics.interfaces.MapNode;

import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * Field of vision calculation class via shadow-casting.
 * inspired on https://journal.stuffwithstuff.com/2015/09/07/what-the-hero-sees/ article.
 *
 * @see FieldOfVision
 */

public class FoVTileset implements FieldOfVision {
    /**
     * octant directions starting from 0..45 degrees sector covering octant and going clockwise.
     */
    private static final int OD_NORTH = 0;
    private static final int OD_NORTH_EAST = 1;
    private static final int OD_EAST = 2;
    private static final int OD_SOUTH_EAST = 3;
    private static final int OD_SOUTH = 4;
    private static final int OD_SOUTH_WEST = 5;
    private static final int OD_WEST = 6;
    private static final int OD_NORTH_WEST = 7;

    /**
     * nodes visible from the current PoV.
     */
    private final Map<List<Integer>, MapNode> fieldOfVision = new HashMap<>();
    private final BattleMap map;

    /**
     * current PoV.
     */
    private MapNode currentPosition;

    /**
     * 1D projection of 2D {@link MapNode} coordinates from the current PoV.
     */
    // CHECKSTYLE:OFF
    private static class Shadow {
        Integer start;
        Integer end;

        Shadow(@NotNull Integer start, @NotNull Integer end) {
            this.start = start < end ? start : end;
            this.end = this.start.equals(start) ? end : start;
        }

        @NotNull Boolean contains(@NotNull Shadow other) {
            return start <= other.start && end >= other.end;
        }
    }

    /**
     * aggregated shadow line from all obstacles.
     */
    private static class ShadowLine {
        /**
         * shadows from obstacles.
         */
        final List<Shadow> shadows = new LinkedList<>();

        /**
         * check if the node's projection is covered by current shadow line.
         *
         * @param projection node coordinates projection from the current PoV.
         * @return true is this projection is covered by shadow line or false otherwise.
         */
        @NotNull Boolean isInShadow(@NotNull Shadow projection) {
            for (Shadow shadow : shadows) {
                if (shadow.contains(projection)) {
                    return true;
                }
            }
            return false;
        }

        /**
         * add a new shadow to the shadow line.
         *
         * @param shadow shadow to add.
         */
        void add(@NotNull Shadow shadow) {
            Integer position = 0;
            for (; position < shadows.size(); ++position) {
                if (shadows.get(position).start >= shadow.start) {
                    break;
                }
            }
            Shadow overlappingPrevious = null;
            if (position > 0 && shadows.get(position - 1).end > shadow.start) {
                overlappingPrevious = shadows.get(position - 1);
            }
            Shadow overlappingNext = null;
            if (position < shadows.size() && shadows.get(position).start < shadow.end) {
                overlappingNext = shadows.get(position);
            }
            if (overlappingNext != null) {
                if (overlappingPrevious != null) {
                    overlappingPrevious.end = overlappingNext.end;
                    shadows.remove(position.intValue());
                } else {
                    overlappingNext.start = shadow.start;
                }
            } else {
                if (overlappingPrevious != null) {
                    overlappingPrevious.end = shadow.end;
                } else {
                    shadows.add(position, shadow);
                }
            }
        }

        /**
         * check if the shadow line covers all observed row.
         *
         * @param width observed row's width.
         * @return true if the shadow line is represented by the single shadow and covers the whole row width
         */
        @NotNull Boolean isFullShadow(@NotNull Integer width) {
            return shadows.size() == 1 && Objects.equals(shadows.get(0).start, 0)
                    && Objects.equals(shadows.get(0).end, width);
        }
    }

    @SuppressWarnings("WeakerAccess")
    private static class Octant {
        private final Integer direction;
        private final MapNode source;

        Octant(@NotNull Integer direction, @NotNull MapNode source) {
            this.direction = direction;
            this.source = source;
        }

        private List<Integer> getRelativeCoordinates(@NotNull Integer row, @NotNull Integer column) {
            final List<Integer> relativeCoordinates = new ArrayList<>(DigitsPairIndices.PAIR_SIZE);
            switch (direction) {
                case OD_NORTH:
                    relativeCoordinates.add(DigitsPairIndices.ROW_COORD_INDEX, column);
                    relativeCoordinates.add(DigitsPairIndices.COL_COORD_INDEX, -1 * row);
                    break;
                case OD_NORTH_EAST:
                    relativeCoordinates.add(DigitsPairIndices.ROW_COORD_INDEX, row);
                    relativeCoordinates.add(DigitsPairIndices.COL_COORD_INDEX, -1 * column);
                    break;
                case OD_EAST:
                    relativeCoordinates.add(DigitsPairIndices.ROW_COORD_INDEX, row);
                    relativeCoordinates.add(DigitsPairIndices.COL_COORD_INDEX, column);
                    break;
                case OD_SOUTH_EAST:
                    relativeCoordinates.add(DigitsPairIndices.ROW_COORD_INDEX, column);
                    relativeCoordinates.add(DigitsPairIndices.COL_COORD_INDEX, row);
                    break;
                case OD_SOUTH:
                    relativeCoordinates.add(DigitsPairIndices.ROW_COORD_INDEX, -1 * column);
                    relativeCoordinates.add(DigitsPairIndices.COL_COORD_INDEX, row);
                    break;
                case OD_SOUTH_WEST:
                    relativeCoordinates.add(DigitsPairIndices.ROW_COORD_INDEX, -1 * row);
                    relativeCoordinates.add(DigitsPairIndices.COL_COORD_INDEX, column);
                    break;
                case OD_WEST:
                    relativeCoordinates.add(DigitsPairIndices.ROW_COORD_INDEX, -1 * row);
                    relativeCoordinates.add(DigitsPairIndices.COL_COORD_INDEX, -1 * column);
                    break;
                case OD_NORTH_WEST:
                    relativeCoordinates.add(DigitsPairIndices.ROW_COORD_INDEX, -1 * column);
                    relativeCoordinates.add(DigitsPairIndices.COL_COORD_INDEX, -1 * row);
                    break;
                default:
                    break;
            }
            return relativeCoordinates;
        }

        private Shadow projectTile(@NotNull Integer row, @NotNull Integer column) {
            final Integer start = column / (row + 2);
            final Integer end = (column + 1) / (row + 1);
            return new Shadow(start, end);
        }

        public @NotNull List<MapNode> getVisibleNodes(@NotNull BattleMap map) {
            final List<MapNode> visbleNodes = new ArrayList<>();
            final ShadowLine shadows = new ShadowLine();
            visbleNodes.add(source);
            for (Integer row = 0;; ++row) {
                final List<Integer> relativeCoords = new ArrayList<>(DigitsPairIndices.PAIR_SIZE);
                final List<Integer> octantRow = getRelativeCoordinates(row, 0);
                relativeCoords.add(DigitsPairIndices.ROW_COORD_INDEX,
                        source.getCoordinate(DigitsPairIndices.ROW_COORD_INDEX)
                                + octantRow.get(DigitsPairIndices.ROW_COORD_INDEX));
                relativeCoords.add(DigitsPairIndices.COL_COORD_INDEX,
                        source.getCoordinate(DigitsPairIndices.COL_COORD_INDEX)
                                + octantRow.get(DigitsPairIndices.COL_COORD_INDEX));
                if (map.getTile(relativeCoords.get(DigitsPairIndices.ROW_COORD_INDEX),
                        relativeCoords.get(DigitsPairIndices.COL_COORD_INDEX)) == null) {
                    break;
                }
                for (Integer column = 0; column <= row; ++column) {
                    final List<Integer> octantCoords = getRelativeCoordinates(row, column);
                    relativeCoords.set(DigitsPairIndices.ROW_COORD_INDEX,
                            source.getCoordinate(DigitsPairIndices.ROW_COORD_INDEX)
                                    + octantCoords.get(DigitsPairIndices.ROW_COORD_INDEX));
                    relativeCoords.set(DigitsPairIndices.COL_COORD_INDEX,
                            source.getCoordinate(DigitsPairIndices.COL_COORD_INDEX)
                                    + octantCoords.get(DigitsPairIndices.COL_COORD_INDEX));
                    if (map.getTile(relativeCoords.get(DigitsPairIndices.ROW_COORD_INDEX),
                            relativeCoords.get(DigitsPairIndices.COL_COORD_INDEX)) == null) {
                        break;
                    }
                    if (!shadows.isFullShadow(row)) {
                        final Shadow projection = projectTile(row, column);
                        final Boolean isVisible = !shadows.isInShadow(projection);
                        if (isVisible) {
                            final MapNode node = map.getTile(
                                    relativeCoords.get(DigitsPairIndices.ROW_COORD_INDEX),
                                    relativeCoords.get(DigitsPairIndices.COL_COORD_INDEX));
                            if (node != null && !visbleNodes.contains(node)) {
                                visbleNodes.add(node);
                                if (!node.getIsPassable()) {
                                    shadows.add(projection);
                                }
                            }
                        }
                    }
                }
            }
            return visbleNodes;
        }
    }


    /**
     * create and calculate FoV for the given PoV.
     *
     * @param currentPosition point of view
     */
    public FoVTileset(@NotNull MapNode currentPosition, @NotNull BattleMap map) {
        this.currentPosition = currentPosition;
        this.map = map;
        initializeFoV();
    }

    /**
     * move the PoV to the new position and re-calculate the FoV for it.
     *
     * @param position new PoV coordinates, [rowCoordinate, colCoordinate]
     */
    @Override
    public void refresh(@NotNull List<Integer> position) {
        movePoV(position);
        fieldOfVision.clear();
        initializeFoV();
    }

    @Override
    public @NotNull Boolean isVisible(@NotNull List<Integer> position) {
        return fieldOfVision.containsKey(position);
    }

    private void initializeFoV() {
        for (Integer octantDirection = OD_NORTH; octantDirection <= OD_NORTH_WEST; ++octantDirection) {
            final Octant octant = new Octant(octantDirection, currentPosition);
            final List<MapNode> visibleNodes = octant.getVisibleNodes(map);
            for (MapNode node : visibleNodes) {
                if (!fieldOfVision.containsKey(node.getCoordinates())) {
                    fieldOfVision.put(node.getCoordinates(), node);
                }
            }
        }
    }

    private void movePoV(@NotNull List<Integer> goal) {
        while (!currentPosition.getCoordinates().equals(goal)) {
            final Integer rowDifference = currentPosition.getCoordinate(DigitsPairIndices.ROW_COORD_INDEX)
                    - goal.get(DigitsPairIndices.ROW_COORD_INDEX);
            if (rowDifference != 0) {
                currentPosition = currentPosition.getAdjacent(
                        rowDifference < 0 ? Directions.UP : Directions.DOWN);
            }
            final Integer colDifference = Objects.requireNonNull(currentPosition)
                    .getCoordinate(DigitsPairIndices.COL_COORD_INDEX)
                    - goal.get(DigitsPairIndices.COL_COORD_INDEX);
            if (colDifference != 0) {
                currentPosition = currentPosition.getAdjacent(colDifference < 0 ? Directions.LEFT : Directions.RIGHT);
            }
        }
    }
}
// CHECKSTYLE:ON