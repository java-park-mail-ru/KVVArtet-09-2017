package gamemechanics.components.properties;

import gamemechanics.globals.Constants;

public class SingleValueProperty implements Property {
    private Integer property;

    public SingleValueProperty(Integer property) {
        this.property = property;
    }

    @Override
    public Integer getProperty() {
        return property;
    }

    @Override
    public Boolean setSingleProperty(Integer property) {
        if (property == null) {
            return false;
        }
        this.property = property;
        return true;
    }

    @Override
    public Boolean modifyByPercentage(Float percentage) {
        property = Math.round(property * (percentage + Constants.PERCENTAGE_CAP_FLOAT));
        return true;
    }

    @Override
    public Boolean modifyByAddition(Integer property) {
        this.property += property;
        return true;
    }
}
