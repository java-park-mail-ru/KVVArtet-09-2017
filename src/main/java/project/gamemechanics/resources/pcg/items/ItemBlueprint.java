package project.gamemechanics.resources.pcg.items;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.Nullable;
import project.gamemechanics.components.properties.MapProperty;
import project.gamemechanics.components.properties.Property;
import project.gamemechanics.components.properties.PropertyCategories;
import project.gamemechanics.components.properties.SingleValueProperty;
import project.gamemechanics.globals.ItemRarity;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@SuppressWarnings("unused")
public final class ItemBlueprint {
    private static final Map<Integer, Integer> RANDOM_ITEM_RARITIES
            = initializeDefaultRarities();

    private final Map<Integer, Property> properties;

    public ItemBlueprint(@JsonProperty("properties")
                         @NotNull Map<Integer, Property> properties) {
        this.properties = properties;
    }

    public ItemBlueprint(@NotNull Integer dropChance,
                         @NotNull Map<Integer, Property> properties,
                         @NotNull Map<Integer, Integer> itemParts,
                         @NotNull Map<Integer, Integer> itemPartsRarities) {
        this.properties = properties;

        if (properties.containsKey(PropertyCategories.PC_DROP_CHANCE)) {
            properties.get(PropertyCategories.PC_DROP_CHANCE).setSingleProperty(dropChance);
        } else {
            properties.put(PropertyCategories.PC_DROP_CHANCE,
                    new SingleValueProperty(dropChance));
        }

        if (properties.containsKey(PropertyCategories.PC_ITEM_PARTS_IDS)) {
            properties.replace(PropertyCategories.PC_ITEM_PARTS_IDS,
                    new MapProperty(itemParts));
        } else {
            properties.put(PropertyCategories.PC_ITEM_PARTS_IDS,
                    new MapProperty(itemParts));
        }

        if (properties.containsKey(PropertyCategories.PC_ITEM_PARTS_RARITIES)) {
            properties.replace(PropertyCategories.PC_ITEM_PARTS_RARITIES,
                    new MapProperty(itemPartsRarities));
        } else {
            properties.put(PropertyCategories.PC_ITEM_PARTS_RARITIES,
                    new MapProperty(itemPartsRarities));
        }
    }

    public ItemBlueprint(@NotNull Integer dropChance,
                         @NotNull Map<Integer, Property> properties,
                         @NotNull Map<Integer, Integer> itemParts) {
        this(dropChance, properties, itemParts, RANDOM_ITEM_RARITIES);
    }

    @JsonIgnore
    public @NotNull Integer getDropChance() {
        return properties.get(PropertyCategories.PC_DROP_CHANCE).getProperty();
    }

    public @NotNull Map<Integer, Property> getProperties() {
        return properties;
    }

    @JsonIgnore
    public @NotNull Set<Integer> getAvailableProperties() {
        return properties.keySet();
    }

    @JsonIgnore
    public @Nullable Map<Integer, Integer> getItemParts() {
        return properties.get(PropertyCategories.PC_ITEM_PARTS_IDS).getPropertyMap();
    }

    @JsonIgnore
    public @Nullable Map<Integer, Integer> getItemPartsRarities() {
        return properties.get(PropertyCategories.PC_ITEM_PARTS_RARITIES).getPropertyMap();
    }

    @JsonIgnore
    public @NotNull Set<Integer> getAvailableItemParts() {
        return Objects.requireNonNull(getItemParts()).keySet();
    }

    private static Map<Integer, Integer> initializeDefaultRarities() {
        final Map<Integer, Integer> defaultRarities = new HashMap<>();
        for (Integer i = 0; i < ItemPart.ITEM_PARTS_COUNT; ++i) {
            defaultRarities.put(i, ItemRarity.IR_UNDEFINED.asInt());
        }
        return defaultRarities;
    }
}
