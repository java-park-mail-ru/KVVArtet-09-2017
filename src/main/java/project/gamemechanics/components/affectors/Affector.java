package project.gamemechanics.components.affectors;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.jetbrains.annotations.Nullable;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(SingleValueAffector.class),
        @JsonSubTypes.Type(ListAffector.class),
        @JsonSubTypes.Type(MapAffector.class),
})
public interface Affector {
    @NotNull Integer getAffection(@NotNull Integer affectionIndex);

    @NotNull Integer getAffection();

    @NotNull Boolean setSingleAffection(@NotNull Integer affection);

    @Nullable List<Integer> getAffectionsList();

    @NotNull Boolean setAffectionsList(@NotNull List<Integer> affections);

    @Nullable Map<Integer, Integer> getAffectionsMap();

    @NotNull Boolean setAffectionsMap(@NotNull Map<Integer, Integer> affections);

    void modifyByPercentage(@NotNull Float percentage);

    @SuppressWarnings("SameReturnValue")
    @NotNull Boolean modifyByAddition(@NotNull Integer toAdd);
}
