package project.gamemechanics.battlefield.map.tilesets;

import project.gamemechanics.globals.Constants;
import project.gamemechanics.globals.DigitsPairIndices;
import project.gamemechanics.globals.Directions;
import project.gamemechanics.interfaces.MapNode;

import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * Field of vision calculation class via shadow-casting
 * inspired on https://journal.stuffwithstuff.com/2015/09/07/what-the-hero-sees/ article
 *
 * @see FieldOfVision
 */
public class FoVTileset implements FieldOfVision {
    /**
     * octant directions starting from 0..45 degrees sector covering octant and going clockwise
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
     * nodes visible from the current PoV
     */
    private final Map<List<Integer>, MapNode> fieldOfVision = new HashMap<>();

    /**
     * current PoV
     */
    private MapNode currentPosition;

    /**
     * 1D projection of 2D {@link MapNode} coordinates from the current PoV
     */
    private static class Shadow {
        Integer start;
        Integer end;

        Shadow(Integer start, Integer end) {
            this.start = start;
            this.end = end;
        }

        Boolean contains(Shadow other) {
            return start <= other.start && end >= other.end;
        }
    }

    /**
     * aggregated shadow line from all obstacles
     */
    private static class ShadowLine {
        /**
         * shadows from obstacles
         */
        final List<Shadow> shadows = new LinkedList<>();

        /**
         * check if the node's projection is covered by current shadow line
         *
         * @param projection node coordinates projection from the current PoV
         * @return true is this projection is covered by shadow line or false otherwise
         */
        Boolean isInShadow(Shadow projection) {
            for (Shadow shadow : shadows) {
                if (shadow.contains(projection)) {
                    return true;
                }
            }
            return false;
        }

        /**
         * add a new shadow to the shadow line
         *
         * @param shadow shadow to add
         */
        void add(Shadow shadow) {
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
         * @param width observed row's width
         * @return true if the shadow line is represented by the single shadow
         * and covers the whole row width
         */
        Boolean isFullShadow(Integer width) {
            return shadows.size() == 1 && Objects.equals(shadows.get(0).start, 0)
                    && Objects.equals(shadows.get(0).end, width);
        }
    }

    /**
     * octant is a 45-degrees sector of the FoV
     */
    private static class Octant {
        /**
         * octant's map nodes
         */
        private final List<List<MapNode>> octant = new LinkedList<>();

        /**
         * current PoV
         */
        private final MapNode source;

        /**
         * octant's direction
         */
        private final Integer direction;

        /**
         * create new octant for given PoV in given direction
         *
         * @param source    PoV to build an octant from
         * @param direction direction to build an octant in
         */
        Octant(@NotNull MapNode source, Integer direction) {
            this.source = source;
            this.direction = direction;

            getHalfCone();
        }

        /**
         * get octant nodes visible from the current PoV
         *
         * @return list of visible nodes
         */
        List<MapNode> getVisibleNodes() {
            final List<MapNode> visibleNodes = new LinkedList<>();
            final ShadowLine shadows = new ShadowLine();
            for (List<MapNode> row : octant) {
                for (MapNode node : row) {
                    if (!shadows.isFullShadow(row.size())) {
                        final List<Integer> relativeNodeCoordinates = getRelativeNodeCoordinates(node.getCoordinates());
                        final Shadow projection = projectNode(
                                relativeNodeCoordinates.get(DigitsPairIndices.ROW_COORD_INDEX),
                                relativeNodeCoordinates.get(DigitsPairIndices.COL_COORD_INDEX));
                        final Boolean nodeVisibility = !shadows.isInShadow(projection);
                        if (!node.getIsPassable() && nodeVisibility) {
                            shadows.add(projection);
                        }
                        if (node.getIsPassable() && nodeVisibility) {
                            visibleNodes.add(node);
                        }
                    }
                }
            }
            return visibleNodes;
        }

        private void getHalfCone() {
            final List<Integer> coneDirections = setConeDirections();
            final List<MapNode> coneHeads = new LinkedList<>();
            coneHeads.add(source);
            MapNode nextHead = source.getAdjacent(coneDirections.get(0));
            while (nextHead != null) {
                if (source.getH(nextHead) > Constants.MAXIMAL_FOV_DISTANCE) {
                    break;
                }
                coneHeads.add(nextHead);
                nextHead = nextHead.getAdjacent(coneDirections.get(0));
            }
            for (MapNode head : coneHeads) {
                final List<MapNode> coneRow = getHalfConeRow(head, coneDirections.get(1));
                octant.add(coneRow);
            }
        }

        private List<Integer> setConeDirections() {
            final List<Integer> directions = new ArrayList<>(DigitsPairIndices.PAIR_SIZE);
            switch (direction) {
                case OD_NORTH:
                    directions.set(0, Directions.UP);
                    directions.set(1, Directions.RIGHT);
                    break;
                case OD_NORTH_EAST:
                    directions.set(0, Directions.RIGHT);
                    directions.set(1, Directions.UP);
                    break;
                case OD_EAST:
                    directions.set(0, Directions.RIGHT);
                    directions.set(1, Directions.DOWN);
                    break;
                case OD_SOUTH_EAST:
                    directions.set(0, Directions.DOWN);
                    directions.set(1, Directions.RIGHT);
                    break;
                case OD_SOUTH:
                    directions.set(0, Directions.DOWN);
                    directions.set(1, Directions.LEFT);
                    break;
                case OD_SOUTH_WEST:
                    directions.set(0, Directions.LEFT);
                    directions.set(1, Directions.DOWN);
                    break;
                case OD_WEST:
                    directions.set(0, Directions.LEFT);
                    directions.set(1, Directions.UP);
                    break;
                case OD_NORTH_WEST:
                    directions.set(0, Directions.UP);
                    directions.set(1, Directions.LEFT);
                    break;
                default:
                    break;
            }
            return directions;
        }

        @SuppressWarnings("ParameterHidesMemberVariable")
        private List<MapNode> getHalfConeRow(@NotNull MapNode rowHead, @NotNull Integer direction) {
            final List<MapNode> row = new LinkedList<>();
            row.add(rowHead);
            MapNode nextNode = rowHead.getAdjacent(direction);
            while (nextNode != null) {
                if (source.getH(rowHead) < rowHead.getH(nextNode)) {
                    break;
                }
                row.add(nextNode);
                nextNode = nextNode.getAdjacent(direction);
            }
            return row;
        }

        private Shadow projectNode(Integer row, Integer column) {
            //noinspection OverlyComplexBooleanExpression
            final Integer nominator = direction == OD_NORTH || direction == OD_SOUTH_EAST
                    || direction == OD_SOUTH || direction == OD_NORTH_WEST ? column : row;
            final Integer denominator = nominator.equals(column) ? row : column;
            final Integer start = denominator > 0 ? nominator / (denominator + 2)
                    : nominator / (denominator - 2);
            Integer end = nominator > 0 ? nominator + 1 : nominator - 1;
            end = denominator > 0 ? end / (denominator + 1)
                    : end / (denominator - 1);
            return new Shadow(start, end);
        }

        private Integer getShadowLineCoordinateIndex() {
            //noinspection OverlyComplexBooleanExpression
            return direction == OD_NORTH || direction == OD_SOUTH_EAST
                    || direction == OD_SOUTH || direction == OD_NORTH_WEST
                    ? DigitsPairIndices.ROW_COORD_INDEX : DigitsPairIndices.COL_COORD_INDEX;
        }

        private List<Integer> getRelativeNodeCoordinates(List<Integer> nodeCoordinates) {
            final List<Integer> relativeCoordinates = new ArrayList<>(DigitsPairIndices.PAIR_SIZE);
            relativeCoordinates.set(DigitsPairIndices.ROW_COORD_INDEX,
                    nodeCoordinates.get(DigitsPairIndices.COL_COORD_INDEX)
                            - source.getCoordinate(DigitsPairIndices.COL_COORD_INDEX));
            relativeCoordinates.set(DigitsPairIndices.COL_COORD_INDEX,
                    nodeCoordinates.get(DigitsPairIndices.ROW_COORD_INDEX)
                            - source.getCoordinate(DigitsPairIndices.ROW_COORD_INDEX));
            return relativeCoordinates;
        }
    }

    /**
     * create and calculate FoV for the given PoV
     *
     * @param currentPosition point of view
     */
    public FoVTileset(@NotNull MapNode currentPosition) {
        this.currentPosition = currentPosition;
        initializeFoV();
    }

    /**
     * move the PoV to the new position and re-calculate the FoV for it
     *
     * @param position new PoV coordinates, [rowCoordinate, colCoordinate]
     */
    @Override
    public void refresh(List<Integer> position) {
        movePoV(position);
        fieldOfVision.clear();
        initializeFoV();
    }

    @Override
    public Boolean isVisible(List<Integer> position) {
        return fieldOfVision.containsKey(position);
    }

    private void initializeFoV() {
        for (Integer octantDirection = OD_NORTH; octantDirection <= OD_NORTH_WEST; ++octantDirection) {
            final Octant octant = new Octant(currentPosition, octantDirection);
            final List<MapNode> visibleNodes = octant.getVisibleNodes();
            for (MapNode node : visibleNodes) {
                if (!fieldOfVision.containsKey(node.getCoordinates())) {
                    fieldOfVision.put(node.getCoordinates(), node);
                }
            }
        }
    }

    private void movePoV(List<Integer> goal) {
        while (currentPosition.getCoordinates() != goal) {
            final Integer rowDifference = currentPosition.getCoordinate(DigitsPairIndices.ROW_COORD_INDEX)
                    - goal.get(DigitsPairIndices.ROW_COORD_INDEX);
            if (rowDifference != 0) {
                currentPosition = currentPosition.getAdjacent(
                        rowDifference < 0 ? Directions.UP : Directions.DOWN);
            }
            final Integer colDifference = currentPosition.getCoordinate(DigitsPairIndices.COL_COORD_INDEX)
                    - goal.get(DigitsPairIndices.COL_COORD_INDEX);
            if (colDifference != 0) {
                currentPosition = currentPosition.getAdjacent(colDifference < 0 ? Directions.LEFT : Directions.RIGHT);
            }
        }
    }
}
