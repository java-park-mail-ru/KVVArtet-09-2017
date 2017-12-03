package gamemechanics.aliveentities.helpers;

import gamemechanics.globals.Constants;

public final class CashCalculator {
    private CashCalculator() {
    }

    public static Integer getCashReward(Integer killedLevel) {
        Float multiplier = Constants.CASH_REWARD_GROWTH_PER_LEVEL * Integer.valueOf(killedLevel
                - Constants.START_LEVEL).floatValue();
        return Math.round(multiplier * Integer.valueOf(Constants.INITIAL_CASH_REWARD).floatValue());
    }

    public static Integer getPartyBiasedCashReward(Integer baseReward, Integer partySize) {
        return baseReward / partySize;
    }
}
