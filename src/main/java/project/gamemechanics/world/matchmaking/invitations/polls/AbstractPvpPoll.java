package project.gamemechanics.world.matchmaking.invitations.polls;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.Nullable;
import project.gamemechanics.battlefield.aliveentitiescontainers.CharactersParty;
import project.gamemechanics.world.matchmaking.invitations.invitations.UserInvitation;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("WeakerAccess")
public abstract class AbstractPvpPoll extends AbstractPoll {
    private final Map<Integer, CharactersParty> parties;
    private final Map<Integer, InvitationPoll> playersStatus = new HashMap<>();

    public AbstractPvpPoll(@NotNull Map<Integer, CharactersParty> parties) {
        this.parties = parties;
        for (Integer partyId : parties.keySet()) {
            final InvitationPoll poll = new HashInvitationPoll();
            for (Integer roleId : parties.get(partyId).getRoleIds()) {
                poll.put(roleId, new UserInvitation());
            }
            playersStatus.put(partyId, poll);
        }
    }

    @JsonProperty("status")
    @Override
    public @NotNull Map<Integer, InvitationPoll> getStatus() {
        return playersStatus;
    }

    @Override
    public @Nullable InvitationPoll getPartyAnswers(@NotNull Integer partyId) {
        if (!playersStatus.containsKey(partyId)) {
            return null;
        }
        return playersStatus.get(partyId);
    }

    @Override
    public void update() {
        for (Integer partyId : playersStatus.keySet()) {
            for (Integer roleId : playersStatus.get(partyId).keySet()) {
                playersStatus.get(partyId).get(roleId).update();
            }
        }
    }

    @Override
    public @NotNull Boolean isExpired() {
        for (Integer partyId : playersStatus.keySet()) {
            for (Integer roleId : playersStatus.get(partyId).keySet()) {
                if (playersStatus.get(partyId).get(roleId).isExpired()) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public @NotNull Boolean isCanceled() {
        for (Integer partyId : playersStatus.keySet()) {
            for (Integer roleId : playersStatus.get(partyId).keySet()) {
                if (playersStatus.get(partyId).get(roleId).isCancel()) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public @NotNull Boolean isReady() {
        for (Integer partyId : playersStatus.keySet()) {
            for (Integer roleId : playersStatus.get(partyId).keySet()) {
                if (!playersStatus.get(partyId).get(roleId).isConfirm()) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    @JsonIgnore
    public @Nullable CharactersParty getParty() {
        return null;
    }

    @Override
    @JsonIgnore
    public @Nullable CharactersParty getParty(@NotNull Integer partyId) {
        return parties.getOrDefault(partyId, null);
    }

    @Override
    @JsonIgnore
    public @NotNull Map<Integer, CharactersParty> getParties() {
        return parties;
    }

}
