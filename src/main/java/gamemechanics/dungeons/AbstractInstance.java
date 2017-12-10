package gamemechanics.dungeons;

import gamemechanics.aliveentities.npcs.ai.AI;
import gamemechanics.battlefield.Battlefield;
import gamemechanics.battlefield.actionresults.ActionResult;
import gamemechanics.battlefield.aliveentitiescontainers.CharactersParty;
import gamemechanics.battlefield.aliveentitiescontainers.Squad;
import gamemechanics.battlefield.map.BattleMap;
import gamemechanics.globals.DigitsPairIndices;
import gamemechanics.globals.Directions;
import gamemechanics.interfaces.MapNode;
import gamemechanics.resources.pcg.PcgContentFactory;
import org.jetbrains.annotations.Nullable;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractInstance implements Instance {
    private static final AtomicInteger INSTANCE_COUNTER = new AtomicInteger(0);
    private final Integer instanceID = INSTANCE_COUNTER.getAndIncrement();

    private final String name;
    private final String description;

    private final Integer level;

    private final Integer gameMode;

    private final Integer roomsCount;
    private final Integer roomsCleared = 0;

    private final List<CharactersParty> squads = new ArrayList<>();

    final PcgContentFactory factory;

    Battlefield currentRoom;

    private static class AbstractInstanceModel {
        public final String name;
        public final String description;
        public final Integer level;
        public final Integer gameMode;
        public final Integer roomsCount;
        @SuppressWarnings("PublicField")
        public final PcgContentFactory factory;
        @SuppressWarnings("PublicField")
        public final List<CharactersParty> squads;

        AbstractInstanceModel(@NotNull String name, @NotNull String description,
                              @NotNull Integer gameMode, @NotNull Integer level,
                              @NotNull Integer roomsCount, @NotNull PcgContentFactory factory,
                              @NotNull List<CharactersParty> squads) {
            this.name = name;
            this.description = description;
            this.level = level;
            this.gameMode = gameMode;
            this.roomsCount = roomsCount;
            this.factory = factory;
            this.squads = squads;
        }
    }

    public static class DungeonInstanceModel extends AbstractInstanceModel {
        @SuppressWarnings("PublicField")
        public final Map<Integer, AI.BehaviorFunction> behaviors;

        public DungeonInstanceModel(@NotNull String name, @NotNull String description,
                                    @NotNull Integer level, @NotNull Integer roomsCount,
                                    @NotNull PcgContentFactory factory,
                                    @NotNull List<CharactersParty> squads,
                                    @NotNull Map<Integer, AI.BehaviorFunction> behaviors) {
            super(name, description, Battlefield.PVE_GAME_MODE, level, roomsCount, factory, squads);
            this.behaviors = behaviors;
        }
    }


    /* TODO: modify data model as LandInstance class will be specified */
    public static class LandInstanceModel extends AbstractInstanceModel {
        public LandInstanceModel(@NotNull String name, @NotNull String description,
                                 @NotNull Integer level, @NotNull Integer roomsCount,
                                 @NotNull PcgContentFactory factory,
                                 @NotNull List<CharactersParty> squads) {
            super(name, description, Battlefield.PVP_GAME_MODE, level, roomsCount, factory, squads);
        }
    }

    AbstractInstance(@NotNull DungeonInstanceModel model) {
        name = model.name;
        description = model.description;
        gameMode = model.gameMode;
        level = model.level;
        roomsCount = model.roomsCount;
        factory = model.factory;
        squads.addAll(model.squads);
    }

    AbstractInstance(@NotNull LandInstanceModel model) {
        name = model.name;
        description = model.description;
        gameMode = model.gameMode;
        level = model.level;
        roomsCount = model.roomsCount;
        factory = model.factory;
        squads.addAll(model.squads);
    }

    @Override
    public Integer getInstancesCount() {
        return INSTANCE_COUNTER.get();
    }

    @Override
    public Integer getID() {
        return instanceID;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public Integer getLevel() {
        return level;
    }

    @Override
    public Integer getRoomsCount() {
        return roomsCount;
    }

    @Override
    public Integer getClearedRoomsCount() {
        return roomsCleared;
    }

    @Override
    public Integer getBattleLogLength() {
        return currentRoom.getBattleLogLength();
    }

    @Override
    public List<ActionResult> getBattleLog() {
        return currentRoom.getBattleLog();
    }

    @Override
    public @Nullable ActionResult getBattleLogEntry(@NotNull Integer entryIndex) {
        return currentRoom.getBattleLogEntry(entryIndex);
    }

    @Override
    public Integer getGameMode() {
        return gameMode;
    }

    @Override
    public List<Integer> getBattlingSquadsIds() {
        final List<Integer> ids = new ArrayList<>();
        for (CharactersParty squad : squads) {
            if (squad != null) {
                ids.add(squad.getID());
            }
        }
        return ids;
    }

    @Override
    public Boolean isInstanceCleared() {
        return Objects.equals(roomsCleared, roomsCount);
    }

    @Override
    public Boolean isInstanceFailed() {
        return squads.get(Squad.PLAYERS_SQUAD_ID).areAllDead();
    }

    @Override
    public @Nullable CharactersParty getParty(@NotNull Integer partyIndex) {
        if (partyIndex < 0 || partyIndex >= squads.size()) {
            return null;
        }
        return squads.get(partyIndex);
    }

    @SuppressWarnings({"SameParameterValue", "OverlyComplexMethod"})
    @Nullable MapNode emplaceSpawnPoint(@NotNull Squad squad, @NotNull Integer sideSize, @NotNull BattleMap map,
                                        @NotNull Set<MapNode> occupiedNodes) {
        if (sideSize <= 0 || sideSize * sideSize < squad.getSquadSize()) {
            return null;
        }

        final Integer mapWidth = map.getSize().get(DigitsPairIndices.ROW_COORD_INDEX);
        final Integer mapHeight = map.getSize().get(DigitsPairIndices.COL_COORD_INDEX);

        final Integer halfWidthBegin = squad.getSquadID() == Squad.PLAYERS_SQUAD_ID ? 0 : mapWidth / 2 + 1;
        final Integer halfWidthEnd = squad.getSquadID() == Squad.PLAYERS_SQUAD_ID ? mapWidth / 2 : mapWidth - 1;

        final Random random = new Random(System.currentTimeMillis());

        Integer triesCount = 0;

        while (true) {
            if (triesCount > mapHeight * mapWidth) {
                break;
            }
            final MapNode spawnPointCenter = map.getTile(halfWidthBegin + random.nextInt(halfWidthEnd
                    - halfWidthBegin), random.nextInt(mapHeight));
            final Set<MapNode> rowCenters = new HashSet<>();
            rowCenters.add(spawnPointCenter);
            final Integer halfSide = (sideSize - 1) / 2;
            MapNode upperRowCenter = spawnPointCenter.getAdjacent(Directions.UP);
            MapNode lowerRowCenter = spawnPointCenter.getAdjacent(Directions.DOWN);
            for (Integer i = 0; i < halfSide; ++i) {
                if (upperRowCenter != null) {
                    rowCenters.add(upperRowCenter);
                    upperRowCenter = upperRowCenter.getAdjacent(Directions.UP);
                }
                if (lowerRowCenter != null) {
                    rowCenters.add(lowerRowCenter);
                    lowerRowCenter = lowerRowCenter.getAdjacent(Directions.DOWN);
                }
            }
            final Set<MapNode> nodesToOccupy = new HashSet<>();
            for (MapNode rowCenter : rowCenters) {
                if (rowCenter.getIsPassable() && !rowCenter.isOccupied()
                        && !occupiedNodes.contains(rowCenter)) {
                    nodesToOccupy.add(rowCenter);
                }
                MapNode leftNode = rowCenter.getAdjacent(Directions.LEFT);
                MapNode rightNode = rowCenter.getAdjacent(Directions.RIGHT);
                for (Integer i = 0; i < halfSide; ++i) {
                    if (leftNode != null) {
                        if (leftNode.getIsPassable() && !leftNode.isOccupied()
                                && !occupiedNodes.contains(leftNode)) {
                            nodesToOccupy.add(leftNode);
                        }
                        leftNode = leftNode.getAdjacent(Directions.LEFT);
                    }
                    if (rightNode != null) {
                        if (rightNode.getIsPassable() && !rightNode.isOccupied()
                                && !occupiedNodes.contains(rightNode)) {
                            nodesToOccupy.add(rightNode);
                        }
                        rightNode = rightNode.getAdjacent(Directions.RIGHT);
                    }
                }
            }

            if (nodesToOccupy.size() >= squad.getSquadSize()) {
                occupiedNodes.addAll(nodesToOccupy);
                return spawnPointCenter;
            }

            ++triesCount;
        }

        return null;
    }
}
