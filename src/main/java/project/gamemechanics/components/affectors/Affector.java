package project.gamemechanics.components.affectors;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(SingleValueAffector.class),
        @JsonSubTypes.Type(ListAffector.class),
        @JsonSubTypes.Type(MapAffector.class),
})
public interface Affector {
    Integer getAffection(@NotNull Integer affectionIndex);

    Integer getAffection();

    Boolean setSingleAffection(@NotNull Integer affection);

    List<Integer> getAffectionsList();

    Boolean setAffectionsList(@NotNull List<Integer> affections);

    Map<Integer, Integer> getAffectionsMap();

    Boolean setAffectionsMap(@NotNull Map<Integer, Integer> affections);

    void modifyByPercentage(@NotNull Float percentage);

    @SuppressWarnings("SameReturnValue")
    Boolean modifyByAddition(@NotNull Integer toAdd);
}
