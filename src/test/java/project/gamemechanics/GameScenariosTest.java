package project.gamemechanics;

import org.junit.Test;
import project.gamemechanics.battlefield.aliveentitiescontainers.CharactersParty;
import project.gamemechanics.battlefield.aliveentitiescontainers.Squad;
import project.gamemechanics.components.properties.PropertyCategories;
import project.gamemechanics.globals.Constants;
import project.gamemechanics.interfaces.AliveEntity;
import project.gamemechanics.interfaces.EquipableItem;
import project.gamemechanics.items.loot.PendingLootPool;
import project.gamemechanics.items.loot.PendingLootPoolImpl;
import project.gamemechanics.resources.assets.AssetProvider;
import project.gamemechanics.resources.assets.AssetProviderImpl;
import project.gamemechanics.resources.assets.DummiesFactory;
import project.gamemechanics.resources.pcg.PcgContentFactory;
import project.gamemechanics.resources.pcg.PcgFactory;
import project.gamemechanics.world.config.ResourcesConfig;

import java.util.Objects;

import static org.junit.Assert.*;

public class GameScenariosTest {
    @Test
    public void charactersPartyJoinLeaveTest() {
        final AssetProvider assets = new AssetProviderImpl(ResourcesConfig.getAssetHoldersFileNames());
        final DummiesFactory dummiesFactory = new DummiesFactory(assets);
        final PendingLootPool lootPool = new PendingLootPoolImpl();

        final AliveEntity firstUserCharacter = dummiesFactory.makeNewDummy(0, "warrior", "");
        assertNotEquals(null, firstUserCharacter );
        assertEquals(Constants.UNDEFINED_ID, Objects.requireNonNull(firstUserCharacter)
                .getProperty(PropertyCategories.PC_PARTY_ID).intValue());
        final AliveEntity secondUserCharacter = dummiesFactory.makeNewDummy(1, "mage", "");
        assertNotEquals(null, secondUserCharacter );
        assertEquals(Constants.UNDEFINED_ID, Objects.requireNonNull(secondUserCharacter)
                .getProperty(PropertyCategories.PC_PARTY_ID).intValue());

        final CharactersParty party = new CharactersParty(lootPool);
        party.addMember(firstUserCharacter.getProperty(PropertyCategories.PC_ACTIVE_ROLE), firstUserCharacter);
        assertEquals(party.getID(), firstUserCharacter.getProperty(PropertyCategories.PC_PARTY_ID));
        party.addMember(secondUserCharacter.getProperty(PropertyCategories.PC_ACTIVE_ROLE), secondUserCharacter);
        assertEquals(party.getID(), secondUserCharacter.getProperty(PropertyCategories.PC_PARTY_ID));

        assertEquals(firstUserCharacter.getProperty(PropertyCategories.PC_PARTY_ID),
                secondUserCharacter.getProperty(PropertyCategories.PC_PARTY_ID));

        party.removeMember(firstUserCharacter.getProperty(PropertyCategories.PC_ACTIVE_ROLE));
        assertNotEquals(firstUserCharacter.getProperty(PropertyCategories.PC_PARTY_ID),
                secondUserCharacter.getProperty(PropertyCategories.PC_PARTY_ID));
        assertEquals(Constants.UNDEFINED_ID, Objects.requireNonNull(firstUserCharacter)
                .getProperty(PropertyCategories.PC_PARTY_ID).intValue());
        party.removeMember(secondUserCharacter.getProperty(PropertyCategories.PC_ACTIVE_ROLE));
        assertNotEquals(party.getID(), secondUserCharacter.getProperty(PropertyCategories.PC_PARTY_ID));

        assertEquals(firstUserCharacter.getProperty(PropertyCategories.PC_PARTY_ID),
                secondUserCharacter.getProperty(PropertyCategories.PC_PARTY_ID));
    }

    @Test
    public void characterPartyToSquadTest() {
        final AssetProvider assets = new AssetProviderImpl(ResourcesConfig.getAssetHoldersFileNames());
        final DummiesFactory dummiesFactory = new DummiesFactory(assets);
        final PendingLootPool lootPool = new PendingLootPoolImpl();

        final AliveEntity firstUserCharacter = dummiesFactory.makeNewDummy(0, "warrior", "");
        assertNotEquals(null, firstUserCharacter );
        assertEquals(Constants.UNDEFINED_ID, Objects.requireNonNull(firstUserCharacter)
                .getProperty(PropertyCategories.PC_PARTY_ID).intValue());
        final AliveEntity secondUserCharacter = dummiesFactory.makeNewDummy(1, "mage", "");
        assertNotEquals(null, secondUserCharacter );
        assertEquals(Constants.UNDEFINED_ID, Objects.requireNonNull(secondUserCharacter)
                .getProperty(PropertyCategories.PC_PARTY_ID).intValue());

        final CharactersParty party = new CharactersParty(lootPool);
        party.addMember(firstUserCharacter.getProperty(PropertyCategories.PC_ACTIVE_ROLE), firstUserCharacter);
        party.addMember(secondUserCharacter.getProperty(PropertyCategories.PC_ACTIVE_ROLE), secondUserCharacter);

        final Squad squad = party.toSquad();
        assertEquals(squad.getSquadID().intValue(), Squad.PLAYERS_SQUAD_ID);
        assertEquals(firstUserCharacter.getProperty(PropertyCategories.PC_SQUAD_ID), squad.getSquadID());
        assertEquals(secondUserCharacter.getProperty(PropertyCategories.PC_SQUAD_ID), squad.getSquadID());

        squad.removeMember(firstUserCharacter);
        assertNotEquals(squad.getSquadID(), firstUserCharacter.getProperty(PropertyCategories.PC_SQUAD_ID));
        assertNotEquals(firstUserCharacter.getProperty(PropertyCategories.PC_SQUAD_ID),
                secondUserCharacter.getProperty(PropertyCategories.PC_SQUAD_ID));
        assertEquals(firstUserCharacter.getProperty(PropertyCategories.PC_PARTY_ID),
                secondUserCharacter.getProperty(PropertyCategories.PC_PARTY_ID));
        squad.removeMember(secondUserCharacter);
        assertNotEquals(squad.getSquadID(), secondUserCharacter.getProperty(PropertyCategories.PC_SQUAD_ID));
        assertEquals(firstUserCharacter.getProperty(PropertyCategories.PC_SQUAD_ID),
                secondUserCharacter.getProperty(PropertyCategories.PC_SQUAD_ID));
    }

    @Test
    public void lootTransferTest() {
        final AssetProvider assets = new AssetProviderImpl(ResourcesConfig.getAssetHoldersFileNames());
        final DummiesFactory dummiesFactory = new DummiesFactory(assets);
        final PendingLootPool lootPool = new PendingLootPoolImpl();
        final PcgContentFactory factory = new PcgFactory(ResourcesConfig.getItemPartsFilename(),
                ResourcesConfig.getNpcPartsFilename(), assets);

        final AliveEntity firstUserCharacter = dummiesFactory.makeNewDummy(0, "warrior", "");
        assertNotEquals(null, firstUserCharacter );
        final AliveEntity secondUserCharacter = dummiesFactory.makeNewDummy(1, "mage", "");
        assertNotEquals(null, secondUserCharacter );

        lootPool.createLootPool(Objects.requireNonNull(firstUserCharacter));
        lootPool.createLootPool(Objects.requireNonNull(secondUserCharacter));

        final EquipableItem firstCharactersItem = factory.makeItem(firstUserCharacter.getLevel());
        assertNotEquals(null, firstCharactersItem);
        final EquipableItem secondCharactersItem = factory.makeItem(secondUserCharacter.getLevel());
        assertNotEquals(null, secondCharactersItem);

        lootPool.offerItemToPool(firstUserCharacter, firstCharactersItem);
        assertEquals(lootPool.getLootPool(firstUserCharacter).size(), 1);
        lootPool.offerItemToPool(secondUserCharacter, secondCharactersItem);
        assertEquals(lootPool.getLootPool(secondUserCharacter).size(), 1);

        lootPool.pollItemFromPool(firstUserCharacter, 0);
        assertTrue(lootPool.getLootPool(firstUserCharacter).isEmpty());
        Integer firstCharFreeSlotsCount = 0;
        Integer firstCharTotalSlotsCount = 0;
        for (Integer i = 0; i < Constants.DEFAULT_PERSONAL_REWARD_BAG_SIZE; ++i) {
            firstCharFreeSlotsCount += firstUserCharacter.getBag(i).getFreeSlotsCount();
            firstCharTotalSlotsCount += firstUserCharacter.getBag(i).getSlotsCount();
        }
        assertEquals(firstCharTotalSlotsCount - firstCharFreeSlotsCount, 1);

        lootPool.rejectItemFromPool(secondUserCharacter, 0);
        assertTrue(lootPool.getLootPool(secondUserCharacter).isEmpty());
        Integer secondCharFreeSlotsCount = 0;
        Integer secondCharTotalSlotsCount = 0;
        for (Integer i = 0; i < Constants.DEFAULT_PERSONAL_REWARD_BAG_SIZE; ++i) {
            secondCharFreeSlotsCount += secondUserCharacter.getBag(i).getFreeSlotsCount();
            secondCharTotalSlotsCount += secondUserCharacter.getBag(i).getSlotsCount();
        }
        assertEquals(secondCharTotalSlotsCount - secondCharFreeSlotsCount, 0);
    }
}
