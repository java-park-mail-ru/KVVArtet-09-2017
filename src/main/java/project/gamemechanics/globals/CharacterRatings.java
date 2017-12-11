package project.gamemechanics.globals;

public enum CharacterRatings {
    CR_CRITICAL_HIT(0, "Critical hit chance"),
    CR_DODGE(1, "Chance to dodge an opponent\'s attack"),
    CR_BLOCK(2, "Chance to block an opponent\'s attack"),
    CR_SIZE(3, "ratings count");

    private final Integer value;
    private final String description;

    CharacterRatings(Integer value, String description) {
        this.value = value;
        this.description = description;
    }

    public Integer asInt() {
        return value;
    }

    public String asText() {
        return description;
    }
}
