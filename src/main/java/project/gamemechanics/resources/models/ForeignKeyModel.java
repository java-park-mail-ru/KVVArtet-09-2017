package project.gamemechanics.resources.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.Nullable;
import project.gamemechanics.components.affectors.Affector;
import project.gamemechanics.components.properties.Property;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unused")
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
    public @NotNull Boolean hasMapping(@NotNull Integer mappingIndex) {
        return mappings.containsKey(mappingIndex);
    }

    @Override
    @JsonIgnore
    public @Nullable Map<Integer, Property> getAllProperties() {
        return null;
    }

    @Override
    @JsonIgnore
    public @Nullable Map<Integer, Affector> getAllAffectors() {
        return null;
    }

    @Override
    @JsonIgnore
    public @Nullable List<GameResource> getAllInlaid() {
        return null;
    }

    @Override
    @JsonIgnore
    public @NotNull Set<Integer> getAvailableMappings() {
        return mappings.keySet();
    }

    @Override
    public @Nullable List<Integer> getMapping(@NotNull Integer mappingIndex) {
        return mappings.getOrDefault(mappingIndex, null);
    }

    @Override
    @JsonProperty("mappings")
    public @NotNull Map<Integer, List<Integer>> getAllMappings() {
        return mappings;
    }
}
