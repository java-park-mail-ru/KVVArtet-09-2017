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
            properties.replace(key, properties.get(key) + toAdd);
        }
        return true;
    }
}
