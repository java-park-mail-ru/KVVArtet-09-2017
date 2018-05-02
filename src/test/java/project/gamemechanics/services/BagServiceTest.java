package project.gamemechanics.services;

import javafx.util.Pair;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import project.gamemechanics.components.properties.PropertyCategories;
import project.gamemechanics.globals.Constants;
import project.gamemechanics.interfaces.EquipableItem;
import project.gamemechanics.resources.assets.AssetProvider;
import project.gamemechanics.resources.assets.AssetProviderImpl;
import project.gamemechanics.resources.pcg.PcgContentFactory;
import project.gamemechanics.resources.pcg.PcgFactory;
import project.gamemechanics.services.dbmodels.BagDatabaseModel;
import project.gamemechanics.services.interfaces.BagDAO;
import project.gamemechanics.world.config.ResourcesConfig;

import java.util.ArrayList;
import java.util.List;
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
    @Transactional
    public void getBagAfterSet_ExpectedEqualsBags() {
        BagDatabaseModel bagBeforeDB = new BagDatabaseModel("testName", "better bag ever");
        Integer bagId = bagDAO.setBag(bagBeforeDB);
        System.out.println(bagId + " BAG ID");
        BagDatabaseModel bagFromDB = bagDAO.getBagById(bagId);
        assertEquals(bagBeforeDB.getDescription(), bagFromDB.getDescription());
        assertEquals(bagId, bagFromDB.getId());
        assertEquals(bagBeforeDB.getName(), bagFromDB.getName());
    }

    @Test
    @Transactional
    public void addOneItemInBag_Successful(){
        final Random random = new Random(System.currentTimeMillis());
        final EquipableItem equipableItem = pcgFactory.makeItem(random.nextInt(Constants.MAX_LEVEL) + Constants.START_LEVEL);
        BagDatabaseModel newBag = new BagDatabaseModel("testName", "awesomeBag");
        Integer bagId = bagDAO.setBag(newBag);
        List<Pair<Integer, Integer>> slotIndexToItemIdList = new ArrayList<>();
        slotIndexToItemIdList.add(new Pair<>(1, equipableItem.getID()));
        bagDAO.updateManySlotsInBag(bagId, slotIndexToItemIdList);
    }

    @Test
    @Transactional
    public void addManyItemsInBag_Successful(){
        final Random random = new Random(System.currentTimeMillis());
        final EquipableItem equipableItem1 = pcgFactory.makeItem(random.nextInt(Constants.MAX_LEVEL) + Constants.START_LEVEL);
        final EquipableItem equipableItem2 = pcgFactory.makeItem(random.nextInt(Constants.MAX_LEVEL) + Constants.START_LEVEL);
        final EquipableItem equipableItem3 = pcgFactory.makeItem(random.nextInt(Constants.MAX_LEVEL) + Constants.START_LEVEL);
        BagDatabaseModel newBag = new BagDatabaseModel("testName", "awesomeBag");
        Integer bagId = bagDAO.setBag(newBag);
        List<Pair<Integer, Integer>> slotIndexToItemIdList = new ArrayList<>();
        slotIndexToItemIdList.add(new Pair<>(1, equipableItem1.getID()));
        slotIndexToItemIdList.add(new Pair<>(2, equipableItem2.getID()));
        slotIndexToItemIdList.add(new Pair<>(3, equipableItem3.getID()));
        bagDAO.updateManySlotsInBag(bagId, slotIndexToItemIdList);
    }

    @Test
    @Transactional
    public void deleteItemFromBag_Successful() {
        final Random random = new Random(System.currentTimeMillis());
        final EquipableItem equipableItem = pcgFactory.makeItem(random.nextInt(Constants.MAX_LEVEL) + Constants.START_LEVEL);
        BagDatabaseModel newBag = new BagDatabaseModel("testName", "awesomeBag");
        Integer bagId = bagDAO.setBag(newBag);

        Pair<Integer, Integer> slotIndexToItemIdPair = new Pair<>(1, equipableItem.getID());
        bagDAO.updateOneSlotInBag(bagId, slotIndexToItemIdPair);

        Pair<Integer, Integer> slotIndexToItemIdPairToDelete = new Pair<>(1, Constants.UNDEFINED_ID);
        bagDAO.updateOneSlotInBag(bagId, slotIndexToItemIdPairToDelete);
    }

    @Test
    @Transactional
    public void equalsUndefinedIdAfterDeleteItemFromBag_Successful() {
        final Random random = new Random(System.currentTimeMillis());
        final EquipableItem equipableItem = pcgFactory.makeItem(random.nextInt(Constants.MAX_LEVEL) + Constants.START_LEVEL);
        BagDatabaseModel newBag = new BagDatabaseModel("testName", "awesomeBag");
        Integer bagId = bagDAO.setBag(newBag);
        System.out.println("FIRST " + bagDAO.getBagById(bagId).toString());

        Pair<Integer, Integer> slotIndexToItemIdPair = new Pair<>(1, equipableItem.getID());
        bagDAO.updateOneSlotInBag(bagId, slotIndexToItemIdPair);

        Pair<Integer, Integer> slotIndexToItemIdPairToDelete = new Pair<>(1, Constants.UNDEFINED_ID);
        bagDAO.updateOneSlotInBag(bagId, slotIndexToItemIdPairToDelete);
        assertEquals(bagDAO.getBagById(bagId).getSlotList().get(1).getItemsToQuantity().get(PropertyCategories.PC_ITEM_ID).getProperty().intValue(), Constants.UNDEFINED_ID);
    }


}
