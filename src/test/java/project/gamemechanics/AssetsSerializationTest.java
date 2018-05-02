package project.gamemechanics;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import org.junit.Test;
import project.gamemechanics.components.affectors.Affector;
import project.gamemechanics.components.affectors.AffectorCategories;
import project.gamemechanics.components.affectors.AffectorsFactory;
import project.gamemechanics.components.properties.PropertiesFactory;
import project.gamemechanics.components.properties.Property;
import project.gamemechanics.components.properties.PropertyCategories;
import project.gamemechanics.components.properties.SingleValueProperty;
import project.gamemechanics.globals.Constants;
import project.gamemechanics.globals.DigitsPairIndices;
import project.gamemechanics.globals.EquipmentKind;
import project.gamemechanics.globals.ItemRarity;
import project.gamemechanics.interfaces.AliveEntity;
import project.gamemechanics.interfaces.EquipableItem;
import project.gamemechanics.resources.assets.*;
import project.gamemechanics.resources.pcg.PcgContentFactory;
import project.gamemechanics.resources.pcg.PcgFactory;
import project.gamemechanics.resources.pcg.items.*;
import project.gamemechanics.resources.pcg.npcs.NpcBlueprint;
import project.gamemechanics.resources.pcg.npcs.NpcPart;
import project.gamemechanics.resources.pcg.npcs.NpcsFactory;
import project.gamemechanics.resources.pcg.npcs.NpcsFactoryImpl;
import project.gamemechanics.world.config.ResourcesConfig;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.*;

@SuppressWarnings({"unused"})
public class AssetsSerializationTest {
    @SuppressWarnings("unused")
    @Test
    public void itemPartStringSerializationDeserialization() {
        final Map<Integer, Property> testProperty = new HashMap<>();
        testProperty.put(PropertyCategories.PC_LEVEL, new SingleValueProperty(Constants.START_LEVEL));
        final Property baseDamage = PropertiesFactory.makeProperty(PropertyCategories.PC_BASE_DAMAGE);
        final List<Integer> damage = new ArrayList<>(DigitsPairIndices.PAIR_SIZE);
        damage.add(DigitsPairIndices.MIN_VALUE_INDEX, 3);
        damage.add(DigitsPairIndices.MAX_VALUE_INDEX, 5);
        assert baseDamage != null;
        baseDamage.setPropertyList(damage);
        testProperty.put(PropertyCategories.PC_BASE_DAMAGE, baseDamage);
        final Map<Integer, Affector> testAffectors = new HashMap<>();
        testAffectors.put(AffectorCategories.AC_DAMAGE_AFFECTOR,
                AffectorsFactory.getAffector(AffectorCategories.AC_DAMAGE_AFFECTOR));

        final ItemPart itemPart = new ItemPartAsset(0,"test asset", "test description",
                ItemPart.FIRST_PART_ID, testAffectors, testProperty);
        final ObjectMapper mapper = new ObjectMapper();
        String jsonAsset = null;
        try {
            jsonAsset = mapper.writeValueAsString(itemPart);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        assertNotNull("data shall be written", jsonAsset);
        assertFalse("serialized json shall contain data", jsonAsset.isEmpty());
        ItemPart deserialized = null;
        //noinspection TryWithIdenticalCatches
        try {
            deserialized = mapper.readValue(jsonAsset, ItemPartAsset.class);
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String secondJson = null;
        try {
            secondJson = mapper.writeValueAsString(deserialized);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        assertEquals("serialized data must be equal", jsonAsset, secondJson);
        assertNotNull(deserialized);
        assertEquals(itemPart.getName(), deserialized.getName());
        assertEquals(itemPart.getDescription(), deserialized.getDescription());
        assertEquals(itemPart.getProperty(PropertyCategories.PC_LEVEL),
                deserialized.getProperty(PropertyCategories.PC_LEVEL));
    }

    @SuppressWarnings("unused")
    @Test
    public void itemPartsLoading() {
        final ObjectMapper mapper = new ObjectMapper();
        ItemsFactory testFactory = null;
        //noinspection OverlyBroadCatchBlock
        try {
            testFactory = mapper.readValue(Resources.getResource(
                    ResourcesConfig.getItemPartsFilename()), ItemFactoryImpl.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertNotEquals(testFactory, null);
    }

    @SuppressWarnings("unused")
    @Test
    public void makeRandomItemTest() {
        final ObjectMapper mapper = new ObjectMapper();
        //noinspection OverlyBroadCatchBlock
        try {
            final ItemsFactory testFactory = mapper.readValue(Resources.getResource(
                    ResourcesConfig.getItemPartsFilename()), ItemFactoryImpl.class);
            final Random random = new Random(System.currentTimeMillis());
            for (Integer tryNumber = 0; tryNumber < 10; ++tryNumber) {
                final Map<Integer, Integer> parts = new HashMap<>();
                for (Integer i = 0; i < ItemPart.ITEM_PARTS_COUNT; ++i) {
                    parts.put(i, Constants.UNDEFINED_ID);
                }
                final Map<Integer, Property> properties = new HashMap<>();
                final Integer level = random.nextInt(Constants.MAX_LEVEL) + Constants.START_LEVEL;
                properties.put(PropertyCategories.PC_LEVEL, new SingleValueProperty(level));
                properties.put(PropertyCategories.PC_ITEM_RARITY,
                        new SingleValueProperty(ItemRarity.IR_UNDEFINED.asInt()));
                properties.put(PropertyCategories.PC_ITEM_KIND,
                        new SingleValueProperty(EquipmentKind.EK_UNDEFINED.asInt()));
                final EquipableItem randomItem = testFactory.makeItem(
                        new ItemBlueprint(Constants.UNDEFINED_RARITY_DEFAULT_DROP_CHANCE, properties, parts));
                assertEquals(level, randomItem.getLevel());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unused")
    @Test
    public void perksTest() {
        final AssetHolder.PerkHolder testHolder = new PerkAssetHolder(ResourcesConfig.getAssetHoldersFileNames()
                    .get(AssetProvider.PERK_RESOURCE_NAME));
        assertFalse(testHolder.getAllAssets().isEmpty());
        final Random random = new Random(System.currentTimeMillis());
        assertTrue(Objects.requireNonNull(testHolder.getAsset(random.nextInt(testHolder.getAllAssets().size())))
                .getAvailableAffectors().isEmpty());
    }

    @SuppressWarnings("unused")
    @Test
    public void perkBranchesTest() {
        final AssetHolder.PerkHolder perkHolder = new PerkAssetHolder(ResourcesConfig.getAssetHoldersFileNames()
                .get(AssetProvider.PERK_RESOURCE_NAME));
        final AssetHolder.PerkBranchHolder testHolder = new PerkBranchAssetHolder(ResourcesConfig
                .getAssetHoldersFileNames().get(AssetProvider.PERK_BRANCH_RESOURCE_NAME), perkHolder.getAllAssets());
        assertFalse(testHolder.getAllAssets().isEmpty());
        final Random random = new Random(System.currentTimeMillis());
        assertTrue(Objects.requireNonNull(Objects.requireNonNull(testHolder.getAsset(random.nextInt(testHolder.getAllAssets().size())))
                .getPerk(0)).getAvailableAffectors().isEmpty());
    }

    @SuppressWarnings("unused")
    @Test
    public void characterRacesTest() {
        final AssetHolder.CharacterRaceHolder testHolder = new CharacterRaceAssetHolder(
                ResourcesConfig.getAssetHoldersFileNames().get(AssetProvider.CHARACTER_RACE_RESOURCE_NAME));
        assertFalse(testHolder.getAllAssets().isEmpty());
    }

    @SuppressWarnings("unused")
    @Test
    public void abilitiesTest() {
        final AssetHolder.AbilityHolder testHolder = new AbilityAssetHolder(
                ResourcesConfig.getAssetHoldersFileNames().get(AssetProvider.ABILITY_RESOURCE_NAME));
        assertFalse(testHolder.getAllAssets().isEmpty());
        final Random random = new Random(System.currentTimeMillis());
        assertFalse(testHolder.getAllAssets().isEmpty());
        assertTrue(Objects.requireNonNull(testHolder.getAsset(random.nextInt(testHolder.getAllAssets().size())))
                .getAppliedEffects().isEmpty());
    }

    @Test
    public void characterClassesTest() {
        final AssetHolder.AbilityHolder abilityHolder = new AbilityAssetHolder(
                ResourcesConfig.getAssetHoldersFileNames().get(AssetProvider.ABILITY_RESOURCE_NAME));
        final AssetHolder.PerkHolder perkHolder = new PerkAssetHolder(ResourcesConfig.getAssetHoldersFileNames()
                .get(AssetProvider.PERK_RESOURCE_NAME));
        final AssetHolder.PerkBranchHolder branchHolder = new PerkBranchAssetHolder(ResourcesConfig
                .getAssetHoldersFileNames().get(AssetProvider.PERK_BRANCH_RESOURCE_NAME), perkHolder.getAllAssets());
        final AssetHolder.CharacterClassHolder testHolder = new CharacterClassAssetHolder(ResourcesConfig
                .getAssetHoldersFileNames().get(AssetProvider.CHARACTER_CLASS_RESOURCE_NAME),
                abilityHolder.getAllAssets(), branchHolder.getAllAssets());
        assertFalse(testHolder.getAllAssets().isEmpty());
    }

    @Test
    public void instancesNameDescrsTest() {
        final ObjectMapper mapper = new ObjectMapper();
        // noinspection OverlyBroadCatchBlock
        try {
            final AssetHolder.InstanceNameDescriptionHolder firstTestHolder =
                    mapper.readValue(Resources.getResource(
                            ResourcesConfig.getAssetHoldersFileNames()
                                    .get(AssetProvider.INSTANCE_NAME_DESCRIPTION_FIRST_RESOURCE_NAME)),
                            InstanceNameDescriptionAssetHolder.class);
            assertFalse(firstTestHolder.getAllAssets().isEmpty());
            final AssetHolder.InstanceNameDescriptionHolder secondTestHolder =
                    mapper.readValue(Resources.getResource(
                            ResourcesConfig.getAssetHoldersFileNames()
                                    .get(AssetProvider.INSTANCE_NAME_DESCRIPTION_SECOND_RESOURCE_NAME)),
                            InstanceNameDescriptionAssetHolder.class);
            assertFalse(secondTestHolder.getAllAssets().isEmpty());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void npcRolesTest() {
        final AssetHolder.AbilityHolder abilityHolder = new AbilityAssetHolder(
                ResourcesConfig.getAssetHoldersFileNames().get(AssetProvider.ABILITY_RESOURCE_NAME));
        final AssetHolder.NpcRoleHolder testHolder = new NpcRoleAssetHolder(
                ResourcesConfig.getAssetHoldersFileNames().get(AssetProvider.NPC_ROLE_RESOURCE_NAME),
                abilityHolder.getAllAssets());
        assertFalse(testHolder.getAllAssets().isEmpty());
    }

    @Test
    public void assetProviderTest() {
        final AssetProvider assetProvider = new AssetProviderImpl(ResourcesConfig.getAssetHoldersFileNames());
        assertNotNull(assetProvider.getNpcRole());
        assertNotNull(assetProvider.getPerk(0));
        assertNotNull(assetProvider.getPerkBranch(0));
        assertNotNull(assetProvider.getAbility(0));
        assertNotNull(assetProvider.getCharacterRace(0));
        assertNotNull(assetProvider.getCharacterRace());
        assertNotNull(assetProvider.getCharacterClass(0));
        assertEquals(assetProvider.makeInstanceNameDescription().size(), DigitsPairIndices.PAIR_SIZE);
    }

    @Test
    public void npcsFactoryTest() {
        final ObjectMapper mapper = new ObjectMapper();
        NpcsFactory testFactory = null;
        //noinspection OverlyBroadCatchBlock
        try {
            testFactory = mapper.readValue(Resources.getResource(
                    ResourcesConfig.getNpcPartsFilename()), NpcsFactoryImpl.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertNotEquals(testFactory, null);
    }

    @Test
    public void makeRandomNpcTest() {
        final ObjectMapper mapper = new ObjectMapper();
        ItemsFactory itemFactory = null;
        //noinspection OverlyBroadCatchBlock
        try {
            itemFactory = mapper.readValue(Resources.getResource(
                    ResourcesConfig.getItemPartsFilename()), ItemFactoryImpl.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertNotEquals(itemFactory, null);
        final AssetProvider assets = new AssetProviderImpl(ResourcesConfig.getAssetHoldersFileNames());
        //noinspection OverlyBroadCatchBlock
        try {
            final NpcsFactory testFactory = mapper.readValue(Resources.getResource(
                    ResourcesConfig.getNpcPartsFilename()), NpcsFactoryImpl.class);
            final Random random = new Random(System.currentTimeMillis());
            for (Integer tryNumber = 0; tryNumber < 10; ++tryNumber) {
                final Map<Integer, Integer> parts = new HashMap<>();
                for (Integer i = 0; i < NpcPart.NPC_PARTS_COUNT; ++i) {
                    parts.put(i, Constants.UNDEFINED_ID);
                }
                final Map<Integer, Property> properties = new HashMap<>();
                final Integer level = random.nextInt(Constants.MAX_LEVEL) + Constants.START_LEVEL;
                properties.put(PropertyCategories.PC_LEVEL, new SingleValueProperty(level));
                properties.put(PropertyCategories.PC_CHARACTER_ROLE_ID,
                        new SingleValueProperty(Constants.UNDEFINED_ID));
                final AliveEntity npc = testFactory.makeNpc(
                        new NpcBlueprint(properties, parts), assets, Objects.requireNonNull(itemFactory));
                assertEquals(level, npc.getLevel());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void pcgContentFactoryTest() {
        final AssetProvider assets = new AssetProviderImpl(ResourcesConfig.getAssetHoldersFileNames());
        final PcgContentFactory factory = new PcgFactory(ResourcesConfig.getItemPartsFilename(),
                ResourcesConfig.getNpcPartsFilename(), assets);
        final Random random = new Random(System.currentTimeMillis());
        final Integer repeats = 10;
        for (Integer i = 0; i < repeats; ++i) {
            final Integer level = random.nextInt(Constants.MAX_LEVEL) + Constants.START_LEVEL;
            assertEquals(factory.makeNpc(level).getLevel(), factory.makeItem(level).getLevel());
        }
    }
}
