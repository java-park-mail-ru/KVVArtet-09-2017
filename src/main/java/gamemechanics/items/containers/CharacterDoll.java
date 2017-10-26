package gamemechanics.items.containers;

import gamemechanics.globals.CharacterRatings;
import gamemechanics.globals.CharacterStats;
import gamemechanics.globals.EquipmentSlots;
import gamemechanics.interfaces.EquipableItem;

import java.util.ArrayList;
import java.util.List;

public class CharacterDoll extends StorageBag {

    private static EmptyBagModel emptyCharacterDoll = new EmptyBagModel("Equipped items",
            "Items put on character", EquipmentSlots.ES_SIZE);

    public CharacterDoll() {
        super(emptyCharacterDoll);
    }

    public CharacterDoll(FilledBagModel model) {
        super(model);
    }

    public Integer getStatBonus(Integer statIndex) {
        Integer statBonus = 0;
        for (EquipableItem item : getContents()) {
            if (item != null) {
                statBonus += item.getStatBonus(statIndex);
            }
        }
        return statBonus;
    }

    public List<Integer> getStatBonuses() {
        List<Integer> statBonuses = new ArrayList<>(CharacterStats.CS_SIZE.asInt());
        for (Integer statIndex = 0; statIndex < CharacterStats.CS_SIZE.asInt(); ++statIndex) {
            statBonuses.set(statIndex, getStatBonus(statIndex));
        }
        return statBonuses;
    }

    public Integer getRatingBonus(Integer ratingIndex) {
        Integer ratingBonus = 0;
        for (EquipableItem item : getContents()) {
            if (item != null) {
                ratingBonus += item.getRatingBonus(ratingIndex);
            }
        }
        return ratingBonus;
    }

    public List<Integer> getRatingBonuses() {
        List<Integer> ratingBonuses = new ArrayList<>(CharacterRatings.CR_SIZE.asInt());
        for (Integer ratingIndex = 0; ratingIndex < CharacterRatings.CR_SIZE.asInt(); ++ratingIndex) {
            ratingBonuses.set(ratingIndex, getRatingBonus(ratingIndex));
        }
        return ratingBonuses;
    }

    public Integer getDamage() {
        Integer damage = 0;
        if (getContents().get(EquipmentSlots.ES_MAINHAND) != null) {
            damage += getContents().get(EquipmentSlots.ES_MAINHAND).getDamage();
        }
        if (getContents().get(EquipmentSlots.ES_OFFHAND) != null) {
            damage += getContents().get(EquipmentSlots.ES_OFFHAND).getDamage();
        }
        return damage;
    }

    public Integer getDefense() {
        Integer defense = 0;
        for (EquipableItem item : getContents()) {
            if (item != null) {
                defense += item.getDefense();
            }
        }
        return defense;
    }
}
