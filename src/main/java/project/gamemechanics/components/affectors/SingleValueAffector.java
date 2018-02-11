package project.gamemechanics.components.affectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.jetbrains.annotations.Nullable;
import project.gamemechanics.globals.Constants;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@SuppressWarnings("RedundantSuppression")
public class SingleValueAffector implements Affector {
    private Integer affection;

    public SingleValueAffector(@JsonProperty("affection") @NotNull Integer affection) {
        this.affection = affection;
    }

    @Override
    public @NotNull Integer getAffection() {
        return affection;
    }

    @Override
    public @NotNull Integer getAffection(@NotNull Integer affectionIndex) {
        return 0;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    @JsonIgnore
    public @Nullable List<Integer> getAffectionsList() {
        return null;
    }

    @Override
    @JsonIgnore
    public @NotNull Boolean setAffectionsList(@NotNull List<Integer> affections) {
        return false;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    @JsonIgnore
    public @Nullable Map<Integer, Integer> getAffectionsMap() {
        return null;
    }

    @Override
    @JsonIgnore
    public @NotNull Boolean setAffectionsMap(@NotNull Map<Integer, Integer> affections) {
        return false;
    }

    // CHECKSTYLE:OFF
    @SuppressWarnings("ParameterHidesMemberVariable")
    @Override
    @JsonSetter("affection")
    public @NotNull Boolean setSingleAffection(@NotNull Integer affection) {
    // CHECKSTYLE:ON
        this.affection = affection;
        return true;
    }

    @Override
    public void modifyByPercentage(@NotNull Float percentage) {
        affection = Math.round(affection * (percentage + Constants.PERCENTAGE_CAP_FLOAT));
    }

    @Override
    public @NotNull Boolean modifyByAddition(@NotNull Integer toAdd) {
        affection += toAdd;
        return true;
    }
}
