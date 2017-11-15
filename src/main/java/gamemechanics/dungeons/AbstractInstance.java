package gamemechanics.dungeons;

import gamemechanics.battlefield.Battlefield;
import gamemechanics.battlefield.actionresults.ActionResult;
import gamemechanics.battlefield.aliveentitiescontainers.Squad;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractInstance implements Instance {
    private static final AtomicInteger instanceCounter = new AtomicInteger(0);
    private final Integer instanceID = instanceCounter.getAndIncrement();

    private final String name;
    private final String description;

    private final Integer level;

    private final Integer gameMode;

    private final Integer roomsCount;
    private final Integer roomsCleared = 0;

    private final List<Squad> squads = new ArrayList<>();

    private Battlefield currentRoom;

    private static class AbstractInstanceModel {
        public String name;
        public String description;
        public Integer level;
        public Integer gameMode;
        public Integer roomsCount;

        private AbstractInstanceModel(@NotNull String name, @NotNull String description,
                                      Integer gameMode, Integer level, Integer roomsCount) {
            this.name = name;
            this.description = description;
            this.level = level;
            this.gameMode = gameMode;
            this.roomsCount = roomsCount;
        }
    }

    /* TODO: modify data model as DungeonInstance class will be specified */
    public static class DungeonInstanceModel extends AbstractInstanceModel {
        public DungeonInstanceModel(@NotNull String name, @NotNull String description,
                                    Integer gameMode, Integer level, Integer roomsCount) {
            super(name, description, gameMode, level, roomsCount);
        }
    }


    /* TODO: modify data model as LandInstance class will be specified */
    public static class LandInstanceModel extends AbstractInstanceModel {
        public LandInstanceModel(@NotNull String name, @NotNull String description,
                                    Integer gameMode, Integer level, Integer roomsCount) {
            super(name, description, gameMode, level, roomsCount);
        }
    }

    AbstractInstance(@NotNull DungeonInstanceModel model) {
        name = model.name;
        description = model.description;
        gameMode = model.gameMode;
        level = model.level;
        roomsCount = model.roomsCount;
    }

    AbstractInstance(@NotNull LandInstanceModel model) {
        name = model.name;
        description = model.description;
        gameMode = model.gameMode;
        level = model.level;
        roomsCount = model.roomsCount;
    }

    @Override
    public Integer getInstancesCount() {
        return instanceCounter.get();
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
    public ActionResult getBattleLogEntry(Integer entryIndex) {
        return currentRoom.getBattleLogEntry(entryIndex);
    }

    @Override
    public Integer getGameMode() {
        return gameMode;
    }

    @Override
    public List<Integer> getBattlingSquadsIds() {
        List<Integer> ids = new ArrayList<>();
        for (Squad squad : squads) {
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
        return squads.get(0).areAllDead(); /* TODO: think about logic for PvE & PvP */
    }
}
