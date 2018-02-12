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

@SuppressWarnings("unused")
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
    public @NotNull Integer getID() {
        return itemPartId;
    }

    @Override
    @JsonIgnore
    public @NotNull Integer getInstancesCount() {
        return 0;
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public @NotNull String getDescription() {
        return description;
    }

    @Override
    @JsonIgnore
    public @NotNull Integer getLevel() {
        return getProperty(PropertyCategories.PC_LEVEL);
    }

    @Override
    public @NotNull Integer getPartIndex() {
        return partIndex;
    }


    @Override
    public @NotNull Boolean hasProperty(@NotNull Integer propertyKind) {
        return properties.containsKey(propertyKind);
    }

    @Override
    @JsonIgnore
    public @NotNull Set<Integer> getAvailableProperties() {
        return properties.keySet();
    }

    @Override
    public @NotNull Integer getProperty(@NotNull Integer propertyKind) {
        final Property property = properties.getOrDefault(propertyKind, null);
        if (property != null) {
            return property.getProperty();
        }
        return Constants.WRONG_INDEX;
    }

    @Override
    public @NotNull Integer getProperty(@NotNull Integer propertyKind, @NotNull Integer propertyIndex) {
        final Property property = properties.getOrDefault(propertyKind, null);
        if (property == null) {
            return Constants.WRONG_INDEX;
        }
        return property.getProperty(propertyIndex);
    }

    @Override
    @JsonProperty("properties")
    public @NotNull Map<Integer, Property> getAllProperties() {
        return properties;
    }

    public void setProperties(@NotNull Map<Integer, Property> properties) {
        this.properties.clear();
        this.properties.putAll(properties);
    }

    @Override
    public @NotNull Boolean hasAffector(@NotNull Integer affectorKind) {
        return affectors.containsKey(affectorKind);
    }

    @Override
    @JsonIgnore
    public @NotNull Set<Integer> getAvailableAffectors() {
        return affectors.keySet();
    }

    @Override
    public @NotNull Integer getAffection(@NotNull Integer affectorKind) {
        final Affector affector = affectors.getOrDefault(affectorKind, null);
        if (affector == null) {
            return Constants.WRONG_INDEX;
        }
        return affector.getAffection();
    }

    @Override
    public @NotNull Integer getAffection(@NotNull Integer affectorKind, @NotNull Integer affectionIndex) {
        final Affector affector = affectors.getOrDefault(affectorKind, null);
        if (affector == null) {
            return Constants.WRONG_INDEX;
        }
        return affector.getAffection(affectionIndex);
    }

    @Override
    @JsonProperty("affectors")
    public @NotNull Map<Integer, Affector> getAllAffectors() {
        return affectors;
    }
}
