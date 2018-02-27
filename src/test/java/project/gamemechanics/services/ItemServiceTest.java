package project.gamemechanics.services;


import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import project.gamemechanics.globals.Constants;
import project.gamemechanics.interfaces.EquipableItem;
import project.gamemechanics.resources.assets.AssetProvider;
import project.gamemechanics.resources.assets.AssetProviderImpl;
import project.gamemechanics.resources.pcg.PcgContentFactory;
import project.gamemechanics.resources.pcg.PcgFactory;
import project.gamemechanics.world.config.ResourcesConfig;

import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
public class ItemServiceTest {

    @Autowired
    private ItemDAO itemDAO;

    private static PcgContentFactory pcgFactory;

    @BeforeClass
    public static void initializeResources() {
        final AssetProvider assetProvider = new AssetProviderImpl(
                ResourcesConfig.getAssetHoldersFileNames());
        pcgFactory = new PcgFactory(ResourcesConfig.getItemPartsFilename(),
                ResourcesConfig.getNpcPartsFilename(), assetProvider);
    }

    @Test
    public void goodIdAfterSetItemTest() {
        final Random random = new Random(System.currentTimeMillis());
        final EquipableItem equipableItem = pcgFactory.makeItem(random.nextInt(Constants.MAX_LEVEL) + Constants.START_LEVEL);
        Integer id = itemDAO.setItem(equipableItem.pack());
        assertNotEquals(id.intValue(), Constants.UNDEFINED_ID);
        assertEquals(id.intValue(), equipableItem.getID().intValue());
    }

    @Test
    public void equalsIdsFromGetterAfterSetItemTest() {
        final Random random = new Random(System.currentTimeMillis());
        final EquipableItem equipableItem = pcgFactory.makeItem(random.nextInt(Constants.MAX_LEVEL) + Constants.START_LEVEL);
        Integer id = itemDAO.setItem(equipableItem.pack());
        EquipableItem item = itemDAO.getItemById(id);
        assertNotNull(item);
        assertEquals(item.getID().intValue(), id.intValue());
    }

    @Test
    public void equalsPropertysFromGetterAfterSetItemTest() {
        final Random random = new Random(System.currentTimeMillis());
        final EquipableItem equipableItem = pcgFactory.makeItem(random.nextInt(Constants.MAX_LEVEL) + Constants.START_LEVEL);
        Integer id = itemDAO.setItem(equipableItem.pack());
        EquipableItem item = itemDAO.getItemById(id);
        assertEquals(item.pack().getProperties(), equipableItem.pack().getProperties());
    }


}
