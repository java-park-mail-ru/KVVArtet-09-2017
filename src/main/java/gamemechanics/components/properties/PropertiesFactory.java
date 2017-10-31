package gamemechanics.components.properties;

import gamemechanics.globals.*;

import java.util.ArrayList;
import java.util.HashMap;

public final class PropertiesFactory {
    private PropertiesFactory() {}

    public static Property makeProperty(Integer propertyIndex) {
        Property property = null;
        switch (propertyIndex) {
            case PropertyCategories.PC_STATS:
                property = makeStatsProperty();
                break;
            case PropertyCategories.PC_RATINGS:
                property = makeRatingsProperty();
                break;
            case PropertyCategories.PC_HITPOINTS:
                property = makeHitpointsProperty();
                break;
            case PropertyCategories.PC_XP_POINTS:
                property = makeXpPointsProperty();
                break;
            case PropertyCategories.PC_BASE_DAMAGE:
                property = makeBaseDamageProperty();
                break;
            case PropertyCategories.PC_BASE_DEFENSE:
                property = makeBaseDefenseProperty();
                break;
            case PropertyCategories.PC_LEVEL:
                property = makeLevelProperty();
                break;
            case PropertyCategories.PC_ITEM_KIND:
                property = makeItemKindProperty();
                break;
            case PropertyCategories.PC_ITEM_SLOTS:
                property = makeItemSlotsProperty();
                break;
            case PropertyCategories.PC_ITEM_PRICE:
                property = makeItemPriceProperty();
                break;
            case PropertyCategories.PC_ITEM_RARITY:
                property = makeItemRarityProperty();
                break;
            case PropertyCategories.PC_COORDINATES:
                property = makeCoordinatesProperty();
                break;
            case PropertyCategories.PC_SKILL_POINTS:
                property = makeSkillPointsProperty();
                break;
            case PropertyCategories.PC_ABILITIES_COOLDOWN:
                property = makeAbilitiesCooldownProperty();
                break;
            case PropertyCategories.PC_INITIATIVE:
                property = makeInitiativeProperty();
                break;
            case PropertyCategories.PC_SPEED:
                property = makeSpeedProperty();
                break;
            default:
                break;
        }
        return property;
    }

    private static Property makeStatsProperty() {
        return new ListProperty(new ArrayList<>(CharacterStats.CS_SIZE.asInt()));
    }

    private static Property makeRatingsProperty() {
        return new ListProperty(new ArrayList<>(CharacterRatings.CR_SIZE.asInt()));
    }

    private static Property makeHitpointsProperty() {
        return new ListProperty(new ArrayList<>(DigitsPairIndices.PAIR_SIZE));
    }

    private static Property makeXpPointsProperty() {
        return new ListProperty(new ArrayList<>(DigitsPairIndices.PAIR_SIZE));
    }

    private static Property makeBaseDamageProperty() {
        return new ListProperty(new ArrayList<>(DigitsPairIndices.PAIR_SIZE));
    }

    private static Property makeBaseDefenseProperty() {
        return new ListProperty(new ArrayList<>(DigitsPairIndices.PAIR_SIZE));
    }

    private static Property makeLevelProperty() {
        return new SingleValueProperty(Constants.START_LEVEL);
    }

    private static Property makeItemKindProperty() {
        return new SingleValueProperty(EquipmentKind.EK_UNDEFINED.asInt());
    }

    private static Property makeItemSlotsProperty() {
        return new ListProperty(new ArrayList<>(EquipmentSlot.ES_SIZE));
    }

    private static Property makeItemPriceProperty() {
        return new SingleValueProperty(0);
    }

    private static Property makeItemRarityProperty() {
        return new SingleValueProperty(ItemRarity.IR_COMMON.asInt());
    }

    private static Property makeCoordinatesProperty() {
        return new ListProperty(new ArrayList<>(DigitsPairIndices.PAIR_SIZE));
    }

    private static Property makeSkillPointsProperty() {
        return new SingleValueProperty(0);
    }

    private static Property makeAbilitiesCooldownProperty() {
        return new AbilitiesCooldownProperty(new HashMap<>());
    }

    private static Property makeInitiativeProperty() {
        return new SingleValueProperty(Constants.DEFAULT_INITIATIVE_VALUE);
    }

    private static Property makeSpeedProperty() {
        return new SingleValueProperty(Constants.DEFAULT_ALIVE_ENTITY_SPEED);
    }
}
