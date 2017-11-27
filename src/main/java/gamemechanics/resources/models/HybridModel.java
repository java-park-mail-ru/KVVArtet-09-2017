package gamemechanics.resources.models;

import gamemechanics.components.affectors.Affector;
import gamemechanics.components.properties.Property;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HybridModel extends AbstractModel {
    private final Map<Integer, Property> properties = new HashMap<>();
    private final Map<Integer, Affector> affectors = new HashMap<>();

    private final Map<Integer, List<Integer>> mappings = new HashMap<>();

    public HybridModel(@NotNull Integer modelID,
                       @NotNull String name, @NotNull String description,
                       @NotNull Map<Integer, Property> properties,
                       @NotNull Map<Integer, Affector> affectors,
                       @NotNull Map<Integer, List<Integer>> mappings) {
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

    public Set<Integer> getAvailableProperties() {
        return properties.keySet();
    }

    @Override
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
    public Map<Integer, Affector> getAllAffectors() {
        return affectors;
    }

    @Override
    public Boolean hasMapping(@NotNull Integer mappingIndex) {
        return mappings.containsKey(mappingIndex);
    }

    @Override
    public Set<Integer> getAvailableMappings() {
        return mappings.keySet();
    }

    @Override
    public List<Integer> getMapping(@NotNull Integer mappingIndex) {
        return mappings.getOrDefault(mappingIndex, null);
    }

    @Override
    public Map<Integer, List<Integer>> getAllMappings() {
        return mappings;
    }
}
