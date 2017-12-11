package project.gamemechanics.world;

import project.gamemechanics.battlefield.aliveentitiescontainers.CharactersParty;
import project.gamemechanics.interfaces.AliveEntity;

import javax.validation.constraints.NotNull;

public interface Lobby {
    void enqueue(@NotNull AliveEntity character, @NotNull Integer gameMode);
    void enqueue(@NotNull CharactersParty party, @NotNull Integer gameMode);

    void dequeue(@NotNull AliveEntity character);
    void dequeue(@NotNull AliveEntity character, @NotNull Integer gameMode);
    void dequeue(@NotNull CharactersParty party);
    void dequeue(@NotNull CharactersParty party, @NotNull Integer gameMode);
}
