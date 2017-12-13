package project.gamemechanics.world;

import project.gamemechanics.battlefield.aliveentitiescontainers.CharactersParty;
import project.gamemechanics.interfaces.AliveEntity;
import project.websocket.messages.Message;

import javax.validation.constraints.NotNull;

public interface Lobby {
    Message enqueue(@NotNull AliveEntity character, @NotNull Integer gameMode);

    Message enqueue(@NotNull CharactersParty party, @NotNull Integer gameMode);

    Message dequeue(@NotNull AliveEntity character);

    Message dequeue(@NotNull AliveEntity character, @NotNull Integer gameMode);

    Message dequeue(@NotNull CharactersParty party, @NotNull Boolean dismissParty);

    Message dequeue(@NotNull CharactersParty party, @NotNull Integer gameMode, @NotNull Boolean dismissParty);

    void tick();
}
