package project.gamemechanics.matchmaking;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import project.gamemechanics.aliveentities.UserCharacter;
import project.gamemechanics.battlefield.aliveentitiescontainers.CharactersParty;
import project.gamemechanics.battlefield.aliveentitiescontainers.Squad;
import project.gamemechanics.charlist.CharacterList;
import project.gamemechanics.charlist.Charlist;
import project.gamemechanics.components.properties.PropertyCategories;
import project.gamemechanics.components.properties.SingleValueProperty;
import project.gamemechanics.dungeons.Instance;
import project.gamemechanics.globals.CharacterRoleIds;
import project.gamemechanics.globals.Constants;
import project.gamemechanics.globals.DigitsPairIndices;
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

@SuppressWarnings("Duplicates")
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
                new CharacterList.CharacterListModel(Constants.MIN_ID_VALUE, Constants.UNDEFINED_ID,
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
        assertNotNull(smartControllersPool.get(userId.get(0)).getOutboxMessage());
        assertEquals(MatchmakingNotificationMessage.class,
                Objects.requireNonNull(smartControllersPool.get(
                        userId.get(0)).getOutboxMessage()).getClass());
    }

    @Test
    public void coopPvpPollAddTest() {
        final InvitationPool invitationPool =
                new InvitationPoolImpl(assetProvider, factory,
                        smartControllersPool, globalPartiesPool,
                        wipPartiesPool, instances);
        final Map<Integer, List<Integer>> teamsUserIds = makeUserIdLists();
        final CharactersParty teamOne = makeTestParty(
                teamsUserIds.get(Squad.TEAM_ONE_SQUAD_ID));
        assertTrue(teamOne.isFull());
        final CharactersParty teamTwo = makeTestParty(
                teamsUserIds.get(Squad.TEAM_TWO_SQUAD_ID));
        assertTrue(teamTwo.isFull());
        final Map<Integer, CharactersParty> parties = new HashMap<>();
        parties.put(teamOne.getID(), teamOne);
        parties.put(teamTwo.getID(), teamTwo);

        final Integer pollId = invitationPool.addPoll(parties, GameModes.GM_COOP_PVP);
        assertNotEquals(Constants.UNDEFINED_ID, pollId.intValue());
        assertNotNull(invitationPool.getPoll(pollId));
    }

    @Test
    public void coopPvpPollIntoInstanceTest() {
        final InvitationPool invitationPool =
                new InvitationPoolImpl(assetProvider, factory,
                        smartControllersPool, globalPartiesPool,
                        wipPartiesPool, instances);
        final Map<Integer, List<Integer>> teamsUserIds = makeUserIdLists();
        final CharactersParty teamOne = makeTestParty(
                teamsUserIds.get(Squad.TEAM_ONE_SQUAD_ID));
        assertTrue(teamOne.isFull());
        final CharactersParty teamTwo = makeTestParty(
                teamsUserIds.get(Squad.TEAM_TWO_SQUAD_ID));
        assertTrue(teamTwo.isFull());
        final Map<Integer, CharactersParty> parties = new HashMap<>();
        parties.put(teamOne.getID(), teamOne);
        parties.put(teamTwo.getID(), teamTwo);

        final Integer pollId = invitationPool.addPoll(parties, GameModes.GM_COOP_PVP);
        assertNotEquals(Constants.UNDEFINED_ID, pollId.intValue());
        assertNotNull(invitationPool.getPoll(pollId));
        for (Integer characterRole : teamOne.getRoleIds()) {
            assertTrue(invitationPool.updatePoll(pollId, GameModes.GM_COOP_PVP,
                    Objects.requireNonNull(teamOne.getMember(characterRole)).getID(),
                    Invitation.VS_CONFIRM));
        }
        for (Integer characterRole : teamTwo.getRoleIds()) {
            assertTrue(invitationPool.updatePoll(pollId, GameModes.GM_COOP_PVP,
                    Objects.requireNonNull(teamTwo.getMember(characterRole)).getID(),
                    Invitation.VS_CONFIRM));
        }
        assertTrue(Objects.requireNonNull(invitationPool.getPoll(pollId)).isReady());
        invitationPool.update();
        assertFalse(invitationPool.updatePoll(pollId, GameModes.GM_COOP_PVP,
                Objects.requireNonNull(teamTwo.getMember(
                        CharacterRoleIds.CR_TANK)).getID(), Invitation.VS_CONFIRM));
        assertFalse(invitationPool.updatePoll(pollId, GameModes.GM_COOP_PVP,
                Objects.requireNonNull(teamOne.getMember(
                        CharacterRoleIds.CR_TANK)).getID(), Invitation.VS_CONFIRM));
        assertFalse(instances.isEmpty());
        assertFalse(globalPartiesPool.isEmpty());
    }

    @Test
    public void squadPvpPollAddTest() {
        final InvitationPool invitationPool =
                new InvitationPoolImpl(assetProvider, factory,
                        smartControllersPool, globalPartiesPool,
                        wipPartiesPool, instances);
        final Map<Integer, List<Integer>> teamsUserIds = makeUserIdLists();
        final CharactersParty teamOne = makeTestParty(
                teamsUserIds.get(Squad.TEAM_ONE_SQUAD_ID));
        assertTrue(teamOne.isFull());
        final CharactersParty teamTwo = makeTestParty(
                teamsUserIds.get(Squad.TEAM_TWO_SQUAD_ID));
        assertTrue(teamTwo.isFull());
        final Map<Integer, CharactersParty> parties = new HashMap<>();
        parties.put(teamOne.getID(), teamOne);
        parties.put(teamTwo.getID(), teamTwo);

        final Integer pollId = invitationPool.addPoll(parties, GameModes.GM_SQUAD_PVP);
        assertNotEquals(Constants.UNDEFINED_ID, pollId.intValue());
        assertNotNull(invitationPool.getPoll(pollId));
    }

    @Test
    public void squadPvpPollIntoInstanceTest() {
        final InvitationPool invitationPool =
                new InvitationPoolImpl(assetProvider, factory,
                        smartControllersPool, globalPartiesPool,
                        wipPartiesPool, instances);
        final Map<Integer, List<Integer>> teamsUserIds = new HashMap<>();
        teamsUserIds.put(Squad.TEAM_ONE_SQUAD_ID, new ArrayList<>());
        teamsUserIds.put(Squad.TEAM_TWO_SQUAD_ID, new ArrayList<>());
        teamsUserIds.get(Squad.TEAM_ONE_SQUAD_ID).add(getRandomUserId());
        while (true) {
            final Integer randomId = getRandomUserId();
            if (!teamsUserIds.get(Squad.TEAM_ONE_SQUAD_ID).contains(randomId)) {
                teamsUserIds.get(Squad.TEAM_TWO_SQUAD_ID).add(randomId);
                break;
            }
        }
        final CharactersParty teamOne = makeTestParty(
                teamsUserIds.get(Squad.TEAM_ONE_SQUAD_ID));
        assertTrue(teamOne.isFull());
        final CharactersParty teamTwo = makeTestParty(
                teamsUserIds.get(Squad.TEAM_TWO_SQUAD_ID));
        assertTrue(teamTwo.isFull());
        final Map<Integer, CharactersParty> parties = new HashMap<>();
        parties.put(teamOne.getID(), teamOne);
        parties.put(teamTwo.getID(), teamTwo);

        final Integer pollId = invitationPool.addPoll(parties, GameModes.GM_SQUAD_PVP);
        assertNotEquals(Constants.UNDEFINED_ID, pollId.intValue());
        assertNotNull(invitationPool.getPoll(pollId));
        for (Integer characterRole : teamOne.getRoleIds()) {
            assertTrue(invitationPool.updatePoll(pollId, GameModes.GM_SQUAD_PVP,
                    Objects.requireNonNull(teamOne.getMember(characterRole)).getID(),
                    Invitation.VS_CONFIRM));
        }
        for (Integer characterRole : teamTwo.getRoleIds()) {
            assertTrue(invitationPool.updatePoll(pollId, GameModes.GM_SQUAD_PVP,
                    Objects.requireNonNull(teamTwo.getMember(characterRole)).getID(),
                    Invitation.VS_CONFIRM));
        }
        assertTrue(Objects.requireNonNull(invitationPool.getPoll(pollId)).isReady());
        invitationPool.update();
        assertFalse(invitationPool.updatePoll(pollId, GameModes.GM_SQUAD_PVP,
                Objects.requireNonNull(teamTwo.getMember(
                        CharacterRoleIds.CR_TANK)).getID(), Invitation.VS_CONFIRM));
        assertFalse(invitationPool.updatePoll(pollId, GameModes.GM_SQUAD_PVP,
                Objects.requireNonNull(teamOne.getMember(
                        CharacterRoleIds.CR_TANK)).getID(), Invitation.VS_CONFIRM));
        assertFalse(instances.isEmpty());
        assertFalse(globalPartiesPool.isEmpty());

        checkOutboxNotEmptyForParties(teamsUserIds);
    }

    @Test
    public void pollExpirationTest() {
        final InvitationPool invitationPool =
                new InvitationPoolImpl(assetProvider, factory,
                        smartControllersPool, globalPartiesPool,
                        wipPartiesPool, instances);
        final Map<Integer, List<Integer>> squadPvpTeamsUserIds = new HashMap<>();
        squadPvpTeamsUserIds.put(Squad.TEAM_ONE_SQUAD_ID, new ArrayList<>());
        squadPvpTeamsUserIds.put(Squad.TEAM_TWO_SQUAD_ID, new ArrayList<>());
        squadPvpTeamsUserIds.get(Squad.TEAM_ONE_SQUAD_ID).add(getRandomUserId());
        while (true) {
            final Integer randomId = getRandomUserId();
            if (!squadPvpTeamsUserIds.get(Squad.TEAM_ONE_SQUAD_ID).contains(randomId)) {
                squadPvpTeamsUserIds.get(Squad.TEAM_TWO_SQUAD_ID).add(randomId);
                break;
            }
        }
        final CharactersParty squadPvpTeamOne = makeTestParty(
                squadPvpTeamsUserIds.get(Squad.TEAM_ONE_SQUAD_ID));
        assertTrue(squadPvpTeamOne.isFull());
        final CharactersParty squadPvpTeamTwo = makeTestParty(
                squadPvpTeamsUserIds.get(Squad.TEAM_TWO_SQUAD_ID));
        assertTrue(squadPvpTeamTwo.isFull());
        final Map<Integer, CharactersParty> squadPvpParties = new HashMap<>();
        squadPvpParties.put(squadPvpTeamOne.getID(), squadPvpTeamOne);
        squadPvpParties.put(squadPvpTeamTwo.getID(), squadPvpTeamTwo);
        final Integer squadPvpPollId = invitationPool.addPoll(
                squadPvpParties, GameModes.GM_SQUAD_PVP);
        assertNotEquals(Constants.UNDEFINED_ID, squadPvpPollId.intValue());
        assertNotNull(invitationPool.getPoll(squadPvpPollId));

        final Map<Integer, List<Integer>> coopPvpTeamsUserIds = makeUserIdLists();
        final CharactersParty coopPvpTeamOne = makeTestParty(
                coopPvpTeamsUserIds.get(Squad.TEAM_ONE_SQUAD_ID));
        assertTrue(coopPvpTeamOne.isFull());
        final CharactersParty coopPvpTeamTwo = makeTestParty(
                coopPvpTeamsUserIds.get(Squad.TEAM_TWO_SQUAD_ID));
        assertTrue(coopPvpTeamTwo.isFull());
        final Map<Integer, CharactersParty> coopPvpParties = new HashMap<>();
        coopPvpParties.put(coopPvpTeamOne.getID(), coopPvpTeamOne);
        coopPvpParties.put(coopPvpTeamTwo.getID(), coopPvpTeamTwo);

        final Integer coopPvpPollId = invitationPool.addPoll(
                coopPvpParties, GameModes.GM_COOP_PVP);
        assertNotEquals(Constants.UNDEFINED_ID, coopPvpPollId.intValue());
        assertNotNull(invitationPool.getPoll(coopPvpPollId));

        final List<Integer> coopPveUserId = new ArrayList<>();
        coopPveUserId.add(getRandomUserId());
        final CharactersParty coopPveParty = makeTestParty(coopPveUserId);
        assertTrue(coopPveParty.isFull());
        final Integer coopPvePollId = invitationPool.addPoll(coopPveParty);
        assertNotEquals(Constants.UNDEFINED_ID, coopPvePollId.intValue());
        assertNotNull(invitationPool.getPoll(coopPvePollId));

        for (Integer i = 0; i <= Invitation.TIMEOUT_LOOPS_COUNT; ++i) {
            invitationPool.update();
        }
        assertNull(invitationPool.getPoll(squadPvpPollId));
        assertNull(invitationPool.getPoll(coopPvpPollId));
        assertNull(invitationPool.getPoll(coopPvePollId));

        checkOutboxNotEmptyForParties(coopPvpTeamsUserIds);
        checkOutboxNotEmptyForParties(squadPvpTeamsUserIds);
        checkOutboxNotEmptyForParty(coopPveUserId);

        assertTrue(globalPartiesPool.isEmpty());
        for (Integer gameMode : wipPartiesPool.keySet()) {
            assertTrue(wipPartiesPool.get(gameMode).isEmpty());
        }
    }

    @Test
    @SuppressWarnings("OverlyComplexMethod")
    public void pollCancelTest() {
        final InvitationPool invitationPool =
                new InvitationPoolImpl(assetProvider, factory,
                        smartControllersPool, globalPartiesPool,
                        wipPartiesPool, instances);
        final Map<Integer, List<Integer>> squadPvpTeamsUserIds = new HashMap<>();
        squadPvpTeamsUserIds.put(Squad.TEAM_ONE_SQUAD_ID, new ArrayList<>());
        squadPvpTeamsUserIds.put(Squad.TEAM_TWO_SQUAD_ID, new ArrayList<>());
        squadPvpTeamsUserIds.get(Squad.TEAM_ONE_SQUAD_ID).add(getRandomUserId());
        while (true) {
            final Integer randomId = getRandomUserId();
            if (!squadPvpTeamsUserIds.get(Squad.TEAM_ONE_SQUAD_ID).contains(randomId)) {
                squadPvpTeamsUserIds.get(Squad.TEAM_TWO_SQUAD_ID).add(randomId);
                break;
            }
        }
        final CharactersParty squadPvpTeamOne = makeTestParty(
                squadPvpTeamsUserIds.get(Squad.TEAM_ONE_SQUAD_ID));
        assertTrue(squadPvpTeamOne.isFull());
        final CharactersParty squadPvpTeamTwo = makeTestParty(
                squadPvpTeamsUserIds.get(Squad.TEAM_TWO_SQUAD_ID));
        assertTrue(squadPvpTeamTwo.isFull());
        final Map<Integer, CharactersParty> squadPvpParties = new HashMap<>();
        squadPvpParties.put(squadPvpTeamOne.getID(), squadPvpTeamOne);
        squadPvpParties.put(squadPvpTeamTwo.getID(), squadPvpTeamTwo);
        final Integer squadPvpPollId = invitationPool.addPoll(
                squadPvpParties, GameModes.GM_SQUAD_PVP);
        assertNotEquals(Constants.UNDEFINED_ID, squadPvpPollId.intValue());
        assertNotNull(invitationPool.getPoll(squadPvpPollId));

        final Map<Integer, List<Integer>> coopPvpTeamsUserIds = makeUserIdLists();
        final CharactersParty coopPvpTeamOne = makeTestParty(
                coopPvpTeamsUserIds.get(Squad.TEAM_ONE_SQUAD_ID));
        assertTrue(coopPvpTeamOne.isFull());
        final CharactersParty coopPvpTeamTwo = makeTestParty(
                coopPvpTeamsUserIds.get(Squad.TEAM_TWO_SQUAD_ID));
        assertTrue(coopPvpTeamTwo.isFull());
        final Map<Integer, CharactersParty> coopPvpParties = new HashMap<>();
        coopPvpParties.put(coopPvpTeamOne.getID(), coopPvpTeamOne);
        coopPvpParties.put(coopPvpTeamTwo.getID(), coopPvpTeamTwo);

        final Integer coopPvpPollId = invitationPool.addPoll(
                coopPvpParties, GameModes.GM_COOP_PVP);
        assertNotEquals(Constants.UNDEFINED_ID, coopPvpPollId.intValue());
        assertNotNull(invitationPool.getPoll(coopPvpPollId));

        final List<Integer> coopPveUserId = new ArrayList<>();
        coopPveUserId.add(getRandomUserId());
        final CharactersParty coopPveParty = makeTestParty(coopPveUserId);
        assertTrue(coopPveParty.isFull());
        final Integer coopPvePollId = invitationPool.addPoll(coopPveParty);
        assertNotEquals(Constants.UNDEFINED_ID, coopPvePollId.intValue());
        assertNotNull(invitationPool.getPoll(coopPvePollId));

        final Random random = new Random(System.currentTimeMillis());
        for (Integer gameMode = GameModes.GM_COOP_PVE;
             gameMode <= GameModes.GM_SQUAD_PVP; ++gameMode) {
            if (gameMode != GameModes.GM_SOLO_PVE) {
                final Integer pollId = GameModes.isPve(gameMode) ? coopPvePollId :
                        gameMode == GameModes.GM_COOP_PVP ? coopPvpPollId : squadPvpPollId;
                Integer cancelerRoleId = random.nextInt(CharacterRoleIds.CR_SIZE);
                cancelerRoleId = cancelerRoleId == CharacterRoleIds.CR_MELEE_DAMAGE_DEALER
                        ? CharacterRoleIds.CR_DAMAGE_DEALER_ONE
                        : cancelerRoleId == CharacterRoleIds.CR_RANGED_DAMAGE_DEALER
                        ? CharacterRoleIds.CR_DAMAGE_DEALER_TWO : cancelerRoleId;
                final Integer characterId;
                final Integer ownerId;
                final CharactersParty cancelerParty;
                if (!GameModes.isPve(gameMode)) {
                    final Integer cancelerTeamId = gameMode == GameModes.GM_COOP_PVP
                            ? coopPvpParties.get(new ArrayList<>(coopPvpParties.keySet())
                            .get(random.nextInt(DigitsPairIndices.PAIR_SIZE))).getID()
                            : squadPvpParties.get(new ArrayList<>(squadPvpParties.keySet())
                            .get(random.nextInt(DigitsPairIndices.PAIR_SIZE))).getID();
                    characterId = gameMode == GameModes.GM_COOP_PVP
                            ? Objects.requireNonNull(
                                    coopPvpParties.get(cancelerTeamId)
                                            .getMember(cancelerRoleId)).getID()
                            : Objects.requireNonNull(
                                    squadPvpParties.get(cancelerTeamId)
                                            .getMember(cancelerRoleId)).getID();
                    ownerId = gameMode == GameModes.GM_COOP_PVP
                            ? Objects.requireNonNull(
                            coopPvpParties.get(cancelerTeamId)
                                    .getMember(cancelerRoleId)).getOwnerID()
                            : Objects.requireNonNull(
                            squadPvpParties.get(cancelerTeamId)
                                    .getMember(cancelerRoleId)).getOwnerID();
                    cancelerParty = gameMode == GameModes.GM_SQUAD_PVP
                            ? squadPvpParties.get(cancelerTeamId)
                            : coopPvpParties.get(cancelerTeamId);
                } else {
                    characterId = Objects.requireNonNull(coopPveParty
                            .getMember(cancelerRoleId)).getID();
                    ownerId = Objects.requireNonNull(coopPveParty
                            .getMember(cancelerRoleId)).getOwnerID();
                    cancelerParty = coopPveParty;
                }
                assertTrue(invitationPool.updatePoll(pollId, gameMode,
                        characterId, Invitation.VS_CANCEL));
                invitationPool.update();

                assertNull(invitationPool.getPoll(pollId));
                assertNotNull(smartControllersPool.get(ownerId).getOutboxMessage());
                assertEquals(MatchmakingNotificationMessage.class,
                        Objects.requireNonNull(smartControllersPool
                                .get(ownerId).getOutboxMessage()).getClass());
                assertNull(cancelerParty.getMember(cancelerRoleId));
            }
        }
    }

    private @NotNull CharactersParty makeTestParty(@NotNull List<Integer> userIds) {
        final CharactersParty party = new CharactersParty(pendingLootPool);

        Integer roleId = CharacterRoleIds.CR_TANK;
        while (!party.isFull()) {
            for (Integer userId : userIds) {
                final Charlist characterList =
                        Objects.requireNonNull(smartControllersPool.get(userId)
                                .getCharacterList());
                if (userIds.size() == 1) {
                    for (Integer i = 0; i < characterList
                            .getCharacterList().size(); ++i) {
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
                    smartControllersPool.get(userId)
                            .setActiveChar((UserCharacter) unit);
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
                        ownerId, Constants.UNDEFINED_ID, new ArrayList<>()));
    }

    private void refillSmartControllersPool() {
        smartControllersPool.clear();
        for (Integer userId = 0; userId < DEFAULT_TEST_USERS_COUNT; ++userId) {
            final CharacterList characterList = makeCharacterList(userId);
            for (Integer i = 0; i < CHARACTER_LIST_SIZE; ++i) {
                final UserCharacter character = (UserCharacter)(
                        dummiesFactory.makeNewDummy(i, "user" + userId
                                + " character" + i, ""));
                if (Objects.requireNonNull(character)
                        .hasProperty(PropertyCategories.PC_OWNER_ID)) {
                    character.setProperty(PropertyCategories.PC_OWNER_ID, userId);
                } else {
                    character.addProperty(PropertyCategories.PC_OWNER_ID,
                            new SingleValueProperty(userId));
                }
                characterList.getCharacterList().add(character);
            }
            final SmartController controller = new MockSmartController();
            controller.set(userId, null, characterList);
            smartControllersPool.put(userId, controller);
            assertTrue(controller.isValid());
        }
    }

    private Map<Integer, List<Integer>> makeUserIdLists() {
        final List<Integer> teamOneUserIds = new ArrayList<>();
        final List<Integer> teamTwoUserIds = new ArrayList<>();
        for (Integer i = 0; i < CharacterRoleIds.CR_SIZE; ++i) {
            while (true) {
                final Integer randomId = getRandomUserId();
                if (!teamOneUserIds.contains(randomId)
                        && !teamTwoUserIds.contains(randomId)) {
                    teamOneUserIds.add(randomId);
                    break;
                }
            }
            while (true) {
                final Integer randomId = getRandomUserId();
                if (!teamOneUserIds.contains(randomId)
                        && !teamTwoUserIds.contains(randomId)) {
                    teamTwoUserIds.add(randomId);
                    break;
                }
            }
        }
        final Map<Integer, List<Integer>> idsMap = new HashMap<>();
        idsMap.put(Squad.TEAM_ONE_SQUAD_ID, teamOneUserIds);
        idsMap.put(Squad.TEAM_TWO_SQUAD_ID, teamTwoUserIds);
        return idsMap;
    }

    private void checkOutboxNotEmptyForParties(
            @NotNull Map<Integer, List<Integer>> partiesUserIds) {
        for (Integer teamId : partiesUserIds.keySet()) {
            checkOutboxNotEmptyForParty(partiesUserIds.get(teamId));
        }
    }

    private void checkOutboxNotEmptyForParty(@NotNull List<Integer> partyUserIds) {
        for (Integer userId : partyUserIds) {
            final SmartController controller =
                    smartControllersPool.get(userId);
            assertNotNull(controller.getOutboxMessage());
            assertEquals(MatchmakingNotificationMessage.class,
                    controller.getOutboxMessage().getClass());
        }
    }
}
