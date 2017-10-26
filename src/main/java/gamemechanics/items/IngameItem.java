package gamemechanics.items;

import gamemechanics.components.affectors.Affector;
import gamemechanics.components.affectors.AffectorCategories;
import gamemechanics.globals.DigitsPairIndices;
import gamemechanics.globals.EquipmentKind;
import gamemechanics.globals.ItemRarity;
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

    private Integer level;

    private Integer price;
    private final ItemRarity rarity;
    private final Integer kind;

    private final Map<Integer, Affector> affectors;

    public static class ItemModel {
        public String name;
        public String description;
        public Integer level;
        public Integer price;
        public ItemRarity rarity;
        public Integer kind;
        public Map<Integer, Affector> affectors;

        public ItemModel(String name, String description, Integer level, Integer price,
                         ItemRarity rarity, Integer kind, Map<Integer, Affector> affectors) {
            this.name = name;
            this.description = description;
            this.level = level;
            this.price = price;
            this.rarity = rarity;
            this.kind = kind;
            this.affectors = affectors;
        }
    }

    public IngameItem(ItemModel model) {
        name = model.name;
        description = model.description;
        level = model.level;
        price = model.price;
        rarity = model.rarity;
        kind = model.kind;
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
        return level;
    }

    @Override
    public Integer getPrice() {
        return price;
    }

    @Override
    public ItemRarity getRarity() {
        return rarity;
    }

    @Override
    public Integer getKind() {
        return kind;
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
        return (kind & EquipmentKind.EK_WEAPON.asInt()) > 0;
    }

    @Override
    public Boolean isArmour() {
        return (kind & EquipmentKind.EK_ARMOUR.asInt()) > 0;
    }

    @Override
    public Boolean isTrinket() {
        return (kind & EquipmentKind.EK_TRINKET.asInt()) > 0;
    }
}
