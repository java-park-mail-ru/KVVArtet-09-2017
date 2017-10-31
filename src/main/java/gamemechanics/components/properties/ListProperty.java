package gamemechanics.components.properties;

import gamemechanics.globals.Constants;

import java.util.List;

public class ListProperty implements Property {
    private final List<Integer> properties;

    public ListProperty(List<Integer> properties) {
        this.properties = properties;
    }

    @Override
    public Integer getProperty(Integer propertyIndex) {
        if (propertyIndex == null) {
            return Constants.WRONG_INDEX;
        }
        if (propertyIndex < 0 || propertyIndex >= properties.size()) {
            return Constants.WRONG_INDEX;
        }
        return properties.get(propertyIndex);
    }

    @Override
    public List<Integer> getPropertyList() {
        return properties;
    }

    @Override
    public Boolean setPropertyList(List<Integer> property) {
        if (property == null) {
            return false;
        }
        if (property.size() != properties.size()) {
            return false;
        }
        for (Integer i = 0; i < properties.size(); ++i) {
            properties.set(i, property.get(i));
        }
        return true;
    }

    @Override
    public Boolean setSingleProperty(Integer propertyIndex, Integer property) {
        if (propertyIndex < 0 || property >= properties.size()) {
            return false;
        }
        properties.set(propertyIndex, property);
        return true;
    }

    @Override
    public Boolean modifyByPercentage(Float percentage) {
        for (Integer propertyIndex = 0; propertyIndex < properties.size(); ++propertyIndex) {
            Integer oldValue = properties.get(propertyIndex);
            Float resultPercentage = percentage + Constants.PERCENTAGE_CAP_FLOAT;
            properties.set(propertyIndex, Math.round(oldValue * resultPercentage));
        }
        return true;
    }

    @Override
    public Boolean modifyByAddition(Integer toAdd) {
        for (Integer propertyIndex = 0; propertyIndex < properties.size(); ++propertyIndex) {
            properties.set(propertyIndex, properties.get(propertyIndex) + toAdd);
        }
        return true;
    }
}
