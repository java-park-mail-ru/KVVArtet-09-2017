package gamemechanics.resources.pcg.npcs;

import gamemechanics.interfaces.AliveEntity;
import gamemechanics.resources.assets.AssetProvider;
import gamemechanics.resources.pcg.items.ItemsFactory;

import javax.validation.constraints.NotNull;

public interface NpcsFactory {
    AliveEntity makeNpc(@NotNull NpcBlueprint blueprint, @NotNull AssetProvider assetProvider,
                        @NotNull ItemsFactory itemsGenerator);
}
