package project.gamemechanics.components.affectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.jetbrains.annotations.Nullable;
import project.gamemechanics.globals.Constants;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@SuppressWarnings("RedundantSuppression")
public class MapAffector implements Affector {
    private final Map<Integer, Integer> affections;

    public MapAffector(@JsonProperty("affections") @NotNull Map<Integer, Integer> affections) {
        this.affections = affections;
    }

    @Override
    @JsonIgnore
    public @NotNull Integer getAffection() {
        return 0;
    }

    @Override
    public @NotNull Integer getAffection(@NotNull Integer affectionIndex) {
        if (!affections.containsKey(affectionIndex)) {
            return 0;
        }
        return affections.get(affectionIndex);
    }

    @Override
    @JsonIgnore
    public @NotNull Boolean setSingleAffection(@NotNull Integer affection) {
        return false;
    }

    @Override
    @JsonProperty("affections")
    public @NotNull Map<Integer, Integer> getAffectionsMap() {
        return affections;
    }

    // CHECKSTYLE:OFF
    @SuppressWarnings("ParameterHidesMemberVariable")
    @Override
    @JsonSetter("affections")
    public @NotNull Boolean setAffectionsMap(@NotNull Map<Integer, Integer> affections) {
    // CHECKSTYLE:ON
        if (!Objects.equals(this.affections.keySet(), affections.keySet())) {
            return false;
        }
        for (Integer key : this.affections.keySet()) {
            this.affections.replace(key, affections.get(key));
        }
        return true;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    @JsonIgnore
    public @Nullable List<Integer> getAffectionsList() {
        return null;
    }

    // CHECKSTYLE:OFF
    @SuppressWarnings("ParameterHidesMemberVariable")
    @Override
    @JsonIgnore
    public @NotNull Boolean setAffectionsList(@NotNull List<Integer> affections) {
        return false;
    }
    // CHECKSTYLE:ON

    @Override
    public void modifyByPercentage(@NotNull Float percentage) {
        for (Integer key : affections.keySet()) {
            final Float resultPercentage = percentage + Constants.PERCENTAGE_CAP_FLOAT;
            affections.replace(key, Math.round(affections.get(key) * resultPercentage));
        }
    }

    @Override
    public @NotNull Boolean modifyByAddition(@NotNull Integer toAdd) {
        for (Integer key : affections.keySet()) {
            affections.replace(key, affections.get(key) + toAdd);
        }
        return true;
    }
}
