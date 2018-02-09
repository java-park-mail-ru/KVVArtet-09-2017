package project.gamemechanics.world.matchmaking.invitations;

import project.gamemechanics.battlefield.aliveentitiescontainers.CharactersParty;

import javax.validation.constraints.NotNull;
import java.util.Map;

public interface InvitationPool {
    void addPoll(@NotNull CharactersParty party);

    void addPoll(@NotNull Map<Integer, CharactersParty> parties, @NotNull Integer gameMode);

    void update();

    void updatePoll(@NotNull Integer pollId, @NotNull Integer gameMode,
                    @NotNull Integer characterId, @NotNull Integer newStatus);
}
