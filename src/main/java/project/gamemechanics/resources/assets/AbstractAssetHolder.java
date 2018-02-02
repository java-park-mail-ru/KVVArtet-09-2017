package project.gamemechanics.resources.assets;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.jetbrains.annotations.Nullable;
import project.gamemechanics.interfaces.Countable;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class AbstractAssetHolder<T extends Countable> implements AssetHolder<T> {
    // CHECKSTYLE:OFF
    final Map<Integer, T> assets = new HashMap<>();
    // CHECKSTYLE:ON

    @Override
    public @NotNull Boolean hasAsset(@NotNull Integer assetIndex) {
        return assets.containsKey(assetIndex);
    }

    @Override
    public @Nullable T getAsset(@NotNull Integer assetIndex) {
        return assets.getOrDefault(assetIndex, null);
    }

    @Override
    @JsonIgnore
    public @NotNull Set<Integer> getAvailableAssets() {
        return assets.keySet();
    }

    @Override
    @JsonIgnore
    public @NotNull Map<Integer, T> getAllAssets() {
        return assets;
    }
}
