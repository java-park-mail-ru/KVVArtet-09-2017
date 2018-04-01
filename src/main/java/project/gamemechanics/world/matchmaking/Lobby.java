package project.gamemechanics.world.matchmaking;

import project.gamemechanics.battlefield.aliveentitiescontainers.CharactersParty;
import project.gamemechanics.interfaces.AliveEntity;
import project.websocket.messages.Message;

import javax.validation.constraints.NotNull;

/**
 * Matchmaking lobby interface. Allows automatical forming balanced character parties
 * {@see CharacterParty} and updating invitation statuses {@see InvitationPool}.
 */
@SuppressWarnings("unused")
public interface Lobby {
    /**
     * Enqueue character {@see AliveEntity} into certain game mode queue
     * in his active role (tank, support or damage dealer).
     *
     * @param character - character to enqueue
     * @param gameMode - game mode to enqueue character in.
     *                 Solo PvE mode isn't supported {@see GameModes}
     * @return {@link project.websocket.messages.matchmaking.LobbyConfirmationMessage} if successful,
     *      or {@link project.websocket.messages.ErrorMessage} otherwise
     */
    @NotNull Message enqueue(@NotNull AliveEntity character, @NotNull Integer gameMode);

    /**
     * Enqueue party {@see CharactersParty} into given game mode queue.
     * Sending solo PvE mode request will create a single-player dungeon {@see DungeonInstance}
     * {@see Instance}.
     *
     * @param party - party to enqueue. Can be full or not.
     * @param gameMode - game mode to enqueue party in.
     * @return {@link project.websocket.messages.matchmaking.LobbyConfirmationMessage} if successful,
     *      or {@link project.websocket.messages.ErrorMessage} otherwise
     */
    @NotNull Message enqueue(@NotNull CharactersParty party, @NotNull Integer gameMode);

    /**
     * Dequeue character {@see AliveEntity} from all queues he's queued in.
     *
     * @param character - character to dequeue
     * @return {@link project.websocket.messages.matchmaking.LobbyConfirmationMessage} if successful,
     *      or {@link project.websocket.messages.ErrorMessage} otherwise
     */
    @NotNull Message dequeue(@NotNull AliveEntity character);

    /**
     * Dequeue character {@see AliveEntity} from specific game mode queue
     * (character may still be queued for other game modes).
     *
     * @param character - character to dequeue
     * @param gameMode - game mode to dequeue character from
     * @return {@link project.websocket.messages.matchmaking.LobbyConfirmationMessage} if successful,
     *      or {@link project.websocket.messages.ErrorMessage} otherwise
     */
    @NotNull Message dequeue(@NotNull AliveEntity character, @NotNull Integer gameMode);

    /**
     * Dequeue party {@see CharacterParty} from all game modes.
     *
     * @param party - party to dequeue
     * @param dismissParty - flag indicating if party shall be destroyed after successful dequeuing
     * @return {@link project.websocket.messages.matchmaking.LobbyConfirmationMessage} if successful,
     *      or {@link project.websocket.messages.ErrorMessage} otherwise
     */
    @NotNull Message dequeue(@NotNull CharactersParty party, @NotNull Boolean dismissParty);

    /**
     * Dequeue party {@see CharactersParty} from specific game mode queue
     * (party may still be queued for other game modes).
     *
     * @param party - party to dequeue
     * @param gameMode - game mode to dequeue party from
     * @param dismissParty - flag indicating if party shall be destroyed after successful dequeuing
     * @return {@link project.websocket.messages.matchmaking.LobbyConfirmationMessage} if successful,
     *      or {@link project.websocket.messages.ErrorMessage} otherwise
     */
    @NotNull Message dequeue(@NotNull CharactersParty party,
                             @NotNull Integer gameMode,
                             @NotNull Boolean dismissParty);

    /**
     * Updates character's vote {@see Invitation} in in specified game mode to given
     * new status by character ID and poll ID.
     *
     * @param pollId - ID of poll to update
     * @param gameMode - game mode to look for character in
     * @param characterId - ID of character whose vote shall be updated
     * @param newStatus - new character vote status
     * @return {@link project.websocket.messages.matchmaking.LobbyConfirmationMessage} if successful,
     *      or {@link project.websocket.messages.ErrorMessage} otherwise
     */
    @NotNull Message updatePoll(@NotNull Integer pollId, @NotNull Integer characterId,
                                @NotNull Integer gameMode, @NotNull Integer newStatus);

    /**
     * Updates character's vote {@see Invitation} in in specified game mode to given
     * new status by character ID.
     *
     * @param gameMode - game mode to look for character in
     * @param characterId - ID of character whose vote shall be updated
     * @param newStatus - new character vote status
     * @return {@link project.websocket.messages.matchmaking.LobbyConfirmationMessage} if successful,
     *      or {@link project.websocket.messages.ErrorMessage} otherwise
     */
    @NotNull Message updatePoll(@NotNull Integer characterId, @NotNull Integer gameMode,
                                @NotNull Integer newStatus);

    /**
     * checks if character {@see AliveEntity} is queued for any game mode by character ID.
     *
     * @param characterId - Id of character to search
     * @return {@link project.websocket.messages.typecontainer.BooleanMessage} with flag containing the result
     *      if input is valid (true if the character is queued for any game mode, false otherwise),
     *      or {@link project.websocket.messages.ErrorMessage} otherwise
     */
    @NotNull Message isQueued(@NotNull Integer characterId);

    /**
     * checks if character {@see AliveEntity} is queued for specified game mode by character ID.
     *
     * @param characterId - Id of character to search
     * @param gameMode - game mode to search in
     * @return {@link project.websocket.messages.typecontainer.BooleanMessage} with flag containing the result
     *      if input is valid (flag will be set to true if there's a character
     *      with such ID, and will be false otherwise),
     *      or {@link project.websocket.messages.ErrorMessage} otherwise
     */
    @NotNull Message isQueued(@NotNull Integer characterId, @NotNull Integer gameMode);

    /**
     * updates lobby instance during the server tick.
     */
    void tick();
}
