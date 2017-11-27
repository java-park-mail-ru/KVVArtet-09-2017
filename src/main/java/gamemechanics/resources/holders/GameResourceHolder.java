package gamemechanics.resources.holders;

import gamemechanics.resources.models.GameResource;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class GameResourceHolder implements ResourceHolder {
    private Map<Integer, GameResource> resources = new HashMap<>();

    public GameResourceHolder() {}

    @Override
    public Boolean hasResource(@NotNull Integer resourceIndex) {
        return resources.containsKey(resourceIndex);
    }

    @Override
    public Set<Integer> getAvailableResources() {
        return resources.keySet();
    }

    @Override
    public GameResource getResource(@NotNull Integer resourceIndex) {
        return resources.getOrDefault(resourceIndex, null);
    }

    @Override
    public Map<Integer, GameResource> getAllResources() {
        return resources;
    }
}
