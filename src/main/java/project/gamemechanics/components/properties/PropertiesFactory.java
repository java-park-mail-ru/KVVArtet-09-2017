package project.gamemechanics.components.properties;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import project.gamemechanics.battlefield.map.tilesets.TilesetShapes;
import project.gamemechanics.globals.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public final class PropertiesFactory {
    private PropertiesFactory() {
    }

    @SuppressWarnings("OverlyComplexMethod")
    public static @Nullable Property makeProperty(@NotNull Integer propertyIndex) {
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
            case PropertyCategories.PC_CASH_AMOUNT:
                property = makeCashAmountProperty();
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
            case PropertyCategories.PC_SQUAD_ID:
                property = makeSquadIdProperty();
                break;
            case PropertyCategories.PC_OWNER_ID:
                property = makeOwnerIdProperty();
                break;
            case PropertyCategories.PC_SKILL_POINTS:
                property = makeSkillPointsProperty();
                break;
            case PropertyCategories.PC_BASE_HEALTH:
                property = makeBaseHitpointsProperty();
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
            case PropertyCategories.PC_MAX_DISTANCE:
                property = makeMaxDistanceProperty();
                break;
            case PropertyCategories.PC_AREA:
                property = makeAreaProperty();
                break;
            case PropertyCategories.PC_INFLICTED_CATEGORIES:
                property = makeInflictedCategoriesProperty();
                break;
            case PropertyCategories.PC_AREA_SHAPE:
                property = makeAreaShapeProperty();
                break;
            case PropertyCategories.PC_ITEM_BLUEPRINT_ID:
                property = makeItemBlueprintIdProperty();
                break;
            case PropertyCategories.PC_CHARACTER_ROLE_ID:
                property = makeCharacterRoleProperty();
                break;
            case PropertyCategories.PC_CHARACTER_RACE_ID:
                property = makeCharacterRaceProperty();
                break;
            case PropertyCategories.PC_AVAILABLE_EQUIPMENT:
                property = makeAvailableEquipmentProperty();
                break;
            case PropertyCategories.PC_ACTIVE_ROLE:
                property = makeActiveRoleProperty();
                break;
            case PropertyCategories.PC_AVAILABLE_ROLES:
                property = makeAvailableRolesProperty();
                break;
            case PropertyCategories.PC_PARTY_ID:
                property = makePartyIdProperty();
                break;
            case PropertyCategories.PC_INSTANCE_ID:
                property = makeInstanceId();
                break;
            case PropertyCategories.PC_STATISTICS:
                property = makeStatisticsProperty();
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

    private static Property makeSquadIdProperty() {
        return new SingleValueProperty(Constants.UNDEFINED_SQUAD_ID);
    }

    private static Property makeOwnerIdProperty() {
        return new SingleValueProperty(Constants.AI_CONTROLLED_NPC_ID);
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

    private static Property makeBaseHitpointsProperty() {
        return new SingleValueProperty(Constants.INITIAL_HITPOINTS_CAP);
    }

    private static Property makeCashAmountProperty() {
        return new SingleValueProperty(0);
    }

    private static Property makeMaxDistanceProperty() {
        return new SingleValueProperty(0);
    }

    private static Property makeAreaProperty() {
        return new SingleValueProperty(0);
    }

    private static Property makeAreaShapeProperty() {
        return new SingleValueProperty(TilesetShapes.TS_POINT);
    }

    private static Property makeInflictedCategoriesProperty() {
        return new SetProperty(new HashSet<>());
    }

    private static Property makeItemBlueprintIdProperty() {
        return new SingleValueProperty(0);
    }

    private static Property makeCharacterRoleProperty() {
        return new SingleValueProperty(0);
    }

    private static Property makeCharacterRaceProperty() {
        return new SingleValueProperty(0);
    }

    private static Property makeAvailableEquipmentProperty() {
        return new SetProperty(new HashSet<>(PropertyCategories.PC_AVAILABLE_EQUIPMENT));
    }

    private static Property makeActiveRoleProperty() {
        return new SingleValueProperty(Constants.UNDEFINED_ID);
    }

    private static Property makeAvailableRolesProperty() {
        return new SetProperty(new HashSet<>(CharacterRoleIds.CR_SIZE));
    }

    private static Property makePartyIdProperty() {
        return new SingleValueProperty(Constants.UNDEFINED_ID);
    }

    private static Property makeInstanceId() {
        return new SingleValueProperty(Constants.UNDEFINED_ID);
    }

    private static Property makeStatisticsProperty() {
        return new ListProperty(new ArrayList<>(UserCharacterStatistics.US_SIZE));
    }
}
