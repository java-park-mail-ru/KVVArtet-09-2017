package gamemechanics.resources.models;

import gamemechanics.components.affectors.Affector;
import gamemechanics.components.properties.Property;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HybridModelWithInlays extends HybridModel {
    private final List<GameResource> inlaidModels = new ArrayList<>();

    public HybridModelWithInlays(@NotNull Integer modelD,
                                 @NotNull String name, @NotNull String description,
                                 @NotNull Map<Integer, Property> properties,
                                 @NotNull Map<Integer, Affector> affectors,
                                 @NotNull Map<Integer, List<Integer>> mappings,
                                 @NotNull List<GameResource> inlaidModels) {
        super(modelD, name, description, properties, affectors, mappings);
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
    public List<GameResource> getAllInlaid() {
        return inlaidModels;
    }
}
