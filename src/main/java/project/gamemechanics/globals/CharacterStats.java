package project.gamemechanics.globals;

public enum CharacterStats {
    CS_ENDURANCE(0, "Endurance"),
    CS_STRENGTH(1, "Strength"),
    CS_AGILITY(2, "Agility"),
    CS_INTELLIGENCE(3, "Intelligence"),
    CS_SIZE(4, "stats count");

    private final Integer index;
    private final String name;

    CharacterStats(Integer index, String name) {
        this.index = index;
        this.name = name;
    }

    public Integer asInt() {
        return index;
    }

    public String asText() {
        return name;
    }
}
