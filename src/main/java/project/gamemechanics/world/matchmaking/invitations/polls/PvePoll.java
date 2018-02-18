package project.gamemechanics.world.matchmaking.invitations.polls;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.jetbrains.annotations.Nullable;
import project.gamemechanics.battlefield.aliveentitiescontainers.CharactersParty;
import project.gamemechanics.globals.GameModes;
import project.gamemechanics.world.matchmaking.invitations.invitations.UserInvitation;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

public class PvePoll extends AbstractPoll {
    private final InvitationPoll playerStatuses = new HashInvitationPoll();
    private final CharactersParty party;

    public PvePoll(@NotNull CharactersParty party) {
        this.party = party;
        for (Integer roleId : party.getRoleIds()) {
            if (party.getMember(roleId) != null) {
                playerStatuses.put(roleId, new UserInvitation());
            }
        }
    }

    @Override
    public @NotNull Integer getGameMode() {
        return GameModes.GM_COOP_PVE;
    }

    @Override
    public @NotNull Map<Integer, InvitationPoll> getStatus() {
        final Map<Integer, InvitationPoll> pollStatus = new HashMap<>();
        pollStatus.put(party.getID(), playerStatuses);
        return pollStatus;
    }

    @Override
    public @Nullable Poll.InvitationPoll getPartyAnswers(@NotNull Integer partyId) {
        return partyId.equals(party.getID()) ? playerStatuses : null;
    }

    @Override
    public void update() {
        for (Integer roleId : playerStatuses.keySet()) {
            playerStatuses.get(roleId).update();
        }
    }

    @Override
    public @NotNull Boolean isExpired() {
        for (Integer roleId : playerStatuses.keySet()) {
            if (playerStatuses.get(roleId).isExpired()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public @NotNull Boolean isCanceled() {
        for (Integer roleId : playerStatuses.keySet()) {
            if (playerStatuses.get(roleId).isCancel()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public @NotNull Boolean isReady() {
        for (Integer roleId : playerStatuses.keySet()) {
            if (!playerStatuses.get(roleId).isConfirm()) {
                return false;
            }
        }
        return true;
    }

    @Override
    @JsonIgnore
    public @NotNull CharactersParty getParty() {
        return party;
    }

    @Override
    @JsonIgnore
    public @Nullable CharactersParty getParty(@NotNull Integer partyId) {
        return partyId.equals(party.getID()) ? party : null;
    }

    @Override
    @JsonIgnore
    public @NotNull Map<Integer, CharactersParty> getParties() {
        final Map<Integer, CharactersParty> parties = new HashMap<>();
        parties.put(party.getID(), party);
        return parties;
    }
}
