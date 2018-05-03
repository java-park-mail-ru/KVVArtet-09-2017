package project.gamemechanics.resources.pcg.npcs;

import com.fasterxml.jackson.annotation.JsonProperty;
import project.gamemechanics.aliveentities.AbstractAliveEntity;
import project.gamemechanics.aliveentities.npcs.NonPlayerCharacter;
import project.gamemechanics.components.affectors.AffectorCategories;
import project.gamemechanics.components.properties.*;
import project.gamemechanics.globals.Constants;
import project.gamemechanics.globals.DigitsPairIndices;
import project.gamemechanics.interfaces.AliveEntity;
import project.gamemechanics.interfaces.Bag;
import project.gamemechanics.interfaces.CharacterRole;
import project.gamemechanics.items.containers.MonsterLootBag;
import project.gamemechanics.resources.assets.AssetProvider;
import project.gamemechanics.resources.pcg.items.ItemBlueprint;
import project.gamemechanics.resources.pcg.items.ItemsFactory;

import javax.validation.constraints.NotNull;
import java.util.*;

public class NpcsFactoryImpl implements NpcsFactory {
    private final Map<Integer, Map<Integer, NpcPart>> npcParts;

    public NpcsFactoryImpl(@JsonProperty("npcParts") @NotNull Map<Integer, Map<Integer, NpcPart>> npcParts) {
        this.npcParts = npcParts;
    }

    @Override
    public @NotNull AliveEntity makeNpc(@NotNull NpcBlueprint blueprint, @NotNull AssetProvider assetProvider,
                                        @NotNull ItemsFactory itemsGenerator) {
        final Map<Integer, Integer> parts = getNpcPartIds(blueprint);
        final CharacterRole npcRole = getNpcRole(blueprint, assetProvider);

        final List<NpcPart> npcPartsList = getNpcParts(parts);

        final StringBuilder nameBuilder = new StringBuilder();
        for (NpcPart part : npcPartsList) {
            nameBuilder.append(part.getName());
            nameBuilder.append(' ');
        }
        nameBuilder.deleteCharAt(nameBuilder.length() - 1);

        final StringBuilder descriptionBuilder = new StringBuilder();
        for (NpcPart part : npcPartsList) {
            descriptionBuilder.append(part.getDescription());
            descriptionBuilder.append(' ');
        }
        descriptionBuilder.deleteCharAt(descriptionBuilder.length() - 1);

        final AbstractAliveEntity.NPCModel model = new AbstractAliveEntity.NPCModel(nameBuilder.toString(),
                descriptionBuilder.toString(), mergeProperties(npcPartsList, blueprint.getLevel(), npcRole),
                generateLoot(npcPartsList, blueprint.getLevel(), itemsGenerator), npcRole);

        return new NonPlayerCharacter(model);
    }

    private @NotNull Map<Integer, Integer> getNpcPartIds(@NotNull NpcBlueprint blueprint) {
        final Map<Integer, Integer> partIds = new HashMap<>(blueprint.getNpcParts());
        final Random random = new Random(System.currentTimeMillis());
        for (Integer partIndex : partIds.keySet()) {
            final List<Integer> keysList = new ArrayList<>(npcParts.get(partIndex).keySet());
            if (partIds.get(partIndex) == Constants.UNDEFINED_ID) {
                partIds.replace(partIndex, keysList.get(random.nextInt(keysList.size())));
            }
        }
        return partIds;
    }

    private @NotNull CharacterRole getNpcRole(@NotNull NpcBlueprint blueprint,
                                               @NotNull AssetProvider assetProvider) {
        final CharacterRole npcRole;
        if (blueprint.getAvailableProperties().contains(PropertyCategories.PC_CHARACTER_ROLE_ID)) {
            if (blueprint.getProperties().get(PropertyCategories.PC_CHARACTER_ROLE_ID).getProperty()
                    != Constants.UNDEFINED_ID) {
                npcRole = assetProvider.getNpcRole(blueprint.getProperties()
                        .get(PropertyCategories.PC_CHARACTER_ROLE_ID).getProperty());
            } else {
                npcRole = assetProvider.getNpcRole();
            }
        } else {
            npcRole = assetProvider.getNpcRole();
        }
        return Objects.requireNonNull(npcRole);
    }

    private @NotNull List<NpcPart> getNpcParts(@NotNull Map<Integer, Integer> npcPartsIndices) {
        final List<NpcPart> partsList = new ArrayList<>();
        for (Integer partId : npcPartsIndices.keySet()) {
            final Integer partIndex = npcPartsIndices.get(partId);
            partsList.add(npcParts.get(partId).get(partIndex));
        }
        return partsList;
    }

    private @NotNull List<Bag> generateLoot(@NotNull List<NpcPart> npcPartsList, @NotNull Integer level,
                                            @NotNull ItemsFactory itemsGenerator) {
        final List<Bag> lootBags = new ArrayList<>();
        lootBags.add(new MonsterLootBag(mergeLootLists(npcPartsList), level, itemsGenerator));
        return lootBags;
    }

    private @NotNull List<ItemBlueprint> mergeLootLists(@NotNull List<NpcPart> npcPartsList) {
        if (npcPartsList.isEmpty()) {
            return new ArrayList<>();
        }
        final List<ItemBlueprint> mergedBlueprints = new ArrayList<>(npcPartsList.get(0).getLootList());
        for (Integer i = 1; i < npcPartsList.size(); ++i) {
            mergedBlueprints.addAll(npcPartsList.get(i).getLootList());
        }
        return mergedBlueprints;
    }

    @SuppressWarnings("OverlyComplexMethod")
    private @NotNull Map<Integer, Property> mergeProperties(@NotNull List<NpcPart> npcPartList,
                                                            @NotNull Integer level,
                                                            @NotNull CharacterRole npcRole) {
        final Set<Integer> propertyIds = new HashSet<>();
        for (NpcPart part : npcPartList) {
            //noinspection Duplicates
            if (propertyIds.isEmpty()) {
                propertyIds.addAll(part.getAvailableProperties());
            } else {
                propertyIds.addAll(part.getAvailableProperties());
            }
        }

        final Float levelGrowth = countLevelGrowth(level);

        final Map<Integer, Property> mergedProperties = new HashMap<>();
        for (Integer propertyId : propertyIds) {

            final List<Property> partProperties = new ArrayList<>(npcPartList.size());
            for (Integer i = 0; i < npcPartList.size(); ++i) {
                final Property property = npcPartList.get(i).getAllProperties().get(propertyId);
                if (property != null) {
                    partProperties.add(i, property);
                }
            }

            final List<Integer> propertyList = new ArrayList<>();
            final Map<Integer, Integer> propertyMap = new HashMap<>();
            final Set<Integer> propertySet = new HashSet<>();
            Integer propertyValue = null;
            for (Property partProperty : partProperties) {
                if (partProperty.getPropertyList() != null) {
                    if (propertyList.isEmpty()) {
                        propertyList.addAll(partProperty.getPropertyList());
                        for (Integer i = 0; i < propertyList.size(); ++i) {
                            propertyList.set(i, isPropertyLevelable(propertyId)
                                    ? Math.round(propertyList.get(i).floatValue() * levelGrowth)
                                    : propertyList.get(i));
                        }
                    } else {
                        if (propertyList.size() == partProperty.getPropertyList().size()) {
                            for (Integer i = 0; i < propertyList.size(); ++i) {
                                propertyList.set(i, isPropertyNonSummable(propertyId) ? propertyList.get(i)
                                        : isPropertyLevelable(propertyId) ? propertyList.get(i)
                                        + Math.round(partProperty.getPropertyList().get(i) * levelGrowth)
                                        : propertyList.get(i) + partProperty.getPropertyList().get(i));
                            }
                        }
                    }
                } else if (partProperty.getPropertyMap() != null) {
                    if (propertyMap.isEmpty()) {
                        propertyMap.putAll(partProperty.getPropertyMap());
                        if (!isPropertyLevelable(propertyId)) {
                            for (Integer propertyKey : propertyMap.keySet()) {
                                propertyMap.replace(propertyKey, isPropertyLevelable(propertyId)
                                        ? Math.round(propertyMap.get(propertyKey).floatValue() * levelGrowth)
                                        : propertyMap.get(propertyKey));
                            }
                        }
                    } else {
                        if (propertyMap.keySet().equals(partProperty.getPropertyMap().keySet())) {
                            for (Integer propertyKey : propertyMap.keySet()) {
                                propertyMap.replace(propertyKey, isPropertyNonSummable(propertyId)
                                        ? propertyMap.get(propertyKey) : isPropertyLevelable(propertyId)
                                        ? propertyMap.get(propertyKey)
                                        + Math.round(partProperty.getPropertyMap().get(propertyKey).floatValue()
                                        * levelGrowth)
                                        : propertyMap.get(propertyKey
                                        + partProperty.getPropertyMap().get(propertyKey)));
                            }
                        }
                    }
                } else if (partProperty.getPropertySet() != null) {
                    //noinspection Duplicates
                    if (propertySet.isEmpty()) {
                        propertySet.addAll(partProperty.getPropertySet());
                    } else {
                        propertySet.addAll(partProperty.getPropertySet());
                    }
                } else {
                    if (propertyValue == null) {
                        propertyValue = isPropertyLevelable(propertyId) ? partProperty.getProperty()
                                : Math.round(partProperty.getProperty().floatValue() * levelGrowth);
                    } else {
                        if (!isPropertyNonSummable(propertyId)) {
                            propertyValue += isPropertyLevelable(propertyId)
                                    ? Math.round(partProperty.getProperty().floatValue() * levelGrowth)
                                    : partProperty.getProperty();
                        }
                    }
                }
            }

            final Property mergedProperty;
            //noinspection Duplicates
            if (!propertyList.isEmpty()) {
                mergedProperty = new ListProperty(propertyList);
            } else if (!propertyMap.isEmpty()) {
                mergedProperty = new MapProperty(propertyMap);
            } else if (!propertySet.isEmpty()) {
                mergedProperty = new SetProperty(propertySet);
            } else {
                mergedProperty = new SingleValueProperty(Objects.requireNonNull(propertyValue));
            }
            final Integer affectionValue = getRoleProperAffector(npcRole, propertyId);
            if (affectionValue != Constants.WRONG_INDEX) {
                mergedProperty.modifyByPercentage(intPercentageToFloat(affectionValue));
            }
            mergedProperties.put(propertyId, mergedProperty);

        }

        final List<Integer> hitpoints = new ArrayList<>();
        for (Integer i = 0; i < DigitsPairIndices.PAIR_SIZE; ++i) {
            hitpoints.add(mergedProperties.get(PropertyCategories.PC_BASE_HEALTH).getProperty());
        }
        mergedProperties.put(PropertyCategories.PC_HITPOINTS, new ListProperty(hitpoints));
        mergedProperties.put(PropertyCategories.PC_LEVEL, new SingleValueProperty(level));

        final Map<Integer, Integer> cooldowns = new HashMap<>();
        for (Integer abilityId : npcRole.getAllAbilities().keySet()) {
            cooldowns.put(abilityId, 0);
        }
        mergedProperties.put(PropertyCategories.PC_ABILITIES_COOLDOWN, new AbilitiesCooldownProperty(cooldowns));

        if (mergedProperties.containsKey(PropertyCategories.PC_SPEED)) {
            if (mergedProperties.get(PropertyCategories.PC_SPEED).getProperty() <= 0) {
                mergedProperties.get(PropertyCategories.PC_SPEED)
                        .setSingleProperty(Constants.DEFAULT_ALIVE_ENTITY_SPEED);
            }
        } else {
            mergedProperties.put(PropertyCategories.PC_SPEED,
                    new SingleValueProperty(Constants.DEFAULT_ALIVE_ENTITY_SPEED));
        }

        return mergedProperties;
    }

    private @NotNull Float intPercentageToFloat(@NotNull Integer intPercentage) {
        return intPercentage.floatValue() / Integer.valueOf(Constants.PERCENTAGE_CAP_INT).floatValue();
    }

    private @NotNull Float countLevelGrowth(@NotNull Integer level) {
        return Constants.STATS_GROWTH_PER_LEVEL * (level - Constants.START_LEVEL);
    }

    private @NotNull Boolean isPropertyNonSummable(@NotNull Integer propertyKind) {
        return propertyKind == PropertyCategories.PC_LEVEL;
    }

    private @NotNull Boolean isPropertyLevelable(@NotNull Integer propertyKind) {
        //noinspection OverlyComplexBooleanExpression
        return propertyKind == PropertyCategories.PC_STATS || propertyKind == PropertyCategories.PC_BASE_HEALTH
                || propertyKind == PropertyCategories.PC_BASE_DAMAGE
                || propertyKind == PropertyCategories.PC_BASE_DEFENSE;
    }

    private @NotNull Integer getRoleProperAffector(@NotNull CharacterRole npcRole,
                                                   @NotNull Integer propertyKind) {
        switch (propertyKind) {
            case PropertyCategories.PC_BASE_HEALTH:
                return npcRole.hasAffector(AffectorCategories.AC_HEALTH_AFFECTOR)
                        ? npcRole.getAffection(AffectorCategories.AC_HEALTH_AFFECTOR) : Constants.WRONG_INDEX;
            case PropertyCategories.PC_BASE_DAMAGE:
                return npcRole.hasAffector(AffectorCategories.AC_BASE_DAMAGE_ROLE_AFFECTOR)
                        ? npcRole.getAffection(AffectorCategories.AC_BASE_DAMAGE_ROLE_AFFECTOR)
                        : Constants.WRONG_INDEX;
            case PropertyCategories.PC_BASE_DEFENSE:
                return npcRole.hasAffector(AffectorCategories.AC_BASE_DEFENSE_ROLE_AFFECTOR)
                        ? npcRole.getAffection(AffectorCategories.AC_BASE_DEFENSE_ROLE_AFFECTOR)
                        : Constants.WRONG_INDEX;
            default:
                return Constants.WRONG_INDEX;
        }
    }
}
