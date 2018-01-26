package project.gamemechanics;

import org.junit.Test;
import project.gamemechanics.aliveentities.helpers.CashCalculator;
import project.gamemechanics.aliveentities.helpers.ExperienceCalculator;
import project.gamemechanics.battlefield.actionresults.ActionResult;
import project.gamemechanics.battlefield.actionresults.events.EventCategories;
import project.gamemechanics.battlefield.aliveentitiescontainers.CharactersParty;
import project.gamemechanics.battlefield.aliveentitiescontainers.Squad;
import project.gamemechanics.battlefield.map.BattleMap;
import project.gamemechanics.battlefield.map.BattleMapGenerator;
import project.gamemechanics.battlefield.map.actions.BattleAction;
import project.gamemechanics.battlefield.map.helpers.Pathfinder;
import project.gamemechanics.battlefield.map.helpers.PathfindingAlgorithm;
import project.gamemechanics.battlefield.map.helpers.Route;
import project.gamemechanics.components.properties.PropertyCategories;
import project.gamemechanics.globals.Constants;
import project.gamemechanics.globals.DigitsPairIndices;
import project.gamemechanics.interfaces.Action;
import project.gamemechanics.interfaces.AliveEntity;
import project.gamemechanics.interfaces.EquipableItem;
import project.gamemechanics.interfaces.MapNode;
import project.gamemechanics.items.loot.PendingLootPool;
import project.gamemechanics.items.loot.PendingLootPoolImpl;
import project.gamemechanics.resources.assets.AssetProvider;
import project.gamemechanics.resources.assets.AssetProviderImpl;
import project.gamemechanics.resources.assets.DummiesFactory;
import project.gamemechanics.resources.pcg.PcgContentFactory;
import project.gamemechanics.resources.pcg.PcgFactory;
import project.gamemechanics.world.config.ResourcesConfig;

import java.util.ArrayList;
import java.util.List;
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
        assertTrue(party.addMember(firstUserCharacter.getProperty(PropertyCategories.PC_ACTIVE_ROLE),
                firstUserCharacter));
        assertTrue(party.addMember(secondUserCharacter.getProperty(PropertyCategories.PC_ACTIVE_ROLE),
                secondUserCharacter));

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

    @Test
    public void partyTwoTanksJoinTest() {
        final AssetProvider assets = new AssetProviderImpl(ResourcesConfig.getAssetHoldersFileNames());
        final DummiesFactory dummiesFactory = new DummiesFactory(assets);
        final PendingLootPool lootPool = new PendingLootPoolImpl();

        final AliveEntity firstUserCharacter = dummiesFactory.makeNewDummy(0, "warrior", "");
        assertNotEquals(null, firstUserCharacter );
        assertEquals(Constants.UNDEFINED_ID, Objects.requireNonNull(firstUserCharacter)
                .getProperty(PropertyCategories.PC_PARTY_ID).intValue());
        final AliveEntity secondUserCharacter = dummiesFactory.makeNewDummy(0, "mage", "");
        assertNotEquals(null, secondUserCharacter );
        assertEquals(Constants.UNDEFINED_ID, Objects.requireNonNull(secondUserCharacter)
                .getProperty(PropertyCategories.PC_PARTY_ID).intValue());

        final CharactersParty party = new CharactersParty(lootPool);
        assertTrue(party.addMember(firstUserCharacter.getProperty(PropertyCategories.PC_ACTIVE_ROLE),
                firstUserCharacter));
        assertFalse(party.addMember(secondUserCharacter.getProperty(PropertyCategories.PC_ACTIVE_ROLE),
                secondUserCharacter));
    }

    @Test
    public void partyTwoDamagersJoinTest() {
        final AssetProvider assets = new AssetProviderImpl(ResourcesConfig.getAssetHoldersFileNames());
        final DummiesFactory dummiesFactory = new DummiesFactory(assets);
        final PendingLootPool lootPool = new PendingLootPoolImpl();

        final AliveEntity firstUserCharacter = dummiesFactory.makeNewDummy(1, "warrior", "");
        assertNotEquals(null, firstUserCharacter );
        assertEquals(Constants.UNDEFINED_ID, Objects.requireNonNull(firstUserCharacter)
                .getProperty(PropertyCategories.PC_PARTY_ID).intValue());
        final AliveEntity secondUserCharacter = dummiesFactory.makeNewDummy(1, "mage", "");
        assertNotEquals(null, secondUserCharacter );
        assertEquals(Constants.UNDEFINED_ID, Objects.requireNonNull(secondUserCharacter)
                .getProperty(PropertyCategories.PC_PARTY_ID).intValue());

        final CharactersParty party = new CharactersParty(lootPool);
        assertTrue(party.addMember(firstUserCharacter.getProperty(PropertyCategories.PC_ACTIVE_ROLE),
                firstUserCharacter));
        assertTrue(party.addMember(secondUserCharacter.getProperty(PropertyCategories.PC_ACTIVE_ROLE),
                secondUserCharacter));
    }

    @Test
    public void mapGenerationTest() {
        final int passableTilesCount = 16;
        final List<List<MapNode>> tiles = BattleMapGenerator.generateBattleMap(4, 4, passableTilesCount);
        Integer passableTiles = 0;
        for (List<MapNode> mapRow : tiles) {
            for (MapNode tile : mapRow) {
                if (tile.getIsPassable()) {
                    ++passableTiles;
                }
            }
        }
        assertEquals(passableTilesCount, passableTiles.intValue());
    }

    @Test
    public void oneVsOneBattleTest() {
        final AssetProvider assets = new AssetProviderImpl(ResourcesConfig.getAssetHoldersFileNames());
        final DummiesFactory dummiesFactory = new DummiesFactory(assets);
        final PcgContentFactory factory = new PcgFactory(ResourcesConfig.getItemPartsFilename(),
                ResourcesConfig.getNpcPartsFilename(), assets);

        final int sideSize = 4;
        final int passableTilesCount = sideSize * sideSize;
        final BattleMap map = new BattleMap(BattleMapGenerator.generateBattleMap(
                sideSize, sideSize, passableTilesCount));
        assertEquals(sideSize, map.getSize().get(0).intValue());
        assertEquals(sideSize, map.getSize().get(1).intValue());

        final AliveEntity warrior = dummiesFactory.makeNewDummy(0, "warrior", "");
        assertNotNull(warrior);

        final MapNode warriorTile = map.getTile(sideSize / 2, sideSize / 2 - 1);
        Objects.requireNonNull(warriorTile).occupy(warrior);
        assertTrue(warriorTile.isOccupied());

        final AliveEntity monster = factory.makeNpc(Constants.START_LEVEL);
        final Integer monsterCurrentHealth = 1;
        monster.setProperty(PropertyCategories.PC_HITPOINTS, DigitsPairIndices.CURRENT_VALUE_INDEX,
                monsterCurrentHealth);
        final MapNode monsterTile = map.getTile(sideSize / 2, sideSize / 2);
        Objects.requireNonNull(monsterTile).occupy(monster);
        assertTrue(monsterTile.isOccupied());

        final Integer xpReward = ExperienceCalculator.getXPReward(warrior.getLevel(), monster.getLevel());
        final Integer goldReward = CashCalculator.getCashReward(monster.getLevel());

        final Action warriorAttack = new BattleAction(warriorTile, monsterTile,
                warrior.getAbility(0), null);
        final ActionResult attackResult = warriorAttack.execute();
        if (!monster.isAlive()) {
            warrior.modifyPropertyByAddition(PropertyCategories.PC_XP_POINTS,
                    DigitsPairIndices.CURRENT_VALUE_INDEX, xpReward);
            warrior.modifyPropertyByAddition(PropertyCategories.PC_CASH_AMOUNT, goldReward);
        }
        assertFalse(monster.isAlive());
        assertEquals(xpReward, warrior.getProperty(PropertyCategories.PC_XP_POINTS,
                DigitsPairIndices.CURRENT_VALUE_INDEX));
        assertEquals(goldReward, warrior.getProperty(PropertyCategories.PC_CASH_AMOUNT));

        assertFalse(attackResult.getEventsCount() == 0);
        assertTrue(attackResult.getEvent(0).getEventKind() == EventCategories.EC_HITPOINTS_CHANGE);
        assertTrue(Math.abs(attackResult.getEvent(0).getAmount()) > monsterCurrentHealth);
    }

    @Test
    public void pathfindingTestValidDataTest() {
        final int sideSize = 4;
        final int passableTilesCount = sideSize * sideSize;
        final BattleMap map = new BattleMap(BattleMapGenerator.generateBattleMap(
                sideSize, sideSize, passableTilesCount));
        final PathfindingAlgorithm pathfinder = new Pathfinder(map);
        final List<Integer> sourceCoordinates = new ArrayList<>(DigitsPairIndices.PAIR_SIZE);
        sourceCoordinates.add(DigitsPairIndices.ROW_COORD_INDEX, 0);
        sourceCoordinates.add(DigitsPairIndices.COL_COORD_INDEX, 0);

        final List<Integer> goalCoordinates = new ArrayList<>(DigitsPairIndices.PAIR_SIZE);
        goalCoordinates.add(DigitsPairIndices.ROW_COORD_INDEX, 0);
        goalCoordinates.add(DigitsPairIndices.COL_COORD_INDEX, sideSize - 1);

        assertNotNull(map.getTile(sourceCoordinates.get(DigitsPairIndices.ROW_COORD_INDEX),
                sourceCoordinates.get(DigitsPairIndices.COL_COORD_INDEX)));
        assertNotNull(map.getTile(goalCoordinates.get(DigitsPairIndices.ROW_COORD_INDEX),
                goalCoordinates.get(DigitsPairIndices.COL_COORD_INDEX)));

        final Route route = pathfinder.getPath(sourceCoordinates, goalCoordinates);
        final Route reversedRoute = pathfinder.getPath(goalCoordinates, sourceCoordinates);

        assertNotNull(route);
        assertTrue(route.getLength() > 0);
        assertEquals(route.getGoalCoordinates(), goalCoordinates);
        assertEquals(route.getStartCoordinates(), sourceCoordinates);
        assertEquals(route.getLength().intValue(), sideSize - 1);

        assertNotNull(reversedRoute);
        assertTrue(reversedRoute.getLength() > 0);
        assertEquals(reversedRoute.getStartCoordinates(), goalCoordinates);
        assertEquals(reversedRoute.getGoalCoordinates(), sourceCoordinates);
        assertEquals(reversedRoute.getLength().intValue(), sideSize - 1);

        assertEquals(route.getGoalCoordinates(), reversedRoute.getStartCoordinates());
        assertEquals(route.getStartCoordinates(), reversedRoute.getGoalCoordinates());
    }

    @Test
    public void pathfindingTestInvalidDataTest() {
        final int sideSize = 4;
        final int passableTilesCount = sideSize * sideSize;
        final BattleMap map = new BattleMap(BattleMapGenerator.generateBattleMap(
                sideSize, sideSize, passableTilesCount));
        final PathfindingAlgorithm pathfinder = new Pathfinder(map);
        final List<Integer> sourceCoordinates = new ArrayList<>(DigitsPairIndices.PAIR_SIZE);
        sourceCoordinates.add(DigitsPairIndices.ROW_COORD_INDEX, -1);
        sourceCoordinates.add(DigitsPairIndices.COL_COORD_INDEX, -1);

        final List<Integer> goalCoordinates = new ArrayList<>(DigitsPairIndices.PAIR_SIZE);
        goalCoordinates.add(DigitsPairIndices.ROW_COORD_INDEX, sideSize);
        goalCoordinates.add(DigitsPairIndices.COL_COORD_INDEX, sideSize);

        assertNull(map.getTile(sourceCoordinates.get(DigitsPairIndices.ROW_COORD_INDEX),
                sourceCoordinates.get(DigitsPairIndices.COL_COORD_INDEX)));
        assertNull(map.getTile(goalCoordinates.get(DigitsPairIndices.ROW_COORD_INDEX),
                goalCoordinates.get(DigitsPairIndices.COL_COORD_INDEX)));

        final Route route = pathfinder.getPath(sourceCoordinates, goalCoordinates);
        final Route reversedRoute = pathfinder.getPath(goalCoordinates, sourceCoordinates);

        assertNull(route);
        assertNull(reversedRoute);

        goalCoordinates.set(DigitsPairIndices.ROW_COORD_INDEX, sideSize - 1);
        goalCoordinates.set(DigitsPairIndices.COL_COORD_INDEX, sideSize - 1);

        assertNull(pathfinder.getPath(sourceCoordinates, goalCoordinates));
        assertNull(pathfinder.getPath(goalCoordinates, sourceCoordinates));
    }

    @Test
    public void pathfinderDiagonalMovementTest() {
        final int sideSize = 4;
        final int passableTilesCount = sideSize * sideSize;
        final BattleMap map = new BattleMap(BattleMapGenerator.generateBattleMap(
                sideSize, sideSize, passableTilesCount));
        final PathfindingAlgorithm pathfinder = new Pathfinder(map);
        final List<Integer> sourceCoordinates = new ArrayList<>(DigitsPairIndices.PAIR_SIZE);
        sourceCoordinates.add(DigitsPairIndices.ROW_COORD_INDEX, 0);
        sourceCoordinates.add(DigitsPairIndices.COL_COORD_INDEX, 0);

        final List<Integer> goalCoordinates = new ArrayList<>(DigitsPairIndices.PAIR_SIZE);
        goalCoordinates.add(DigitsPairIndices.ROW_COORD_INDEX, sideSize - 1);
        goalCoordinates.add(DigitsPairIndices.COL_COORD_INDEX, sideSize - 1);

        assertNotNull(map.getTile(sourceCoordinates.get(DigitsPairIndices.ROW_COORD_INDEX),
                sourceCoordinates.get(DigitsPairIndices.COL_COORD_INDEX)));
        assertNotNull(map.getTile(goalCoordinates.get(DigitsPairIndices.ROW_COORD_INDEX),
                goalCoordinates.get(DigitsPairIndices.COL_COORD_INDEX)));

        final Route route = pathfinder.getPath(sourceCoordinates, goalCoordinates);
        final Route reversedRoute = pathfinder.getPath(goalCoordinates, sourceCoordinates);

        assertNotNull(route);
        assertNotNull(reversedRoute);

        assertEquals(route.getLength().intValue(), reversedRoute.getLength().intValue());

        assertEquals(route.getStartCoordinates(), reversedRoute.getGoalCoordinates());
        assertEquals(route.getGoalCoordinates(), reversedRoute.getStartCoordinates());

        assertEquals(route.getLength().intValue(), (sideSize - 1) * 2);
    }

    @Test
    public void pathfinderComplexMovementTest() {
        final int sideSize = 4;
        final int passableTilesCount = sideSize * sideSize;
        final BattleMap map = new BattleMap(BattleMapGenerator.generateBattleMap(
                sideSize, sideSize, passableTilesCount));
        final PathfindingAlgorithm pathfinder = new Pathfinder(map);
        final List<Integer> sourceCoordinates = new ArrayList<>(DigitsPairIndices.PAIR_SIZE);
        sourceCoordinates.add(DigitsPairIndices.ROW_COORD_INDEX, 0);
        sourceCoordinates.add(DigitsPairIndices.COL_COORD_INDEX, 0);

        final List<Integer> goalCoordinates = new ArrayList<>(DigitsPairIndices.PAIR_SIZE);
        goalCoordinates.add(DigitsPairIndices.ROW_COORD_INDEX, sideSize / 2 - 1);
        goalCoordinates.add(DigitsPairIndices.COL_COORD_INDEX, sideSize - 1);

        assertNotNull(map.getTile(sourceCoordinates.get(DigitsPairIndices.ROW_COORD_INDEX),
                sourceCoordinates.get(DigitsPairIndices.COL_COORD_INDEX)));
        assertNotNull(map.getTile(goalCoordinates.get(DigitsPairIndices.ROW_COORD_INDEX),
                goalCoordinates.get(DigitsPairIndices.COL_COORD_INDEX)));

        final Route route = pathfinder.getPath(sourceCoordinates, goalCoordinates);
        final Route reversedRoute = pathfinder.getPath(goalCoordinates, sourceCoordinates);

        assertNotNull(route);
        assertNotNull(reversedRoute);

        assertEquals(route.getLength().intValue(), reversedRoute.getLength().intValue());
        assertEquals(route.getStartCoordinates(), reversedRoute.getGoalCoordinates());
        assertEquals(route.getGoalCoordinates(), reversedRoute.getStartCoordinates());
    }

    @Test
    public void pathfinderMapWithWallsTest() {
        final int sideSize = 5;
        final int halfSize = 2;
        final int passableTilesCount = sideSize * sideSize;
        final BattleMap map = new BattleMap(BattleMapGenerator.generateBattleMap(
                sideSize, sideSize, passableTilesCount));

        Objects.requireNonNull(map.getTile(halfSize, halfSize)).setIsPassable(false);
        Objects.requireNonNull(map.getTile(halfSize, halfSize - 1)).setIsPassable(false);
        Objects.requireNonNull(map.getTile(halfSize, halfSize + 1)).setIsPassable(false);
        Objects.requireNonNull(map.getTile(halfSize + 1, halfSize)).setIsPassable(false);
        Objects.requireNonNull(map.getTile(halfSize - 1, halfSize)).setIsPassable(false);

        final PathfindingAlgorithm pathfinder = new Pathfinder(map);

        final List<Integer> sourceCoordinates = new ArrayList<>(DigitsPairIndices.PAIR_SIZE);
        sourceCoordinates.add(DigitsPairIndices.ROW_COORD_INDEX, 0);
        sourceCoordinates.add(DigitsPairIndices.COL_COORD_INDEX, 0);

        final List<Integer> goalCoordinates = new ArrayList<>(DigitsPairIndices.PAIR_SIZE);
        goalCoordinates.add(DigitsPairIndices.ROW_COORD_INDEX, halfSize);
        goalCoordinates.add(DigitsPairIndices.COL_COORD_INDEX, sideSize - 1);

        assertNotNull(map.getTile(sourceCoordinates.get(DigitsPairIndices.ROW_COORD_INDEX),
                sourceCoordinates.get(DigitsPairIndices.COL_COORD_INDEX)));
        assertNotNull(map.getTile(goalCoordinates.get(DigitsPairIndices.ROW_COORD_INDEX),
                goalCoordinates.get(DigitsPairIndices.COL_COORD_INDEX)));

        final Route route = pathfinder.getPath(sourceCoordinates, goalCoordinates);
        final Route reversedRoute = pathfinder.getPath(goalCoordinates, sourceCoordinates);

        final List<Integer> fourthStep = new ArrayList<>();
        fourthStep.add(DigitsPairIndices.ROW_COORD_INDEX, 0);
        fourthStep.add(DigitsPairIndices.COL_COORD_INDEX, 4);

        assertNotNull(route);
        assertNotNull(reversedRoute);
        assertEquals(6, route.getLength().intValue());
        assertEquals(route.getLength().intValue(), reversedRoute.getLength().intValue());
        assertEquals(route.getGoalCoordinates(4).get(DigitsPairIndices.ROW_COORD_INDEX).intValue(),
                fourthStep.get(DigitsPairIndices.ROW_COORD_INDEX).intValue());
        assertEquals(route.getGoalCoordinates(4).get(DigitsPairIndices.COL_COORD_INDEX).intValue(),
                fourthStep.get(DigitsPairIndices.COL_COORD_INDEX).intValue());
    }

    @Test
    public void pathfinderTraverseTest() {
        final int sideSize = 5;
        final int halfSize = 2;
        final int passableTilesCount = sideSize * sideSize;
        final AssetProvider assets = new AssetProviderImpl(ResourcesConfig.getAssetHoldersFileNames());
        final DummiesFactory dummiesFactory = new DummiesFactory(assets);
        final BattleMap map = new BattleMap(BattleMapGenerator.generateBattleMap(
                sideSize, sideSize, passableTilesCount));

        Objects.requireNonNull(map.getTile(halfSize, halfSize)).setIsPassable(false);
        Objects.requireNonNull(map.getTile(halfSize, halfSize - 1)).setIsPassable(false);
        Objects.requireNonNull(map.getTile(halfSize, halfSize + 1)).setIsPassable(false);
        Objects.requireNonNull(map.getTile(halfSize + 1, halfSize)).setIsPassable(false);
        Objects.requireNonNull(map.getTile(halfSize - 1, halfSize)).setIsPassable(false);

        final PathfindingAlgorithm pathfinder = new Pathfinder(map);
        final AliveEntity walker = dummiesFactory.makeNewDummy(0, "warrior", "");
        assertNotNull(walker);

        Objects.requireNonNull(map.getTile(0, 0)).occupy(walker);

        final List<Integer> sourceCoordinates = new ArrayList<>(DigitsPairIndices.PAIR_SIZE);
        sourceCoordinates.add(DigitsPairIndices.ROW_COORD_INDEX, 0);
        sourceCoordinates.add(DigitsPairIndices.COL_COORD_INDEX, 0);

        final List<Integer> goalCoordinates = new ArrayList<>(DigitsPairIndices.PAIR_SIZE);
        goalCoordinates.add(DigitsPairIndices.ROW_COORD_INDEX, halfSize);
        goalCoordinates.add(DigitsPairIndices.COL_COORD_INDEX, sideSize - 1);

        assertNotNull(map.getTile(sourceCoordinates.get(DigitsPairIndices.ROW_COORD_INDEX),
                sourceCoordinates.get(DigitsPairIndices.COL_COORD_INDEX)));
        assertNotNull(map.getTile(goalCoordinates.get(DigitsPairIndices.ROW_COORD_INDEX),
                goalCoordinates.get(DigitsPairIndices.COL_COORD_INDEX)));

        final Route route = pathfinder.getPath(sourceCoordinates, goalCoordinates);
        final Route reversedRoute = pathfinder.getPath(goalCoordinates, sourceCoordinates);

        assertNotNull(route);
        assertNotNull(reversedRoute);

        route.walkThrough();
        assertEquals(walker.getProperty(PropertyCategories.PC_COORDINATES, DigitsPairIndices.ROW_COORD_INDEX),
                route.getGoalCoordinates().get(DigitsPairIndices.ROW_COORD_INDEX));
        assertEquals(walker.getProperty(PropertyCategories.PC_COORDINATES, DigitsPairIndices.COL_COORD_INDEX),
                route.getGoalCoordinates().get(DigitsPairIndices.COL_COORD_INDEX));
        assertFalse(Objects.requireNonNull(map.getTile(0,0)).isOccupied());
        assertTrue(Objects.requireNonNull(map.getTile(goalCoordinates.get(DigitsPairIndices.ROW_COORD_INDEX),
                goalCoordinates.get(DigitsPairIndices.COL_COORD_INDEX))).isOccupied());

        reversedRoute.walkThrough();
        assertEquals(walker.getProperty(PropertyCategories.PC_COORDINATES, DigitsPairIndices.ROW_COORD_INDEX),
                reversedRoute.getGoalCoordinates().get(DigitsPairIndices.ROW_COORD_INDEX));
        assertEquals(walker.getProperty(PropertyCategories.PC_COORDINATES, DigitsPairIndices.COL_COORD_INDEX),
                reversedRoute.getGoalCoordinates().get(DigitsPairIndices.COL_COORD_INDEX));
        assertTrue(Objects.requireNonNull(map.getTile(0,0)).isOccupied());
        assertFalse(Objects.requireNonNull(map.getTile(goalCoordinates.get(DigitsPairIndices.ROW_COORD_INDEX),
                goalCoordinates.get(DigitsPairIndices.COL_COORD_INDEX))).isOccupied());
    }

    @Test
    public void aiDecisionMakingTest() {

    }
}
