package project.gamemechanics.resources.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import project.gamemechanics.components.affectors.Affector;
import project.gamemechanics.components.properties.Property;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HybridModelWithInlays extends HybridModel {
    private final List<GameResource> inlaidModels = new ArrayList<>();

    public HybridModelWithInlays(@JsonProperty("modelId") @NotNull Integer modelID,
                                 @JsonProperty("name") @NotNull String name,
                                 @JsonProperty("description") @NotNull String description,
                                 @JsonProperty("properties") @NotNull Map<Integer, Property> properties,
                                 @JsonProperty("affectors") @NotNull Map<Integer, Affector> affectors,
                                 @JsonProperty("mappings") @NotNull Map<Integer, List<Integer>> mappings,
                                 @JsonProperty("inlaid") @NotNull List<GameResource> inlaidModels) {
        super(modelID, name, description, properties, affectors, mappings);
        this.inlaidModels.addAll(inlaidModels);
    }

    @Override
    public GameResource getInlaid(@NotNull Integer inlaidIndex) {
        if (inlaidIndex < 0 || inlaidIndex >= inlaidModels.size()) {
            return null;
        }
        return inlaidModels.get(inlaidIndex);
    }

    @Override
    @JsonProperty("inlaid")
    public List<GameResource> getAllInlaid() {
        return inlaidModels;
    }
}
