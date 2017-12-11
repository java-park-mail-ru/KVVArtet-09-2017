package project.gamemechanics.resources.pcg.npcs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import project.gamemechanics.components.properties.Property;
import project.gamemechanics.components.properties.PropertyCategories;
import project.gamemechanics.globals.Constants;

import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.Set;

public final class NpcBlueprint {
    private final Map<Integer, Property> properties;
    private final Map<Integer, Integer> npcParts;

    public NpcBlueprint(@JsonProperty("properties") @NotNull Map<Integer, Property> properties,
                        @JsonProperty("npcParts") @NotNull Map<Integer, Integer> npcParts) {
        this.properties = properties;
        this.npcParts = npcParts;
    }

    public Map<Integer, Property> getProperties() {
        return properties;
    }

    @JsonIgnore
    public Set<Integer> getAvailableProperties() {
        return properties.keySet();
    }

    @JsonIgnore
    public Integer getLevel() {
        return properties.containsKey(PropertyCategories.PC_LEVEL)
                ? properties.get(PropertyCategories.PC_LEVEL).getProperty() : Constants.UNDEFINED_ID;
    }

    public Map<Integer, Integer> getNpcParts() {
        return npcParts;
    }

    @JsonIgnore
    public Set<Integer> getAvailableNpcParts() {
        return npcParts.keySet();
    }
}
