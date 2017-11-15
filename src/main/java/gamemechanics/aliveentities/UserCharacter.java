package gamemechanics.aliveentities;

import gamemechanics.components.affectors.AffectorCategories;
import gamemechanics.components.properties.PropertyCategories;
import gamemechanics.flyweights.CharacterRace;
import gamemechanics.globals.*;
import gamemechanics.items.containers.CharacterDoll;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class UserCharacter extends AbstractAliveEntity {
    private static final AtomicInteger instanceCounter = new AtomicInteger(0);
    private final Integer characterID = instanceCounter.getAndIncrement();

    private final CharacterRace characterRace;

    private final CharacterDoll equipment;

    private final Map<Integer, Map<Integer, Integer>> perkRanks;

    public UserCharacter(UserCharacterModel model) {
        super(model);
        characterRace = model.characterRace;
        equipment = model.equipment;
        perkRanks = model.perkRanks;
    }

    public CharacterRace getCharacterRace() {
        return characterRace;
    }

    @Override
    public Integer getID() {
        return characterID;
    }

    @Override
    public Integer getInstancesCount() {
        return instanceCounter.get();
    }

    @Override
    public void levelUp() {
        modifyPropertyByAddition(PropertyCategories.PC_LEVEL, 1);
        List<Integer> nextLevelUpCap = new ArrayList<>(DigitsPairIndices.PAIR_SIZE);
        for (Integer i = 0; i < nextLevelUpCap.size(); ++i) {
            nextLevelUpCap.set(i, getProperty(PropertyCategories.PC_XP_POINTS, i));
        }
        // grant him some skill points to spend on perks
        modifyPropertyByAddition(PropertyCategories.PC_SKILL_POINTS, Constants.SKILL_POINTS_GRANTED_PER_LEVEL);

        // raise his base damage, defense and stats
        modifyPropertyByPercentage(PropertyCategories.PC_BASE_DAMAGE, Constants.STATS_GROWTH_PER_LEVEL);
        modifyPropertyByPercentage(PropertyCategories.PC_BASE_DEFENSE, Constants.STATS_GROWTH_PER_LEVEL);
        modifyPropertyByPercentage(PropertyCategories.PC_HITPOINTS, Constants.STATS_GROWTH_PER_LEVEL);

        // recount hitpoints cap and fully heal the character
        setProperty(PropertyCategories.PC_HITPOINTS, DigitsPairIndices.MAX_VALUE_INDEX,
                calculateActualHealthCap());
        affectHitpoints(getProperty(PropertyCategories.PC_HITPOINTS, DigitsPairIndices.MAX_VALUE_INDEX)
                - getProperty(PropertyCategories.PC_HITPOINTS, DigitsPairIndices.MIN_VALUE_INDEX));
    }

    @Override
    public Integer getDamage() {
        Integer damage = equipment.getDamage();
        if (damage == 0) {
            damage = getProperty(PropertyCategories.PC_BASE_DAMAGE);
        }
        return damage + calculateBonusDamage();
    }

    @Override
    public Integer getDefense() {
        Integer defense = getDefenseWithoutBonuses();
        for (Integer armourKind = EquipmentKind.EK_CLOTH_ARMOUR.asInt();
             armourKind <= EquipmentKind.EK_PLATE_ARMOUR.asInt(); ++armourKind) {
            defense += calculateDefenseBonus(armourKind);
        }
        return defense;
    }

    private Integer calculateBonusDamage() {
        return getStatsDamageBonus() + getItemKindDamageBonus();
    }

    private Integer getStatsDamageBonus() {
        Integer damageStatValue;
        Integer damageStatIndex;
        Boolean isArmed = equipment.getItem(EquipmentSlot.ES_MAINHAND.asInt()) != null;
        // for ranged weapons bonus is based on agility, for melee weapons - on strength
        if (isArmed && equipment.getItem(EquipmentSlot.ES_MAINHAND.asInt())
                .getProperty(PropertyCategories.PC_MAX_DISTANCE) > 1) {
            damageStatValue = equipment.getStatBonus(CharacterStats.CS_AGILITY.asInt());
            damageStatIndex = CharacterStats.CS_AGILITY.asInt();
        } else {
            damageStatValue = equipment.getStatBonus(CharacterStats.CS_STRENGTH.asInt());
            damageStatIndex = CharacterStats.CS_STRENGTH.asInt();
        }
        Float bonusPercentage = calculateStatBonusDamagePercentage(damageStatValue, damageStatIndex);
        Integer baseDamage = equipment.getDamage();
        if (baseDamage == 0) {
            baseDamage = getProperty(PropertyCategories.PC_BASE_DAMAGE);
        }
        return Math.round((Constants.PERCENTAGE_CAP_FLOAT + bonusPercentage) * baseDamage);
    }

    private Integer getItemKindDamageBonus() {
        if (equipment.getItem(EquipmentSlot.ES_MAINHAND.asInt()) != null) {
            Integer weaponKind = equipment.getItem(EquipmentSlot.ES_MAINHAND.asInt())
                    .getProperty(PropertyCategories.PC_ITEM_KIND);
            Float equipmentPercentage = intToFloatPercentage(equipment.getEquipmentAffection(
                    AffectorCategories.AC_DAMAGE_AFFECTOR, weaponKind));
            Float effectsPercentage = intToFloatPercentage(getEffectsAffection(
                    AffectorCategories.AC_DAMAGE_AFFECTOR, weaponKind));
            Float perksPercentage = intToFloatPercentage(getPerksAffection(
                    AffectorCategories.AC_DAMAGE_AFFECTOR, weaponKind));
            return Math.round(equipment.getDamage().floatValue() * (equipmentPercentage
                    + effectsPercentage + perksPercentage));
        }
        return 0;
    }

    private Float calculateStatBonusDamagePercentage(Integer statValue, Integer statIndex) {
        Integer baseStatValue = getProperty(PropertyCategories.PC_STATS, statIndex);
        return ((3.0f * statValue.floatValue()) / baseStatValue.floatValue()) - 1.0f;
    }

    private Integer calculateDefenseBonus(Integer armourKind) {
        Integer bonus = equipment.getDefense(armourKind);
        Float equipmentPercentage = intToFloatPercentage(equipment.getEquipmentAffection(
                AffectorCategories.AC_DEFENSE_AFFECTOR, armourKind));
        Float perksPercentage = intToFloatPercentage(getPerksAffection(
                AffectorCategories.AC_DEFENSE_AFFECTOR, armourKind));
        Float effectsPercentage = intToFloatPercentage(getEffectsAffection(
                AffectorCategories.AC_DEFENSE_AFFECTOR, armourKind));
        return Math.round(bonus.floatValue() * (Constants.PERCENTAGE_CAP_FLOAT + equipmentPercentage
                + perksPercentage + effectsPercentage));
    }

    private Integer getDefenseWithoutBonuses() {
        return getProperty(PropertyCategories.PC_BASE_DEFENSE) + equipment.getDefense();
    }

    private Integer calculateActualHealthCap() {
        Integer healthCap = getProperty(PropertyCategories.PC_BASE_HEALTH);
        healthCap += calculateEnduranceHitpointsBonus();
        return Math.round(healthCap.floatValue() * (getHitpointsBonusPercentage() + Constants.PERCENTAGE_CAP_FLOAT));
    }

    private Integer calculateEnduranceHitpointsBonus() {
        Integer endurance = getProperty(PropertyCategories.PC_STATS, CharacterStats.CS_ENDURANCE.asInt());
        endurance += equipment.getStatBonus(CharacterStats.CS_ENDURANCE.asInt());
        if (endurance < Constants.ENDURANCE_BONUS_JUMP_POINT) {
            return endurance * Constants.HITPOINTS_PER_FIRST_TWENTY_POINTS;
        }
        Integer hitpointsBonus = Constants.ENDURANCE_BONUS_JUMP_POINT * Constants.HITPOINTS_PER_FIRST_TWENTY_POINTS;
        return hitpointsBonus + Constants.HITPOINTS_PER_ENDURANCE_POINT
                * (endurance - Constants.ENDURANCE_BONUS_JUMP_POINT);
    }

    private Float getHitpointsBonusPercentage() {
        Float equipmentPercentage = intToFloatPercentage(equipment.getEquipmentAffection(
                AffectorCategories.AC_HEALTH_AFFECTOR));
        Float perksPercentage = intToFloatPercentage(getPerksAffection(AffectorCategories.AC_HEALTH_AFFECTOR));
        Float effectsPercentage = intToFloatPercentage(getEffectsAffection(AffectorCategories.AC_HEALTH_AFFECTOR));
        return equipmentPercentage + perksPercentage + effectsPercentage;
    }

    private Integer getPerksAffection(Integer affectorKind) {
        Integer perksAffection = 0;
        for (Integer branchID : perkRanks.keySet()) {
            for (Integer perkID : perkRanks.get(branchID).keySet()) {
                if (perkRanks.get(branchID).get(perkID) > 0) {
                    Integer perkBonus = getCharacterRole().getPerk(branchID, perkID)
                            .getRankBasedAffection(affectorKind, perkRanks.get(branchID).get(perkID));
                    if (perkBonus != Integer.MIN_VALUE) {
                        perksAffection += perkBonus;
                    }
                }
            }
        }
        return perksAffection;
    }

    private Integer getPerksAffection(Integer affectorKind, Integer affectionIndex) {
        Integer perksAffection = 0;
        for (Integer branchID : perkRanks.keySet()) {
            for (Integer perkID : perkRanks.get(branchID).keySet()) {
                if (perkRanks.get(branchID).get(perkID) > 0) {
                    Integer perkBonus = getCharacterRole().getPerk(branchID, perkID)
                            .getRankBasedAffection(affectorKind,
                                    affectionIndex, perkRanks.get(branchID).get(perkID));
                    if (perkBonus != Integer.MIN_VALUE) {
                        perksAffection += perkBonus;
                    }
                }
            }
        }
        return perksAffection;
    }
}
