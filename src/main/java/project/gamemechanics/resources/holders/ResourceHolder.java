package project.gamemechanics.resources.holders;

import project.gamemechanics.resources.models.GameResource;

import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.Set;

public interface ResourceHolder {
    Boolean hasResource(@NotNull Integer resourceIndex);

    GameResource getResource(@NotNull Integer resourceIndex);

    Set<Integer> getAvailableResources();

    Map<Integer, GameResource> getAllResources();
}
