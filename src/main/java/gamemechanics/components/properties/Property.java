package gamemechanics.components.properties;

import java.util.List;

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


}
