package gamemechanics.interfaces;

import java.util.Set;

/**
 * An interface providing access to entity's properties
 * @see gamemechanics.components.properties.Property
 */
public interface PropertyProvider {
    /**
     * check if entity has any {@link gamemechanics.components.properties.Property} registered
     * under by given ID
     * @param propertyKind property ID to check
     * @return true if there's some property registered under such ID or false otherwise
     */
    Boolean hasProperty(Integer propertyKind);

    /**
     * get all registered property IDs for the entity
     * @return set of registered properties' IDs
     */
    Set<Integer> getAvailableProperties();

    /**
     * get single value from multi-value property by its ID
     * @param propertyKind ID of the property to get value from
     * @param propertyIndex index of value to get from the property
     * @return requested value if both IDs are valid
     * or special constant if either at least one of IDs is invalid
     * or the requested property is not a {@link gamemechanics.components.properties.ListProperty}
     * or {@link gamemechanics.components.properties.MapProperty}
     * @see gamemechanics.components.properties.Property
     * @see gamemechanics.components.properties.ListProperty
     * @see gamemechanics.components.properties.MapProperty
     */
    Integer getProperty(Integer propertyKind, Integer propertyIndex);

    /**
     * get a single-value property's value by iD
     * @param propertyIndex ID of the property to get the value from
     * @return requested property's value if ID is valid
     * and the property is {@link gamemechanics.components.properties.SingleValueProperty}
     * or a special constant otherwise
     * @see gamemechanics.components.properties.Property
     * @see gamemechanics.components.properties.SingleValueProperty
     */
    Integer getProperty(Integer propertyIndex);
}
