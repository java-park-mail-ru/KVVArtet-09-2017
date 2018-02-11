package project.gamemechanics.aliveentities;

import project.gamemechanics.aliveentities.helpers.ExperienceCalculator;
import project.gamemechanics.components.affectors.AffectorCategories;
import project.gamemechanics.components.mappers.PropertyToAffectorMapper;
import project.gamemechanics.components.properties.PropertyCategories;
import project.gamemechanics.flyweights.CharacterRace;
import project.gamemechanics.globals.*;
import project.gamemechanics.items.containers.CharacterDoll;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@SuppressWarnings("ALL")
public class UserCharacter extends AbstractAliveEntity {
    private static final AtomicInteger INSTANCE_COUNTER = new AtomicInteger(0);
    private final Integer characterID = INSTANCE_COUNTER.getAndIncrement();

    private final CharacterRace characterRace;

    private final CharacterDoll equipment;

    private final Map<Integer, Map<Integer, Integer>> perkRanks;

    public UserCharacter(@NotNull UserCharacterModel model) {
        super(model);
        characterRace = model.characterRace;
        equipment = model.equipment;
        perkRanks = model.perkRanks;
    }

    public @NotNull CharacterRace getCharacterRace() {
        return characterRace;
    }

    @Override
    public @NotNull Integer getID() {
        return characterID;
    }

    @Override
    public @NotNull Integer getInstancesCount() {
        return INSTANCE_COUNTER.get();
    }

    @Override
    public void levelUp() {
        modifyPropertyByAddition(PropertyCategories.PC_LEVEL, 1);
        final List<Integer> nextLevelUpCap = new ArrayList<>(DigitsPairIndices.PAIR_SIZE);
        setProperty(PropertyCategories.PC_XP_POINTS, DigitsPairIndices.MAX_VALUE_INDEX,
                ExperienceCalculator.getNewLevelUpCap(getProperty(PropertyCategories.PC_LEVEL)));
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
    public @NotNull Integer getProperty(@NotNull Integer propertyKind, @NotNull Integer propertyIndex) {
        return super.getProperty(propertyKind, propertyIndex)
                + equipment.getEquipmentAffection(PropertyToAffectorMapper.getAffectorKind(propertyKind),
                propertyIndex) + getPerksAffection(PropertyToAffectorMapper.getAffectorKind(propertyKind),
                propertyIndex) + getEffectsAffection(PropertyToAffectorMapper.getAffectorKind(propertyKind),
                propertyIndex);
    }

    @Override
    public @NotNull Integer getDamage() {
        Integer damage = equipment.getDamage();
        if (damage == 0) {
            damage = getProperty(PropertyCategories.PC_BASE_DAMAGE);
        }
        return damage + calculateBonusDamage();
    }

    @Override
    public @NotNull Integer getDefense() {
        Integer defense = getDefenseWithoutBonuses();
        for (Integer armourKind = EquipmentKind.EK_CLOTH_ARMOUR.asInt();
             armourKind <= EquipmentKind.EK_PLATE_ARMOUR.asInt(); ++armourKind) {
            defense += calculateDefenseBonus(armourKind);
        }
        return defense;
    }

    private @NotNull Integer calculateBonusDamage() {
        return getStatsDamageBonus() + getItemKindDamageBonus();
    }

    private @NotNull Integer getStatsDamageBonus() {
        final Integer damageStatValue;
        final Integer damageStatIndex;
        final Boolean isArmed = equipment.getItem(EquipmentSlot.ES_MAINHAND.asInt()) != null;
        // for ranged weapons bonus is based on agility, for melee weapons - on strength
        if (isArmed && equipment.getItem(EquipmentSlot.ES_MAINHAND.asInt())
                .getProperty(PropertyCategories.PC_MAX_DISTANCE) > 1) {
            damageStatValue = equipment.getStatBonus(CharacterStats.CS_AGILITY.asInt());
            damageStatIndex = CharacterStats.CS_AGILITY.asInt();
        } else {
            damageStatValue = equipment.getStatBonus(CharacterStats.CS_STRENGTH.asInt());
            damageStatIndex = CharacterStats.CS_STRENGTH.asInt();
        }
        final Float bonusPercentage = calculateStatBonusDamagePercentage(damageStatValue, damageStatIndex);
        Integer baseDamage = equipment.getDamage();
        if (baseDamage == 0) {
            baseDamage = getProperty(PropertyCategories.PC_BASE_DAMAGE);
        }
        return Math.round((Constants.PERCENTAGE_CAP_FLOAT + bonusPercentage) * baseDamage);
    }

    private @NotNull Integer getItemKindDamageBonus() {
        if (equipment.getItem(EquipmentSlot.ES_MAINHAND.asInt()) != null) {
            final Integer weaponKind = equipment.getItem(EquipmentSlot.ES_MAINHAND.asInt())
                    .getProperty(PropertyCategories.PC_ITEM_KIND);
            final Float equipmentPercentage = intToFloatPercentage(equipment.getEquipmentAffection(
                    AffectorCategories.AC_DAMAGE_AFFECTOR, weaponKind));
            final Float effectsPercentage = intToFloatPercentage(getEffectsAffection(
                    AffectorCategories.AC_DAMAGE_AFFECTOR, weaponKind));
            final Float perksPercentage = intToFloatPercentage(getPerksAffection(
                    AffectorCategories.AC_DAMAGE_AFFECTOR, weaponKind));
            return Math.round(equipment.getDamage().floatValue() * (equipmentPercentage
                    + effectsPercentage + perksPercentage));
        }
        return 0;
    }

    private @NotNull Float calculateStatBonusDamagePercentage(@NotNull Integer statValue,
                                                              @NotNull Integer statIndex) {
        final Integer baseStatValue = getProperty(PropertyCategories.PC_STATS, statIndex);
        final Float statBonusBaseMultiplier = 3.0f;
        final Float statBonusBaseConstant = 1.0f;
        return ((statBonusBaseMultiplier * statValue.floatValue()) / baseStatValue.floatValue())
                - statBonusBaseConstant;
    }

    private @NotNull Integer calculateDefenseBonus(@NotNull Integer armourKind) {
        final Integer bonus = equipment.getDefense(armourKind);
        final Float equipmentPercentage = intToFloatPercentage(equipment.getEquipmentAffection(
                AffectorCategories.AC_DEFENSE_AFFECTOR, armourKind));
        final Float perksPercentage = intToFloatPercentage(getPerksAffection(
                AffectorCategories.AC_DEFENSE_AFFECTOR, armourKind));
        final Float effectsPercentage = intToFloatPercentage(getEffectsAffection(
                AffectorCategories.AC_DEFENSE_AFFECTOR, armourKind));
        return Math.round(bonus.floatValue() * (Constants.PERCENTAGE_CAP_FLOAT + equipmentPercentage
                + perksPercentage + effectsPercentage));
    }

    private @NotNull Integer getDefenseWithoutBonuses() {
        return getProperty(PropertyCategories.PC_BASE_DEFENSE) + equipment.getDefense();
    }

    private @NotNull Integer calculateActualHealthCap() {
        Integer healthCap = getProperty(PropertyCategories.PC_BASE_HEALTH);
        healthCap += calculateEnduranceHitpointsBonus();
        return Math.round(healthCap.floatValue() * (getHitpointsBonusPercentage() + Constants.PERCENTAGE_CAP_FLOAT));
    }

    private @NotNull Integer calculateEnduranceHitpointsBonus() {
        Integer endurance = getProperty(PropertyCategories.PC_STATS, CharacterStats.CS_ENDURANCE.asInt());
        endurance += equipment.getStatBonus(CharacterStats.CS_ENDURANCE.asInt());
        if (endurance < Constants.ENDURANCE_BONUS_JUMP_POINT) {
            return endurance * Constants.HITPOINTS_PER_FIRST_TWENTY_POINTS;
        }
        final Integer hitpointsBonus = Constants.ENDURANCE_BONUS_JUMP_POINT * Constants.HITPOINTS_PER_FIRST_TWENTY_POINTS;
        return hitpointsBonus + Constants.HITPOINTS_PER_ENDURANCE_POINT
                * (endurance - Constants.ENDURANCE_BONUS_JUMP_POINT);
    }

    private @NotNull Float getHitpointsBonusPercentage() {
        final Float equipmentPercentage = intToFloatPercentage(equipment.getEquipmentAffection(
                AffectorCategories.AC_HEALTH_AFFECTOR));
        final Float perksPercentage = intToFloatPercentage(getPerksAffection(AffectorCategories.AC_HEALTH_AFFECTOR));
        final Float effectsPercentage = intToFloatPercentage(getEffectsAffection(AffectorCategories.AC_HEALTH_AFFECTOR));
        return equipmentPercentage + perksPercentage + effectsPercentage;
    }

    @SuppressWarnings("SameParameterValue")
    private @NotNull Integer getPerksAffection(@NotNull Integer affectorKind) {
        Integer perksAffection = 0;
        for (Integer branchID : perkRanks.keySet()) {
            for (Integer perkID : perkRanks.get(branchID).keySet()) {
                if (perkRanks.get(branchID).get(perkID) > 0) {
                    final Integer perkBonus = getCharacterRole().getPerk(branchID, perkID)
                            .getRankBasedAffection(affectorKind, perkRanks.get(branchID).get(perkID));
                    if (perkBonus != Integer.MIN_VALUE) {
                        perksAffection += perkBonus;
                    }
                }
            }
        }
        return perksAffection;
    }

    private @NotNull Integer getPerksAffection(@NotNull Integer affectorKind,
                                               @NotNull Integer affectionIndex) {
        Integer perksAffection = 0;
        for (Integer branchID : perkRanks.keySet()) {
            for (Integer perkID : perkRanks.get(branchID).keySet()) {
                if (perkRanks.get(branchID).get(perkID) > 0) {
                    final Integer perkBonus = getCharacterRole().getPerk(branchID, perkID)
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
