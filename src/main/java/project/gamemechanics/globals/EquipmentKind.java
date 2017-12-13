package project.gamemechanics.globals;

import javax.validation.constraints.NotNull;

public enum EquipmentKind {
    EK_UNDEFINED(Constants.UNDEFINED_ID, "undefined"),

    EK_CLOTH_ARMOUR(0, "Cloth armour"),
    EK_LEATHER_ARMOUR(1, "Leather armour"),
    EK_CHAINMAIL_ARMOUR(2, "Chainmail armour"),
    EK_PLATE_ARMOUR(3, "Plate armour"),

    EK_SWORD(4, "Sword"),
    EK_DAGGER(5, "Dagger"),
    EK_AXE(6, "Axe"),
    EK_MACE(7, "Mace"),
    EK_SPEAR(8, "Spear"),
    EK_STAFF(9, "Staff"),
    EK_BOW(10, "Bow"),
    EK_CROSSBOW(11, "Crossbow"),

    EK_TRINKET(12, "Trinket"),

    EK_SIZE(13, "equipment kinds count");

    private final Integer kindID;
    private final String description;

    EquipmentKind(@NotNull Integer kindID, @NotNull String description) {
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
