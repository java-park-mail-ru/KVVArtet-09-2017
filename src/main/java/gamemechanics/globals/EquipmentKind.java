package gamemechanics.globals;

public enum EquipmentKind {
    EK_HEAD(1, "Head"),
    EK_CHEST(1 << 1, "Chest"),
    EK_HANDS(1 << 2, "Hands"),
    EK_BOOTS(1 << 3, "Boots"),
    EK_MAINHAND(1 << 4, "Main hand"),
    EK_OFFHAND(1 << 5, "Off hand"),
    EK_RING_ONE(1 << 6, "Ring"),
    EK_RING_TWO(1 << 7, "Ring"),
    EK_NECKLACE(1 << 8, "Neck"),

    EK_SLOTS_COUNT(9, "slots count"),

    EK_WEAPON_SLOT(EK_MAINHAND.asInt() | EK_OFFHAND.asInt(), "Weapon slots"),
    EK_ARMOUR_SLOT(EK_HEAD.asInt() | EK_CHEST.asInt() | EK_HANDS.asInt() | EK_BOOTS.asInt(), "Armour slots"),

    EK_RING(EK_RING_ONE.asInt() | EK_RING_TWO.asInt(), "Ring"),
    EK_TRINKET(EK_RING.asInt() | EK_NECKLACE.asInt(), "Trinket"),

    EK_CLOTH_ARMOUR(1 << 9, "Cloth armour"),
    EK_LEATHER_ARMOUR(1 << 10, "Leather armour"),
    EK_CHAINMAIL_ARMOUR(1 << 11, "Chainmail armour"),
    EK_PLATE_ARMOUR(1 << 12, "Plate armour"),
    EK_ARMOUR(EK_CLOTH_ARMOUR.asInt() | EK_LEATHER_ARMOUR.asInt()
            | EK_CHAINMAIL_ARMOUR.asInt() | EK_PLATE_ARMOUR.asInt(), "Armour"),

    EK_SWORD(1 << 13, "Sword"),
    EK_DAGGER(1 << 14, "Dagger"),
    EK_AXE(1 << 15, "Axe"),
    EK_MACE(1 << 16, "Mace"),
    EK_SPEAR(1 << 17, "Spear"),
    EK_STAFF(1 << 18, "Staff"),
    EK_BOW(1 << 19, "Bow"),
    EK_CROSSBOW(1 << 20, "Crossbow"),
    EK_WEAPON(EK_SWORD.asInt() | EK_DAGGER.asInt() | EK_AXE.asInt()
            | EK_MACE.asInt() | EK_SPEAR.asInt() | EK_STAFF.asInt()
            | EK_BOW.asInt() | EK_CROSSBOW.asInt(),"Weapon"),

    EK_ONEHANDED(1 << 21, "One-handed"),
    EK_TWOHANDED(1 << 22, "Two-handed");

    private final Integer kindID;
    private final String description;

    EquipmentKind(Integer kindID, String description) {
        this.kindID = kindID;
        this.description = description;
    }

    public Integer asInt() {
        return kindID;
    }

    public String asText() {
        return description;
    }
}
