package gamemechanics.resources.pcg.npcs;

import gamemechanics.interfaces.AliveEntity;
import gamemechanics.resources.assets.AssetProvider;

import javax.validation.constraints.NotNull;

public interface NpcsFactory {
    AliveEntity makeNpc(@NotNull NpcBlueprint blueprint, @NotNull AssetProvider assetProvider);
}
