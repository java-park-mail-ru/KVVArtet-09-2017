package project.gamemechanics.resources.holders;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.jetbrains.annotations.Nullable;
import project.gamemechanics.resources.models.GameResource;

import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unused")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(GameResourceHolder.class)
})
public interface ResourceHolder {
    @NotNull Boolean hasResource(@NotNull Integer resourceIndex);

    @Nullable GameResource getResource(@NotNull Integer resourceIndex);

    @NotNull Set<Integer> getAvailableResources();

    @NotNull Map<Integer, GameResource> getAllResources();
}
