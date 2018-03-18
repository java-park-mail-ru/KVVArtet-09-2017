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
import project.gamemechanics.items.containers.StorageBag;
import project.gamemechanics.resources.assets.AssetProvider;
import project.gamemechanics.resources.assets.AssetProviderImpl;
import project.gamemechanics.resources.pcg.PcgContentFactory;
import project.gamemechanics.resources.pcg.PcgFactory;
import project.gamemechanics.world.config.ResourcesConfig;

import java.util.Random;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
public class BagServiceTest {

    @Autowired
    private BagDAO bagDAO;

    private static PcgContentFactory pcgFactory;

    @BeforeClass
    public static void initializeResources() {
        final AssetProvider assetProvider = new AssetProviderImpl(
                ResourcesConfig.getAssetHoldersFileNames());
        pcgFactory = new PcgFactory(ResourcesConfig.getItemPartsFilename(),
                ResourcesConfig.getNpcPartsFilename(), assetProvider);
    }


    @Test
    public void getBagAfterSetFilledBag_ExpectedEqualsBags() {
        StorageBag.EmptyBagModel emptyBagModel = new StorageBag.EmptyBagModel("my", "MY BEST BAG", 16);
        Integer bagId = bagDAO.setFilledBag(emptyBagModel);
        StorageBag.FilledBagModel filledBagModel = bagDAO.getSerializeBagById(bagId);
        StorageBag storageBagBefore = new StorageBag(emptyBagModel);
        StorageBag storageBagAfter = new StorageBag(filledBagModel);
        assertEquals(storageBagBefore.getDescription(), storageBagAfter.getDescription());
        assertEquals(bagId, storageBagAfter.getID());
        assertEquals(storageBagBefore.getName(), storageBagAfter.getName());
    }

    @Test
    public void addOneItemInBagSuccessful(){
        StorageBag.EmptyBagModel emptyBagModel = new StorageBag.EmptyBagModel("my", "MY BEST BAG", 16);
        Integer bagId = bagDAO.setFilledBag(emptyBagModel);
        final Random random = new Random(System.currentTimeMillis());
        final EquipableItem equipableItem = pcgFactory.makeItem(random.nextInt(Constants.MAX_LEVEL) + Constants.START_LEVEL);
        bagDAO.addItemsArrayToBag(bagId, new Integer[]{equipableItem.getID()});
    }

    @Test
    public void addManyItemsInBagSuccessful(){
        StorageBag.EmptyBagModel emptyBagModel = new StorageBag.EmptyBagModel("my", "MY BEST BAG", 16);
        Integer bagId = bagDAO.setFilledBag(emptyBagModel);
        final Random random = new Random(System.currentTimeMillis());
        final EquipableItem equipableItem1 = pcgFactory.makeItem(random.nextInt(Constants.MAX_LEVEL) + Constants.START_LEVEL);
        final EquipableItem equipableItem2 = pcgFactory.makeItem(random.nextInt(Constants.MAX_LEVEL) + Constants.START_LEVEL);
        bagDAO.addItemsArrayToBag(bagId, new Integer[]{equipableItem1.getID(), equipableItem2.getID()});
    }

    @Test
    public void deleteOneItemInBagSuccessful() {
        StorageBag.EmptyBagModel emptyBagModel = new StorageBag.EmptyBagModel("my", "MY BEST BAG", 16);
        Integer bagId = bagDAO.setFilledBag(emptyBagModel);
        final Random random = new Random(System.currentTimeMillis());
        final EquipableItem equipableItem = pcgFactory.makeItem(random.nextInt(Constants.MAX_LEVEL) + Constants.START_LEVEL);
        bagDAO.addItemsArrayToBag(bagId, new Integer[]{equipableItem.getID()});
        bagDAO.deleteItemsArrayFromBag(bagId, new Integer[]{equipableItem.getID()});
    }

    @Test
    public void deleteManyItemsInBagSuccessful() {
        StorageBag.EmptyBagModel emptyBagModel = new StorageBag.EmptyBagModel("my", "MY BEST BAG", 16);
        Integer bagId = bagDAO.setFilledBag(emptyBagModel);
        final Random random = new Random(System.currentTimeMillis());
        final EquipableItem equipableItem1 = pcgFactory.makeItem(random.nextInt(Constants.MAX_LEVEL) + Constants.START_LEVEL);
        final EquipableItem equipableItem2 = pcgFactory.makeItem(random.nextInt(Constants.MAX_LEVEL) + Constants.START_LEVEL);
        bagDAO.addItemsArrayToBag(bagId, new Integer[]{equipableItem1.getID(), equipableItem2.getID()});
        bagDAO.deleteItemsArrayFromBag(bagId, new Integer[]{equipableItem1.getID(), equipableItem2.getID()});
    }


}
