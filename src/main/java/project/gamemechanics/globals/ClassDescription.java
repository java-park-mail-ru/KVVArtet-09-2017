package project.gamemechanics.globals;

import javax.validation.constraints.NotNull;

@SuppressWarnings("unused")
public enum ClassDescription {
    CD_Warrior(0, "Warrior"),
    CD_Mage(1, "Mage"),
    CD_Rogue(2, "Rogue"),
    CD_Priest(3, "Priest");

    ClassDescription(@NotNull Integer id, @NotNull String name) {
        this.id = id;
        this.name = name;
    }

    public @NotNull Integer asInt() {
        return id;
    }

    public @NotNull String asText() {
        return name;
    }

    private final Integer id;
    private final String name;
}
