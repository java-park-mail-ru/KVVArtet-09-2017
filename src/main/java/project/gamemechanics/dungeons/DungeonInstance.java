package project.gamemechanics.dungeons;

import project.gamemechanics.aliveentities.helpers.CashCalculator;
import project.gamemechanics.aliveentities.helpers.ExperienceCalculator;
import project.gamemechanics.aliveentities.npcs.ai.AI;
import project.gamemechanics.battlefield.Battlefield;
import project.gamemechanics.battlefield.aliveentitiescontainers.SpawnPoint;
import project.gamemechanics.battlefield.aliveentitiescontainers.Squad;
import project.gamemechanics.battlefield.map.BattleMap;
import project.gamemechanics.battlefield.map.BattleMapGenerator;
import project.gamemechanics.components.properties.PropertyCategories;
import project.gamemechanics.globals.Constants;
import project.gamemechanics.globals.UserCharacterStatistics;
import project.gamemechanics.interfaces.AliveEntity;
import project.websocket.messages.ErrorMessage;
import project.websocket.messages.Message;
import project.websocket.messages.battle.ActionRequestMessage;

import javax.validation.constraints.NotNull;
import java.util.*;

@SuppressWarnings("unused")
public class DungeonInstance extends AbstractInstance {
    private final Map<Integer, AI.BehaviorFunction> behaviors;

    public DungeonInstance(@NotNull DungeonInstanceModel model) {
        super(model);
        behaviors = model.behaviors;
        currentRoom = generateNewRoom();
    }

    private @NotNull Boolean isRoomCleared() {
        return currentRoom.isVictory();
    }

    private @NotNull Battlefield generateNewRoom() {
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

    private @NotNull Integer generatePacksCount() {
        return Constants.DEFAULT_PACKS_COUNT + (new Random(System.currentTimeMillis())
                .nextInt(Constants.DEFAULT_PACKS_COUNT) - 1);
    }

    private @NotNull Squad generateMonsterSquad(@NotNull Integer level) {
        Integer chanceToContinue = Constants.PERCENTAGE_CAP_INT;
        final Random random = new Random(System.currentTimeMillis());

        final List<AliveEntity> monsters = new ArrayList<>();
        while (random.nextInt(Constants.PERCENTAGE_CAP_INT) <= chanceToContinue) {
            monsters.add(generateMonster(level));
            chanceToContinue -= Constants.DECREMENT_PER_SPAWNED_MONSTER;
        }

        return new Squad(monsters, Squad.MONSTER_SQUAD_ID);
    }

    private @NotNull AliveEntity generateMonster(@NotNull Integer level) {
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
            final AliveEntity member = Objects.requireNonNull(getParty(Squad.PLAYERS_SQUAD_ID)).getMember(roleId);
            if (member != null) {
                member.modifyPropertyByAddition(PropertyCategories.PC_STATISTICS,
                        UserCharacterStatistics.US_DUNGEONS_CLEARED, 1);
            }
        }
    }

    @Override
    public @NotNull Message handleMessage(@NotNull ActionRequestMessage message) {
        if (!isInstanceCleared() && !isInstanceFailed()) {
            return currentRoom.pushAction(message);
        } else {
            if (isInstanceCleared()) {
                return new ErrorMessage("dungeon cleared");
            } else {
                return new ErrorMessage("dungeon failed");
            }
        }
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
