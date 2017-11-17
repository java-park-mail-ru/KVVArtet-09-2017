package gamemechanics.globals;

public enum ItemRarity {
    IR_TRASH(0, "Trash"),
    IR_COMMON(1, "Common"),
    IR_GOOD(2, "Good"),
    IR_RARE(3, "Rare"),
    IR_EPIC(4, "Epic"),
    IR_LEGENDARY(5, "Legendary");

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
