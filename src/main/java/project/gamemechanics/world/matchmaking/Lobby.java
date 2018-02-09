package project.gamemechanics.world.matchmaking;

import project.gamemechanics.battlefield.aliveentitiescontainers.CharactersParty;
import project.gamemechanics.interfaces.AliveEntity;
import project.websocket.messages.Message;

import javax.validation.constraints.NotNull;

@SuppressWarnings("unused")
public interface Lobby {
    @NotNull Message enqueue(@NotNull AliveEntity character, @NotNull Integer gameMode);

    @NotNull Message enqueue(@NotNull CharactersParty party, @NotNull Integer gameMode);

    @NotNull Message dequeue(@NotNull AliveEntity character);

    @NotNull Message dequeue(@NotNull AliveEntity character, @NotNull Integer gameMode);

    @NotNull Message dequeue(@NotNull CharactersParty party, @NotNull Boolean dismissParty);

    @NotNull Message dequeue(@NotNull CharactersParty party,
                             @NotNull Integer gameMode,
                             @NotNull Boolean dismissParty);

    void tick();
}
