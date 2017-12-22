package project.gamemechanics.components.affectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.jetbrains.annotations.Nullable;
import project.gamemechanics.globals.Constants;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

public class ListAffector implements Affector {
    private final List<Integer> affections;

    public ListAffector(@JsonProperty("affections") @NotNull List<Integer> affections) {
        this.affections = affections;
    }

    @Override
    @JsonIgnore
    public Integer getAffection() {
        return 0;
    }

    @Override
    public Integer getAffection(@NotNull Integer affectionIndex) {
        if (affectionIndex < 0 || affectionIndex >= affections.size()) {
            return 0;
        }
        return affections.get(affectionIndex);
    }

    @Override
    @JsonIgnore
    public Boolean setSingleAffection(@NotNull Integer affection) {
        return false;
    }

    @Override
    @JsonProperty("affections")
    public List<Integer> getAffectionsList() {
        return affections;
    }

    // CHECKSTYLE:OFF
    @SuppressWarnings("ParameterHidesMemberVariable")
    @Override
    @JsonSetter("affections")
    public Boolean setAffectionsList(@NotNull List<Integer> affections) {
    // CHECKSTYLE:ON
        this.affections.clear();
        this.affections.addAll(affections);
        return true;
    }

    @Override
    @JsonIgnore
    public @Nullable Map<Integer, Integer> getAffectionsMap() {
        return null;
    }
    // CHECKSTYLE:OFF

    @SuppressWarnings("ParameterHidesMemberVariable")
    @Override
    @JsonIgnore
    public Boolean setAffectionsMap(@NotNull Map<Integer, Integer> affections) {
        return false;
    }
    // CHECKSTYLE:ON

    @Override
    public void modifyByPercentage(@NotNull Float percentage) {
        for (Integer affectionIndex = 0; affectionIndex < affections.size(); ++affectionIndex) {
            final Integer oldValue = affections.get(affectionIndex);
            final Float resultPercentage = percentage + Constants.PERCENTAGE_CAP_FLOAT;
            affections.set(affectionIndex, Math.round(oldValue * resultPercentage));
        }
    }

    @Override
    public Boolean modifyByAddition(@NotNull Integer toAdd) {
        for (Integer affectionIndex = 0; affectionIndex < affections.size(); ++affectionIndex) {
            affections.set(affectionIndex, affections.get(affectionIndex) + toAdd);
        }
        return true;
    }
}
