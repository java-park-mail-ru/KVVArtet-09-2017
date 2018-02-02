package project.gamemechanics.resources.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.Nullable;
import project.gamemechanics.components.affectors.Affector;
import project.gamemechanics.components.properties.Property;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unused")
public class BasicModel extends AbstractModel {
    private final Map<Integer, Property> properties;
    private final Map<Integer, Affector> affectors;

    public BasicModel(@JsonProperty("modelId") @NotNull Integer modelID,
                      @JsonProperty("name") @NotNull String name,
                      @JsonProperty("description") @NotNull String description,
                      @JsonProperty("properties") @Nullable Map<Integer, Property> properties,
                      @JsonProperty("affectors") @Nullable Map<Integer, Affector> affectors) {
        super(modelID, name, description);
        this.properties = properties;
        this.affectors = affectors;
    }


    @Override
    public @NotNull Boolean hasProperty(@NotNull Integer propertyKind) {
        return properties != null && properties.containsKey(propertyKind);
    }

    @Override
    @JsonIgnore
    public @NotNull Set<Integer> getAvailableProperties() {
        return properties != null ? properties.keySet() : new HashSet<>();
    }

    @Override
    public @Nullable Property getProperty(@NotNull Integer propertyKind) {
        return properties != null ? properties.getOrDefault(propertyKind, null) : null;
    }

    @Override
    @JsonProperty("properties")
    public @Nullable Map<Integer, Property> getAllProperties() {
        return properties;
    }

    @Override
    public @NotNull Boolean hasAffector(@NotNull Integer affectorKind) {
        return affectors != null && affectors.containsKey(affectorKind);
    }

    @Override
    @JsonIgnore
    public @NotNull Set<Integer> getAvailableAffectors() {
        return affectors != null ? affectors.keySet() : new HashSet<>();
    }

    @Override
    public @Nullable Affector getAffector(@NotNull Integer affectorIndex) {
        return affectors != null ? affectors.getOrDefault(affectorIndex, null) : null;
    }

    @Override
    @JsonProperty("affectors")
    public @Nullable Map<Integer, Affector> getAllAffectors() {
        return affectors;
    }

    @Override
    @JsonIgnore
    public @Nullable Map<Integer, List<Integer>> getAllMappings() {
        return null;
    }

    @Override
    @JsonIgnore
    public @Nullable List<GameResource> getAllInlaid() {
        return null;
    }
}
