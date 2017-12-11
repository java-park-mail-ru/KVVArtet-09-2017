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

public class HybridModel extends AbstractModel {
    private final Map<Integer, Property> properties = new HashMap<>();
    private final Map<Integer, Affector> affectors = new HashMap<>();

    private final Map<Integer, List<Integer>> mappings = new HashMap<>();

    public HybridModel(@JsonProperty("modelId") @NotNull Integer modelID,
                       @JsonProperty("name") @NotNull String name,
                       @JsonProperty("description") @NotNull String description,
                       @JsonProperty("properties") Map<Integer, Property> properties,
                       @JsonProperty("affectors") Map<Integer, Affector> affectors,
                       @JsonProperty("mappings") @NotNull Map<Integer, List<Integer>> mappings) {
        super(modelID, name, description);
        this.properties.putAll(properties);
        this.affectors.putAll(affectors);
        this.mappings.putAll(mappings);
    }

    @Override
    public Boolean hasProperty(@NotNull Integer propertyIndex) {
        return properties.containsKey(propertyIndex);
    }

    @Override
    public Property getProperty(@NotNull Integer propertyIndex) {
        return properties.getOrDefault(propertyIndex, null);
    }

    @Override
    @JsonIgnore
    public Set<Integer> getAvailableProperties() {
        return properties.keySet();
    }

    @Override
    @JsonProperty("properties")
    public Map<Integer, Property> getAllProperties() {
        return properties;
    }

    @Override
    public Boolean hasAffector(@NotNull Integer affectorIndex) {
        return affectors.containsKey(affectorIndex);
    }

    @Override
    public Affector getAffector(@NotNull Integer affectorIndex) {
        return affectors.getOrDefault(affectorIndex, null);
    }

    @Override
    public Set<Integer> getAvailableAffectors() {
        return affectors.keySet();
    }

    @Override
    @JsonProperty("affectors")
    public Map<Integer, Affector> getAllAffectors() {
        return affectors;
    }

    @Override
    public Boolean hasMapping(@NotNull Integer mappingIndex) {
        return mappings.containsKey(mappingIndex);
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

    @Override
    @JsonIgnore
    public List<GameResource> getAllInlaid() {
        return null;
    }
}
