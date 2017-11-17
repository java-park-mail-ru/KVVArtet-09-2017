package gamemechanics.components.affectors;

import gamemechanics.globals.Constants;

import java.util.List;

public class ListAffector implements Affector {
    private final List<Integer> affections;

    public ListAffector(List<Integer> affections) {
        this.affections = affections;
    }

    @Override
    public Integer getAffection(Integer affectionIndex) {
        if (affectionIndex < 0 || affectionIndex >= affections.size()) {
            return Integer.MIN_VALUE;
        }
        return affections.get(affectionIndex);
    }

    @Override
    public List<Integer> getAffectionsList() {
        return affections;
    }

    @Override
    public Boolean setAffectionsList(List<Integer> affections) {
        if (affections.size() != this.affections.size()) {
            return false;
        }
        for (Integer affectionIndex = 0; affectionIndex < this.affections.size(); ++affectionIndex) {
            this.affections.set(affectionIndex, affections.get(affectionIndex));
        }
        return true;
    }

    @Override
    public Boolean modifyByPercentage(Float percentage) {
        for (Integer affectionIndex = 0; affectionIndex < affections.size(); ++affectionIndex) {
            Integer oldValue = affections.get(affectionIndex);
            Float resultPercentage = percentage + Constants.PERCENTAGE_CAP_FLOAT;
            affections.set(affectionIndex, Math.round(oldValue * resultPercentage));
        }
        return true;
    }

    @Override
    public Boolean modifyByAddition(Integer toAdd) {
        for (Integer affectionIndex = 0; affectionIndex < affections.size(); ++affectionIndex) {
            affections.set(affectionIndex, affections.get(affectionIndex) + toAdd);
        }
        return true;
    }
}
