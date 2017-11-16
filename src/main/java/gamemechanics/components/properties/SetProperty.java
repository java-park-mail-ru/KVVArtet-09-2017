package gamemechanics.components.properties;

import gamemechanics.globals.Constants;

import java.util.Set;

public class SetProperty implements Property {
    private final Set<Integer> properties;

    public SetProperty(Set<Integer> properties) {
        this.properties = properties;
    }

    @Override
    public Integer getProperty(Integer propertyIndex) {
        if (!properties.contains(propertyIndex)) {
            return Constants.WRONG_INDEX;
        }
        return propertyIndex;
    }

    @Override
    public Set<Integer> getPropertySet() {
        return properties;
    }

    @Override
    public Boolean setPropertySet(Set<Integer> properties) {
        if (properties == null) {
            return false;
        }
        this.properties.clear();
        this.properties.addAll(properties);
        return true;
    }
}
