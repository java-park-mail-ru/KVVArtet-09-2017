package project.gamemechanics.world;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import project.gamemechanics.battlefield.aliveentitiescontainers.CharactersParty;
import project.gamemechanics.dungeons.Instance;
import project.gamemechanics.interfaces.AliveEntity;
import project.gamemechanics.items.loot.PendingLootPool;

import java.util.Map;

public interface World {
    void tick();

    Map<Integer, AliveEntity> getLoggedCharacters();

    Map<Integer, CharactersParty> getPartiesPool();

    Map<Integer, Instance> getActiveInstances();

    PendingLootPool getPendingLootPool();

    Lobby getLobby();

    void reset();
}
