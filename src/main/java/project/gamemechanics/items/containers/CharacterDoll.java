package project.gamemechanics.items.containers;

import project.gamemechanics.components.affectors.AffectorCategories;
import project.gamemechanics.components.properties.PropertyCategories;
import project.gamemechanics.globals.CharacterRatings;
import project.gamemechanics.globals.CharacterStats;
import project.gamemechanics.globals.EquipmentSlot;
import project.gamemechanics.interfaces.EquipableItem;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("unused")
public class CharacterDoll extends StorageBag {

    private static final EmptyBagModel EMPTY_CHARACTER_DOLL = new EmptyBagModel("Equipped items",
            "Items put on character", EquipmentSlot.ES_SIZE);

    public CharacterDoll() {
        super(EMPTY_CHARACTER_DOLL);
    }

    public CharacterDoll(@NotNull FilledBagModel model) {
        super(model);
    }

    public @NotNull Integer getStatBonus(@NotNull Integer statIndex) {
        Integer statBonus = 0;
        for (EquipableItem item : getContents()) {
            if (item != null) {
                statBonus += item.getAffection(AffectorCategories.AC_STATS_AFFECTOR, statIndex);
            }
        }
        return statBonus;
    }

    public @NotNull List<Integer> getStatBonuses() {
        final List<Integer> statBonuses = new ArrayList<>(CharacterStats.CS_SIZE.asInt());
        for (Integer statIndex = 0; statIndex < CharacterStats.CS_SIZE.asInt(); ++statIndex) {
            statBonuses.set(statIndex, getStatBonus(statIndex));
        }
        return statBonuses;
    }

    private @NotNull Integer getRatingBonus(@NotNull Integer ratingIndex) {
        Integer ratingBonus = 0;
        for (EquipableItem item : getContents()) {
            if (item != null) {
                ratingBonus += item.getAffection(AffectorCategories.AC_RATINGS_AFFECTOR, ratingIndex);
            }
        }
        return ratingBonus;
    }

    public @NotNull List<Integer> getRatingBonuses() {
        final List<Integer> ratingBonuses = new ArrayList<>(CharacterRatings.CR_SIZE.asInt());
        for (Integer ratingIndex = 0; ratingIndex < CharacterRatings.CR_SIZE.asInt(); ++ratingIndex) {
            ratingBonuses.set(ratingIndex, getRatingBonus(ratingIndex));
        }
        return ratingBonuses;
    }

    public @NotNull Integer getDamage() {
        Integer damage = 0;
        if (getContents().get(EquipmentSlot.ES_MAINHAND.asInt()) != null) {
            damage += getContents().get(EquipmentSlot.ES_MAINHAND.asInt())
                    .getAffection(AffectorCategories.AC_WEAPON_DAMAGE_AFFECTOR);
        }
        if (getContents().get(EquipmentSlot.ES_OFFHAND.asInt()) != null) {
            damage += getContents().get(EquipmentSlot.ES_OFFHAND.asInt())
                    .getAffection(AffectorCategories.AC_WEAPON_DAMAGE_AFFECTOR);
        }
        return damage;
    }

    public @NotNull Integer getDefense() {
        Integer defense = 0;
        for (EquipableItem item : getContents()) {
            if (item != null) {
                defense += item.getAffection(AffectorCategories.AC_ARMOUR_DEFENSE_AFFECTOR);
            }
        }
        return defense;
    }

    public @NotNull Integer getDefense(@NotNull Integer armourKind) {
        Integer armourKindDefense = 0;
        for (EquipableItem item : getContents()) {
            if (item != null) {
                if (Objects.equals(item.getProperty(PropertyCategories.PC_ITEM_KIND), armourKind)) {
                    armourKindDefense += item.getAffection(AffectorCategories.AC_ARMOUR_DEFENSE_AFFECTOR);
                }
            }
        }
        return armourKindDefense;
    }

    public @NotNull Integer getEquipmentAffection(@NotNull Integer affectorKind) {
        Integer affection = 0;
        for (EquipableItem item : getContents()) {
            if (item != null) {
                final Integer itemBonus = item.getAffection(affectorKind);
                if (itemBonus != Integer.MIN_VALUE) {
                    affection += itemBonus;
                }
            }
        }
        return affection;
    }

    public @NotNull Integer getEquipmentAffection(@NotNull Integer affectorKind, @NotNull Integer affectionIndex) {
        Integer affection = 0;
        for (EquipableItem item : getContents()) {
            if (item != null) {
                final Integer itemBonus = item.getAffection(affectorKind, affectionIndex);
                if (itemBonus != Integer.MIN_VALUE) {
                    affection += itemBonus;
                }
            }
        }
        return affection;
    }
}
