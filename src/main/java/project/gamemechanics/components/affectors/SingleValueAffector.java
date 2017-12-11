package project.gamemechanics.components.affectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import project.gamemechanics.globals.Constants;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

public class SingleValueAffector implements Affector {
    private Integer affection;

    public SingleValueAffector(@JsonProperty("affection") @NotNull Integer affection) {
        this.affection = affection;
    }

    @Override
    public Integer getAffection() {
        return affection;
    }

    @Override
    public Integer getAffection(@NotNull Integer affectionIndex) {
        return 0;
    }

    @Override
    @JsonIgnore
    public List<Integer> getAffectionsList() {
        return null;
    }

    @Override
    @JsonIgnore
    public Boolean setAffectionsList(@NotNull List<Integer> affections) {
        return false;
    }

    @Override
    @JsonIgnore
    public Map<Integer, Integer> getAffectionsMap() {
        return null;
    }

    @Override
    @JsonIgnore
    public Boolean setAffectionsMap(@NotNull Map<Integer, Integer> affections) {
        return false;
    }

    @SuppressWarnings("ParameterHidesMemberVariable")
    @Override
    @JsonSetter("affection")
    public Boolean setSingleAffection(@NotNull Integer affection) {
        this.affection = affection;
        return true;
    }

    @Override
    public void modifyByPercentage(@NotNull Float percentage) {
        affection = Math.round(affection * (percentage + Constants.PERCENTAGE_CAP_FLOAT));
    }

    @Override
    public Boolean modifyByAddition(@NotNull Integer toAdd) {
        affection += toAdd;
        return true;
    }
}
