package project.websocket.messages;

import project.gamemechanics.battlefield.actionresults.ActionResult;
import project.gamemechanics.battlefield.map.BattleMap;
import project.gamemechanics.interfaces.AliveEntity;

import java.util.Deque;
import java.util.List;

public class NextRoomResponseMessage extends Message {
    private final BattleMap map;
    private final Deque<AliveEntity> battlersQueue;
    private final List<ActionResult> battleLog;
    private Integer turnCounter;

    public NextRoomResponseMessage(BattleMap map,
                                  Deque<AliveEntity> battlersQueue,
                                  List<ActionResult> battleLog,
                                  Integer turnCounter) {
        this.map = map;
        this.battlersQueue = battlersQueue;
        this.battleLog = battleLog;
        this.turnCounter = turnCounter;
    }

    public BattleMap getMap() {
        return map;
    }

    public Deque<AliveEntity> getBattlersQueue() {
        return battlersQueue;
    }

    public List<ActionResult> getBattleLog() {
        return battleLog;
    }

    public Integer getTurnCounter() {
        return turnCounter;
    }
}
