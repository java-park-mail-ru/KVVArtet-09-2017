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

}
