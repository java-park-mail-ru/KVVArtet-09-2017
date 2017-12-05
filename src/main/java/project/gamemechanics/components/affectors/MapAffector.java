package project.gamemechanics.components.affectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import project.gamemechanics.globals.Constants;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

public class MapAffector implements Affector {
    private final Map<Integer, Integer> affections;

    public MapAffector(@JsonProperty("affections") @NotNull Map<Integer, Integer> affections) {
        this.affections = affections;
    }

    @Override
    @JsonIgnore
    public Integer getAffection() {
        return 0;
    }

    @Override
    @JsonIgnore
    public Boolean setSingleAffection(@NotNull Integer affection) {
        return false;
    }

    @Override
    public Integer getAffection(@NotNull Integer affectionIndex) {
        if (!affections.containsKey(affectionIndex)) {
            return 0;
        }
        return affections.get(affectionIndex);
    }

    @Override
    @JsonProperty("affections")
    public Map<Integer, Integer> getAffectionsMap() {
        return affections;
    }

    @SuppressWarnings("ParameterHidesMemberVariable")
    @Override
    @JsonSetter("affections")
    public Boolean setAffectionsMap(@NotNull Map<Integer, Integer> affections) {
        if (this.affections.keySet() != affections.keySet()) {
            return false;
        }
        for (Integer key : this.affections.keySet()) {
            this.affections.replace(key, affections.get(key));
        }
        return true;
    }

    @Override
    @JsonIgnore
    public List<Integer> getAffectionsList() {
        return null;
    }

    @SuppressWarnings("ParameterHidesMemberVariable")
    @Override
    @JsonIgnore
    public Boolean setAffectionsList(@NotNull List<Integer> affections) {
        return false;
    }

    @Override
    public void modifyByPercentage(@NotNull Float percentage) {
        for (Integer key : affections.keySet()) {
            final Float resultPercentage = percentage + Constants.PERCENTAGE_CAP_FLOAT;
            affections.replace(key, Math.round(affections.get(key) * resultPercentage));
        }
    }

    @Override
    public Boolean modifyByAddition(@NotNull Integer toAdd) {
        for (Integer key : affections.keySet()) {
            affections.replace(key, affections.get(key) + toAdd);
        }
        return true;
    }
}
