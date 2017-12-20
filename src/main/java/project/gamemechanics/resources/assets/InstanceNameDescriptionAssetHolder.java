package project.gamemechanics.resources.assets;

import com.fasterxml.jackson.annotation.JsonProperty;
import project.gamemechanics.resources.models.InstanceNameDescription;

import javax.validation.constraints.NotNull;
import java.util.Map;

public class InstanceNameDescriptionAssetHolder extends AbstractAssetHolder<InstanceNameDescription>
        implements AssetHolder.InstanceNameDescriptionHolder {
    public InstanceNameDescriptionAssetHolder(
            @JsonProperty("assets") @NotNull Map<Integer, InstanceNameDescription> assets) {
        super();
        this.assets.putAll(assets);
    }
}
