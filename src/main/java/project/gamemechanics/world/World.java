package project.gamemechanics.world;

import project.gamemechanics.battlefield.aliveentitiescontainers.CharactersParty;
import project.gamemechanics.dungeons.Instance;
import project.gamemechanics.interfaces.AliveEntity;
import project.gamemechanics.items.loot.PendingLootPool;
import project.gamemechanics.world.matchmaking.Lobby;

import javax.validation.constraints.NotNull;
import java.util.Map;

@SuppressWarnings("unused")
public interface World {
    void tick();

    @NotNull Map<Integer, AliveEntity> getLoggedCharacters();

    @NotNull Map<Integer, CharactersParty> getPartiesPool();

    @NotNull Map<Integer, Instance> getActiveInstances();

    @NotNull PendingLootPool getPendingLootPool();

    @NotNull Lobby getLobby();

    void reset();
}
