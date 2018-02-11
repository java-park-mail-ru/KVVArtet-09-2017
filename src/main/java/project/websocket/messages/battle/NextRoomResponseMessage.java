package project.websocket.messages.battle;

import project.gamemechanics.battlefield.actionresults.ActionResult;
import project.gamemechanics.battlefield.map.BattleMap;
import project.gamemechanics.interfaces.AliveEntity;
import project.websocket.messages.Message;

import javax.validation.constraints.NotNull;
import java.util.Deque;
import java.util.List;

@SuppressWarnings("unused")
public class NextRoomResponseMessage extends Message {
    private final BattleMap map;
    private final Deque<AliveEntity> battlersQueue;
    private final List<ActionResult> battleLog;
    private final Integer turnCounter;

    public NextRoomResponseMessage(@NotNull BattleMap map,
                                   @NotNull Deque<AliveEntity> battlersQueue,
                                   @NotNull List<ActionResult> battleLog,
                                   @NotNull Integer turnCounter) {
        this.map = map;
        this.battlersQueue = battlersQueue;
        this.battleLog = battleLog;
        this.turnCounter = turnCounter;
    }

    public @NotNull List<Long> getMap() {
        return map.encode();
    }

    public @NotNull Deque<AliveEntity> getBattlersQueue() {
        return battlersQueue;
    }

    public @NotNull List<ActionResult> getBattleLog() {
        return battleLog;
    }

    public @NotNull Integer getTurnCounter() {
        return turnCounter;
    }
}
