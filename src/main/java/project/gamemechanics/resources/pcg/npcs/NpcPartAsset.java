package project.gamemechanics.resources.pcg.npcs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import project.gamemechanics.components.properties.Property;
import project.gamemechanics.components.properties.PropertyCategories;
import project.gamemechanics.globals.Constants;
import project.gamemechanics.resources.pcg.items.ItemBlueprint;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unused")
public class NpcPartAsset implements NpcPart {
    private final Integer npcPartAssetId;

    private final String name;
    private final String description;

    private final Map<Integer, Property> properties;
    private final List<ItemBlueprint> lootList;

    private final Integer partIndex;

    public NpcPartAsset(@JsonProperty("id") @NotNull Integer npcPartAssetId,
                        @JsonProperty("name") @NotNull String name,
                        @JsonProperty("description") @NotNull String description,
                        @JsonProperty("properties") @NotNull Map<Integer, Property> properties,
                        @JsonProperty("lootList") @NotNull List<ItemBlueprint> lootList,
                        @JsonProperty("partIndex") @NotNull Integer partIndex) {
        this.npcPartAssetId = npcPartAssetId;
        this.name = name;
        this.description = description;
        this.properties = properties;
        this.lootList = lootList;
        this.partIndex = partIndex;
    }

    @Override
    @JsonProperty("npcPartAssetId")
    public @NotNull Integer getID() {
        return npcPartAssetId;
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
        return properties.containsKey(PropertyCategories.PC_LEVEL)
                ? properties.get(PropertyCategories.PC_LEVEL).getProperty() : Constants.UNDEFINED_ID;
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
        return hasProperty(propertyKind) ? properties.get(propertyKind).getProperty() : 0;
    }

    @Override
    public @NotNull Integer getProperty(@NotNull Integer propertyKind, @NotNull Integer propertyIndex) {
        return hasProperty(propertyKind) ? properties.get(propertyKind).getProperty(propertyIndex) : 0;
    }

    @Override
    @JsonProperty("properties")
    public @NotNull Map<Integer, Property> getAllProperties() {
        return properties;
    }

    @Override
    public @NotNull Integer getPartIndex() {
        return partIndex;
    }

    @Override
    public @NotNull List<ItemBlueprint> getLootList() {
        return lootList;
    }
}
