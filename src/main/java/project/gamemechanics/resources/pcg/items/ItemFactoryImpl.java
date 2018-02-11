package project.gamemechanics.resources.pcg.items;

import com.fasterxml.jackson.annotation.JsonProperty;
import project.gamemechanics.components.affectors.*;
import project.gamemechanics.components.properties.*;
import project.gamemechanics.globals.Constants;
import project.gamemechanics.globals.EquipmentKind;
import project.gamemechanics.globals.ItemRarity;
import project.gamemechanics.interfaces.EquipableItem;
import project.gamemechanics.items.IngameItem;

import javax.validation.constraints.NotNull;
import java.util.*;

public class ItemFactoryImpl implements ItemsFactory {
    private final Map<Integer, Map<Integer, ItemPart>> itemParts;

    public ItemFactoryImpl(@JsonProperty("itemParts") @NotNull Map<Integer, Map<Integer, ItemPart>> itemParts) {
        this.itemParts = itemParts;
    }

    @Override
    public @NotNull EquipableItem makeItem(@NotNull ItemBlueprint blueprint) {
        final Random random = new Random(System.currentTimeMillis());
        final Integer level = blueprint.getProperties().containsKey(PropertyCategories.PC_LEVEL)
                ? blueprint.getProperties().get(PropertyCategories.PC_LEVEL).getProperty()
                : random.nextInt(Constants.MAX_LEVEL) + Constants.START_LEVEL;

        final Integer rarity = blueprint.getProperties().containsKey(PropertyCategories.PC_ITEM_RARITY)
                ? blueprint.getProperties().get(PropertyCategories.PC_ITEM_RARITY).getProperty()
                : ItemRarity.IR_UNDEFINED.asInt();

        final Integer kind = blueprint.getProperties().containsKey(PropertyCategories.PC_ITEM_KIND)
                ? blueprint.getProperties().get(PropertyCategories.PC_ITEM_KIND).getProperty()
                : EquipmentKind.EK_UNDEFINED.asInt();

        final List<ItemPart> itemPartsList = getItemParts(rarity, kind, blueprint.getItemParts());
        return new IngameItem(makeItemModel(level, rarity, itemPartsList));
    }

    private @NotNull List<ItemPart> getItemParts(@NotNull Integer rarity, @NotNull Integer kind,
                                                 @NotNull Map<Integer, Integer> itemPartsList) {
        final Random random = new Random(System.currentTimeMillis());
        final List<ItemPart> parts = new ArrayList<>(ItemPart.ITEM_PARTS_COUNT);

        for (Integer itemPartIndex = ItemPart.FIRST_PART_ID; itemPartIndex < ItemPart.ITEM_PARTS_COUNT;
             ++itemPartIndex) {
            final Map<Integer, ItemPart> partsScope = this.itemParts.get(itemPartIndex);
            final List<Integer> scopePartsIds = new ArrayList<>(partsScope.keySet());

            Integer partId = itemPartsList.get(itemPartIndex);
            if (partId < Constants.MIN_ID_VALUE) {
                while (true) {
                    final Integer tmpId = scopePartsIds.get(random.nextInt(scopePartsIds.size()));
                    if (kind.equals(EquipmentKind.EK_UNDEFINED.asInt())) {
                        kind = partsScope.get(tmpId).getProperty(PropertyCategories.PC_ITEM_KIND);
                        partId = tmpId;
                        break;
                    }
                    if (partsScope.get(tmpId).getProperty(PropertyCategories.PC_ITEM_KIND).equals(kind)
                            && (partsScope.get(tmpId).getProperty(PropertyCategories.PC_ITEM_RARITY).equals(rarity)
                                || partsScope.get(tmpId).getProperty(PropertyCategories.PC_ITEM_RARITY)
                                <= ItemRarity.IR_COMMON.asInt())) {
                        if (!parts.isEmpty()
                                && Objects.requireNonNull(partsScope.get(tmpId).getAllProperties()
                                .get(PropertyCategories.PC_ITEM_SLOTS).getPropertySet())
                                .equals(parts.get(itemPartIndex - 1).getAllProperties()
                                        .get(PropertyCategories.PC_ITEM_SLOTS).getPropertySet())) {
                            partId = tmpId;
                            break;
                        }
                    }
                }
                parts.add(partsScope.get(partId));
            } else {
                parts.add(this.itemParts.get(itemPartIndex).get(itemPartsList.get(itemPartIndex)));
            }
        }

        return parts;
    }

    private @NotNull IngameItem.ItemModel makeItemModel(@NotNull Integer level, @NotNull Integer rarity,
                                                        @NotNull List<ItemPart> parts) {
        final Random random = new Random(System.currentTimeMillis());
        final List<Integer> partRarities = new ArrayList<>(ItemPart.ITEM_PARTS_COUNT);
        for (Integer i = 0; i < parts.size(); ++i) {
            if (rarity.equals(ItemRarity.IR_UNDEFINED.asInt())) {
                partRarities.add(i, random.nextInt(ItemRarity.IR_SIZE.asInt()));
            } else {
                partRarities.add(i, rarity);
            }
        }

        final StringBuilder name = new StringBuilder();
        for (Integer partIndex = 0; partIndex < parts.size(); ++partIndex) {
            final ItemPart part = parts.get(partIndex);
            if (!part.getName().isEmpty()) {
                if (partIndex == ItemPart.SECOND_PART_ID) {
                    name.append(" with ");
                } else if (partIndex == ItemPart.THIRD_PART_ID) {
                    name.append(" and ");
                }
            }
            name.append(part.getName());
        }

        final StringBuilder description = new StringBuilder();
        for (ItemPart part : parts) {
            description.append(part.getDescription());
            description.append(' ');
        }
        description.deleteCharAt(description.length() - 1);

        final Float percentage = Constants.STATS_GROWTH_PER_LEVEL * (level - Constants.START_LEVEL);
        final Map<Integer, Affector> mergedAffectors = mergeAffectors(parts, partRarities, percentage);
        final Map<Integer, Property> mergedProperties = mergeProperties(parts, partRarities, percentage);
        if (mergedProperties.containsKey(PropertyCategories.PC_LEVEL)) {
                mergedProperties.get(PropertyCategories.PC_LEVEL).setSingleProperty(level);
        } else {
            mergedProperties.put(PropertyCategories.PC_LEVEL, new SingleValueProperty(level));
        }
        return new IngameItem.ItemModel(name.toString(), description.toString(), mergedProperties, mergedAffectors);
    }

    @SuppressWarnings("OverlyComplexMethod")
    private @NotNull Map<Integer, Affector> mergeAffectors(@NotNull List<ItemPart> parts,
                                                           @NotNull List<Integer> rarities,
                                                           @NotNull Float growth) {

        final Set<Integer> affectorIds = new HashSet<>();
        for (ItemPart itemPart : parts) {
            if (affectorIds.isEmpty()) {
                affectorIds.addAll(itemPart.getAvailableAffectors());
            } else {
                for (Integer affectorId : itemPart.getAvailableAffectors()) {
                    if (!affectorIds.contains(affectorId)) {
                        affectorIds.add(affectorId);
                    }
                }
            }
        }

        final Map<Integer, Affector> mergedAffectors = new HashMap<>();
        for (Integer affectorId : affectorIds) {
            final List<Affector> affectors = new ArrayList<>(ItemPart.ITEM_PARTS_COUNT);
            for (ItemPart part : parts) {
                affectors.add(null);
                if (part.getAvailableAffectors().contains(affectorId)) {
                    affectors.set(part.getPartIndex(), part.getAllAffectors().get(affectorId));
                }
            }
            final List<Integer> affectionsList = new ArrayList<>();
            final Map<Integer, Integer> affectionsMap = new HashMap<>();
            Integer affection = null;

            for (Integer i = 0; i < affectors.size(); ++i) {
                final Affector affector = affectors.get(i);
                if (affector == null) {
                    continue;
                }
                if (affector.getAffectionsList() != null) {
                    if (affectionsList.isEmpty()) {
                        affectionsList.addAll(affector.getAffectionsList());
                        for (Integer j = 0; j < affectionsList.size(); ++j) {
                            affectionsList.set(j, applyRarityBonus(affectionsList.get(j), rarities.get(i)));
                        }
                    } else {
                        if (affectionsList.size() == affector.getAffectionsList().size()) {
                            for (Integer j = 0; j < affectionsList.size(); ++j) {
                                affectionsList.set(j, affectionsList.get(j)
                                        + applyRarityBonus(affector.getAffection(j), rarities.get(i)));
                            }
                        }
                    }
                } else if (affector.getAffectionsMap() != null) {
                    if (affectionsMap.isEmpty()) {
                        affectionsMap.putAll(affector.getAffectionsMap());
                        for (Integer affectionId : affectionsMap.keySet()) {
                            affectionsMap.replace(affectionId, applyRarityBonus(affectionsMap.get(affectionId),
                                    rarities.get(i)));
                        }
                    } else {
                        if (affectionsMap.keySet().equals(affector.getAffectionsMap().keySet())) {
                            for (Integer affectionId : affectionsMap.keySet()) {
                                affectionsMap.replace(affectionId, affectionsMap.get(affectionId)
                                        + applyRarityBonus(affector.getAffection(affectionId), rarities.get(i)));
                            }
                        }
                    }
                } else {
                    if (affection == null) {
                        affection = applyRarityBonus(affector.getAffection(), rarities.get(i));
                    } else {
                        affection += applyRarityBonus(affector.getAffection(), rarities.get(i));
                    }
                }
            }

            final Affector affector;
            if (!affectionsList.isEmpty()) {
                affector = new ListAffector(affectionsList);
            } else if (!affectionsMap.isEmpty()) {
                affector = new MapAffector(affectionsMap);
            } else {
                affector = new SingleValueAffector(Objects.requireNonNull(affection));
            }

            // apply level-up growth to level-dependent affectors
            if (affectorId == AffectorCategories.AC_WEAPON_DAMAGE_AFFECTOR
                    || affectorId == AffectorCategories.AC_ARMOUR_DEFENSE_AFFECTOR
                    || affectorId == AffectorCategories.AC_STATS_AFFECTOR) {
                affector.modifyByPercentage(growth);
            }

            mergedAffectors.put(affectorId, affector);
        }
        return mergedAffectors;
    }

    @SuppressWarnings("OverlyComplexMethod")
    private @NotNull Map<Integer, Property> mergeProperties(@NotNull List<ItemPart> parts,
                                                            @NotNull List<Integer> rarities,
                                                            @NotNull Float growth) {

        final Set<Integer> propertyIds = new HashSet<>();
        for (ItemPart part : parts) {
            //noinspection Duplicates
            if (propertyIds.isEmpty()) {
                propertyIds.addAll(part.getAvailableProperties());
            } else {
                for (Integer propertyId : part.getAvailableProperties()) {
                    if (!propertyIds.contains(propertyId)) {
                        propertyIds.add(propertyId);
                    }
                }
            }
        }

        final Map<Integer, Property> mergedProperties = new HashMap<>();
        for (Integer propertyId : propertyIds) {
            final List<Property> partProperties = new ArrayList<>(ItemPart.ITEM_PARTS_COUNT);
            for (ItemPart part : parts) {
                partProperties.add(null);
                if (part.getAvailableProperties().contains(propertyId)) {
                    partProperties.set(part.getPartIndex(), part.getAllProperties().get(propertyId));
                }
            }

            final List<Integer> propertiesList = new ArrayList<>();
            final Map<Integer, Integer> propertiesMap = new HashMap<>();
            final Set<Integer> propertiesSet = new HashSet<>();
            Integer propertyValue = null;
            for (Integer i = 0; i < partProperties.size(); ++i) {
                final Property property = partProperties.get(i);
                if (property == null) {
                    continue;
                }

                if (property.getPropertyList() != null) {
                    if (propertiesList.isEmpty()) {
                        propertiesList.addAll(property.getPropertyList());
                        for (Integer j = 0; j < propertiesList.size(); ++j) {
                            if (isPropertyAlterable(propertyId)) {
                                propertiesList.set(j, applyRarityBonus(propertiesList.get(j), rarities.get(i)));
                            }
                        }
                    } else if (propertiesList.size() == property.getPropertyList().size()) {
                        for (Integer j = 0; j < propertiesList.size(); ++j) {
                            if (isPropertyAlterable(propertyId)) {
                                propertiesList.set(j, propertiesList.get(j)
                                        + applyRarityBonus(property.getProperty(j), rarities.get(i)));
                            } else if (!isPropertyNonSummable(propertyId)) {
                                propertiesList.set(j, propertiesList.get(j) + property.getProperty(j));
                            }
                        }
                    }
                } else if (property.getPropertyMap() != null) {
                    if (propertiesMap.isEmpty()) {
                        propertiesMap.putAll(property.getPropertyMap());
                        if (isPropertyAlterable(propertyId)) {
                            for (Integer propertyIndex : propertiesMap.keySet()) {
                                propertiesMap.replace(propertyIndex,
                                        applyRarityBonus(propertiesMap.get(propertyIndex), rarities.get(i)));
                            }
                        }
                    } else if (propertiesMap.keySet().equals(property.getPropertyMap().keySet())) {
                        for (Integer propertyIndex : propertiesMap.keySet()) {
                            if (isPropertyAlterable(propertyId)) {
                                propertiesMap.replace(propertyIndex, propertiesMap.get(propertyIndex)
                                        + applyRarityBonus(property.getProperty(propertyIndex), rarities.get(i)));
                            } else if (!isPropertyNonSummable(propertyId)) {
                                propertiesMap.replace(propertyIndex, propertiesMap.get(propertyIndex)
                                        + property.getProperty(propertyIndex));
                            }
                        }
                    }
                } else if (property.getPropertySet() != null) {
                    //noinspection Duplicates
                    if (propertiesSet.isEmpty()) {
                        propertiesSet.addAll(property.getPropertySet());
                    } else {
                        for (Integer propertySetValue : property.getPropertySet()) {
                            if (!propertiesSet.contains(propertySetValue)) {
                                propertiesSet.add(propertySetValue);
                            }
                        }
                    }
                } else {
                    if (propertyValue == null) {
                        if (isPropertyAlterable(propertyId)) {
                            propertyValue = applyRarityBonus(property.getProperty(), rarities.get(i));
                        } else {
                            propertyValue = property.getProperty();
                        }
                    } else if (!isPropertyNonSummable(propertyId)) {
                        if (isPropertyAlterable(propertyId)) {
                            propertyValue += applyRarityBonus(property.getProperty(), rarities.get(i));
                        } else {
                            propertyValue += property.getProperty();
                        }
                    }
                }
            }

            final Property mergedProperty;
            //noinspection Duplicates
            if (!propertiesList.isEmpty()) {
                mergedProperty = new ListProperty(propertiesList);
            } else if (!propertiesMap.isEmpty()) {
                mergedProperty = new MapProperty(propertiesMap);
            } else if (!propertiesSet.isEmpty()) {
                mergedProperty = new SetProperty(propertiesSet);
            } else {
                mergedProperty = new SingleValueProperty(Objects.requireNonNull(propertyValue));
            }

            if (isPropertyLevelable(propertyId)) {
                mergedProperty.modifyByPercentage(growth);
            }

            mergedProperties.put(propertyId, mergedProperty);
        }

        return mergedProperties;
    }

    private @NotNull Boolean isPropertyNonSummable(@NotNull Integer propertyKind) {
        return propertyKind == PropertyCategories.PC_LEVEL || propertyKind == PropertyCategories.PC_ITEM_KIND
                || propertyKind == PropertyCategories.PC_ITEM_SLOTS;
    }

    private @NotNull Boolean isPropertyAlterable(@NotNull Integer propertyKind) {
        return propertyKind == PropertyCategories.PC_ITEM_PRICE;
    }

    private @NotNull Boolean isPropertyLevelable(@NotNull Integer propertyKind) {
        return propertyKind == PropertyCategories.PC_ITEM_PRICE;
    }

    private @NotNull Integer applyRarityBonus(@NotNull Integer baseValue, @NotNull Integer rarity) {
        return Long.valueOf(Math.round(baseValue.floatValue() * Math.pow(Constants.STATS_GROWTH_PER_LEVEL,
                rarity.floatValue() - ItemRarity.IR_COMMON.asInt().floatValue()))).intValue();
    }
}
