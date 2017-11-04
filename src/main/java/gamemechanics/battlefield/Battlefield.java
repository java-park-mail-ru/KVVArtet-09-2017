package gamemechanics.battlefield;

import gamemechanics.battlefield.aliveentitiescontainers.SpawnPoint;
import gamemechanics.battlefield.aliveentitiescontainers.Squad;
import gamemechanics.battlefield.map.BattleMap;
import gamemechanics.battlefield.map.helpers.Pathfinder;
import gamemechanics.components.properties.PropertyCategories;
import gamemechanics.interfaces.AliveEntity;
import gamemechanics.interfaces.Effect;
import gamemechanics.interfaces.Updateable;

import java.util.*;

public class Battlefield implements Updateable {
    private static final int SQUADS_COUNT = 2;

    private static final int INITIAL_TURN_NUMBER = 1;

    private static final int ACTIONS_PER_TURN = 2;

    public static final int PVE_GAME_MODE = 0;
    public static final int PVP_GAME_MODE = 1;

    private final BattleMap map;
    private final Pathfinder pathfinder;

    private final List<Squad> squads = new ArrayList<>(SQUADS_COUNT);

    private final Deque<AliveEntity> battlersQueue = new ArrayDeque<>();

    private final Deque<Action> actionsQueue = new ArrayDeque<>();
    private Integer activeBattlerActionsPooled = 0;

    private final List<Effect> appliedEffects = new ArrayList<>();

    private Integer turnCounter = INITIAL_TURN_NUMBER;

    private final Integer mode;

    public static class BattlefieldModel {
        public BattleMap map;
        public List<SpawnPoint> spawnPoints;
        public Integer mode;

        public BattlefieldModel(BattleMap map, List<SpawnPoint> spawnPoints, Integer mode) {
            this.map = map;
            this.spawnPoints = spawnPoints;
            this.mode = mode;
        }
    }

    public Battlefield(BattlefieldModel model) {
        map = model.map;
        mode = model.mode;
        pathfinder = new Pathfinder(map);

        emplaceBattlers(model.spawnPoints);

        switch (mode) {
            case PVE_GAME_MODE:
                squads.set(Squad.PLAYERS_SQUAD_ID, model.spawnPoints.get(Squad.PLAYERS_SQUAD_ID).getSquad());
                mergeMonsterSquads(model.spawnPoints);
                break;
            case PVP_GAME_MODE:
                squads.set(Squad.TEAM_ONE_SQUAD_ID, model.spawnPoints.get(Squad.TEAM_ONE_SQUAD_ID).getSquad());
                squads.set(Squad.TEAM_TWO_SQUAD_ID, model.spawnPoints.get(Squad.TEAM_TWO_SQUAD_ID).getSquad());
                break;
            default:
                break;
        }

        List<AliveEntity> battlersList = new ArrayList<>();
        for (Squad squad : squads) {
            for (Integer i = 0; i < squad.getSquadSize(); ++i) {
                if (squad.getMember(i) != null) {
                    battlersList.add(squad.getMember(i));
                }
            }
        }
        battlersList.sort(Comparator.comparingInt(AliveEntity::getInitiative));
        for (AliveEntity battler : battlersList) {
            battlersQueue.addFirst(battler);
        }
    }

    public Integer getTurn() {
        return turnCounter;
    }

    public void update() {
        ++turnCounter;
        while (!actionsQueue.isEmpty()) {
            actionsQueue.getFirst().execute();
        }

        for (Squad squad : squads) {
            for (Integer i = 0; i < squad.getSquadSize(); ++i) {
                AliveEntity member = squad.getMember(i);
                if (member != null) {
                    member.update();
                }
            }
        }

        removeDead();

        removeExpiredEffects();

        for (Effect effect : appliedEffects) {
            effect.tick();
        }

        if (!isBattleFinished()) {
            AliveEntity activeBattler = battlersQueue.getFirst();
            if (activeBattler.isControlledByAI()) {
                while (activeBattlerActionsPooled < ACTIONS_PER_TURN) {
                    pushAction(activeBattler.makeDecision());
                    actionsQueue.pollFirst().execute();
                }
            }

            if (activeBattlerActionsPooled == ACTIONS_PER_TURN) {
                activeBattlerActionsPooled = 0;
                battlersQueue.addLast(battlersQueue.pollFirst());
            }
        } else {
            onBattleEnd();
        }
    }

    public Boolean isBattleFinished() {
        return isVictory() || isDefeat();
    }

    public Boolean isVictory() {
        return squads.get(Squad.TEAM_TWO_SQUAD_ID).areAllDead();
    }

    public Boolean isDefeat() {
        return squads.get(Squad.TEAM_ONE_SQUAD_ID).areAllDead();
    }

    public Boolean pushAction(Action action) {
        if (action.getSender().getInhabitant() == battlersQueue.getFirst()
                && activeBattlerActionsPooled < ACTIONS_PER_TURN) {
            actionsQueue.addLast(action);
            ++activeBattlerActionsPooled;
            return true;
        }
        return false;
    }

    public Boolean pushAction(/* JSON packet here */) {
        return false;
    }

    private void emplaceBattlers(List<SpawnPoint> spawnPoints) {
        for (SpawnPoint spawnPoint : spawnPoints) {
            if (spawnPoint != null) {
                spawnPoint.emplaceSquad();
            }
        }
    }

    private void mergeMonsterSquads(List<SpawnPoint> spawnPoints) {
        Squad monsterSquad = new Squad(new ArrayList<>(), Squad.MONSTER_SQUAD_ID);
        for (SpawnPoint spawnPoint : spawnPoints) {
            if (spawnPoint != null) {
                if (spawnPoint.getSquad().getSquadID() == Squad.MONSTER_SQUAD_ID) {
                    for (Integer i = 0; i < spawnPoint.getSquad().getSquadSize(); ++i) {
                        monsterSquad.addMember(spawnPoint.getSquad().getMember(i));
                    }
                }
            }
        }
        squads.set(Squad.MONSTER_SQUAD_ID, monsterSquad);
    }

    private void removeDead() {
        for (AliveEntity battler : battlersQueue) {
            if (!battler.isAlive()) {
                battlersQueue.remove(battler);
            }
        }
    }

    private void removeExpiredEffects() {
        for (Effect effect : appliedEffects) {
            if (effect.isExpired()) {
                appliedEffects.remove(effect);
            }
        }
    }

    private void endBattleCleanup() {
        for (Squad squad : squads) {
            for (Integer i = 0; i < squad.getSquadSize(); ++i) {
               squad.getMember(i).removeProperty(PropertyCategories.PC_COORDINATES);
            }
        }
    }

    private void generateLoot() {
        // collect all loot lists from mobs and generate loot
        for (Integer i = 0; i < squads.get(Squad.MONSTER_SQUAD_ID).getSquadSize(); ++i) {

        }
    }

    private void rewardTeam(Integer teamID) {
        Integer looseTeamID;
        switch (teamID) {
            case Squad.TEAM_ONE_SQUAD_ID:
                looseTeamID = Squad.TEAM_TWO_SQUAD_ID;
                break;
            case Squad.TEAM_TWO_SQUAD_ID:
                looseTeamID = Squad.TEAM_ONE_SQUAD_ID;
                break;
            default:
                return;
        }
        // reward winners with full-scale
        for (Integer i = 0; i < squads.get(teamID).getSquadSize(); ++i) {

        }
        // also reward the team that's lost but with lesser reward
        for (Integer i = 0; i < squads.get(looseTeamID).getSquadSize(); ++i) {

        }
    }

    private void generateReward() {
        switch (mode) {
            case PVE_GAME_MODE:
                if (isVictory()) {
                    generateLoot();
                }
                break;
            case PVP_GAME_MODE:
                if (isVictory()) {
                    rewardTeam(Squad.TEAM_ONE_SQUAD_ID);
                } else {
                    rewardTeam(Squad.TEAM_TWO_SQUAD_ID);
                }
                break;
            default:
                break;
        }
    }

    private void onBattleEnd() {
        endBattleCleanup();
        generateReward();
    }
}
