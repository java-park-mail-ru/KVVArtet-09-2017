package project.gamemechanics.world;

import gamemechanics.battlefield.aliveentitiescontainers.CharactersParty;
import gamemechanics.components.properties.PropertyCategories;
import gamemechanics.dungeons.Instance;
import gamemechanics.globals.Constants;
import gamemechanics.interfaces.AliveEntity;

import javax.validation.constraints.NotNull;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LobbyImpl implements Lobby {
    private final Map<Integer, CharactersParty> globalPartiesPool;

    private final Map<Integer, Instance> instancesPool;

    private final Map<Integer, Deque<CharactersParty>> wipPartiesPool = new ConcurrentHashMap<>();
    private final Map<Integer, Map<Integer, Deque<AliveEntity>>> queuedCharacters = new ConcurrentHashMap<>();

    public LobbyImpl(@NotNull Map<Integer, CharactersParty> globalPartiesPool,
                     @NotNull Map<Integer, Instance> instancesPool) {
        this.globalPartiesPool = globalPartiesPool;
        this.instancesPool = instancesPool;
    }

    @Override
    @SuppressWarnings("OverlyComplexMethod")
    public void enqueue(@NotNull AliveEntity character, @NotNull Integer gameMode) {
        if (!queuedCharacters.containsKey(gameMode)) {
            return;
        }
        if (character.hasProperty(PropertyCategories.PC_INSTANCE_ID)
                && character.getProperty(PropertyCategories.PC_INSTANCE_ID) != Constants.UNDEFINED_ID) {
            return;
        }
        if (!character.hasProperty(PropertyCategories.PC_ACTIVE_ROLE)
                || character.getProperty(PropertyCategories.PC_ACTIVE_ROLE) == Constants.UNDEFINED_ID) {
            return;
        }
        // note: do we need to actually check character's presence in currently WIP parties?
        if (character.hasProperty(PropertyCategories.PC_PARTY_ID)
                && character.getProperty(PropertyCategories.PC_PARTY_ID) != Constants.UNDEFINED_ID) {
            for (Integer gameModeId : wipPartiesPool.keySet()) {
                for (CharactersParty wipParty : wipPartiesPool.get(gameModeId)) {
                    if (wipParty.getID().equals(character.getProperty(PropertyCategories.PC_PARTY_ID))) {
                        return;
                    }
                }
            }
        }
        // note: do we need to check character's presence in other game modes' queues also?
        for (Integer roleId : queuedCharacters.get(gameMode).keySet()) {
            final Deque<AliveEntity> roleQueue = queuedCharacters.get(gameMode).get(roleId);
            if (roleQueue.contains(character)) {
                return;
            }
        }

        queuedCharacters.get(gameMode).get(character.getProperty(PropertyCategories.PC_ACTIVE_ROLE))
                .offerLast(character);
    }

    @Override
    public void enqueue(@NotNull CharactersParty party, @NotNull Integer gameMode) {
        if (!wipPartiesPool.containsKey(gameMode)) {
            return;
        }
        for (Integer gameModeId : wipPartiesPool.keySet()) {
            if (wipPartiesPool.get(gameModeId).contains(party)) {
                return;
            }
        }
        wipPartiesPool.get(gameMode).offerLast(party);
    }

    @Override
    public void dequeue(@NotNull AliveEntity character) {
        for (Integer gameMode : queuedCharacters.keySet()) {
            dequeueCharacterFromQueue(character, gameMode);
        }
        for (Integer gameMode : wipPartiesPool.keySet()) {
            if (dequeueCharacterFromParties(character, gameMode)) {
                break;
            }
        }
    }

    @Override
    public void dequeue(@NotNull AliveEntity character, @NotNull Integer gameMode) {
        dequeueCharacterFromQueue(character, gameMode);
        dequeueCharacterFromParties(character, gameMode);
    }

    @Override
    public void dequeue(@NotNull CharactersParty party) {

    }

    @Override
    public void dequeue(@NotNull CharactersParty party, @NotNull Integer gameMode)  {

    }

    private Boolean dequeueCharacterFromParties(@NotNull AliveEntity character, @NotNull Integer gameMode) {
        for (CharactersParty party : wipPartiesPool.get(gameMode)) {
            for (Integer roleId : party.getRoleIds()) {
                if (party.getMember(roleId).equals(character)) {
                    party.removeMember(roleId);
                    return true;
                }
            }
        }
        return false;
    }

    private void dequeueCharacterFromQueue(@NotNull AliveEntity character, @NotNull Integer gameMode) {
        for (Integer roleId : queuedCharacters.get(gameMode).keySet()) {
            final Deque<AliveEntity> queuedForRole = queuedCharacters.get(gameMode).get(roleId);
            if (queuedForRole.contains(character)) {
                queuedForRole.remove(character);
            }
        }
    }
}
