package project.gamemechanics.globals;

import javax.validation.constraints.NotNull;

@SuppressWarnings("unused")
public enum CharacterStats {
    CS_ENDURANCE(0, "Endurance"),
    CS_STRENGTH(1, "Strength"),
    CS_AGILITY(2, "Agility"),
    CS_INTELLIGENCE(3, "Intelligence"),
    CS_SIZE(4, "stats count");

    private final Integer index;
    private final String name;

    CharacterStats(@NotNull Integer index, @NotNull String name) {
        this.index = index;
        this.name = name;
    }

    public @NotNull Integer asInt() {
        return index;
    }

    public @NotNull String asText() {
        return name;
    }
}
