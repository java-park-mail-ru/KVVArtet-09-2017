package gamemechanics.items;

import gamemechanics.components.affectors.Affector;
import gamemechanics.components.affectors.AffectorCategories;
import gamemechanics.components.properties.Property;
import gamemechanics.components.properties.PropertyCategories;
import gamemechanics.globals.DigitsPairIndices;
import gamemechanics.interfaces.EquipableItem;

import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class IngameItem implements EquipableItem {
    private final AtomicInteger instanceCounter = new AtomicInteger(0);
    private final Integer itemID = instanceCounter.getAndIncrement();

    private final String name;
    private final String description;

    private final Map<Integer, Property> properties;

    private final Map<Integer, Affector> affectors;

    public static class ItemModel {
        public String name;
        public String description;
        public Map<Integer, Property> properties;
        public Map<Integer, Affector> affectors;

        public ItemModel(String name, String description,
                         Map<Integer, Property> properties, Map<Integer, Affector> affectors) {
            this.name = name;
            this.description = description;
            this.properties = properties;
            this.affectors = affectors;
        }
    }

    public IngameItem(ItemModel model) {
        name = model.name;
        description = model.description;
        properties = model.properties;
        affectors = model.affectors;
    }

    @Override
    public Integer getInstancesCount() {
        return instanceCounter.get();
    }

    @Override
    public Integer getID() {
        return itemID;
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
    public Integer getLevel() {
        return getProperty(PropertyCategories.PC_LEVEL);
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
    public Integer getProperty(Integer propertyKind, Integer propertyIndex) {
        if (!hasAffector(propertyKind)) {
            return Integer.MIN_VALUE;
        }
        return properties.get(propertyKind).getProperty(propertyIndex);
    }

    @Override
    public Integer getProperty(Integer propertyKind) {
        if (!hasAffector(propertyKind)) {
            return Integer.MIN_VALUE;
        }
        return properties.get(propertyKind).getProperty();
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
    public Integer getAffection(Integer affectorKind) {
        if (!hasAffector(affectorKind)) {
            return Integer.MIN_VALUE;
        }
        if ((affectorKind & AffectorCategories.AC_REDUCABLE_AFFECTORS) != 0) {
            Random random = new Random(System.currentTimeMillis());
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
    public Integer getAffection(Integer affectorKind, Integer affectionIndex) {
        if ((affectorKind & AffectorCategories.AC_MULTI_VALUE_AFFECTORS) == 0 || !hasAffector(affectorKind)) {
            return Integer.MIN_VALUE;
        }
        return affectors.get(affectorKind).getAffection(affectionIndex);
    }

    @Override
    public Boolean isWeapon() {
        return false;
    }

    @Override
    public Boolean isArmour() {
        return false;
    }

    @Override
    public Boolean isTrinket() {
        return false;
    }
}
