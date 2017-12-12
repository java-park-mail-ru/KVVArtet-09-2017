package project.gamemechanics.resources.assets;

import com.fasterxml.jackson.annotation.JsonIgnore;
import project.gamemechanics.interfaces.Countable;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class AbstractAssetHolder<T extends Countable> implements AssetHolder<T> {
    final Map<Integer, T> assets = new HashMap<>();

    @Override
    public Boolean hasAsset(@NotNull Integer assetIndex) {
        return assets.containsKey(assetIndex);
    }

    @Override
    public T getAsset(@NotNull Integer assetIndex) {
        return assets.getOrDefault(assetIndex, null);
    }

    @Override
    @JsonIgnore
    public Set<Integer> getAvailableAssets() {
        return assets.keySet();
    }

    @Override
    @JsonIgnore
    public Map<Integer, T> getAllAssets() {
        return assets;
    }
}
