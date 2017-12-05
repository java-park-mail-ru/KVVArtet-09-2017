package project.gamemechanics.resources.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import project.gamemechanics.components.affectors.Affector;
import project.gamemechanics.components.properties.Property;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ForeignKeyModel extends AbstractModel {
    private final Map<Integer, List<Integer>> mappings = new HashMap<>();

    public ForeignKeyModel(@JsonProperty("modelId") @NotNull Integer modelID,
                           @JsonProperty("name") @NotNull String name,
                           @JsonProperty("description") @NotNull String description,
                           @JsonProperty("mappings") @NotNull Map<Integer, List<Integer>> mappings) {
        super(modelID, name, description);
        this.mappings.putAll(mappings);
    }

    @Override
    public Boolean hasMapping(@NotNull Integer mappingIndex) {
        return mappings.containsKey(mappingIndex);
    }

    @Override
    @JsonIgnore
    public Map<Integer, Property> getAllProperties() {
        return null;
    }

    @Override
    @JsonIgnore
    public Map<Integer, Affector> getAllAffectors() {
        return null;
    }

    @Override
    @JsonIgnore
    public List<GameResource> getAllInlaid() {
        return null;
    }

    @Override
    @JsonIgnore
    public Set<Integer> getAvailableMappings() {
        return mappings.keySet();
    }

    @Override
    public List<Integer> getMapping(@NotNull Integer mappingIndex) {
        return mappings.getOrDefault(mappingIndex, null);
    }

    @Override
    @JsonProperty("mappings")
    public Map<Integer, List<Integer>> getAllMappings() {
        return mappings;
    }
}
