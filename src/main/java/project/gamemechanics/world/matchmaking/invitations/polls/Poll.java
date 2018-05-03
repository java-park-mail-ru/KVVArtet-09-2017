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

/**
 * Contains users' invitation response statuses. Provides info about aggregated poll status.
 * {@see Invitation}
 * {@see InvitationPool}
 */
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

    /**
     * get poll's game mode.
     *
     * @return game mode of the instance {@see Instance}
     *      that'll be created when after all users send confirmations.
     */
    @JsonProperty("mode")
    @NotNull Integer getGameMode();

    /**
     * get all users' invitation statuses.
     *
     * @return invitation polls map
     *      (invitation poll is character IDs mapped
     *      to respective characters' invitation statuses)
     */
    @JsonProperty("status")
    @NotNull Map<Integer, InvitationPoll> getStatus();

    /**
     * get single party invitation statuses by party ID.
     *
     * @param partyId - party ID to get invitation poll for
     * @return party invitation poll (character IDs mapped
     *      to respective characters' invitation statuses)
     *      or null if partyId is invalid
     */
    @Nullable InvitationPoll getPartyAnswers(@NotNull Integer partyId);

    /**
     * updates poll during server tick.
     */
    void update();

    /**
     * checks if poll is expired.
     *
     * @return true if one or more invitations're expired
     *      or false if there're no expired invitations
     */
    @NotNull Boolean isExpired();

    /**
     * checks if poll is canceled.
     *
     * @return true if there's one or more canceled invitations
     *      or false if there're no canceled invitations
     */
    @NotNull Boolean isCanceled();

    /**
     * checks if all users're ready.
     *
     * @return true if all invitations're confirmed or false otherwise
     */
    @NotNull Boolean isReady();

    /**
     * get party {@see CharactersParty} attached to this poll.
     *
     * @return attached party or null otherwise (works only with coop PvE mode)
     */
    @JsonIgnore
    @SuppressWarnings("unused")
    @Nullable CharactersParty getParty();

    /**
     * get party {@see CharacterParty} attached to this poll by party ID.
     *
     * @param partyID - party ID to look for
     * @return party or null if invalid ID
     */
    @JsonIgnore
    @Nullable CharactersParty getParty(@NotNull Integer partyID);

    /**
     * get all parties attached to the poll mapped with their IDs.
     *
     * @return parties IDs - parties {@see CharactersParty} map
     */
    @JsonIgnore
    @NotNull Map<Integer, CharactersParty> getParties();
}
