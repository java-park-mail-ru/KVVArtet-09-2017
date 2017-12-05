package project.gamemechanics.items;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import project.gamemechanics.components.affectors.Affector;
import project.gamemechanics.components.affectors.AffectorCategories;
import project.gamemechanics.components.properties.Property;
import project.gamemechanics.components.properties.PropertyCategories;
import project.gamemechanics.globals.Constants;
import project.gamemechanics.globals.DigitsPairIndices;
import project.gamemechanics.globals.EquipmentKind;
import project.gamemechanics.interfaces.EquipableItem;

import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class IngameItem implements EquipableItem {
    private final AtomicInteger instanceCounter = new AtomicInteger(0);
    private final Integer itemID;

    private final String name;
    private final String description;

    private final Map<Integer, Property> properties;

    private final Map<Integer, Affector> affectors;

    public static class ItemModel {
        public final Integer id;
        public final String name;
        public final String description;
        @SuppressWarnings("PublicField")
        public final Map<Integer, Property> properties;
        @SuppressWarnings("PublicField")
        public final Map<Integer, Affector> affectors;

        public ItemModel(@NotNull Integer id,
                         @NotNull String name, @NotNull String description,
                         @NotNull Map<Integer, Property> properties,
                         @NotNull Map<Integer, Affector> affectors) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.properties = properties;
            this.affectors = affectors;
        }

        public ItemModel(@NotNull String name, @NotNull String description,
                         @NotNull Map<Integer, Property> properties,
                         @NotNull Map<Integer, Affector> affectors) {
            this(Constants.UNDEFINED_ID, name, description, properties, affectors);
        }
    }

    public IngameItem(@NotNull ItemModel model) {
        itemID = model.id == Constants.UNDEFINED_ID ? instanceCounter.getAndIncrement() : model.id;
        name = model.name;
        description = model.description;
        properties = model.properties;
        affectors = model.affectors;
    }

    @Override
    @JsonIgnore
    public Integer getInstancesCount() {
        return instanceCounter.get();
    }

    @Override
    @JsonProperty("id")
    public Integer getID() {
        return itemID;
    }

    @SuppressWarnings("unused")
    @Override
    public String getName() {
        return name;
    }

    @SuppressWarnings("unused")
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
    public Boolean hasProperty(@NotNull Integer propertyKind) {
        return properties.containsKey(propertyKind);
    }

    @Override
    @JsonIgnore
    public Set<Integer> getAvailableProperties() {
        return properties.keySet();
    }

    @Override
    public Integer getProperty(@NotNull Integer propertyKind, @NotNull Integer propertyIndex) {
        if (!hasAffector(propertyKind)) {
            return Integer.MIN_VALUE;
        }
        return properties.get(propertyKind).getProperty(propertyIndex);
    }

    @Override
    public Integer getProperty(@NotNull Integer propertyKind) {
        if (!hasAffector(propertyKind)) {
            return Integer.MIN_VALUE;
        }
        return properties.get(propertyKind).getProperty();
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
        if (!hasAffector(affectorKind)) {
            return Integer.MIN_VALUE;
        }
        if ((affectorKind & AffectorCategories.AC_REDUCABLE_AFFECTORS) != 0) {
            final Random random = new Random(System.currentTimeMillis());
            return affectors.get(affectorKind).getAffection(DigitsPairIndices.MIN_VALUE_INDEX)
                    + random.nextInt(affectors.get(affectorKind).getAffection(DigitsPairIndices.MAX_VALUE_INDEX)
                    - affectors.get(affectorKind).getAffection(DigitsPairIndices.MIN_VALUE_INDEX));
        }
        if ((affectorKind & AffectorCategories.AC_SINGLE_VALUE_AFFECTORS) != 0) {
            return affectors.get(affectorKind).getAffection();
        }
        return Integer.MIN_VALUE;
    }

    @Override
    public Integer getAffection(@NotNull Integer affectorKind, @NotNull Integer affectionIndex) {
        if ((affectorKind & AffectorCategories.AC_MULTI_VALUE_AFFECTORS) == 0 || !hasAffector(affectorKind)) {
            return Integer.MIN_VALUE;
        }
        return affectors.get(affectorKind).getAffection(affectionIndex);
    }

    @Override
    public Boolean isWeapon() {
        final Integer kind = getProperty(PropertyCategories.PC_ITEM_KIND);
        return kind >= EquipmentKind.EK_SWORD.asInt() && kind <= EquipmentKind.EK_CROSSBOW.asInt();
    }

    @Override
    public Boolean isArmour() {
        final Integer kind = getProperty(PropertyCategories.PC_ITEM_KIND);
        return kind >= EquipmentKind.EK_CLOTH_ARMOUR.asInt() && kind <= EquipmentKind.EK_PLATE_ARMOUR.asInt();
    }

    @Override
    public Boolean isTrinket() {
        final Integer kind = getProperty(PropertyCategories.PC_ITEM_KIND);
        return kind.equals(EquipmentKind.EK_TRINKET.asInt());
    }
}
