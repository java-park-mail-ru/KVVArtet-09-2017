package project.gamemechanics.resources.pcg.items;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import project.gamemechanics.components.affectors.Affector;
import project.gamemechanics.components.properties.Property;
import project.gamemechanics.components.properties.PropertyCategories;
import project.gamemechanics.globals.Constants;

import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.Set;

public class ItemPartAsset implements ItemPart {
    private final Integer itemPartId;

    private final String name;
    private final String description;

    private final Integer partIndex;

    private final Map<Integer, Affector> affectors;
    private final Map<Integer, Property> properties;


    public ItemPartAsset(@JsonProperty("itemPartId") @NotNull Integer itemPartId,
                         @JsonProperty("name") @NotNull String name,
                         @JsonProperty("description") @NotNull String description,
                         @JsonProperty("partIndex") @NotNull Integer partIndex,
                         @JsonProperty("affectors") @NotNull Map<Integer, Affector> affectors,
                         @JsonProperty("properties") @NotNull Map<Integer, Property> properties) {
        this.itemPartId = itemPartId;
        this.name = name;
        this.description = description;
        this.partIndex = partIndex;
        this.affectors = affectors;
        this.properties = properties;
    }

    @Override
    @JsonProperty("itemPartId")
    public Integer getID() {
        return itemPartId;
    }

    @Override
    @JsonIgnore
    public Integer getInstancesCount() {
        return 0;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    @JsonIgnore
    public Integer getLevel() {
        return getProperty(PropertyCategories.PC_LEVEL);
    }

    @Override
    public Integer getPartIndex() {
        return partIndex;
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
    public Integer getProperty(@NotNull Integer propertyKind) {
        final Property property = properties.getOrDefault(propertyKind, null);
        if (property != null) {
            return property.getProperty();
        }
        return Constants.WRONG_INDEX;
    }

    @Override
    public Integer getProperty(@NotNull Integer propertyKind, @NotNull Integer propertyIndex) {
        final Property property = properties.getOrDefault(propertyKind, null);
        if (property == null) {
            return Constants.WRONG_INDEX;
        }
        return property.getProperty(propertyIndex);
    }

    @Override
    @JsonProperty("properties")
    public Map<Integer, Property> getAllProperties() {
        return properties;
    }

    public void setProperties(@NotNull Map<Integer, Property> properties) {
        this.properties.clear();
        this.properties.putAll(properties);
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
    public Integer getAffection(@NotNull Integer affectorKind) {
        final Affector affector = affectors.getOrDefault(affectorKind, null);
        if (affector == null) {
            return Constants.WRONG_INDEX;
        }
        return affector.getAffection();
    }

    @Override
    public Integer getAffection(@NotNull Integer affectorKind, @NotNull Integer affectionIndex) {
        final Affector affector = affectors.getOrDefault(affectorKind, null);
        if (affector == null) {
            return Constants.WRONG_INDEX;
        }
        return affector.getAffection(affectionIndex);
    }

    @Override
    @JsonProperty("affectors")
    public Map<Integer, Affector> getAllAffectors() {
        return affectors;
    }
}
