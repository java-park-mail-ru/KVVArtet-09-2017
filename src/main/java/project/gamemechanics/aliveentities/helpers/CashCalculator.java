package project.gamemechanics.aliveentities.helpers;

import project.gamemechanics.globals.Constants;

import javax.validation.constraints.NotNull;

public final class CashCalculator {
    private CashCalculator() {
    }

    public static @NotNull Integer getCashReward(@NotNull Integer killedLevel) {
        final Float multiplier = Constants.CASH_REWARD_GROWTH_PER_LEVEL * Integer.valueOf(killedLevel
                - Constants.START_LEVEL).floatValue();
        return Math.round(multiplier * Integer.valueOf(Constants.INITIAL_CASH_REWARD).floatValue());
    }

    public static @NotNull Integer getPartyBiasedCashReward(@NotNull Integer baseReward, @NotNull Integer partySize) {
        return baseReward / partySize;
    }
}
