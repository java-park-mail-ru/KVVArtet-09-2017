package project.gamemechanics.resources.pcg.items;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import project.gamemechanics.components.properties.Property;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unused")
public final class ItemBlueprint {
    private final Integer dropChance;
    private final Map<Integer, Property> properties;
    private final Map<Integer, Integer> itemParts = new HashMap<>();

    public ItemBlueprint(@JsonProperty("dropChance") @NotNull Integer dropChance,
                         @JsonProperty("properties") @NotNull Map<Integer, Property> properties,
                         @JsonProperty("itemParts") @NotNull Map<Integer, Integer> itemParts) {
        this.dropChance = dropChance;
        this.properties = properties;
        this.itemParts.putAll(itemParts);
    }

    public @NotNull Integer getDropChance() {
        return dropChance;
    }

    public @NotNull Map<Integer, Property> getProperties() {
        return properties;
    }

    @JsonIgnore
    public @NotNull Set<Integer> getAvailableProperties() {
        return properties.keySet();
    }

    public @NotNull Map<Integer, Integer> getItemParts() {
        return itemParts;
    }

    @JsonIgnore
    public @NotNull Set<Integer> getAvailableItemParts() {
        return itemParts.keySet();
    }
}
