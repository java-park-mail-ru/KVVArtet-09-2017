package project.gamemechanics.pcg;

import org.junit.BeforeClass;
import org.junit.Test;
import project.gamemechanics.components.properties.PropertyCategories;
import project.gamemechanics.globals.Constants;
import project.gamemechanics.interfaces.EquipableItem;
import project.gamemechanics.resources.assets.AssetProvider;
import project.gamemechanics.resources.assets.AssetProviderImpl;
import project.gamemechanics.resources.pcg.PcgContentFactory;
import project.gamemechanics.resources.pcg.PcgFactory;
import project.gamemechanics.resources.pcg.items.ItemBlueprint;
import project.gamemechanics.world.config.ResourcesConfig;

import java.util.Random;

import static org.junit.Assert.*;

public class ItemRestorationTest {
    private static PcgContentFactory factory;

    @BeforeClass
    public static void initializeResources() {
        final AssetProvider assetProvider = new AssetProviderImpl(
                ResourcesConfig.getAssetHoldersFileNames());
        factory = new PcgFactory(ResourcesConfig.getItemPartsFilename(),
                ResourcesConfig.getNpcPartsFilename(), assetProvider);
    }

    @Test
    public void restoreAfterSerializationTest() {
        final Random random = new Random(System.currentTimeMillis());
        final EquipableItem item = factory.makeItem(
                random.nextInt(Constants.MAX_LEVEL)
                        + Constants.START_LEVEL);
        assertNotNull(item);
        final ItemBlueprint serialized = item.pack();
        assertNotNull(serialized);
        assertEquals(item.getID().intValue(), serialized.getProperties()
                .get(PropertyCategories.PC_ITEM_ID).getProperty().intValue());
        final EquipableItem unpackedItem = factory.makeItem(serialized);
        assertNotNull(unpackedItem);
        assertEquals(item.getID(), unpackedItem.getID());
        for (Integer propertyId : item.getAvailableProperties()) {
            assertTrue(unpackedItem.hasProperty(propertyId));
        }
        for(Integer affectorId : item.getAvailableAffectors()) {
            assertTrue(unpackedItem.hasAffector(affectorId));
        }
        assertEquals(item.getProperty(PropertyCategories.PC_LEVEL).intValue(),
                unpackedItem.getProperty(PropertyCategories.PC_LEVEL).intValue());
        assertEquals(item.getProperty(PropertyCategories.PC_ITEM_KIND).intValue(),
                unpackedItem.getProperty(PropertyCategories.PC_ITEM_KIND).intValue());
        assertEquals(item.getProperty(PropertyCategories.PC_ITEM_RARITY).intValue(),
                unpackedItem.getProperty(PropertyCategories.PC_ITEM_RARITY).intValue());
        assertEquals(item.getName(), unpackedItem.getName());
        assertEquals(item.getDescription(), unpackedItem.getDescription());
    }
}
