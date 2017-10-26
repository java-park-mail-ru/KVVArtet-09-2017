package gamemechanics.components.affectors;

import gamemechanics.globals.Constants;

public class SingleValueAffector implements Affector {
    private Integer affection;

    public SingleValueAffector(Integer affection) {
        this.affection = affection;
    }

    @Override
    public Integer getAffection() {
        return affection;
    }

    @Override
    public Boolean setSingleAffection(Integer affection) {
        this.affection = affection;
        return true;
    }

    @Override
    public Boolean modifyByPercentage(Float percentage) {
        affection = Math.round(affection * (percentage + Constants.PERCENTAGE_CAP_FLOAT));
        return true;
    }

    @Override
    public Boolean modifyByAddition(Integer toAdd) {
        affection += toAdd;
        return true;
    }
}
