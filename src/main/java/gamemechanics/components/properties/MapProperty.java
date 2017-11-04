package gamemechanics.components.properties;

import gamemechanics.globals.Constants;

import java.util.Map;

public class MapProperty implements Property {
    private final Map<Integer, Integer> properties;

    public MapProperty(Map<Integer, Integer> properties) {
        this.properties = properties;
    }

    @Override
    public Integer getProperty(Integer propertyIndex) {
        if (!properties.containsKey(propertyIndex)) {
            return Constants.WRONG_INDEX;
        }
        return properties.get(propertyIndex);
    }

    @Override
    public Map<Integer, Integer> getPropertyMap() {
        return properties;
    }

    @Override
    public Boolean setPropertyMap(Map<Integer, Integer> properties) {
        if (properties.isEmpty()) {
            return false;
        }
        this.properties.clear();
        for (Integer key : properties.keySet()) {
            this.properties.put(key, properties.get(key));
        }
        return true;
    }

    @Override
    public Boolean setSingleProperty(Integer propertyIndex, Integer propertyValue) {
        if (properties.containsKey(propertyIndex)) {
            properties.replace(propertyIndex, propertyValue);
        } else {
            properties.put(propertyIndex, propertyValue);
        }
        return true;
    }

    @Override
    public Boolean modifyByAddition(Integer toAdd) {
        for (Integer key : properties.keySet()) {
            Boolean result = modifyByAddition(key, toAdd);
            if (!result) {
                return result;
            }
        }
        return true;
    }

    @Override
    public Boolean modifyByAddition(Integer propertyIndex, Integer toAdd) {
        if (!properties.containsKey(propertyIndex)) {
            return false;
        }
        properties.replace(propertyIndex, properties.get(propertyIndex) + toAdd);
        return true;
    }

    @Override
    public Boolean modifyByPercentage(Integer propertyIndex, Float percentage) {
        if (!properties.containsKey(propertyIndex)) {
            return false;
        }
        properties.replace(propertyIndex, Math.round(properties.get(propertyIndex).floatValue()
                * (Constants.PERCENTAGE_CAP_FLOAT + percentage)));
        return true;
    }

    @Override
    public Boolean modifyByPercentage(Float percentage) {
        for (Integer key : properties.keySet()) {
            Boolean result = modifyByPercentage(key, percentage);
            if (!result) {
                return result;
            }
        }
        return true;
    }
}
