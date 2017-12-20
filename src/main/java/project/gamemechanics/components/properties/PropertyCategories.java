package project.gamemechanics.components.properties;

@SuppressWarnings("unused")
public class PropertyCategories {
    // general properties
    public static final int PC_STATS = 1;
    public static final int PC_RATINGS = 2;
    public static final int PC_HITPOINTS = 3;
    public static final int PC_XP_POINTS = 4;
    public static final int PC_CASH_AMOUNT = 5;
    public static final int PC_BASE_DAMAGE = 6;
    public static final int PC_BASE_DEFENSE = 7;
    public static final int PC_LEVEL = 8;
    public static final int PC_ABILITIES_COOLDOWN = 9;
    public static final int PC_INITIATIVE = 10;
    public static final int PC_SPEED = 11;

    // items-specific properties
    public static final int PC_ITEM_KIND = 12;
    public static final int PC_ITEM_SLOTS = 13;
    public static final int PC_ITEM_PRICE = 14;
    public static final int PC_ITEM_RARITY = 15;

    public static final int PC_ITEM_BLUEPRINT_ID = 26;

    // battle participants-specific properties
    public static final int PC_COORDINATES = 16;
    public static final int PC_SQUAD_ID = 17;
    public static final int PC_OWNER_ID = 18;

    // user characters-specific properties
    public static final int PC_SKILL_POINTS = 19;
    public static final int PC_BASE_HEALTH = 20;
    public static final int PC_AVAILABLE_EQUIPMENT = 29;
    public static final int PC_ACTIVE_ROLE = 32;

    public static final int PC_STATISTICS = 34;

    //Ability-specific properties
    public static final int PC_MAX_DISTANCE = 21;
    public static final int PC_AREA = 22;
    public static final int PC_INFLICTED_CATEGORIES = 23;
    public static final int PC_COOLDOWN = 24;
    public static final int PC_AREA_SHAPE = 25;

    //serialized AliveEntity implementations-specific properties
    public static final int PC_CHARACTER_ROLE_ID = 27;
    public static final int PC_CHARACTER_RACE_ID = 28;

    //character party-related character properties
    public static final int PC_PARTY_ID = 30;
    public static final int PC_INSTANCE_ID = 31;

    public static final int PC_AVAILABLE_ROLES = 33;

    public static final int PC_MAX_RANK = 35;
}
