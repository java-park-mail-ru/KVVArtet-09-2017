package gamemechanics.resources.models;

import gamemechanics.components.affectors.Affector;
import gamemechanics.components.properties.Property;

import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.Set;

public class BasicModel extends AbstractModel {
    private final Map<Integer, Property> properties;
    private final Map<Integer, Affector> affectors;

    public BasicModel(@NotNull Integer modelID,
                      @NotNull String name, @NotNull String description,
                      @NotNull Map<Integer, Property> properties, @NotNull Map<Integer, Affector> affectors) {
        super(modelID, name, description);
        this.properties = properties;
        this.affectors = affectors;
    }


    @Override
    public Boolean hasProperty(Integer propertyKind) {
        return properties.containsKey(propertyKind);
    }

    @Override
    public Set<Integer> getAvailableProperties() {
        return properties.keySet();
    }

    @Override
    public Property getProperty(@NotNull Integer propertyKind) {
        return properties.getOrDefault(propertyKind, null);
    }

    @Override
    public Map<Integer, Property> getAllProperties() {
        return properties;
    }

    @Override
    public Boolean hasAffector(Integer affectorKind) {
        return affectors.containsKey(affectorKind);
    }

    @Override
    public Set<Integer> getAvailableAffectors() {
        return affectors.keySet();
    }

    @Override
    public Affector getAffector(@NotNull Integer affectorIndex) {
        return affectors.getOrDefault(affectorIndex, null);
    }

    @Override
    public Map<Integer, Affector> getAllAffectors() {
        return affectors;
    }
}
