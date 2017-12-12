package project.gamemechanics.globals;

public final class Constants {
    public static final int WRONG_INDEX = Integer.MIN_VALUE;
    public static final int UNDEFINED_SQUAD_ID = -1;

    public static final int MIN_ID_VALUE = 0;
    public static final int UNDEFINED_ID = -1;

    public static final int AI_CONTROLLED_NPC_ID = -1;

    public static final int PERCENTAGE_CAP_INT = 100;
    public static final int WIDE_PERCENTAGE_CAP_INT = 10000;
    public static final float PERCENTAGE_CAP_FLOAT = 1.0f;
    public static final float ONE_PERCENT_FLOAT = 0.01f;

    public static final int CRITICAL_HIT_MULTIPLIER = 2;

    public static final int PERPETUAL_EFFECT_DURATION = -1;

    public static final int START_LEVEL = 1;
    public static final int MAX_LEVEL = 60;
    public static final int FIRST_LEVEL_UP_CAP = 100;
    public static final int SKILL_POINTS_GRANTED_PER_LEVEL = 1;
    public static final int LEVEL_RANGE_FOR_LOOT_DROPPING = 5;

    public static final int GENERIC_REWARD_LOOT_BAG_ID = 0;
    public static final int PERSONAL_REWARD_LOOT_BAG_ID = 1;
    public static final int DEFAULT_PERSONAL_REWARD_BAG_SIZE = 3;
    @SuppressWarnings("FieldNamingConvention")
    public static final int UNDEFINED_RARITY_DEFAULT_DROP_CHANCE = 5000;

    public static final int INITIAL_HITPOINTS_CAP = 100;
    public static final int HITPOINTS_PER_ENDURANCE_POINT = 60;
    @SuppressWarnings("FieldNamingConvention")
    public static final int HITPOINTS_PER_FIRST_TWENTY_POINTS = 1;
    public static final int ENDURANCE_BONUS_JUMP_POINT = 20;

    public static final float MINIMAL_DAMAGE_REDUCTION = 0.05f;

    public static final float STATS_GROWTH_PER_LEVEL = 0.03f;
    public static final float CASH_REWARD_GROWTH_PER_LEVEL = 0.025f;

    public static final int INITIAL_CASH_REWARD = 5;

    public static final int DEFAULT_INITIATIVE_VALUE = 10;
    public static final int DEFAULT_ALIVE_ENTITY_SPEED = 4;

    public static final int DEFAULT_MOVEMENT_COST = 1;

    public static final int MAXIMAL_FOV_DISTANCE = 12;

    public static final int DEFAULT_COLS_COUNT = 16;
    public static final int DEFAULT_ROWS_COUNT = 12;
    public static final int DEFAULT_WALLS_COUNT = 20;

    public static final int DEFAULT_PACKS_COUNT = 3;
    public static final int DEFAULT_ROOMS_COUNT = 5;

    public static final int DECREMENT_PER_SPAWNED_MONSTER = 15;

    public static final int DEFAULT_SPAWN_POINT_SIDE_SIZE = 3;
}
