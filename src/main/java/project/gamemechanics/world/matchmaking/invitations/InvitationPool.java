package project.gamemechanics.world.matchmaking.invitations;

import org.jetbrains.annotations.Nullable;
import project.gamemechanics.battlefield.aliveentitiescontainers.CharactersParty;
import project.gamemechanics.world.matchmaking.invitations.polls.Poll;

import javax.validation.constraints.NotNull;
import java.util.Map;

public interface InvitationPool {
    @NotNull Integer addPoll(@NotNull CharactersParty party);

    @NotNull Integer addPoll(@NotNull Map<Integer, CharactersParty> parties,
                             @NotNull Integer gameMode);

    void update();

    @NotNull Boolean updatePoll(@NotNull Integer pollId, @NotNull Integer gameMode,
                                @NotNull Integer characterId, @NotNull Integer newStatus);

    @Nullable Poll getPoll(@NotNull Integer pollId);
}
