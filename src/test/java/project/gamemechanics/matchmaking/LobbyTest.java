package project.gamemechanics.matchmaking;

import org.jetbrains.annotations.Nullable;
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
import project.gamemechanics.world.matchmaking.Lobby;
import project.gamemechanics.world.matchmaking.LobbyImpl;
import project.gamemechanics.world.matchmaking.invitations.invitations.Invitation;
import project.websocket.messages.ErrorMessage;
import project.websocket.messages.Message;
import project.websocket.messages.matchmaking.LobbyConfirmationMessage;
import project.websocket.messages.matchmaking.MatchmakingNotificationMessage;
import project.websocket.messages.typecontainer.BooleanMessage;

import javax.validation.constraints.NotNull;
import java.util.*;

import static org.junit.Assert.*;

@SuppressWarnings({"Duplicates", "unused"})
public class LobbyTest {

    private static final int DEFAULT_TEST_USERS_COUNT = 16;
    private static final int CHARACTER_LIST_SIZE = 4;

    private static AssetProvider assetProvider;
    private static PcgContentFactory factory;
    private static Map<Integer, SmartController> smartControllersPool;
    private static DummiesFactory dummiesFactory;
    private static PendingLootPool pendingLootPool;

    private static Map<Integer, Instance> instances;
    private static Map<Integer, CharactersParty> globalPartiesPool;

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
    }

    @Before
    public void cleanup() {
        pendingLootPool.reset();
        refillSmartControllersPool();
        instances.clear();
        globalPartiesPool.clear();
    }

    @Test
    public void characterEnqueueTestSuccess() {
        final Lobby testLobby = makeLobby();
        final Set<Integer> usedUserIds = new HashSet<>();
        final Random random = new Random(System.currentTimeMillis());
        for (Integer gameMode = GameModes.GM_COOP_PVE;
             gameMode <= GameModes.GM_SQUAD_PVP; ++gameMode) {
            if (gameMode == GameModes.GM_SOLO_PVE
                    || gameMode == GameModes.GM_SQUAD_PVP) {
                continue;
            }
            Integer userId;
            while (true) {
                userId = getRandomUserId();
                if (!usedUserIds.contains(userId)) {
                    usedUserIds.add(userId);
                    break;
                }
            }
            final Charlist characterList = Objects.requireNonNull(
                    smartControllersPool.get(userId).getCharacterList());
            final Message message = testLobby.enqueue(
                    characterList.getCharacterList().get(random.nextInt(characterList
                            .getCharacterList().size())), gameMode);
            assertEquals(LobbyConfirmationMessage.class, message.getClass());
        }
    }

    @Test
    public void partyEnqueueTest() {
        final Lobby testLobby = makeLobby();
        final List<Integer> characterIds = new ArrayList<>();
        characterIds.add(getRandomUserId());
        final CharactersParty soloPveParty = makeTestParty(characterIds);
        assertEquals(LobbyConfirmationMessage.class,
                testLobby.enqueue(soloPveParty, GameModes.GM_SOLO_PVE).getClass());
        final List<Integer> coopPveIds = makeRandomUserIdsList();
        final CharactersParty coopPveParty = makeTestParty(coopPveIds);
        assertEquals(LobbyConfirmationMessage.class,
                testLobby.enqueue(coopPveParty, GameModes.GM_COOP_PVE).getClass());
        final List<Integer> coopPvpIds = makeRandomUserIdsList();
        final CharactersParty coopPvpParty = makeTestParty(coopPvpIds);
        assertEquals(LobbyConfirmationMessage.class,
                testLobby.enqueue(coopPvpParty, GameModes.GM_COOP_PVP).getClass());
        final List<Integer> squadPvpIds = makeRandomUserIdsList();
        final CharactersParty squadPvpParty = makeTestParty(squadPvpIds);
        assertEquals(LobbyConfirmationMessage.class,
                testLobby.enqueue(squadPvpParty, GameModes.GM_SQUAD_PVP).getClass());
        assertEquals(ErrorMessage.class, testLobby.enqueue(squadPvpParty,
                GameModes.GM_SQUAD_PVP + 1).getClass());
        assertEquals(ErrorMessage.class, testLobby.enqueue(squadPvpParty,
                GameModes.GM_SQUAD_PVP).getClass());
        assertEquals(ErrorMessage.class, testLobby.enqueue(coopPvpParty,
                GameModes.GM_COOP_PVE).getClass());
    }

    @Test
    public void singleCharacterDequeueTest() {
        final Lobby testLobby = makeLobby();
        final Integer userId = getRandomUserId();
        final @Nullable Charlist list = Objects.requireNonNull(
                smartControllersPool.get(userId)).getCharacterList();
        final Random random = new Random(System.currentTimeMillis());
        final Integer characterIndex = random.nextInt(
                Objects.requireNonNull(list).getCharacterList().size());
        final Integer characterID = list.getCharacterList().get(characterIndex).getID();
        final Integer gameMode = random.nextInt() % 2 == 0 ? GameModes.GM_COOP_PVE : GameModes.GM_COOP_PVP;
        assertEquals(LobbyConfirmationMessage.class,
                testLobby.enqueue(list.getCharacterList().get(characterIndex), gameMode).getClass());
        Message isQueued = testLobby.isQueued(characterID, gameMode);
        assertEquals(BooleanMessage.class, isQueued.getClass());
        BooleanMessage booleanIsQueued = (BooleanMessage)(isQueued);
        assertTrue(booleanIsQueued.getValue());
        testLobby.dequeue(list.getCharacterList().get(characterIndex));
        isQueued = testLobby.isQueued(characterID, gameMode);
        assertEquals(BooleanMessage.class, isQueued.getClass());
        booleanIsQueued = (BooleanMessage)(isQueued);
        assertFalse(booleanIsQueued.getValue());
    }

    @Test
    public void singleCharacterDequeueFromGameModeTest() {
        final Lobby testLobby = makeLobby();
        final Integer userId = getRandomUserId();
        final @Nullable Charlist list = Objects.requireNonNull(
                smartControllersPool.get(userId)).getCharacterList();
        final Random random = new Random(System.currentTimeMillis());
        final Integer characterIndex = random.nextInt(
                Objects.requireNonNull(list).getCharacterList().size());
        final Integer characterID = list.getCharacterList().get(characterIndex).getID();
        for (Integer gameMode = GameModes.GM_COOP_PVE;
             gameMode <= GameModes.GM_SQUAD_PVP; ++gameMode) {
            if (gameMode == GameModes.GM_SOLO_PVE
                    || gameMode == GameModes.GM_SQUAD_PVP) {
                continue;
            }
            Message isQueued = testLobby.isQueued(characterID, gameMode);
            assertEquals(BooleanMessage.class, isQueued.getClass());
            BooleanMessage booleanIsQueued = (BooleanMessage)(isQueued);
            assertFalse(booleanIsQueued.getValue());
            assertEquals(LobbyConfirmationMessage.class,
                    testLobby.enqueue(list.getCharacterList().get(characterIndex),
                            gameMode).getClass());
            isQueued = testLobby.isQueued(characterID, gameMode);
            assertEquals(BooleanMessage.class, isQueued.getClass());
            booleanIsQueued = (BooleanMessage)(isQueued);
            assertTrue(booleanIsQueued.getValue());
            assertEquals(LobbyConfirmationMessage.class, testLobby.dequeue(
                    list.getCharacterList().get(characterIndex), gameMode).getClass());
            isQueued = testLobby.isQueued(characterID);
            booleanIsQueued = (BooleanMessage)(isQueued);
            assertFalse(booleanIsQueued.getValue());
        }
    }

    @Test
    public void partyDequeueTest() {
        final Lobby testLobby = makeLobby();
        final List<Integer> coopPveIds = makeRandomUserIdsList();
        final CharactersParty coopPveParty = makeTestParty(coopPveIds);
        assertEquals(LobbyConfirmationMessage.class,
                testLobby.enqueue(coopPveParty, GameModes.GM_COOP_PVE).getClass());
        final List<Integer> coopPvpIds = makeRandomUserIdsList();
        final CharactersParty coopPvpParty = makeTestParty(coopPvpIds);
        assertEquals(LobbyConfirmationMessage.class,
                testLobby.enqueue(coopPvpParty, GameModes.GM_COOP_PVP).getClass());
        final List<Integer> squadPvpIds = makeRandomUserIdsList();
        final CharactersParty squadPvpParty = makeTestParty(squadPvpIds);
        assertEquals(LobbyConfirmationMessage.class,
                testLobby.enqueue(squadPvpParty, GameModes.GM_SQUAD_PVP).getClass());
        assertEquals(ErrorMessage.class, testLobby.enqueue(squadPvpParty,
                GameModes.GM_SQUAD_PVP + 1).getClass());
        assertEquals(ErrorMessage.class, testLobby.enqueue(squadPvpParty,
                GameModes.GM_SQUAD_PVP).getClass());
        assertEquals(ErrorMessage.class, testLobby.enqueue(coopPvpParty,
                GameModes.GM_COOP_PVE).getClass());
        assertEquals(LobbyConfirmationMessage.class,
                testLobby.dequeue(coopPveParty, true).getClass());
        assertEquals(LobbyConfirmationMessage.class,
                testLobby.dequeue(squadPvpParty, true).getClass());
        assertEquals(LobbyConfirmationMessage.class,
                testLobby.dequeue(coopPvpParty, true).getClass());
    }

    @Test
    public void partyDequeueFromGameModeTest() {
        final Lobby testLobby = makeLobby();
        final List<Integer> coopPveIds = makeRandomUserIdsList();
        final CharactersParty coopPveParty = makeTestParty(coopPveIds);
        assertEquals(LobbyConfirmationMessage.class,
                testLobby.enqueue(coopPveParty, GameModes.GM_COOP_PVE).getClass());
        final List<Integer> coopPvpIds = makeRandomUserIdsList();
        final CharactersParty coopPvpParty = makeTestParty(coopPvpIds);
        assertEquals(LobbyConfirmationMessage.class,
                testLobby.enqueue(coopPvpParty, GameModes.GM_COOP_PVP).getClass());
        final List<Integer> squadPvpIds = makeRandomUserIdsList();
        final CharactersParty squadPvpParty = makeTestParty(squadPvpIds);
        assertEquals(LobbyConfirmationMessage.class,
                testLobby.enqueue(squadPvpParty, GameModes.GM_SQUAD_PVP).getClass());
        assertEquals(ErrorMessage.class, testLobby.enqueue(squadPvpParty,
                GameModes.GM_SQUAD_PVP + 1).getClass());
        assertEquals(ErrorMessage.class, testLobby.enqueue(squadPvpParty,
                GameModes.GM_SQUAD_PVP).getClass());
        assertEquals(ErrorMessage.class, testLobby.enqueue(coopPvpParty,
                GameModes.GM_COOP_PVE).getClass());
        assertEquals(ErrorMessage.class,
                testLobby.dequeue(coopPveParty, GameModes.GM_COOP_PVP, false).getClass());
        assertEquals(LobbyConfirmationMessage.class,
                testLobby.dequeue(coopPveParty, GameModes.GM_COOP_PVE, false).getClass());
        assertEquals(ErrorMessage.class,
                testLobby.dequeue(squadPvpParty, GameModes.GM_COOP_PVP, false).getClass());
        assertEquals(LobbyConfirmationMessage.class,
                testLobby.dequeue(squadPvpParty, GameModes.GM_SQUAD_PVP, false).getClass());
        assertEquals(ErrorMessage.class,
                testLobby.dequeue(coopPvpParty, GameModes.GM_COOP_PVE, false).getClass());
        assertEquals(LobbyConfirmationMessage.class,
                testLobby.dequeue(coopPvpParty, GameModes.GM_COOP_PVP, false).getClass());
    }

    @Test
    public void updatePollByPollIdTest() {
        final Lobby testLobby = makeLobby();
        final List<Integer> coopPveIds = makeRandomUserIdsList();
        final CharactersParty coopPveParty = makeTestParty(coopPveIds);
        assertEquals(LobbyConfirmationMessage.class,
                testLobby.enqueue(coopPveParty, GameModes.GM_COOP_PVE).getClass());

        final Map<Integer, CharactersParty> coopPvpParties = makeTestPartiesPair();
        final CharactersParty coopPvpParty = coopPvpParties.get(Squad.TEAM_ONE_SQUAD_ID);
        final CharactersParty coopPvpPartyTwo = coopPvpParties.get(Squad.TEAM_TWO_SQUAD_ID);
        assertEquals(LobbyConfirmationMessage.class,
                testLobby.enqueue(coopPvpParty, GameModes.GM_COOP_PVP).getClass());
        assertEquals(LobbyConfirmationMessage.class,
                testLobby.enqueue(coopPvpPartyTwo, GameModes.GM_COOP_PVP).getClass());

        final Map<Integer, CharactersParty> squadPvpParties = makeTestPartiesPair();
        final CharactersParty squadPvpParty = squadPvpParties.get(Squad.TEAM_ONE_SQUAD_ID);
        final CharactersParty squadPvpPartyTwo = squadPvpParties.get(Squad.TEAM_TWO_SQUAD_ID);
        assertEquals(LobbyConfirmationMessage.class,
                testLobby.enqueue(squadPvpParty, GameModes.GM_SQUAD_PVP).getClass());
        assertEquals(ErrorMessage.class, testLobby.enqueue(squadPvpParty,
                GameModes.GM_SQUAD_PVP + 1).getClass());
        assertEquals(ErrorMessage.class, testLobby.enqueue(squadPvpParty,
                GameModes.GM_SQUAD_PVP).getClass());
        assertEquals(ErrorMessage.class, testLobby.enqueue(coopPvpParty,
                GameModes.GM_COOP_PVE).getClass());

        final Random random = new Random(System.currentTimeMillis());
        assertEquals(ErrorMessage.class, testLobby.updatePoll(random.nextInt(),
                Objects.requireNonNull(coopPveParty.getMember(CharacterRoleIds.CR_TANK)).getID(),
                GameModes.GM_COOP_PVE, Invitation.VS_CONFIRM).getClass());
        assertEquals(ErrorMessage.class, testLobby.updatePoll(random.nextInt(),
                Objects.requireNonNull(coopPvpParty.getMember(CharacterRoleIds.CR_TANK)).getID(),
                GameModes.GM_COOP_PVP, Invitation.VS_CONFIRM).getClass());

        assertEquals(ErrorMessage.class, testLobby.updatePoll(random.nextInt(),
                Objects.requireNonNull(squadPvpParty.getMember(CharacterRoleIds.CR_TANK)).getID(),
                GameModes.GM_SQUAD_PVP, Invitation.VS_CONFIRM).getClass());

        testLobby.tick();

    }

    @Test
    public void updatePollByCharIdTest() {
        final Lobby testLobby = makeLobby();
        final List<Integer> coopPveIds = makeRandomUserIdsList();
        final CharactersParty coopPveParty = makeTestParty(coopPveIds);
        assertEquals(LobbyConfirmationMessage.class,
                testLobby.enqueue(coopPveParty, GameModes.GM_COOP_PVE).getClass());

        final Map<Integer, CharactersParty> coopPvpParties = makeTestPartiesPair();
        final CharactersParty coopPvpParty = coopPvpParties.get(Squad.TEAM_ONE_SQUAD_ID);
        final CharactersParty coopPvpPartyTwo = coopPvpParties.get(Squad.TEAM_TWO_SQUAD_ID);
        assertEquals(LobbyConfirmationMessage.class,
                testLobby.enqueue(coopPvpParty, GameModes.GM_COOP_PVP).getClass());
        assertEquals(LobbyConfirmationMessage.class,
                testLobby.enqueue(coopPvpPartyTwo, GameModes.GM_COOP_PVP).getClass());

        final Map<Integer, CharactersParty> squadPvpParties = makeTestPartiesPair();
        final CharactersParty squadPvpParty = squadPvpParties.get(Squad.TEAM_ONE_SQUAD_ID);
        final CharactersParty squadPvpPartyTwo = squadPvpParties.get(Squad.TEAM_TWO_SQUAD_ID);
        assertEquals(LobbyConfirmationMessage.class,
                testLobby.enqueue(squadPvpParty, GameModes.GM_SQUAD_PVP).getClass());
        assertEquals(ErrorMessage.class, testLobby.enqueue(squadPvpParty,
                GameModes.GM_SQUAD_PVP + 1).getClass());
        assertEquals(ErrorMessage.class, testLobby.enqueue(squadPvpParty,
                GameModes.GM_SQUAD_PVP).getClass());
        assertEquals(ErrorMessage.class, testLobby.enqueue(coopPvpParty,
                GameModes.GM_COOP_PVE).getClass());

        assertEquals(ErrorMessage.class, testLobby.updatePoll(
                Objects.requireNonNull(coopPveParty.getMember(CharacterRoleIds.CR_TANK)).getID(),
                GameModes.GM_COOP_PVE, Invitation.VS_CONFIRM).getClass());
        assertEquals(ErrorMessage.class, testLobby.updatePoll(
                Objects.requireNonNull(coopPvpParty.getMember(CharacterRoleIds.CR_TANK)).getID(),
                GameModes.GM_COOP_PVP, Invitation.VS_CONFIRM).getClass());
        assertEquals(ErrorMessage.class, testLobby.updatePoll(
                Objects.requireNonNull(squadPvpParty.getMember(CharacterRoleIds.CR_TANK)).getID(),
                GameModes.GM_SQUAD_PVP, Invitation.VS_CONFIRM).getClass());

        testLobby.tick();
        assertEquals(LobbyConfirmationMessage.class, testLobby.updatePoll(
                Objects.requireNonNull(coopPveParty.getMember(CharacterRoleIds.CR_TANK)).getID(),
                GameModes.GM_COOP_PVE, Invitation.VS_CONFIRM).getClass());

        assertEquals(LobbyConfirmationMessage.class, testLobby.updatePoll(
                Objects.requireNonNull(coopPvpParty.getMember(CharacterRoleIds.CR_TANK)).getID(),
                GameModes.GM_COOP_PVP, Invitation.VS_CONFIRM).getClass());
        assertEquals(LobbyConfirmationMessage.class, testLobby.updatePoll(
                Objects.requireNonNull(coopPvpPartyTwo.getMember(CharacterRoleIds.CR_TANK)).getID(),
                GameModes.GM_COOP_PVP, Invitation.VS_CONFIRM).getClass());

        assertEquals(ErrorMessage.class, testLobby.updatePoll(
                Objects.requireNonNull(squadPvpParty.getMember(CharacterRoleIds.CR_TANK)).getID(),
                GameModes.GM_SQUAD_PVP, Invitation.VS_CONFIRM).getClass());
        assertEquals(LobbyConfirmationMessage.class,
                testLobby.enqueue(squadPvpPartyTwo, GameModes.GM_SQUAD_PVP).getClass());
        testLobby.tick();
        assertEquals(LobbyConfirmationMessage.class, testLobby.updatePoll(
                Objects.requireNonNull(squadPvpParty.getMember(CharacterRoleIds.CR_TANK)).getID(),
                GameModes.GM_SQUAD_PVP, Invitation.VS_CONFIRM).getClass());
        assertEquals(LobbyConfirmationMessage.class, testLobby.updatePoll(
                Objects.requireNonNull(squadPvpPartyTwo.getMember(CharacterRoleIds.CR_TANK)).getID(),
                GameModes.GM_SQUAD_PVP, Invitation.VS_CONFIRM).getClass());

        assertEquals(LobbyConfirmationMessage.class, testLobby.updatePoll(
                Objects.requireNonNull(coopPveParty.getMember(CharacterRoleIds.CR_SUPPORT)).getID(),
                GameModes.GM_COOP_PVE, Invitation.VS_CANCEL).getClass());
        assertEquals(LobbyConfirmationMessage.class, testLobby.updatePoll(
                Objects.requireNonNull(coopPvpParty.getMember(CharacterRoleIds.CR_SUPPORT)).getID(),
                GameModes.GM_COOP_PVP, Invitation.VS_CANCEL).getClass());
        assertEquals(LobbyConfirmationMessage.class, testLobby.updatePoll(
                Objects.requireNonNull(squadPvpPartyTwo.getMember(CharacterRoleIds.CR_SUPPORT)).getID(),
                GameModes.GM_SQUAD_PVP, Invitation.VS_CANCEL).getClass());

        testLobby.tick();
        assertEquals(ErrorMessage.class, testLobby.updatePoll(
                Objects.requireNonNull(coopPveParty.getMember(CharacterRoleIds.CR_TANK)).getID(),
                GameModes.GM_COOP_PVE, Invitation.VS_CONFIRM).getClass());
        assertEquals(ErrorMessage.class, testLobby.updatePoll(
                Objects.requireNonNull(coopPvpParty.getMember(CharacterRoleIds.CR_TANK)).getID(),
                GameModes.GM_COOP_PVP, Invitation.VS_CONFIRM).getClass());
        assertEquals(ErrorMessage.class, testLobby.updatePoll(
                Objects.requireNonNull(squadPvpParty.getMember(CharacterRoleIds.CR_TANK)).getID(),
                GameModes.GM_SQUAD_PVP, Invitation.VS_CONFIRM).getClass());
    }

    @Test
    public void isQueuedTest() {
        final Lobby testLobby = makeLobby();
        final Map<Integer, CharactersParty> parties = makeTestPartiesPair();
        final CharactersParty party = parties.get(Squad.TEAM_ONE_SQUAD_ID);
        final CharactersParty partyTwo = parties.get(Squad.TEAM_TWO_SQUAD_ID);
        Integer randomGameMode;
        final Random random = new Random(System.currentTimeMillis());
        do {
            randomGameMode = random.nextInt(GameModes.GM_SQUAD_PVP + 1);
        } while (randomGameMode == GameModes.GM_SOLO_PVE);
        assertEquals(LobbyConfirmationMessage.class,
                testLobby.enqueue(party, randomGameMode).getClass());
        do {
            randomGameMode = random.nextInt(GameModes.GM_SQUAD_PVP + 1);
        } while (randomGameMode == GameModes.GM_SOLO_PVE);
        assertEquals(LobbyConfirmationMessage.class,
                testLobby.enqueue(partyTwo, randomGameMode).getClass());
        checkAreAllMembersQueuedAnywhere(party, testLobby);
        checkAreAllMembersQueuedAnywhere(partyTwo, testLobby);
    }

    @Test
    public void isQueuedInGameModeTest() {
        final Lobby testLobby = makeLobby();
        final List<Integer> coopPveIds = makeRandomUserIdsList();
        final CharactersParty coopPveParty = makeTestParty(coopPveIds);
        assertEquals(LobbyConfirmationMessage.class,
                testLobby.enqueue(coopPveParty, GameModes.GM_COOP_PVE).getClass());
        checkAreAllMembersQueuedGameMode(coopPveParty, testLobby, GameModes.GM_COOP_PVE);

        final Map<Integer, CharactersParty> coopPvpParties = makeTestPartiesPair();
        final CharactersParty coopPvpParty = coopPvpParties.get(Squad.TEAM_ONE_SQUAD_ID);
        final CharactersParty coopPvpPartyTwo = coopPvpParties.get(Squad.TEAM_TWO_SQUAD_ID);
        assertEquals(LobbyConfirmationMessage.class,
                testLobby.enqueue(coopPvpParty, GameModes.GM_COOP_PVP).getClass());
        assertEquals(LobbyConfirmationMessage.class,
                testLobby.enqueue(coopPvpPartyTwo, GameModes.GM_COOP_PVP).getClass());
        checkAreAllMembersQueuedGameMode(coopPvpParty, testLobby, GameModes.GM_COOP_PVP);
        checkAreAllMembersQueuedGameMode(coopPvpPartyTwo, testLobby, GameModes.GM_COOP_PVP);

        final Map<Integer, CharactersParty> squadPvpParties = makeTestPartiesPair();
        final CharactersParty squadPvpParty = squadPvpParties.get(Squad.TEAM_ONE_SQUAD_ID);
        final CharactersParty squadPvpPartyTwo = squadPvpParties.get(Squad.TEAM_TWO_SQUAD_ID);
        assertEquals(LobbyConfirmationMessage.class,
                testLobby.enqueue(squadPvpParty, GameModes.GM_SQUAD_PVP).getClass());
        assertEquals(LobbyConfirmationMessage.class,
                testLobby.enqueue(squadPvpPartyTwo, GameModes.GM_SQUAD_PVP).getClass());
        checkAreAllMembersQueuedGameMode(squadPvpParty, testLobby, GameModes.GM_SQUAD_PVP);
        checkAreAllMembersQueuedGameMode(squadPvpPartyTwo, testLobby, GameModes.GM_SQUAD_PVP);
    }

    private @NotNull Lobby makeLobby() {
        return new LobbyImpl(assetProvider, factory, pendingLootPool,
                smartControllersPool, globalPartiesPool, instances);
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

    private List<Integer> makeRandomUserIdsList() {
        final List<Integer> userIds = new ArrayList<>();
        for (Integer i = 0; i < CharacterRoleIds.CR_SIZE; ++i) {
            while (true) {
                final Integer randomId = getRandomUserId();
                if (!userIds.contains(randomId)) {
                    userIds.add(randomId);
                    break;
                }
            }
        }
        return userIds;
    }

    private @NotNull CharactersParty makeTestParty(@NotNull List<Integer> userIds) {
        final CharactersParty party = new CharactersParty(pendingLootPool);

        Integer roleId = CharacterRoleIds.CR_TANK;
        while (!party.isFull()) {
            for (Integer userId : userIds) {
                final @Nullable Charlist characterList =
                        Objects.requireNonNull(smartControllersPool.get(userId)
                                .getCharacterList());
                if (userIds.size() == 1) {
                    for (Integer i = 0; i < Objects.requireNonNull(characterList)
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
                            Objects.requireNonNull(characterList).getCharacterList().get(
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

    private @NotNull Map<Integer, CharactersParty> makeTestPartiesPair() {
        final CharactersParty partyOne = new CharactersParty(pendingLootPool);
        final CharactersParty partyTwo = new CharactersParty(pendingLootPool);
        for (Integer i = 0; i < CharacterRoleIds.CR_SIZE; ++i) {
            final AliveEntity userCharacterOne =
                    dummiesFactory.makeNewDummy(
                            0, "characterOne" + i, "");
            assertTrue(partyOne.addMember(i, Objects.requireNonNull(userCharacterOne)));
            final AliveEntity userCharacterTwo =
                    dummiesFactory.makeNewDummy(
                            0, "characterTwo" + i, "");
            assertTrue(partyTwo.addMember(i, Objects.requireNonNull(userCharacterTwo)));
        }
        assertEquals(partyOne.getPartySize().intValue(), CharacterRoleIds.CR_SIZE);
        assertTrue(partyOne.isFull());
        assertEquals(partyTwo.getPartySize().intValue(), CharacterRoleIds.CR_SIZE);
        assertTrue(partyTwo.isFull());
        final Map<Integer, CharactersParty> parties = new HashMap<>();
        parties.put(Squad.TEAM_ONE_SQUAD_ID, partyOne);
        parties.put(Squad.TEAM_TWO_SQUAD_ID, partyTwo);
        return parties;
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

    private void checkAreAllMembersQueuedAnywhere(@NotNull CharactersParty party,
                                                  @NotNull Lobby testLobby) {
        for (Integer roleId : party.getRoleIds()) {
            if (party.getMember(roleId) != null) {
                final Message isQueued = testLobby.isQueued(
                        Objects.requireNonNull(party.getMember(roleId)).getID());
                assertEquals(BooleanMessage.class, isQueued.getClass());
                final BooleanMessage booleanIsQueued = (BooleanMessage) isQueued;
                assertTrue(booleanIsQueued.getValue());
            }
        }
    }

    private void checkAreAllMembersQueuedGameMode(@NotNull CharactersParty party,
                                                  @NotNull Lobby testLobby,
                                                  @NotNull Integer gameMode) {
        for (Integer roleId : party.getRoleIds()) {
            if (party.getMember(roleId) != null) {
                final Message isQueued = testLobby.isQueued(
                        Objects.requireNonNull(party.getMember(roleId)).getID(), gameMode);
                assertEquals(BooleanMessage.class, isQueued.getClass());
                final BooleanMessage booleanIsQueued = (BooleanMessage) isQueued;
                assertTrue(booleanIsQueued.getValue());
            }
        }
    }
}
