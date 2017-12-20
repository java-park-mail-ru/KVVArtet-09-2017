package project.gamemechanics.components.affectors;

// CHECKSTYLE:OFF
public final class AffectorCategories {
    public static final int AC_STATS_AFFECTOR = 1;
    public static final int AC_RATINGS_AFFECTOR = 1 << 1;
    public static final int AC_DAMAGE_AFFECTOR = 1 << 2;
    public static final int AC_DEFENSE_AFFECTOR = 1 << 3;
    public static final int AC_HEALTH_AFFECTOR = 1 << 4;
    public static final int AC_OVER_TIME_AFFECTOR = 1 << 5;
    public static final int AC_WEAPON_DAMAGE_AFFECTOR = 1 << 6;
    public static final int AC_ARMOUR_DEFENSE_AFFECTOR = 1 << 7;
    public static final int AC_ABILITY_HEALTH_AFFECTOR = 1 << 8;

    public static final int AC_BASE_DAMAGE_ROLE_AFFECTOR = 1 << 9;
    public static final int AC_BASE_DEFENSE_ROLE_AFFECTOR = 1 << 10;

    public static final int AC_MULTI_VALUE_AFFECTORS = AC_STATS_AFFECTOR | AC_RATINGS_AFFECTOR | AC_DAMAGE_AFFECTOR
            | AC_DEFENSE_AFFECTOR | AC_OVER_TIME_AFFECTOR | AC_WEAPON_DAMAGE_AFFECTOR | AC_ABILITY_HEALTH_AFFECTOR;

    public static final int AC_SINGLE_VALUE_AFFECTORS = AC_HEALTH_AFFECTOR | AC_ARMOUR_DEFENSE_AFFECTOR
            | AC_BASE_DAMAGE_ROLE_AFFECTOR | AC_BASE_DEFENSE_ROLE_AFFECTOR;

    public static final int AC_REDUCABLE_AFFECTORS = AC_OVER_TIME_AFFECTOR | AC_WEAPON_DAMAGE_AFFECTOR
            | AC_ABILITY_HEALTH_AFFECTOR;
}
// CHECKSTYLE:ON