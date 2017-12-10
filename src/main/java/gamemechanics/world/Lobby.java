package gamemechanics.world;

import gamemechanics.battlefield.aliveentitiescontainers.CharactersParty;
import gamemechanics.interfaces.AliveEntity;

import javax.validation.constraints.NotNull;

public interface Lobby {
    void enqueue(@NotNull AliveEntity character, @NotNull Integer gameMode);
    void enqueue(@NotNull CharactersParty party, @NotNull Integer gameMode);

    void dequeue(@NotNull AliveEntity character);
    void dequeue(@NotNull AliveEntity character, @NotNull Integer gameMode);
    void dequeue(@NotNull CharactersParty party);
    void dequeue(@NotNull CharactersParty party, @NotNull Integer gameMode);
}
