package gamemechanics.items.containers;

import gamemechanics.components.affectors.AffectorCategories;
import gamemechanics.globals.CharacterRatings;
import gamemechanics.globals.CharacterStats;
import gamemechanics.globals.EquipmentSlot;
import gamemechanics.interfaces.EquipableItem;

import java.util.ArrayList;
import java.util.List;

public class CharacterDoll extends StorageBag {

    private static EmptyBagModel emptyCharacterDoll = new EmptyBagModel("Equipped items",
            "Items put on character", EquipmentSlot.ES_SIZE);

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
                statBonus += item.getAffection(AffectorCategories.AC_STATS_AFFECTOR, statIndex);
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
                ratingBonus += item.getAffection(AffectorCategories.AC_RATINGS_AFFECTOR, ratingIndex);
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
        if (getContents().get(EquipmentSlot.ES_MAINHAND.asInt()) != null) {
            damage += getContents().get(EquipmentSlot.ES_MAINHAND.asInt())
                    .getAffection(AffectorCategories.AC_DAMAGE_AFFECTOR);
        }
        if (getContents().get(EquipmentSlot.ES_OFFHAND.asInt()) != null) {
            damage += getContents().get(EquipmentSlot.ES_OFFHAND.asInt())
                    .getAffection(AffectorCategories.AC_DAMAGE_AFFECTOR);
        }
        return damage;
    }

    public Integer getDefense() {
        Integer defense = 0;
        for (EquipableItem item : getContents()) {
            if (item != null) {
                defense += item.getAffection(AffectorCategories.AC_DEFENSE_AFFECTOR);
            }
        }
        return defense;
    }
}
