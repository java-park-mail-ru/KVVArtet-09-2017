package project.gamemechanics.globals;

import javax.validation.constraints.NotNull;

@SuppressWarnings("unused")
public enum RaceDescription {
    RC_Human(0, "Human"),
    RC_Elf(1, "Elf"),
    RC_Dwarf(2, "Dwarf");

    RaceDescription(@NotNull Integer id, @NotNull String name) {
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
