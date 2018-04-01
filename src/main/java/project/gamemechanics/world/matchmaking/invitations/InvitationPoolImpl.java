package project.gamemechanics.world.matchmaking.invitations;

import org.jetbrains.annotations.Nullable;
import project.gamemechanics.aliveentities.npcs.ai.AIBehaviors;
import project.gamemechanics.battlefield.aliveentitiescontainers.CharactersParty;
import project.gamemechanics.dungeons.AbstractInstance;
import project.gamemechanics.dungeons.DungeonInstance;
import project.gamemechanics.dungeons.Instance;
import project.gamemechanics.dungeons.LandInstance;
import project.gamemechanics.globals.CharacterRoleIds;
import project.gamemechanics.globals.Constants;
import project.gamemechanics.globals.DigitsPairIndices;
import project.gamemechanics.globals.GameModes;
import project.gamemechanics.interfaces.AliveEntity;
import project.gamemechanics.resources.assets.AssetProvider;
import project.gamemechanics.resources.pcg.PcgContentFactory;
import project.gamemechanics.smartcontroller.SmartController;
import project.gamemechanics.world.matchmaking.invitations.polls.CoopPvpPoll;
import project.gamemechanics.world.matchmaking.invitations.polls.Poll;
import project.gamemechanics.world.matchmaking.invitations.polls.PvePoll;
import project.gamemechanics.world.matchmaking.invitations.polls.SquadPvpPoll;
import project.websocket.messages.Message;
import project.websocket.messages.matchmaking.MatchmakingNotificationMessage;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class InvitationPoolImpl implements InvitationPool {
    private final AssetProvider assetProvider;
    private final PcgContentFactory factory;
    private final Map<Integer, SmartController> smartControllersPool;

    private final Map<Integer, CharactersParty> globalPartiesPool;
    private final Map<Integer, Deque<CharactersParty>> wipPartiesPool;

    private final Map<Integer, Instance> instancesPool;

    private final Map<Integer, Map<Integer, Poll>> polls = new HashMap<>();
    private final Map<Integer, Map<Integer, Poll>> pollsToCharIds = new HashMap<>();

    public InvitationPoolImpl(@NotNull AssetProvider assetProvider,
                              @NotNull PcgContentFactory factory,
                              @NotNull Map<Integer, SmartController> smartControllersPool,
                              @NotNull Map<Integer, CharactersParty> globalPartiesPool,
                              @NotNull Map<Integer, Deque<CharactersParty>> wipPartiesPool,
                              @NotNull Map<Integer, Instance> instancesPool) {
        this.assetProvider = assetProvider;
        this.factory = factory;
        this.smartControllersPool = smartControllersPool;
        this.globalPartiesPool = globalPartiesPool;
        this.wipPartiesPool = wipPartiesPool;
        this.instancesPool = instancesPool;

        polls.put(GameModes.GM_COOP_PVE, new ConcurrentHashMap<>());
        polls.put(GameModes.GM_COOP_PVP, new ConcurrentHashMap<>());
        polls.put(GameModes.GM_SQUAD_PVP, new ConcurrentHashMap<>());

        pollsToCharIds.put(GameModes.GM_COOP_PVE, new ConcurrentHashMap<>());
        pollsToCharIds.put(GameModes.GM_COOP_PVP, new ConcurrentHashMap<>());
        pollsToCharIds.put(GameModes.GM_SQUAD_PVP, new ConcurrentHashMap<>());
    }

    @Override
    public @NotNull Integer addPoll(@NotNull CharactersParty party) {
        if (!contains(party, GameModes.GM_COOP_PVE)) {
            final Poll pvePoll = new PvePoll(party);
            polls.get(GameModes.GM_COOP_PVE).put(pvePoll.getID(), pvePoll);
            mapPollToCharacterIds(pvePoll);
            return pvePoll.getID();
        }
        return Constants.UNDEFINED_ID;
    }

    @Override
    public @NotNull Integer addPoll(@NotNull Map<Integer, CharactersParty> parties,
                                    @NotNull Integer gameMode) {
        if (parties.size() > DigitsPairIndices.PAIR_SIZE
                || gameMode == GameModes.GM_COOP_PVE
                || gameMode == GameModes.GM_SOLO_PVE) {
            return Constants.UNDEFINED_ID;
        }
        for (Integer partyId : parties.keySet()) {
            if (contains(parties.get(partyId), gameMode)) {
                return Constants.UNDEFINED_ID;
            }
        }
        final Poll pvpPoll = gameMode == GameModes.GM_SQUAD_PVP
                ? new SquadPvpPoll(parties) : new CoopPvpPoll(parties);
        polls.get(pvpPoll.getGameMode()).put(pvpPoll.getID(), pvpPoll);
        mapPollToCharacterIds(pvpPoll);
        return pvpPoll.getID();
    }

    @Override
    public void update() {
        for (Integer gameMode : polls.keySet()) {
            for (Integer pollId : polls.get(gameMode).keySet()) {
                final Poll poll = polls.get(gameMode).get(pollId);
                if (poll.isReady()) {
                    final Instance instance = makeInstance(poll);
                    instancesPool.put(instance.getID(), instance);
                    for (Integer teamId : poll.getParties().keySet()) {
                        globalPartiesPool.put(Objects.requireNonNull(
                                poll.getParty(teamId)).getID(),
                                poll.getParty(teamId));
                    }
                    // CHECKSTYLE:OFF
                    // TODO: broadcast NewInstanceMessage to all participants
                    // CHECKSTYLE:ON
                    polls.get(gameMode).remove(pollId);
                    unmapPollFromCharacterIds(poll);
                    continue;
                }
                poll.update();
                if (poll.isExpired() || poll.isCanceled()) {
                    processCanceledOrExpired(poll);
                    polls.get(gameMode).remove(pollId);
                }
            }
        }
    }

    @Override
    public @NotNull Boolean updatePoll(@NotNull Integer pollId, @NotNull Integer gameMode,
                                       @NotNull Integer characterId,
                                       @NotNull Integer newStatus) {
        if (!polls.containsKey(gameMode)) {
            return false;
        }
        if (!polls.get(gameMode).containsKey(pollId)) {
            return false;
        }
        final Poll requestedPoll = polls.get(gameMode).get(pollId);
        for (Integer partyId : requestedPoll.getParties().keySet()) {
            final CharactersParty party = requestedPoll.getParty(partyId);
            for (Integer roleId : Objects.requireNonNull(party).getRoleIds()) {
                if (Objects.requireNonNull(
                        party.getMember(roleId)).getID().equals(characterId)) {
                    Objects.requireNonNull(requestedPoll.getPartyAnswers(partyId))
                            .get(roleId).setStatus(newStatus);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public @Nullable Poll getPoll(@NotNull Integer pollId) {
        for (Integer gameMode : polls.keySet()) {
           final Poll poll = polls.get(gameMode).getOrDefault(pollId, null);
           if (poll != null) {
               return poll;
           }
        }
        return null;
    }

    @Override
    public @NotNull Integer getPollId(@NotNull Integer characterId,
                                      @NotNull Integer gameMode) {
        if (characterId < Constants.MIN_ID_VALUE || !polls.containsKey(gameMode)) {
            return Constants.UNDEFINED_ID;
        }
        final Map<Integer, Poll> pendingPolls = pollsToCharIds.get(gameMode);
        return pendingPolls.containsKey(characterId)
                ? pendingPolls.get(characterId).getID() : Constants.UNDEFINED_ID;
    }

    private void mapPollToCharacterIds(@NotNull Poll poll) {
        final Map<Integer, CharactersParty> parties = poll.getParties();
        for (Integer partyId : parties.keySet()) {
            for (Integer roleId : parties.get(partyId).getRoleIds()) {
                final AliveEntity member = parties.get(partyId).getMember(roleId);
                if (member != null) {
                    pollsToCharIds.get(poll.getGameMode()).put(member.getID(), poll);
                }
            }
        }
    }

    private void unmapPollFromCharacterIds(@NotNull Poll poll) {
        final Map<Integer, CharactersParty> parties = poll.getParties();
        for (Integer partyId : parties.keySet()) {
            for (Integer roleId : parties.get(partyId).getRoleIds()) {
                final AliveEntity member = parties.get(partyId).getMember(roleId);
                if (member != null) {
                    pollsToCharIds.remove(member.getID());
                }
            }
        }
    }

    private Boolean contains(@NotNull CharactersParty party, @NotNull Integer gameMode) {
        final Map<Integer, Poll> gameModePolls = polls.getOrDefault(gameMode, null);
        if (gameModePolls == null) {
            return true;
        }
        for (Integer pollId : gameModePolls.keySet()) {
            final Map<Integer, CharactersParty> parties =
                    gameModePolls.get(pollId).getParties();
            for (Integer partyId : parties.keySet()) {
                if (parties.get(partyId).equals(party)) {
                    return true;
                }
            }
        }

        return false;
    }

    private void broadcast(@NotNull CharactersParty party, @NotNull Message message) {
        final Map<Integer, SmartController> broadcastPool = new HashMap<>();
        for (Integer roleId : party.getRoleIds()) {
            if (party.getMember(roleId) != null) {
                final Integer ownerId = Objects.requireNonNull(
                        party.getMember(roleId)).getOwnerID();
                final SmartController controller =
                        smartControllersPool.getOrDefault(ownerId, null);
                if (controller != null && !broadcastPool.containsKey(ownerId)) {
                    broadcastPool.put(ownerId, controller);
                }
            }
        }
        for (Integer userId : broadcastPool.keySet()) {
            broadcastPool.get(userId).addInboxMessage(message);
        }
    }

    private void broadcast(@NotNull Map<Integer, CharactersParty> parties,
                           @NotNull Message message) {
        for (Integer partyId : parties.keySet()) {
            broadcast(parties.get(partyId), message);
        }
    }

    @SuppressWarnings("unused")
    private void sendMessageTo(@NotNull Integer characterId, @NotNull Message message) {
        for (Integer userId : smartControllersPool.keySet()) {
            if (smartControllersPool.get(userId).getActiveChar() != null
                    && Objects.requireNonNull(
                            smartControllersPool.get(userId).getActiveChar())
                    .getID().equals(characterId)) {
                smartControllersPool.get(userId).addInboxMessage(message);
                return;
            }
        }
    }

    private void sendMessageTo(@NotNull AliveEntity character, @NotNull Message message) {
        for (Integer userId : smartControllersPool.keySet()) {
            if (character.equals(smartControllersPool.get(userId).getActiveChar())) {
                smartControllersPool.get(userId).addInboxMessage(message);
                return;
            }
        }
    }

    private @NotNull Instance makeInstance(@NotNull Poll poll) {
        final List<String> instanceNameDescription =
                assetProvider.makeInstanceNameDescription();
        final List<CharactersParty> parties = new ArrayList<>();
        for (Integer partyId : poll.getParties().keySet()) {
            parties.add(poll.getParty(partyId));
        }
        final Message notification = new MatchmakingNotificationMessage(
                "instance is ready and waiting for you!");
        if (poll.getGameMode() == GameModes.GM_COOP_PVE) {
            final AbstractInstance.DungeonInstanceModel model =
                    new AbstractInstance.DungeonInstanceModel(
                            instanceNameDescription.get(0),
                            instanceNameDescription.get(1),
                            parties.get(0).getAverageLevel(),
                            Constants.DEFAULT_ROOMS_COUNT,
                            factory, parties,
                            AIBehaviors.getAllBehaviors());
            broadcast(poll.getParties(), notification);
            return new DungeonInstance(model);
        } else {
            Integer averageLevel = 0;
            for (CharactersParty party : parties) {
                averageLevel += party.getAverageLevel();
            }
            averageLevel /= parties.size();
            final AbstractInstance.LandInstanceModel model =
                    new AbstractInstance.LandInstanceModel(
                            instanceNameDescription.get(0),
                            instanceNameDescription.get(1),
                            averageLevel, factory, parties);
            broadcast(poll.getParties(), notification);
            return new LandInstance(model);
        }
    }

    @SuppressWarnings("OverlyComplexMethod")
    private void processCanceledOrExpired(@NotNull Poll poll) {
        final Map<Integer, Poll.InvitationPoll> answers = poll.getStatus();
        final Map<Integer, CharactersParty> parties = poll.getParties();
        final List<Integer> ownerId = new ArrayList<>();

        for (Integer partyId : answers.keySet()) {
            final Boolean isExpired = poll.isExpired();
            if (poll.getGameMode() == GameModes.GM_SQUAD_PVP) {
                ownerId.add(Objects.requireNonNull(parties.get(partyId)
                        .getMember(CharacterRoleIds.CR_TANK)).getOwnerID());
            }
            for (Integer roleId : answers.get(partyId).keySet()) {
                if (answers.get(partyId).get(roleId).isCancel()
                        || answers.get(partyId).get(roleId).isExpired()) {
                    final AliveEntity member = Objects.requireNonNull(
                            parties.get(partyId).getMember(roleId));
                    final String notification;
                    if (answers.get(partyId).get(roleId).isExpired()) {
                        notification =
                                "you were removed from queue. Reason: timeout expired";
                    } else {
                        notification =
                                "you've declined the invitation "
                                        + "and were removed from the queue";
                    }
                    if (roleId == CharacterRoleIds.CR_TANK
                            || poll.getGameMode() != GameModes.GM_SQUAD_PVP) {
                        sendMessageTo(member,
                                new MatchmakingNotificationMessage(notification));
                    }
                    parties.get(partyId).removeMember(roleId);
                }
            }
            globalPartiesPool.remove(partyId);
            if (!wipPartiesPool.get(poll.getGameMode()).contains(parties.get(partyId))
                    && parties.get(partyId).getPartySize() > 0) {
                wipPartiesPool.get(poll.getGameMode()).offerFirst(parties.get(partyId));
            }
            final String notification;
            if (isExpired) {
                notification = "your party was returned to "
                        + "the queue. Reason: timeout expired";
            } else {
                notification = "someone has declined an invitation."
                        + " Your party was returned to the top of the queue.";
            }
            broadcast(parties.get(partyId),
                    new MatchmakingNotificationMessage(notification));
            if (poll.getGameMode() == GameModes.GM_SQUAD_PVP) {
                for (Integer address : ownerId) {
                    smartControllersPool.get(address).addInboxMessage(
                            new MatchmakingNotificationMessage(notification));
                }
            }
        }
        unmapPollFromCharacterIds(poll);
    }
}
