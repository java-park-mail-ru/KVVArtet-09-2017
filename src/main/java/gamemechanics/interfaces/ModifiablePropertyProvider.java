package gamemechanics.interfaces;

import gamemechanics.components.properties.Property;

import java.util.List;

public interface ModifiablePropertyProvider extends PropertyProvider {

    Boolean addProperty(Integer propertyKind, Property property);
    Boolean removeProperty(Integer propertyKind);

    Boolean setProperty(Integer propertyKind, Integer propertyValue);
    Boolean setProperty(Integer propertyKind, Integer propertyIndex, Integer propertyValue);
    Boolean setProperty(Integer propertyKind, List<Integer> propertyValue);

    Boolean modifyPropertyByPercentage(Integer propertyKind, Float percentage);
    Boolean modifyPropertyByAddition(Integer propertyKind, Integer toAdd);
}
