package gamemechanics.globals;

public enum ItemRarity {
    IR_UNDEFINED(-1, "undefined"),
    IR_TRASH(0, "Trash"),
    IR_COMMON(1, "Common"),
    IR_GOOD(2, "Good"),
    IR_RARE(3, "Rare"),
    IR_EPIC(4, "Epic"),
    IR_LEGENDARY(5, "Legendary"),
    IR_SIZE(6, "rarity grades count");

    private final Integer rarityGrade;
    private final String description;

    ItemRarity(Integer rarityGrade, String description) {
        this.rarityGrade = rarityGrade;
        this.description = description;
    }

    public Integer asInt() {
        return rarityGrade;
    }

    public String asText() {
        return description;
    }
}
