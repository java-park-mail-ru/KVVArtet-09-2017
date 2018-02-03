package project.gamemechanics.resources.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.Nullable;
import project.gamemechanics.components.affectors.Affector;
import project.gamemechanics.components.properties.Property;

import javax.validation.constraints.NotNull;
import java.util.*;

@SuppressWarnings("unused")
public class HybridModel extends AbstractModel {
    private final Map<Integer, Property> properties = new HashMap<>();
    private final Map<Integer, Affector> affectors = new HashMap<>();

    private final Map<Integer, List<Integer>> mappings = new HashMap<>();

    public HybridModel(@JsonProperty("modelId") @NotNull Integer modelID,
                       @JsonProperty("name") @NotNull String name,
                       @JsonProperty("description") @NotNull String description,
                       @JsonProperty("properties") @Nullable Map<Integer, Property> properties,
                       @JsonProperty("affectors") @Nullable Map<Integer, Affector> affectors,
                       @JsonProperty("mappings") @NotNull Map<Integer, List<Integer>> mappings) {
        super(modelID, name, description);
        if (properties != null) {
            this.properties.putAll(properties);
        }
        if (affectors != null) {
            this.affectors.putAll(affectors);
        }
        this.mappings.putAll(mappings);
    }

    @Override
    public @NotNull Boolean hasProperty(@NotNull Integer propertyIndex) {
        return properties != null && properties.containsKey(propertyIndex);
    }

    @Override
    public @Nullable Property getProperty(@NotNull Integer propertyIndex) {
        return properties != null ? properties.getOrDefault(propertyIndex, null) : null;
    }

    @Override
    @JsonIgnore
    public @NotNull Set<Integer> getAvailableProperties() {
        return properties != null ? properties.keySet() : new HashSet<>();
    }

    @Override
    @JsonProperty("properties")
    public @Nullable Map<Integer, Property> getAllProperties() {
        return properties;
    }

    @Override
    public @NotNull Boolean hasAffector(@NotNull Integer affectorIndex) {
        return affectors != null && affectors.containsKey(affectorIndex);
    }

    @Override
    public @Nullable Affector getAffector(@NotNull Integer affectorIndex) {
        return affectors != null ? affectors.getOrDefault(affectorIndex, null) : null;
    }

    @Override
    public @NotNull Set<Integer> getAvailableAffectors() {
        return affectors != null ? affectors.keySet() : new HashSet<>();
    }

    @Override
    @JsonProperty("affectors")
    public @Nullable Map<Integer, Affector> getAllAffectors() {
        return affectors;
    }

    @Override
    public @NotNull Boolean hasMapping(@NotNull Integer mappingIndex) {
        return mappings.containsKey(mappingIndex);
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

    @Override
    @JsonIgnore
    public @Nullable List<GameResource> getAllInlaid() {
        return null;
    }
}
