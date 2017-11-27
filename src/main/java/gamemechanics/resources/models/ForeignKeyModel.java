package gamemechanics.resources.models;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ForeignKeyModel extends AbstractModel {
    private final Map<Integer, List<Integer>> mappings = new HashMap<>();

    public ForeignKeyModel(@NotNull Integer modelID, @NotNull String name, @NotNull String description,
                           @NotNull Map<Integer, List<Integer>> mappings) {
        super(modelID, name, description);
        this.mappings.putAll(mappings);
    }

    @Override
    public Boolean hasMapping(@NotNull Integer mappingIndex) {
        return mappings.containsKey(mappingIndex);
    }

    @Override
    public Set<Integer> getAvailableMappings() {
        return mappings.keySet();
    }

    @Override
    public List<Integer> getMapping(@NotNull Integer mappingIndex) {
        return mappings.getOrDefault(mappingIndex, null);
    }

    @Override
    public Map<Integer, List<Integer>> getAllMappings() {
        return mappings;
    }
}
