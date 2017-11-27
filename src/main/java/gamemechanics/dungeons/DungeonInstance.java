package gamemechanics.dungeons;

import gamemechanics.aliveentities.npcs.ai.AI;
import gamemechanics.battlefield.Battlefield;
import gamemechanics.battlefield.aliveentitiescontainers.SpawnPoint;
import gamemechanics.battlefield.aliveentitiescontainers.Squad;
import gamemechanics.battlefield.map.BattleMap;
import gamemechanics.battlefield.map.BattleMapGenerator;
import gamemechanics.globals.Constants;
import gamemechanics.interfaces.AliveEntity;
import gamemechanics.interfaces.MapNode;

import javax.validation.constraints.NotNull;
import java.util.*;

public class DungeonInstance extends AbstractInstance {
    private final Map<Integer, AI.BehaviorFunction> behaviors;

    public DungeonInstance(@NotNull DungeonInstanceModel model) {
        super(model);
        behaviors = model.behaviors;
        currentRoom = generateNewRoom();
    }

    private Boolean isRoomCleared() {
        return currentRoom.isVictory();
    }

    private Battlefield generateNewRoom() {
        List<Squad> squadList = new ArrayList<>();
        squadList.add(getParty(Squad.PLAYERS_SQUAD_ID).toSquad());
        Integer packsCount = generatePacksCount();
        for (Integer i = 0; i < packsCount; ++i) {
            Squad monstersSquad = generateMonsterSquad(getParty(Squad.PLAYERS_SQUAD_ID).getAverageLevel());
            squadList.add(monstersSquad);
        }

        BattleMap newMap = new BattleMap(BattleMapGenerator.generateBattleMap(Constants.DEFAULT_COLS_COUNT,
                Constants.DEFAULT_ROWS_COUNT, Constants.DEFAULT_ROWS_COUNT
                        * Constants.DEFAULT_COLS_COUNT - Constants.DEFAULT_WALLS_COUNT));

        List<SpawnPoint> spawnPoints = initializeSpawnPoints(squadList, newMap);

        Battlefield.BattlefieldModel newRoomModel = new Battlefield.BattlefieldModel(behaviors, newMap,
                spawnPoints, getGameMode());
        return new Battlefield(newRoomModel);
    }

    private Integer generatePacksCount() {
        return Constants.DEFAULT_PACKS_COUNT + (new Random(System.currentTimeMillis())
                .nextInt(Constants.DEFAULT_PACKS_COUNT) - 1);
    }

    private Squad generateMonsterSquad(@NotNull Integer level) {
        Integer chanceToContinue = Constants.PERCENTAGE_CAP_INT;
        Random random = new Random(System.currentTimeMillis());

        List<AliveEntity> monsters = new ArrayList<>();
        while (true) {
            if (random.nextInt(Constants.PERCENTAGE_CAP_INT) > chanceToContinue) {
                break;
            }
            monsters.add(generateMonster(level));
            chanceToContinue -= Constants.DECREMENT_PER_SPAWNED_MONSTER;
        }

        return new Squad(monsters, Squad.MONSTER_SQUAD_ID);
    }

    private AliveEntity generateMonster(@NotNull Integer level) {
        return null;
    }

    private List<SpawnPoint> initializeSpawnPoints(@NotNull List<Squad> squadList, @NotNull BattleMap map) {
        List<SpawnPoint> spawnPoints = new ArrayList<>();
        Set<MapNode> reservedNodes = new HashSet<>();
        for (Squad squad : squadList) {
            SpawnPoint spawnPoint = new SpawnPoint(emplaceSpawnPoint(squad, Constants.DEFAULT_SPAWN_POINT_SIDE_SIZE,
                    map, reservedNodes), Constants.DEFAULT_SPAWN_POINT_SIDE_SIZE, squad);
            spawnPoints.add(spawnPoint);
        }
        return spawnPoints;
    }
}
