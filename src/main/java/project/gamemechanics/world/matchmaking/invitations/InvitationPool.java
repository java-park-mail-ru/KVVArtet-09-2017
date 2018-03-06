package project.gamemechanics.world.matchmaking.invitations;

import org.jetbrains.annotations.Nullable;
import project.gamemechanics.battlefield.aliveentitiescontainers.CharactersParty;
import project.gamemechanics.world.matchmaking.invitations.polls.Poll;

import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * contains user confirmation polls {@see Poll} for various game modes (except solo PvE).
 * responsible for automatically notifying users on any changes in poll they belong to,
 * incoming and automatic user invitation statuses updates and actual matchmaking
 * (instantiating the dungeon or PvP map, {@see Instance} {@see LandInstance}
 * {@see DungeonInstance}) when everyone's ready to play.
 */
public interface InvitationPool {
    /**
     * adds a new poll {@see Poll} for specified party {@see CharactersParty}
     * overload for coop PvE game mode.
     *
     * @param party - party to create new poll for
     * @return created poll ID if successful or Constants.UNDEFINED_ID (-1) otherwise
     */
    @NotNull Integer addPoll(@NotNull CharactersParty party);

    /**
     * adds a new poll {@see Poll} for specified parties
     * {@see CharactersParty} in specified game mode.
     *
     * @param parties - parties to create new poll for
     * @param gameMode - gameMode to create poll in
     * @return created poll ID if successful or Constants.UNDEFINED_ID (-1) otherwise
     */
    @NotNull Integer addPoll(@NotNull Map<Integer, CharactersParty> parties,
                             @NotNull Integer gameMode);

    /**
     * updates user responses during the server tick.
     */
    void update();

    /**
     * updates user response in specified poll to specified new status.
     *
     * @param pollId - poll ID to update user response in
     * @param gameMode - gameMode to look for the poll to update
     * @param characterId - character ID to update user response for
     * @param newStatus - status to set in user response that shall be updated
     * @return true if successful or false otherwise
     */
    @NotNull Boolean updatePoll(@NotNull Integer pollId, @NotNull Integer gameMode,
                                @NotNull Integer characterId, @NotNull Integer newStatus);

    /**
     * get poll by its ID. Poll will be searched in all available game modes.
     *
     * @param pollId - poll ID to look for
     * @return poll if poll with given ID exists or null otherwise
     */
    @Nullable Poll getPoll(@NotNull Integer pollId);

    /**
     * get poll ID by specified chracter ID and game mode.
     *
     * @param characterId - ID of character attached to the poll we're looking for
     * @param gameMode - game mode to look for the poll
     * @return poll ID if such poll exists for the specified game mode
     *      or Constants.UNDEFINED_ID (-1) otherwise
     */
    @NotNull Integer getPollId(@NotNull Integer characterId, @NotNull Integer gameMode);
}
