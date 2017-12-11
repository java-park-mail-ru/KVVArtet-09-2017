package project.gamemechanics.resources.holders;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import project.gamemechanics.resources.models.GameResource;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class GameResourceHolder implements ResourceHolder {
    private final Map<Integer, GameResource> resources = new HashMap<>();

    public GameResourceHolder() {
    }

    public GameResourceHolder(@JsonProperty("resources") @NotNull Map<Integer, GameResource> resources) {
        this.resources.putAll(resources);
    }

    @Override
    public Boolean hasResource(@NotNull Integer resourceIndex) {
        return resources.containsKey(resourceIndex);
    }

    @Override
    @JsonIgnore
    public Set<Integer> getAvailableResources() {
        return resources.keySet();
    }

    @Override
    public GameResource getResource(@NotNull Integer resourceIndex) {
        return resources.getOrDefault(resourceIndex, null);
    }

    @Override
    @JsonProperty("resources")
    public Map<Integer, GameResource> getAllResources() {
        return resources;
    }
}
