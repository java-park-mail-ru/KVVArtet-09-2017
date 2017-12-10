package gamemechanics.dungeons;

import gamemechanics.aliveentities.helpers.CashCalculator;
import gamemechanics.aliveentities.helpers.ExperienceCalculator;
import gamemechanics.aliveentities.npcs.ai.AI;
import gamemechanics.battlefield.Battlefield;
import gamemechanics.battlefield.aliveentitiescontainers.SpawnPoint;
import gamemechanics.battlefield.aliveentitiescontainers.Squad;
import gamemechanics.battlefield.map.BattleMap;
import gamemechanics.battlefield.map.BattleMapGenerator;
import gamemechanics.globals.Constants;
import gamemechanics.interfaces.AliveEntity;

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
        final List<Squad> squadList = new ArrayList<>();
        squadList.add(Objects.requireNonNull(getParty(Squad.PLAYERS_SQUAD_ID)).toSquad());
        final Integer packsCount = generatePacksCount();
        for (Integer i = 0; i < packsCount; ++i) {
            final Squad monstersSquad = generateMonsterSquad(
                    Objects.requireNonNull(getParty(Squad.PLAYERS_SQUAD_ID)).getAverageLevel());
            squadList.add(monstersSquad);
        }

        final BattleMap newMap = new BattleMap(BattleMapGenerator.generateBattleMap(Constants.DEFAULT_COLS_COUNT,
                Constants.DEFAULT_ROWS_COUNT, Constants.DEFAULT_ROWS_COUNT
                        * Constants.DEFAULT_COLS_COUNT - Constants.DEFAULT_WALLS_COUNT));

        final List<SpawnPoint> spawnPoints = initializeSpawnPoints(squadList, newMap);

        final Battlefield.BattlefieldModel newRoomModel = new Battlefield.BattlefieldModel(behaviors, newMap,
                spawnPoints, factory.getItemsFactory(), getGameMode());
        return new Battlefield(newRoomModel);
    }

    private Integer generatePacksCount() {
        return Constants.DEFAULT_PACKS_COUNT + (new Random(System.currentTimeMillis())
                .nextInt(Constants.DEFAULT_PACKS_COUNT) - 1);
    }

    private Squad generateMonsterSquad(@NotNull Integer level) {
        Integer chanceToContinue = Constants.PERCENTAGE_CAP_INT;
        final Random random = new Random(System.currentTimeMillis());

        final List<AliveEntity> monsters = new ArrayList<>();
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
        return factory.makeNpc(level);
    }

    @Override
    public void giveRewards() {
        final Integer partyLevel = Objects.requireNonNull(getParty(Squad.PLAYERS_SQUAD_ID)).getAverageLevel();
        final Random random = new Random(System.currentTimeMillis());
        final Integer extraExp = ExperienceCalculator.getPartyBiasedXPReward(
                ExperienceCalculator.getXPReward(partyLevel, partyLevel
                        + random.nextInt(Constants.LEVEL_RANGE_FOR_LOOT_DROPPING / 2)),
                Objects.requireNonNull(getParty(Squad.PLAYERS_SQUAD_ID)).getPartySize());

        final Integer extraGold = CashCalculator.getPartyBiasedCashReward(CashCalculator.getCashReward(
                partyLevel + random.nextInt(Constants.LEVEL_RANGE_FOR_LOOT_DROPPING / 2)),
                Objects.requireNonNull(getParty(Squad.PLAYERS_SQUAD_ID)).getPartySize());
        for (Integer roleId : Objects.requireNonNull(getParty(Squad.PLAYERS_SQUAD_ID)).getRoleIds()) {
            Objects.requireNonNull(getParty(Squad.PLAYERS_SQUAD_ID)).giveRewardForInstance(roleId,
                    extraExp, extraGold, factory.getItemsFactory());
        }
    }

    @Override
    public Boolean handlePacket() {
        return true;
    }

    @Override
    public void update(@NotNull Integer timestep) {
        if (!isInstanceCleared() || isInstanceFailed()) {
            if (currentRoom.isBattleFinished()) {
                if (currentRoom.isVictory()) {
                    // invoking cleared room update method to reward players;
                    currentRoom.update();
                    if (++roomsCleared < getRoomsCount()) {
                        currentRoom = generateNewRoom();
                    }
                }
            }
        }
    }
}
