package project.gamemechanics.globals;

import javax.validation.constraints.NotNull;

public enum ItemRarity {
    IR_UNDEFINED(Constants.UNDEFINED_ID, 0, "undefined"),
    IR_TRASH(0, Constants.WIDE_PERCENTAGE_CAP_INT, "Trash"),
    IR_COMMON(1, 9000, "Common"),
    IR_GOOD(2, 2000, "Good"),
    IR_RARE(3, 300, "Rare"),
    IR_EPIC(4, 100, "Epic"),
    IR_LEGENDARY(5, 20, "Legendary"),
    IR_SIZE(6, 0, "rarity grades count");

    private final Integer rarityGrade;
    private final Integer defaultDropChance;
    private final String description;

    ItemRarity(@NotNull Integer rarityGrade, @NotNull Integer defaultDropChance, @NotNull String description) {
        this.rarityGrade = rarityGrade;
        this.defaultDropChance = defaultDropChance;
        this.description = description;
    }

    public Integer asInt() {
        return rarityGrade;
    }

    public Integer getDefaultDropChance() {
        return defaultDropChance;
    }

    public String asText() {
        return description;
    }
}
