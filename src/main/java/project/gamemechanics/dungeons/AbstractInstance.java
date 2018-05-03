package project.gamemechanics.dungeons;

import org.jetbrains.annotations.Nullable;
import project.gamemechanics.aliveentities.npcs.ai.AI;
import project.gamemechanics.battlefield.Battlefield;
import project.gamemechanics.battlefield.actionresults.ActionResult;
import project.gamemechanics.battlefield.aliveentitiescontainers.CharactersParty;
import project.gamemechanics.battlefield.aliveentitiescontainers.SpawnPoint;
import project.gamemechanics.battlefield.aliveentitiescontainers.Squad;
import project.gamemechanics.battlefield.map.BattleMap;
import project.gamemechanics.components.properties.PropertyCategories;
import project.gamemechanics.components.properties.SingleValueProperty;
import project.gamemechanics.globals.Constants;
import project.gamemechanics.globals.DigitsPairIndices;
import project.gamemechanics.globals.Directions;
import project.gamemechanics.interfaces.AliveEntity;
import project.gamemechanics.interfaces.MapNode;
import project.gamemechanics.resources.pcg.PcgContentFactory;
import project.websocket.messages.Message;
import project.websocket.messages.battle.ActionRequestMessage;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@SuppressWarnings("RedundantSuppression")
public abstract class AbstractInstance implements Instance {
    //noinspection VisibilityModifier
    @SuppressWarnings("RedundantSuppression")
    private static final AtomicInteger INSTANCE_COUNTER = new AtomicInteger(0);
    private final Integer instanceID = INSTANCE_COUNTER.getAndIncrement();

    private final String name;
    private final String description;

    private final Integer level;

    private final Integer gameMode;

    private final Integer roomsCount;
    // CHECKSTYLE:OFF
    Integer roomsCleared = 0;

    private final List<CharactersParty> squads = new ArrayList<>();

    final PcgContentFactory factory;

    Battlefield currentRoom;

    @Override
    public abstract @NotNull Message handleMessage(@NotNull ActionRequestMessage message);

    private static class AbstractInstanceModel {
        final String name;
        final String description;
        final Integer level;
        final Integer gameMode;
        final Integer roomsCount;
        final PcgContentFactory factory;
        final List<CharactersParty> squads;

        AbstractInstanceModel(@NotNull String name, @NotNull String description,
                              @NotNull Integer gameMode, @NotNull Integer level,
                              @NotNull Integer roomsCount,
                              @NotNull PcgContentFactory factory,
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
        final Map<Integer, AI.BehaviorFunction> behaviors;

        public DungeonInstanceModel(@NotNull String name, @NotNull String description,
                                    @NotNull Integer level, @NotNull Integer roomsCount,
                                    @NotNull PcgContentFactory factory,
                                    @NotNull List<CharactersParty> squads,
                                    @NotNull Map<Integer, AI.BehaviorFunction> behaviors) {
            super(name, description, Battlefield.PVE_GAME_MODE, level,
                    roomsCount, factory, squads);
            this.behaviors = behaviors;
        }
    }
    // CHECKSTYLE:ON

    public static class LandInstanceModel extends AbstractInstanceModel {
        public LandInstanceModel(@NotNull String name, @NotNull String description,
                                 @NotNull Integer level,
                                 @NotNull PcgContentFactory factory,
                                 @NotNull List<CharactersParty> squads) {
            super(name, description, Battlefield.PVP_GAME_MODE,
                    level, 1, factory, squads);
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
        setInstanceIdToParticipants();
    }

    AbstractInstance(@NotNull LandInstanceModel model) {
        name = model.name;
        description = model.description;
        gameMode = model.gameMode;
        level = model.level;
        roomsCount = model.roomsCount;
        factory = model.factory;
        squads.addAll(model.squads);
        setInstanceIdToParticipants();
    }

    @Override
    public @NotNull Integer getInstancesCount() {
        return INSTANCE_COUNTER.get();
    }

    @Override
    public @NotNull Integer getID() {
        return instanceID;
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public @NotNull String getDescription() {
        return description;
    }

    @Override
    public @NotNull Integer getLevel() {
        return level;
    }

    @Override
    public @NotNull Integer getRoomsCount() {
        return roomsCount;
    }

    @Override
    public @NotNull Integer getClearedRoomsCount() {
        return roomsCleared;
    }

    @Override
    public @NotNull Integer getBattleLogLength() {
        return currentRoom.getBattleLogLength();
    }

    @Override
    public @NotNull List<ActionResult> getBattleLog() {
        return currentRoom.getBattleLog();
    }

    @Override
    public @Nullable ActionResult getBattleLogEntry(@NotNull Integer entryIndex) {
        return currentRoom.getBattleLogEntry(entryIndex);
    }

    @Override
    public @NotNull Integer getGameMode() {
        return gameMode;
    }

    @Override
    public @NotNull List<Integer> getBattlingSquadsIds() {
        final List<Integer> ids = new ArrayList<>();
        for (CharactersParty squad : squads) {
            if (squad != null) {
                ids.add(squad.getID());
            }
        }
        return ids;
    }

    @Override
    public @NotNull Boolean isInstanceCleared() {
        return Objects.equals(roomsCleared, roomsCount);
    }

    @Override
    public @NotNull Boolean isInstanceFailed() {
        return squads.get(Squad.PLAYERS_SQUAD_ID).areAllDead();
    }

    @Override
    public @Nullable CharactersParty getParty(@NotNull Integer partyIndex) {
        if (partyIndex < 0 || partyIndex >= squads.size()) {
            return null;
        }
        return squads.get(partyIndex);
    }

    @Override
    public @NotNull List<Long> encodeCurrentRoomMap() {
        return currentRoom.encodeMap();
    }

    @SuppressWarnings({"SameParameterValue", "OverlyComplexMethod"})
    private @Nullable MapNode emplaceSpawnPoint(@NotNull Squad squad,
                                                @NotNull Integer sideSize,
                                                @NotNull BattleMap map,
                                                @NotNull Set<MapNode> occupiedNodes) {
        if (sideSize <= 0 || sideSize * sideSize < squad.getSquadSize()) {
            return null;
        }

        final Integer mapWidth = map.getSize().get(DigitsPairIndices.ROW_COORD_INDEX);
        final Integer mapHeight = map.getSize().get(DigitsPairIndices.COL_COORD_INDEX);

        final Integer halfWidthBegin = squad.getSquadID()
                == Squad.PLAYERS_SQUAD_ID ? 0 : mapWidth / 2 + 1;
        final Integer halfWidthEnd = squad.getSquadID()
                == Squad.PLAYERS_SQUAD_ID ? mapWidth / 2 : mapWidth - 1;

        final Random random = new Random(System.currentTimeMillis());

        Integer triesCount = 0;

        while (triesCount <= mapHeight * mapWidth) {
            final MapNode spawnPointCenter = map.getTile(halfWidthBegin
                    + random.nextInt(halfWidthEnd
                    - halfWidthBegin), random.nextInt(mapHeight));
            final Set<MapNode> rowCenters = new HashSet<>();
            rowCenters.add(spawnPointCenter);
            final Integer halfSide = (sideSize - 1) / 2;
            MapNode upperRowCenter = Objects.requireNonNull(spawnPointCenter)
                    .getAdjacent(Directions.UP);
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


    @NotNull List<SpawnPoint> initializeSpawnPoints(@NotNull List<Squad> squadList,
                                                    @NotNull BattleMap map) {
        final List<SpawnPoint> spawnPoints = new ArrayList<>();
        final Set<MapNode> reservedNodes = new HashSet<>();
        for (Squad squad : squadList) {
            final SpawnPoint spawnPoint = new SpawnPoint(
                    Objects.requireNonNull(emplaceSpawnPoint(squad,
                    Constants.DEFAULT_SPAWN_POINT_SIDE_SIZE, map, reservedNodes)),
                    Constants.DEFAULT_SPAWN_POINT_SIDE_SIZE, squad);
            spawnPoints.add(spawnPoint);
        }
        return spawnPoints;
    }

    private void setInstanceIdToParticipants() {
        for (CharactersParty squad : squads) {
            for (Integer roleId : squad.getRoleIds()) {
                final AliveEntity member = Objects.requireNonNull(squad.getMember(roleId));
                if (member.hasProperty(PropertyCategories.PC_INSTANCE_ID)) {
                    member.setProperty(PropertyCategories.PC_INSTANCE_ID, instanceID);
                } else {
                    member.addProperty(PropertyCategories.PC_INSTANCE_ID,
                            new SingleValueProperty(instanceID));
                }
            }
        }
    }
}
