package project.gamemechanics.aliveentities.helpers;

import project.gamemechanics.globals.Constants;

import java.util.Objects;

public final class ExperienceCalculator {

    @SuppressWarnings("FieldNamingConvention")
    private static final int LEVEL_UP_CAP_CALCULATION_CONSTANT = 8;
    private static final int BASE_XP_REWARD = 45;
    private static final int XP_CALCULATION_CONSTANT = 5;

    private static final int NO_XP_LEVEL_DIFFERENCE_CAP = 9;

    private static final float XP_BOOST_PER_LEVEL_ABOVE = 0.05f;

    private static final int ZERO_DIFFERENCE_LEVEL_STEP = 6;
    private static final float ZERO_DIFFERENCE_MIN_VALUE = 5.0f;
    private static final float ZERO_DIFFERENCE_MAX_VALUE = 17.0f;

    private ExperienceCalculator() {
    }

    public static Integer getNewLevelUpCap(Integer level) {
        return LEVEL_UP_CAP_CALCULATION_CONSTANT * level * getXPReward(level, level);
    }

    public static Integer getXPReward(Integer killerLevel, Integer killedLevel) {
        if (!Objects.equals(killerLevel, killedLevel)) {
            final Integer levelDifference = killedLevel - killerLevel;
            if (Math.abs(levelDifference) > NO_XP_LEVEL_DIFFERENCE_CAP) {
                return 0;
            }
            if (levelDifference < 0) {
                return Math.round(getBasicXPReward(killerLevel).floatValue() * (Constants.PERCENTAGE_CAP_FLOAT
                        - Integer.valueOf(Math.abs(levelDifference)).floatValue() * zeroDifferenceValue(killerLevel)));
            } else {
                return Math.round(getBasicXPReward(killerLevel).floatValue() * (Constants.PERCENTAGE_CAP_FLOAT
                        + XP_BOOST_PER_LEVEL_ABOVE * levelDifference.floatValue()));
            }
        }
        return getBasicXPReward(killerLevel);
    }

    public static Integer getPartyBiasedXPReward(Integer reward, Integer alivePartyMembersCount) {
        return reward / alivePartyMembersCount;
    }

    private static Integer getBasicXPReward(Integer level) {
        return XP_CALCULATION_CONSTANT * level + BASE_XP_REWARD;
    }

    private static Float zeroDifferenceValue(Integer level) {
        final Float calculatedZeroDifference = Integer.valueOf(Math.floorDiv(level, ZERO_DIFFERENCE_LEVEL_STEP)).floatValue();
        if (calculatedZeroDifference > ZERO_DIFFERENCE_MAX_VALUE) {
            return ZERO_DIFFERENCE_MAX_VALUE;
        }
        if (calculatedZeroDifference < ZERO_DIFFERENCE_MIN_VALUE) {
            return ZERO_DIFFERENCE_MIN_VALUE;
        }
        return calculatedZeroDifference;
    }
}
