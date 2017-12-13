package project.gamemechanics.world;

import project.gamemechanics.battlefield.aliveentitiescontainers.CharactersParty;
import project.gamemechanics.dungeons.Instance;
import project.gamemechanics.interfaces.AliveEntity;
import project.gamemechanics.items.loot.PendingLootPool;
import project.gamemechanics.items.loot.PendingLootPoolImpl;
import project.gamemechanics.resources.assets.AssetProvider;
import project.gamemechanics.resources.assets.AssetProviderImpl;
import project.gamemechanics.resources.pcg.PcgContentFactory;
import project.gamemechanics.resources.pcg.PcgFactory;
import project.gamemechanics.smartcontroller.SmartController;
import project.gamemechanics.world.config.ResourcesConfig;

import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WorldImpl implements World {
    private final AssetProvider assetProvider = new AssetProviderImpl(ResourcesConfig.getAssetHoldersFileNames());
    @SuppressWarnings("FieldCanBeLocal")
    private final PcgContentFactory pcgContentFactory = new PcgFactory(ResourcesConfig.getItemPartsFilename(),
            ResourcesConfig.getNpcPartsFilename(), assetProvider);

    @SuppressWarnings("FieldCanBeLocal")
    private final Map<Integer, SmartController> smartControllersPool;
    private final Map<Integer, AliveEntity> charactersPool = new ConcurrentHashMap<>();

    private final Map<Integer, Instance> instancesPool = new ConcurrentHashMap<>();
    private final Map<Integer, CharactersParty> partiesPool = new ConcurrentHashMap<>();

    private final PendingLootPool lootPool = new PendingLootPoolImpl();

    private final Lobby lobby;

    public WorldImpl(@NotNull Map<Integer, SmartController> smartControllersPool) {
        this.smartControllersPool = smartControllersPool;
        lobby = new LobbyImpl(assetProvider, pcgContentFactory, lootPool, this.smartControllersPool, partiesPool,
                instancesPool);
    }

    @Override
    public void tick() {

    }

    @Override
    public Map<Integer, AliveEntity> getLoggedCharacters() {
        return charactersPool;
    }

    @Override
    public Map<Integer, Instance> getActiveInstances() {
        return instancesPool;
    }

    @Override
    public Map<Integer, CharactersParty> getPartiesPool() {
        return partiesPool;
    }

    @Override
    public PendingLootPool getPendingLootPool() {
        return lootPool;
    }

    @Override
    public Lobby getLobby() {
        return lobby;
    }
}
