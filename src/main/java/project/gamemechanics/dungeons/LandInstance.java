package project.gamemechanics.dungeons;

import project.gamemechanics.aliveentities.helpers.CashCalculator;
import project.gamemechanics.aliveentities.helpers.ExperienceCalculator;
import project.gamemechanics.battlefield.Battlefield;
import project.gamemechanics.battlefield.aliveentitiescontainers.SpawnPoint;
import project.gamemechanics.battlefield.aliveentitiescontainers.Squad;
import project.gamemechanics.battlefield.map.BattleMap;
import project.gamemechanics.battlefield.map.BattleMapGenerator;
import project.gamemechanics.components.properties.PropertyCategories;
import project.gamemechanics.globals.Constants;
import project.gamemechanics.globals.UserCharacterStatistics;
import project.gamemechanics.interfaces.AliveEntity;
import project.websocket.messages.Message;
import project.websocket.messages.battle.ActionRequestMessage;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LandInstance extends AbstractInstance {
    public LandInstance(@NotNull LandInstanceModel model) {
        super(model);

        final List<Squad> squadList = new ArrayList<>();
        squadList.add(Objects.requireNonNull(getParty(Squad.TEAM_ONE_SQUAD_ID)).toSquad());
        squadList.add(Objects.requireNonNull(getParty(Squad.TEAM_TWO_SQUAD_ID)).toSquad());

        final BattleMap newMap = new BattleMap(BattleMapGenerator.generateBattleMap(Constants.DEFAULT_COLS_COUNT,
                Constants.DEFAULT_ROWS_COUNT, Constants.DEFAULT_ROWS_COUNT
                        * Constants.DEFAULT_COLS_COUNT - Constants.DEFAULT_WALLS_COUNT));

        final List<SpawnPoint> spawnPoints = initializeSpawnPoints(squadList, newMap);

        final Battlefield.BattlefieldModel newRoomModel = new Battlefield.BattlefieldModel(null, newMap,
                spawnPoints, factory.getItemsFactory(), getGameMode());

        currentRoom = new Battlefield(newRoomModel);
    }

    @Override
    public @NotNull Boolean isInstanceCleared() {
        return determineWinner() != Constants.UNDEFINED_ID;
    }

    @Override
    public @NotNull Boolean isInstanceFailed() {
        return false;
    }

    @Override
    public void giveRewards() {
        final Integer winnerSquad = determineWinner();
        if (winnerSquad == Constants.UNDEFINED_ID) {
            return;
        }
        final Integer loserSquad = winnerSquad == Squad.TEAM_ONE_SQUAD_ID
                ? Squad.TEAM_TWO_SQUAD_ID : Squad.TEAM_ONE_SQUAD_ID;

        final Integer winnersPartyLevel = Objects.requireNonNull(getParty(winnerSquad)).getAverageLevel();
        final Integer losersPartyLevel = Objects.requireNonNull(getParty(loserSquad)).getAverageLevel();

        final Integer winnersExtraExp = ExperienceCalculator.getPartyBiasedXPReward(
                ExperienceCalculator.getXPReward(winnersPartyLevel, losersPartyLevel),
                Objects.requireNonNull(getParty(winnerSquad)).getPartySize());
        final Integer winnersExtraGold = CashCalculator.getPartyBiasedCashReward(
                CashCalculator.getCashReward(losersPartyLevel),
                Objects.requireNonNull(getParty(winnerSquad)).getPartySize());
        for (Integer roleId : Objects.requireNonNull(getParty(winnerSquad)).getRoleIds()) {
            Objects.requireNonNull(getParty(winnerSquad)).giveRewardForInstance(roleId,
                    winnersExtraExp, winnersExtraGold, factory.getItemsFactory());
            final AliveEntity member = Objects.requireNonNull(getParty(Squad.PLAYERS_SQUAD_ID)).getMember(roleId);
            if (member != null) {
                member.modifyPropertyByAddition(PropertyCategories.PC_STATISTICS,
                        UserCharacterStatistics.US_PVP_MATCHES_WON, 1);
            }
        }

        final Integer losersExtraExp = ExperienceCalculator.getPartyBiasedXPReward(
                ExperienceCalculator.getXPReward(losersPartyLevel, winnersPartyLevel),
                Objects.requireNonNull(getParty(loserSquad)).getPartySize()) / 2;
        final Integer losersExtraGold = CashCalculator.getPartyBiasedCashReward(
                CashCalculator.getCashReward(winnersPartyLevel),
                Objects.requireNonNull(getParty(loserSquad)).getPartySize()) / 2;
        for (Integer roleId : Objects.requireNonNull(getParty(loserSquad)).getRoleIds()) {
            Objects.requireNonNull(getParty(loserSquad)).giveRewardForInstance(roleId,
                    losersExtraExp, losersExtraGold, null);
        }
    }

    @Override
    public Message handleMessage(ActionRequestMessage message) {
        return message;
    }

    private Integer determineWinner() {
        return Objects.requireNonNull(getParty(Squad.TEAM_ONE_SQUAD_ID)).areAllDead() ? Squad.TEAM_TWO_SQUAD_ID
                : Objects.requireNonNull(getParty(Squad.TEAM_TWO_SQUAD_ID)).areAllDead() ? Squad.TEAM_ONE_SQUAD_ID
                : Constants.UNDEFINED_ID;
    }
}
