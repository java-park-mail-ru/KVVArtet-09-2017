package project.gamemechanics.matchmaking;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import project.gamemechanics.aliveentities.UserCharacter;
import project.gamemechanics.battlefield.aliveentitiescontainers.CharactersParty;
import project.gamemechanics.battlefield.aliveentitiescontainers.Squad;
import project.gamemechanics.charlist.CharacterList;
import project.gamemechanics.components.properties.PropertyCategories;
import project.gamemechanics.components.properties.SingleValueProperty;
import project.gamemechanics.dungeons.Instance;
import project.gamemechanics.globals.CharacterRoleIds;
import project.gamemechanics.globals.Constants;
import project.gamemechanics.globals.GameModes;
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
import project.websocket.messages.Message;
import project.websocket.messages.matchmaking.LobbyConfirmationMessage;
import project.websocket.messages.matchmaking.MatchmakingNotificationMessage;

import javax.validation.constraints.NotNull;
import java.util.*;

import static org.junit.Assert.*;

@SuppressWarnings("Duplicates")
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
            final CharacterList characterList = Objects.requireNonNull(
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

    private static @NotNull CharacterList makeCharacterList(@NotNull Integer ownerId) {
        return new CharacterList(
                new CharacterList.CharacterListModel(
                        ownerId, new ArrayList<>()));
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
