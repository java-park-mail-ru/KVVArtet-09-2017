package gamemechanics.components.affectors;

import gamemechanics.globals.Constants;

import java.util.Map;

public class MapAffector implements Affector {
    private final Map<Integer, Integer> affections;

    public MapAffector(Map<Integer, Integer> affections) {
        this.affections = affections;
    }

    @Override
    public Integer getAffection(Integer affectionIndex) {
        if (!affections.containsKey(affectionIndex)) {
            return 0;
        }
        return affections.get(affectionIndex);
    }

    @Override
    public Map<Integer, Integer> getAffectionsMap() {
        return affections;
    }

    @Override
    public Boolean setAffectionsMap(Map<Integer, Integer> affections) {
        if (this.affections.keySet() != affections.keySet()) {
            return false;
        }
        for (Integer key : this.affections.keySet()) {
            this.affections.replace(key, affections.get(key));
        }
        return true;
    }

    @Override
    public Boolean modifyByPercentage(Float percentage) {
        for (Integer key : affections.keySet()) {
            Float resultPercentage = percentage + Constants.PERCENTAGE_CAP_FLOAT;
            affections.replace(key, Math.round(affections.get(key) * resultPercentage));
        }
        return true;
    }

    @Override
    public Boolean modifyByAddition(Integer toAdd) {
        for (Integer key : affections.keySet()) {
            affections.replace(key, affections.get(key) + toAdd);
        }
        return true;
    }
}
