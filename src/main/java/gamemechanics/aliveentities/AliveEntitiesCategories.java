package gamemechanics.aliveentities;

public class AliveEntitiesCategories {
    public static final int AE_USER_CHARACTER = 1;
    public static final int AE_NPC = 1 << 2;
    public static final int AE_FRIENDLY = 1 << 3;

    public static final int AE_TANK_ROLE = 1 << 4;
    public static final int AE_MELEE_DAMAGE_DEALER_ROLE = 1 << 5;
    public static final int AE_RANGED_DAMAGE_DEALER_ROLE = 1 << 6;
    public static final int AE_SUPPORT_ROLE = 1 << 7;

    public static final int AE_MELEE_FIGHTER = AE_TANK_ROLE | AE_MELEE_DAMAGE_DEALER_ROLE;
    public static final int AE_DAMAGE_DEALER = AE_MELEE_DAMAGE_DEALER_ROLE | AE_RANGED_DAMAGE_DEALER_ROLE;
    public static final int AE_RANGED_FIGHTER = AE_SUPPORT_ROLE | AE_RANGED_DAMAGE_DEALER_ROLE;
}
