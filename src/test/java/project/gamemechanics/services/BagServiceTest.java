package project.gamemechanics.services;

import javafx.util.Pair;
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
    public void getBagAfterSet_ExpectedEqualsBags() {
        BagDatabaseModel bagBeforeDB = new BagDatabaseModel("testName", "better bag ever");
        Integer bagId = bagDAO.setBag(bagBeforeDB);
        BagDatabaseModel bagFromDB = bagDAO.getBagById(bagId);
        assertEquals(bagBeforeDB.getDescription(), bagFromDB.getDescription());
        assertEquals(bagId, bagFromDB.getId());
        assertEquals(bagBeforeDB.getName(), bagFromDB.getName());
    }

    @Test
    public void addOneItemInBag_Successful(){
        final Random random = new Random(System.currentTimeMillis());
        final EquipableItem equipableItem = pcgFactory.makeItem(random.nextInt(Constants.MAX_LEVEL) + Constants.START_LEVEL);
        BagDatabaseModel newBag = new BagDatabaseModel("testName", "awesomeBag");
        Integer bagId = bagDAO.setBag(newBag);
        List<Pair<Integer, Integer>> slotIndexToItemIdList = new ArrayList<>();
        slotIndexToItemIdList.add(new Pair<>(1, equipableItem.getID()));
        bagDAO.updateSlotsInBag(bagId, slotIndexToItemIdList);
    }

    @Test
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
        bagDAO.updateSlotsInBag(bagId, slotIndexToItemIdList);
    }

    @Test
    public void deleteItemFromBag_Successful() {
        final Random random = new Random(System.currentTimeMillis());
        final EquipableItem equipableItem = pcgFactory.makeItem(random.nextInt(Constants.MAX_LEVEL) + Constants.START_LEVEL);
        BagDatabaseModel newBag = new BagDatabaseModel("testName", "awesomeBag");
        Integer bagId = bagDAO.setBag(newBag);
        List<Pair<Integer, Integer>> slotIndexToItemIdList = new ArrayList<>();
        slotIndexToItemIdList.add(new Pair<>(1, equipableItem.getID()));
        bagDAO.updateSlotsInBag(bagId, slotIndexToItemIdList);
        List<Pair<Integer, Integer>> slotIndexToItemIdListToDelete = new ArrayList<>();
        slotIndexToItemIdListToDelete.add(new Pair<>(1, Constants.UNDEFINED_ID));
        bagDAO.updateSlotsInBag(bagId, slotIndexToItemIdListToDelete);
    }

    @Test
    public void equalsUndefinedIdAfterDeleteItemFromBag_Successful() {
        final Random random = new Random(System.currentTimeMillis());
        final EquipableItem equipableItem = pcgFactory.makeItem(random.nextInt(Constants.MAX_LEVEL) + Constants.START_LEVEL);
        BagDatabaseModel newBag = new BagDatabaseModel("testName", "awesomeBag");
        Integer bagId = bagDAO.setBag(newBag);
        List<Pair<Integer, Integer>> slotIndexToItemIdList = new ArrayList<>();
        slotIndexToItemIdList.add(new Pair<>(1, equipableItem.getID()));
        bagDAO.updateSlotsInBag(bagId, slotIndexToItemIdList);
        List<Pair<Integer, Integer>> slotIndexToItemIdListToDelete = new ArrayList<>();
        slotIndexToItemIdListToDelete.add(new Pair<>(1, Constants.UNDEFINED_ID));
        bagDAO.updateSlotsInBag(bagId, slotIndexToItemIdListToDelete);
        assertEquals(bagDAO.getBagById(bagId).getId().intValue(), Constants.UNDEFINED_ID);
    }


}
