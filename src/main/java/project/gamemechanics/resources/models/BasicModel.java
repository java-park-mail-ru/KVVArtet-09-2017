package project.gamemechanics.resources.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import project.gamemechanics.components.affectors.Affector;
import project.gamemechanics.components.properties.Property;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BasicModel extends AbstractModel {
    private final Map<Integer, Property> properties;
    private final Map<Integer, Affector> affectors;

    public BasicModel(@JsonProperty("modelId") @NotNull Integer modelID,
                      @JsonProperty("name") @NotNull String name,
                      @JsonProperty("description") @NotNull String description,
                      @JsonProperty("properties") Map<Integer, Property> properties,
                      @JsonProperty("affectors") Map<Integer, Affector> affectors) {
        super(modelID, name, description);
        this.properties = properties;
        this.affectors = affectors;
    }


    @Override
    public Boolean hasProperty(@NotNull Integer propertyKind) {
        return properties.containsKey(propertyKind);
    }

    @Override
    @JsonIgnore
    public Set<Integer> getAvailableProperties() {
        return properties.keySet();
    }

    @Override
    public Property getProperty(@NotNull Integer propertyKind) {
        return properties.getOrDefault(propertyKind, null);
    }

    @Override
    @JsonProperty("properties")
    public Map<Integer, Property> getAllProperties() {
        return properties;
    }

    @Override
    public Boolean hasAffector(@NotNull Integer affectorKind) {
        return affectors.containsKey(affectorKind);
    }

    @Override
    @JsonIgnore
    public Set<Integer> getAvailableAffectors() {
        return affectors.keySet();
    }

    @Override
    public Affector getAffector(@NotNull Integer affectorIndex) {
        return affectors.getOrDefault(affectorIndex, null);
    }

    @Override
    @JsonProperty("affectors")
    public Map<Integer, Affector> getAllAffectors() {
        return affectors;
    }

    @Override
    @JsonIgnore
    public Map<Integer, List<Integer>> getAllMappings() {
        return null;
    }

    @Override
    @JsonIgnore
    public List<GameResource> getAllInlaid() {
        return null;
    }
}
