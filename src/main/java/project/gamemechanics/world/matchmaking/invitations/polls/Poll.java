package project.gamemechanics.world.matchmaking.invitations.polls;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.jetbrains.annotations.Nullable;
import project.gamemechanics.battlefield.aliveentitiescontainers.CharactersParty;
import project.gamemechanics.interfaces.Countable;
import project.gamemechanics.world.matchmaking.invitations.invitations.Invitation;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(PvePoll.class),
        @JsonSubTypes.Type(SquadPvpPoll.class),
        @JsonSubTypes.Type(CoopPvpPoll.class)
})
public interface Poll extends Countable {
    interface InvitationPoll extends Map<Integer, Invitation> {
    }

    class HashInvitationPoll extends HashMap<Integer, Invitation> implements InvitationPoll {
    }

    @JsonProperty("mode")
    @NotNull Integer getGameMode();

    @JsonProperty("status")
    @NotNull Map<Integer, InvitationPoll> getStatus();

    @Nullable InvitationPoll getPartyAnswers(@NotNull Integer partyId);

    void update();

    @NotNull Boolean isExpired();

    @NotNull Boolean isCanceled();

    @NotNull Boolean isReady();

    @JsonIgnore
    @Nullable CharactersParty getParty();

    @JsonIgnore
    @Nullable CharactersParty getParty(@NotNull Integer partyID);

    @JsonIgnore
    @NotNull Map<Integer, CharactersParty> getParties();
}
