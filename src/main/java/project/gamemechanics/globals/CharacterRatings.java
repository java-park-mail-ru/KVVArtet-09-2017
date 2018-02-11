package project.gamemechanics.globals;

import javax.validation.constraints.NotNull;

@SuppressWarnings("unused")
public enum CharacterRatings {
    CR_CRITICAL_HIT(0, "Critical hit chance"),
    CR_DODGE(1, "Chance to dodge an opponent\'s attack"),
    CR_BLOCK(2, "Chance to block an opponent\'s attack"),
    CR_SIZE(3, "ratings count");

    private final Integer value;
    private final String description;

    CharacterRatings(@NotNull Integer value, @NotNull String description) {
        this.value = value;
        this.description = description;
    }

    public @NotNull Integer asInt() {
        return value;
    }

    public @NotNull String asText() {
        return description;
    }
}
