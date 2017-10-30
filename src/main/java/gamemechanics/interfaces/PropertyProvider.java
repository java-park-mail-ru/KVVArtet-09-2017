package gamemechanics.interfaces;

import java.util.Set;

public interface PropertyProvider {
    Boolean hasProperty(Integer propertyKind);
    Set<Integer> getAvailableProperties();

    Integer getProperty(Integer propertyKind, Integer propertyIndex);
    Integer getProperty(Integer propertyIndex);
}
