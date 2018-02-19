package project.gamemechanics.matchmaking;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import project.gamemechanics.aliveentities.UserCharacter;
import project.gamemechanics.battlefield.aliveentitiescontainers.CharactersParty;
import project.gamemechanics.charlist.CharacterList;
import project.gamemechanics.components.properties.PropertyCategories;
import project.gamemechanics.dungeons.Instance;
import project.gamemechanics.globals.CharacterRoleIds;
import project.gamemechanics.globals.Constants;
import project.gamemechanics.globals.GameModes;
import project.gamemechanics.interfaces.AliveEntity;
import project.gamemechanics.items.loot.PendingLootPool;
import project.gamemechanics.items.loot.PendingLootPoolImpl;
import project.gamemechanics.matchmaking.mock.MockSmartController;
import project.gamemechanics.resources.assets.AssetProvider;
import project.gamemechanics.resources.assets.AssetProviderImpl;
import project.gamemechanics.resources.assets.DummiesFactory;
import project.gamemechanics.resources.pcg.PcgContentFactory;
import project.gamemechanics.resources.pcg.PcgFactory;
import project.gamemechanics.smartcontroller.SmartController;
import project.gamemechanics.world.config.ResourcesConfig;
import project.gamemechanics.world.matchmaking.invitations.InvitationPool;
import project.gamemechanics.world.matchmaking.invitations.InvitationPoolImpl;
import project.gamemechanics.world.matchmaking.invitations.invitations.Invitation;
import project.websocket.messages.Message;
import project.websocket.messages.matchmaking.MatchmakingNotificationMessage;

import javax.validation.constraints.NotNull;
import java.util.*;

import static org.junit.Assert.*;

public class InvitationsPoolTest {
    private static final int DEFAULT_TEST_USERS_COUNT = 16;
    private static final int CHARACTER_LIST_SIZE = 4;

    private static AssetProvider assetProvider;
    private static PcgContentFactory factory;
    private static Map<Integer, SmartController> smartControllersPool;
    private static DummiesFactory dummiesFactory;
    private static PendingLootPool pendingLootPool;

    private static Map<Integer, Instance> instances;
    private static Map<Integer, CharactersParty> globalPartiesPool;
    private static Map<Integer, Deque<CharactersParty>> wipPartiesPool;

    @BeforeClass
    public static void initialize() {
        assetProvider = new AssetProviderImpl(
                ResourcesConfig.getAssetHoldersFileNames());
        factory = new PcgFactory(ResourcesConfig.getItemPartsFilename(),
                ResourcesConfig.getNpcPartsFilename(), assetProvider);
        smartControllersPool = new HashMap<>();
        dummiesFactory = new DummiesFactory(assetProvider);
        pendingLootPool = new PendingLootPoolImpl();

        instances = new HashMap<>();
        globalPartiesPool = new HashMap<>();
        wipPartiesPool = new HashMap<>();
        fillWipPartiesPool();
    }

    @Before
    public void cleanup() {
        pendingLootPool.reset();
        refillSmartControllersPool();
        instances.clear();
        globalPartiesPool.clear();
        wipPartiesPool.clear();
        fillWipPartiesPool();
    }

    @Test
    public void mockSmartControllerTest() {
        final SmartController mockController = new MockSmartController();
        assertFalse("controller shall be invalid on creation",
                mockController.isValid());

        final CharacterList.CharacterListModel model =
                new CharacterList.CharacterListModel(Constants.MIN_ID_VALUE,
                        new ArrayList<>());
        assertTrue("set() method test",
                mockController.set(Constants.MIN_ID_VALUE,
                null, new CharacterList(model)));
        assertFalse("using set() on a valid controller is prohibited",
                mockController.set(Constants.MIN_ID_VALUE,
                        null, new CharacterList(model)));
        assertTrue("controller shall be valid after setup",
                mockController.isValid());
        assertNull("mock controller has no websocket session",
                mockController.getWebSocketSession());
        assertNull("outbox shall be empty on creation",
                mockController.getOutboxMessage());
        assertNull(mockController.getActiveChar());

        final Message testMessage = new MatchmakingNotificationMessage("test");
        mockController.addInboxMessage(testMessage);
        assertEquals(testMessage, mockController.getOutboxMessage());
        mockController.tick();
        assertNull(mockController.getOutboxMessage());

        mockController.reset();
        assertNull(mockController.getCharacterList());
        assertEquals(Constants.UNDEFINED_ID, mockController.getOwnerID().intValue());
        assertFalse(mockController.isValid());
        mockController.addInboxMessage(testMessage);
        assertNotEquals(testMessage, mockController.getOutboxMessage());
        assertNull(mockController.getOutboxMessage());

        assertTrue(mockController.set(Constants.MIN_ID_VALUE,
                null, new CharacterList(model)));
        assertNotNull(mockController.getCharacterList());
        assertEquals(Constants.MIN_ID_VALUE, mockController.getOwnerID().intValue());
        mockController.addInboxMessage(testMessage);
        assertEquals(testMessage, mockController.getOutboxMessage());
        mockController.tick();
        assertNull(mockController.getOutboxMessage());
    }

    @Test
    public void getNonexistingPollTest() {
        final InvitationPool invitationPool =
                new InvitationPoolImpl(assetProvider, factory,
                        smartControllersPool, globalPartiesPool,
                        wipPartiesPool, instances);
        assertNull(invitationPool.getPoll(Constants.UNDEFINED_ID));
        assertNull(invitationPool.getPoll(new Random(System.currentTimeMillis()).nextInt()));
    }

    @Test
    public void addCoopPvePollTest() {
        final InvitationPool invitationPool =
                new InvitationPoolImpl(assetProvider, factory,
                        smartControllersPool, globalPartiesPool,
                        wipPartiesPool, instances);

        final List<Integer> userId = new ArrayList<>();
        userId.add(getRandomUserId());

        final CharactersParty soloPveParty = makeTestParty(userId);
        assertTrue(soloPveParty.isFull());

        final Integer pollId = invitationPool.addPoll(soloPveParty);
        assertNotEquals(Constants.UNDEFINED_ID, pollId.intValue());
        assertNotNull(invitationPool.getPoll(pollId));
    }

    @Test
    public void coopPvePollIntoInstanceTest() {
        assertTrue(instances.isEmpty());
        assertTrue(globalPartiesPool.isEmpty());
        assertTrue(wipPartiesPool.get(GameModes.GM_COOP_PVE).isEmpty());

        final InvitationPool invitationPool =
                new InvitationPoolImpl(assetProvider, factory,
                        smartControllersPool, globalPartiesPool,
                        wipPartiesPool, instances);

        final List<Integer> userId = new ArrayList<>();
        userId.add(getRandomUserId());

        final CharactersParty soloPveParty = makeTestParty(userId);
        assertTrue(soloPveParty.isFull());

        final Integer pollId = invitationPool.addPoll(soloPveParty);
        assertNotEquals(Constants.UNDEFINED_ID, pollId.intValue());
        assertNotNull(invitationPool.getPoll(pollId));

        for (Integer roleId : soloPveParty.getRoleIds()) {
            assertTrue(invitationPool.updatePoll(pollId, GameModes.GM_COOP_PVE,
                    Objects.requireNonNull(soloPveParty.getMember(roleId)).getID(),
                    Invitation.VS_CONFIRM));
        }

        invitationPool.update();
        assertFalse(instances.isEmpty());
        assertFalse(globalPartiesPool.isEmpty());
        assertFalse(invitationPool.updatePoll(pollId, GameModes.GM_COOP_PVE,
                Objects.requireNonNull(soloPveParty.getMember(
                        CharacterRoleIds.CR_TANK)).getID(),
                Invitation.VS_CONFIRM));
    }

    @Test
    public void coopPvpPollAddTest() {

    }

    @Test
    public void coopPvpPollIntoInstanceTest() {

    }

    @Test
    public void squadPvpPollAddTest() {

    }

    @Test
    public void squadPvpPollIntoInstanceTest() {

    }

    @Test
    public void pollExpirationTest() {

    }

    @Test
    public void pollCancelTest() {

    }

    private @NotNull CharactersParty makeTestParty(@NotNull List<Integer> userIds) {
        final CharactersParty party = new CharactersParty(pendingLootPool);

        Integer roleId = CharacterRoleIds.CR_TANK;
        while (!party.isFull()) {
            for (Integer userId : userIds) {
                final CharacterList characterList =
                        Objects.requireNonNull(smartControllersPool.get(userId)
                                .getCharacterList());
                if (userIds.size() == 1) {
                    for (Integer i = 0; i < characterList.getCharacterList().size(); ++i) {
                        final AliveEntity unit = characterList.getCharacterList().get(i);
                        party.addMember(roleId++, unit);
                        roleId = roleId % CharacterRoleIds.CR_SIZE;
                        if (party.isFull()) {
                            return party;
                        }
                    }
                } else {
                    final Random random = new Random(System.currentTimeMillis());
                    final AliveEntity unit =
                            characterList.getCharacterList().get(
                                    random.nextInt(characterList
                                            .getCharacterList().size()));
                    party.addMember(roleId++, unit);
                    roleId = roleId % CharacterRoleIds.CR_SIZE;
                }
            }
        }

        return party;
    }

    private @NotNull Integer getRandomUserId() {
        final Random random = new Random(System.currentTimeMillis());
        while (true) {
            for (Integer userId : smartControllersPool.keySet()) {
                if (random.nextInt(Constants.WIDE_PERCENTAGE_CAP_INT) % 2 == 0
                        && random.nextInt(Constants.WIDE_PERCENTAGE_CAP_INT) % 2 == 0) {
                    return userId;
                }
            }
        }
    }

    private static void fillWipPartiesPool() {
        for (Integer gameMode = GameModes.GM_COOP_PVE;
             gameMode <= GameModes.GM_SQUAD_PVP; ++gameMode) {
            wipPartiesPool.put(gameMode, new ArrayDeque<>());
        }
    }

    private static @NotNull CharacterList makeCharacterList(@NotNull Integer ownerId) {
        return new CharacterList(
                new CharacterList.CharacterListModel(
                        ownerId, new ArrayList<>()));
    }

    private  void refillSmartControllersPool() {
        smartControllersPool.clear();
        for (Integer userId = 0; userId < DEFAULT_TEST_USERS_COUNT; ++userId) {
            final CharacterList characterList = makeCharacterList(userId);
            for (Integer i = 0; i < CHARACTER_LIST_SIZE; ++i) {
                final UserCharacter character = (UserCharacter)(
                        dummiesFactory.makeNewDummy(i, "user" + userId
                                + " character" + i, ""));
                Objects.requireNonNull(character)
                        .setProperty(PropertyCategories.PC_OWNER_ID, userId);
                characterList.getCharacterList().add(character);
            }
            final SmartController controller = new MockSmartController();
            controller.set(userId, null, characterList);
            smartControllersPool.put(userId, controller);
        }
    }
}
