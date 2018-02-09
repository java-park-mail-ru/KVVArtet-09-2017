package project.gamemechanics.world.matchmaking;

import project.gamemechanics.aliveentities.npcs.ai.AIBehaviors;
import project.gamemechanics.battlefield.aliveentitiescontainers.CharactersParty;
import project.gamemechanics.components.properties.PropertyCategories;
import project.gamemechanics.dungeons.AbstractInstance;
import project.gamemechanics.dungeons.DungeonInstance;
import project.gamemechanics.dungeons.Instance;
import project.gamemechanics.globals.CharacterRoleIds;
import project.gamemechanics.globals.Constants;
import project.gamemechanics.globals.GameModes;
import project.gamemechanics.interfaces.AliveEntity;
import project.gamemechanics.items.loot.PendingLootPool;
import project.gamemechanics.resources.assets.AssetProvider;
import project.gamemechanics.resources.pcg.PcgContentFactory;
import project.gamemechanics.smartcontroller.SmartController;
import project.gamemechanics.world.matchmaking.invitations.InvitationPool;
import project.gamemechanics.world.matchmaking.invitations.InvitationPoolImpl;
import project.websocket.messages.ErrorMessage;
import project.websocket.messages.Message;
import project.websocket.messages.matchmaking.LobbyConfirmationMessage;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

public class LobbyImpl implements Lobby {
    private final AssetProvider assetProvider;
    private final PcgContentFactory factory;
    private final PendingLootPool lootPool;

    private final Map<Integer, CharactersParty> globalPartiesPool;

    private final Map<Integer, Instance> instancesPool;

    private final Map<Integer, Deque<CharactersParty>> wipPartiesPool =
            new ConcurrentHashMap<>();
    private final Map<Integer, Map<Integer, Deque<AliveEntity>>> queuedCharacters =
            new ConcurrentHashMap<>();

    private final InvitationPool invitationService;

    public LobbyImpl(@NotNull AssetProvider assetProvider,
                     @NotNull PcgContentFactory factory,
                     @NotNull PendingLootPool lootPool,
                     @NotNull Map<Integer, SmartController> smartControllersPool,
                     @NotNull Map<Integer, CharactersParty> globalPartiesPool,
                     @NotNull Map<Integer, Instance> instancesPool) {
        this.assetProvider = assetProvider;
        this.factory = factory;
        this.lootPool = lootPool;
        this.globalPartiesPool = globalPartiesPool;
        this.instancesPool = instancesPool;

        this.invitationService = new InvitationPoolImpl(
                this.assetProvider, this.factory,
                smartControllersPool, this.globalPartiesPool,
                this.wipPartiesPool, this.instancesPool);

        initGameModeQueues();
    }

    @Override
    @SuppressWarnings("OverlyComplexMethod")
    public @NotNull Message enqueue(@NotNull AliveEntity character, @NotNull Integer gameMode) {
        if (!queuedCharacters.containsKey(gameMode)) {
            return new ErrorMessage("unknown game mode");
        }
        if (character.hasProperty(PropertyCategories.PC_INSTANCE_ID)
                && character.getProperty(PropertyCategories.PC_INSTANCE_ID)
                != Constants.UNDEFINED_ID) {
            return new ErrorMessage("character is already in an instance");
        }
        if (!character.hasProperty(PropertyCategories.PC_ACTIVE_ROLE)
                || character.getProperty(PropertyCategories.PC_ACTIVE_ROLE)
                == Constants.UNDEFINED_ID) {
            return new ErrorMessage("uknown character role");
        }
        // note: do we need to actually check character's presence in currently WIP parties?
        if (character.hasProperty(PropertyCategories.PC_PARTY_ID)
                && character.getProperty(PropertyCategories.PC_PARTY_ID)
                != Constants.UNDEFINED_ID) {
            for (Integer gameModeId : wipPartiesPool.keySet()) {
                for (CharactersParty wipParty : wipPartiesPool.get(gameModeId)) {
                    if (wipParty.getID().equals(character
                            .getProperty(PropertyCategories.PC_PARTY_ID))) {
                        return new ErrorMessage("character is already in queued party");
                    }
                }
            }
        }
        // note: do we need to check character's presence in other game modes' queues also?
        for (Integer roleId : queuedCharacters.get(gameMode).keySet()) {
            final Deque<AliveEntity> roleQueue = queuedCharacters.get(gameMode).get(roleId);
            if (roleQueue.contains(character)) {
                return new ErrorMessage("character is already queued in other game mode");
            }
        }

        queuedCharacters.get(gameMode).get(
                character.getProperty(PropertyCategories.PC_ACTIVE_ROLE))
                .offerLast(character);
        return new LobbyConfirmationMessage();
    }

    @Override
    public @NotNull Message enqueue(@NotNull CharactersParty party, @NotNull Integer gameMode) {
        if (!wipPartiesPool.containsKey(gameMode)) {
            // as PvP modes always require enqueuing, we check if that non-enqueuing mode is a PvE one
            if (!GameModes.isPve(gameMode)) {
                return new ErrorMessage("unknown game mode");
            } else {
                final List<CharactersParty> parties = new ArrayList<>();
                parties.add(party);
                final AbstractInstance.DungeonInstanceModel newDungeonModel =
                        makePveInstanceModel(parties);
                final Instance newPveInstance = new DungeonInstance(newDungeonModel);
                instancesPool.put(newPveInstance.getID(), newPveInstance);
                // CHECKSTYLE:OFF
                return new LobbyConfirmationMessage(); /* TODO: change to proper message */
                // CHECKSTYLE:ON
            }
        }
        for (Integer gameModeId : wipPartiesPool.keySet()) {
            if (wipPartiesPool.get(gameModeId).contains(party)) {
                return new ErrorMessage("party is already queued");
            }
        }
        wipPartiesPool.get(gameMode).offerLast(party);
        return new LobbyConfirmationMessage();
    }

    @Override
    public @NotNull Message dequeue(@NotNull AliveEntity character) {
        for (Integer gameMode : queuedCharacters.keySet()) {
            dequeueCharacterFromQueue(character, gameMode);
        }
        for (Integer gameMode : wipPartiesPool.keySet()) {
            if (dequeueCharacterFromParties(character, gameMode)) {
                break;
            }
        }
        return new LobbyConfirmationMessage();
    }

    @Override
    public @NotNull Message dequeue(@NotNull AliveEntity character, @NotNull Integer gameMode) {
        dequeueCharacterFromQueue(character, gameMode);
        dequeueCharacterFromParties(character, gameMode);
        return new LobbyConfirmationMessage();
    }

    @Override
    public @NotNull Message dequeue(@NotNull CharactersParty party, @NotNull Boolean dismissParty) {
        for (Integer gameMode : wipPartiesPool.keySet()) {
            if (wipPartiesPool.get(gameMode).contains(party)) {
                wipPartiesPool.get(gameMode).remove(party);
            }
        }
        if (dismissParty) {
            dismissCharactersParty(party);
        }
        return new LobbyConfirmationMessage();
    }

    @Override
    public @NotNull Message dequeue(@NotNull CharactersParty party,
                                    @NotNull Integer gameMode,
                                    @NotNull Boolean dismissParty)  {
        if (dismissParty) {
            return dequeue(party, true);
        }
        if (!wipPartiesPool.containsKey(gameMode)) {
            return new ErrorMessage("unknown game mode");
        }
        if (!wipPartiesPool.get(gameMode).contains(party)) {
            return new ErrorMessage("party is not queued");
        }
        wipPartiesPool.get(gameMode).remove(party);
        return new LobbyConfirmationMessage();
    }

    @Override
    public void tick() {
        traversePartiesQueue();
        invitationService.update();
        traverseCharactersQueue();
    }

    private @NotNull Boolean dequeueCharacterFromParties(@NotNull AliveEntity character,
                                                         @NotNull Integer gameMode) {
        for (CharactersParty party : wipPartiesPool.get(gameMode)) {
            for (Integer roleId : party.getRoleIds()) {
                if (Objects.requireNonNull(party.getMember(roleId))
                        .equals(character)) {
                    party.removeMember(roleId);
                    return true;
                }
            }
        }
        return false;
    }

    private void dequeueCharacterFromQueue(@NotNull AliveEntity character,
                                           @NotNull Integer gameMode) {
        for (Integer roleId : queuedCharacters.get(gameMode).keySet()) {
            final Deque<AliveEntity> queuedForRole = queuedCharacters.get(gameMode).get(roleId);
            if (queuedForRole.contains(character)) {
                queuedForRole.remove(character);
            }
        }
    }

    private void fillGameModeQueueWithRoles(@NotNull Map<Integer, Deque<AliveEntity>> gameModeQueue) {
        gameModeQueue.put(CharacterRoleIds.CR_TANK, new ConcurrentLinkedDeque<>());
        gameModeQueue.put(CharacterRoleIds.CR_MELEE_DAMAGE_DEALER, new ConcurrentLinkedDeque<>());
        gameModeQueue.put(CharacterRoleIds.CR_RANGED_DAMAGE_DEALER, new ConcurrentLinkedDeque<>());
        gameModeQueue.put(CharacterRoleIds.CR_SUPPORT, new ConcurrentLinkedDeque<>());
    }

    private void initGameModeQueues() {
        wipPartiesPool.put(GameModes.GM_COOP_PVE, new ConcurrentLinkedDeque<>());
        wipPartiesPool.put(GameModes.GM_COOP_PVP, new ConcurrentLinkedDeque<>());
        wipPartiesPool.put(GameModes.GM_SQUAD_PVP, new ConcurrentLinkedDeque<>());

        queuedCharacters.put(GameModes.GM_COOP_PVE, new ConcurrentHashMap<>());
        queuedCharacters.put(GameModes.GM_COOP_PVP, new ConcurrentHashMap<>());
        for (Integer gameMode : queuedCharacters.keySet()) {
            fillGameModeQueueWithRoles(queuedCharacters.get(gameMode));
        }
    }

    private void dismissCharactersParty(@NotNull CharactersParty party) {
        if (globalPartiesPool.containsKey(party.getID())) {
            globalPartiesPool.remove(party.getID());
        }
        for (Integer roleId : party.getRoleIds()) {
            party.removeMember(roleId);
        }
    }

    private @NotNull AbstractInstance.DungeonInstanceModel makePveInstanceModel(
            @NotNull List<CharactersParty> parties) {
        final List<String> instanceNameDescription = assetProvider.makeInstanceNameDescription();
        return new AbstractInstance.DungeonInstanceModel(instanceNameDescription.get(0),
                        instanceNameDescription.get(1), parties.get(0).getAverageLevel(),
                        Constants.DEFAULT_ROOMS_COUNT, factory, parties, AIBehaviors.getAllBehaviors());
    }

    private void traversePartiesQueue() {
        for (Integer gameMode : wipPartiesPool.keySet()) {
            CharactersParty firstTeam = null;
            for (CharactersParty party : wipPartiesPool.get(gameMode)) {
                for (Integer roleId : party.getRoleIds()) {
                    if (party.getMember(roleId) == null) {
                        final AliveEntity candidate =
                                queuedCharacters.get(gameMode).get(roleId).pollFirst();
                        if (candidate != null) {
                            party.addMember(roleId, candidate);
                        }
                    }
                }
                if (party.isFull()) {
                    if (GameModes.isPvp(gameMode)) {
                        if (firstTeam == null) {
                            firstTeam = party;
                        } else {
                            final Map<Integer, CharactersParty> parties = new HashMap<>();
                            parties.put(firstTeam.getID(), firstTeam);
                            parties.put(party.getID(), party);
                            invitationService.addPoll(parties, gameMode);
                        }
                    } else {
                        invitationService.addPoll(party);
                    }
                }
            }
        }
    }

    private void traverseCharactersQueue() {
        for (Integer gameMode : queuedCharacters.keySet()) {
            for (Integer roleId : queuedCharacters.get(gameMode).keySet()) {
                for (AliveEntity character : queuedCharacters.get(gameMode).get(roleId)) {
                    if (wipPartiesPool.get(gameMode).isEmpty()) {
                        final CharactersParty party = new CharactersParty(lootPool);
                        party.addMember(roleId, character);
                        queuedCharacters.get(gameMode).get(roleId).remove(character);
                    } else {
                        for (CharactersParty party : wipPartiesPool.get(gameMode)) {
                            if (!party.hasRole(roleId)) {
                                party.addMember(roleId, character);
                                queuedCharacters.get(gameMode).get(roleId).remove(character);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }
}
