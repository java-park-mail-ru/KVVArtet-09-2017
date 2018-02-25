package project.gamemechanics.items;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import project.gamemechanics.components.affectors.Affector;
import project.gamemechanics.components.affectors.AffectorCategories;
import project.gamemechanics.components.properties.Property;
import project.gamemechanics.components.properties.PropertyCategories;
import project.gamemechanics.components.properties.SingleValueProperty;
import project.gamemechanics.globals.Constants;
import project.gamemechanics.globals.DigitsPairIndices;
import project.gamemechanics.globals.EquipmentKind;
import project.gamemechanics.interfaces.EquipableItem;
import project.gamemechanics.resources.pcg.items.ItemBlueprint;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class IngameItem implements EquipableItem {
    private final AtomicInteger instanceCounter = new AtomicInteger(0);
    private final Integer itemID;

    private final String name;
    private final String description;

    private final Map<Integer, Property> properties = new HashMap<>();

    private final Map<Integer, Affector> affectors = new HashMap<>();

    @SuppressWarnings("SameParameterValue")
    public static class ItemModel {
        // CHECKSTYLE:OFF
        final Integer id;
        final String name;
        final String description;
        final Map<Integer, Property> properties = new HashMap<>();
        final Map<Integer, Affector> affectors = new HashMap<>();
        // CHECKSTYLE:ON

        public ItemModel(@NotNull Integer id,
                         @NotNull String name, @NotNull String description,
                         @NotNull Map<Integer, Property> properties,
                         @NotNull Map<Integer, Affector> affectors) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.properties.putAll(properties);
            this.affectors.putAll(affectors);
        }

        public ItemModel(@NotNull String name, @NotNull String description,
                         @NotNull Map<Integer, Property> properties,
                         @NotNull Map<Integer, Affector> affectors) {
            this(Constants.UNDEFINED_ID, name, description, properties, affectors);
        }
    }

    public IngameItem(@NotNull ItemModel model) {
        itemID = model.id == Constants.UNDEFINED_ID
                ? instanceCounter.getAndIncrement() : model.id;
        name = model.name;
        description = model.description;
        properties.putAll(model.properties);
        affectors.putAll(model.affectors);
    }

    @Override
    @JsonIgnore
    public @NotNull Integer getInstancesCount() {
        return instanceCounter.get();
    }

    @Override
    @JsonProperty("id")
    public @NotNull Integer getID() {
        return itemID;
    }

    @SuppressWarnings("unused")
    @Override
    public @NotNull String getName() {
        return name;
    }

    @SuppressWarnings("unused")
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
    public @NotNull Boolean hasProperty(@NotNull Integer propertyKind) {
        return properties.containsKey(propertyKind);
    }

    @Override
    @JsonIgnore
    public @NotNull Set<Integer> getAvailableProperties() {
        return properties.keySet();
    }

    @Override
    public @NotNull Integer getProperty(@NotNull Integer propertyKind,
                                        @NotNull Integer propertyIndex) {
        if (!hasProperty(propertyKind)) {
            return Integer.MIN_VALUE;
        }
        return properties.get(propertyKind).getProperty(propertyIndex);
    }

    @Override
    public @NotNull Integer getProperty(@NotNull Integer propertyKind) {
        if (!hasProperty(propertyKind)) {
            return Integer.MIN_VALUE;
        }
        return properties.get(propertyKind).getProperty();
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
        if (!hasAffector(affectorKind)) {
            return Integer.MIN_VALUE;
        }
        if ((affectorKind & AffectorCategories.AC_REDUCABLE_AFFECTORS) != 0) {
            final Random random = new Random(System.currentTimeMillis());
            return affectors.get(affectorKind).getAffection(
                    DigitsPairIndices.MIN_VALUE_INDEX)
                    + random.nextInt(affectors.get(affectorKind)
                    .getAffection(DigitsPairIndices.MAX_VALUE_INDEX)
                    - affectors.get(affectorKind).getAffection(
                            DigitsPairIndices.MIN_VALUE_INDEX));
        }
        if ((affectorKind & AffectorCategories.AC_SINGLE_VALUE_AFFECTORS) != 0) {
            return affectors.get(affectorKind).getAffection();
        }
        return Integer.MIN_VALUE;
    }

    @Override
    public @NotNull Integer getAffection(@NotNull Integer affectorKind, @NotNull Integer affectionIndex) {
        if ((affectorKind & AffectorCategories.AC_MULTI_VALUE_AFFECTORS) == 0
                || !hasAffector(affectorKind)) {
            return Integer.MIN_VALUE;
        }
        return affectors.get(affectorKind).getAffection(affectionIndex);
    }

    @Override
    public @NotNull Boolean isWeapon() {
        final Integer kind = getProperty(PropertyCategories.PC_ITEM_KIND);
        return kind >= EquipmentKind.EK_SWORD.asInt()
                && kind <= EquipmentKind.EK_CROSSBOW.asInt();
    }

    @Override
    public @NotNull Boolean isArmour() {
        final Integer kind = getProperty(PropertyCategories.PC_ITEM_KIND);
        return kind >= EquipmentKind.EK_CLOTH_ARMOUR.asInt()
                && kind <= EquipmentKind.EK_PLATE_ARMOUR.asInt();
    }

    @Override
    public @NotNull Boolean isTrinket() {
        final Integer kind = getProperty(PropertyCategories.PC_ITEM_KIND);
        return kind.equals(EquipmentKind.EK_TRINKET.asInt());
    }

    @Override
    @JsonIgnore
    public @NotNull ItemBlueprint pack() {
        final Map<Integer, Property> blueprintProps = new HashMap<>();

        blueprintProps.put(PropertyCategories.PC_ITEM_ID,
                new SingleValueProperty(itemID));
        blueprintProps.put(PropertyCategories.PC_LEVEL,
                properties.get(PropertyCategories.PC_LEVEL));
        blueprintProps.put(PropertyCategories.PC_ITEM_PARTS_IDS,
                properties.get(PropertyCategories.PC_ITEM_PARTS_IDS));
        blueprintProps.put(PropertyCategories.PC_ITEM_PARTS_RARITIES,
                properties.get(PropertyCategories.PC_ITEM_PARTS_RARITIES));
        blueprintProps.put(PropertyCategories.PC_ITEM_KIND,
                properties.get(PropertyCategories.PC_ITEM_KIND));

        return new ItemBlueprint(blueprintProps);
    }
}
