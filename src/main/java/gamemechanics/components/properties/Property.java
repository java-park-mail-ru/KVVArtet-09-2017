package gamemechanics.components.properties;

import java.util.List;
import java.util.Map;

public interface Property {
    default Integer getProperty() {
        return 0;
    }

    default Integer getProperty(Integer propertyIndex) {
        return 0;
    }

    default Boolean setSingleProperty(Integer property) {
        return false;
    }

    default List<Integer> getPropertyList() {
        return null;
    }

    default Boolean setPropertyList(List<Integer> properties) {
        return false;
    }

    default Boolean setSingleProperty(Integer propertyIndex, Integer property) {
        return false;
    }

    default Map<Integer, Integer> getPropertyMap() {
        return null;
    }

    default Boolean setPropertyMap(Map<Integer, Integer> property) {
        return false;
    }

    default Boolean modifyByPercentage(Float percentage) {
        return false;
    }

    default Boolean modifyByPercentage(Integer itemIndex, Float percentage) {
        return false;
    }

    default Boolean modifyByAddition(Integer toAdd) {
        return false;
    }

    default Boolean modifyByAddition(Integer propertyIndex, Integer toAdd) {
        return false;
    }
}
