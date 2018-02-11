package project.gamemechanics.world;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.gamemechanics.battlefield.aliveentitiescontainers.CharactersParty;
import project.gamemechanics.dungeons.Instance;
import project.gamemechanics.interfaces.AliveEntity;
import project.gamemechanics.items.loot.PendingLootPool;
import project.gamemechanics.items.loot.PendingLootPoolImpl;
import project.gamemechanics.resources.assets.AssetProvider;
import project.gamemechanics.resources.assets.AssetProviderImpl;
import project.gamemechanics.resources.pcg.PcgContentFactory;
import project.gamemechanics.resources.pcg.PcgFactory;
import project.gamemechanics.world.config.ResourcesConfig;
import project.gamemechanics.world.matchmaking.Lobby;
import project.gamemechanics.world.matchmaking.LobbyImpl;
import project.websocket.services.ConnectionPoolService;

import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class WorldImpl implements World {
    private final AssetProvider assetProvider = new AssetProviderImpl(ResourcesConfig.getAssetHoldersFileNames());
    private final PcgContentFactory pcgContentFactory = new PcgFactory(ResourcesConfig.getItemPartsFilename(),
            ResourcesConfig.getNpcPartsFilename(), assetProvider);

    private final ConnectionPoolService connectionPoolService;
    private final Map<Integer, AliveEntity> charactersPool = new ConcurrentHashMap<>();

    private final Map<Integer, Instance> instancesPool = new ConcurrentHashMap<>();
    private final Map<Integer, CharactersParty> partiesPool = new ConcurrentHashMap<>();

    private final PendingLootPool lootPool = new PendingLootPoolImpl();

    private final Lobby lobby;

    @Autowired
    public WorldImpl(@NotNull ConnectionPoolService connectionPoolService) {
        this.connectionPoolService = connectionPoolService;
        lobby = new LobbyImpl(assetProvider, pcgContentFactory, lootPool,
                this.connectionPoolService.getActiveSmartControllers(), partiesPool, instancesPool);
    }

    @Override
    public void tick() {
        lobby.tick();
    }

    @Override
    public void reset() {
        connectionPoolService.reset();
        charactersPool.clear();
        instancesPool.clear();
        partiesPool.clear();
        assetProvider.reset();
        pcgContentFactory.reset();
    }

    @Override
    public @NotNull Map<Integer, AliveEntity> getLoggedCharacters() {
        return charactersPool;
    }

    @Override
    public @NotNull Map<Integer, Instance> getActiveInstances() {
        return instancesPool;
    }

    @Override
    public @NotNull Map<Integer, CharactersParty> getPartiesPool() {
        return partiesPool;
    }

    @Override
    public @NotNull PendingLootPool getPendingLootPool() {
        return lootPool;
    }

    @Override
    public @NotNull Lobby getLobby() {
        return lobby;
    }
}
