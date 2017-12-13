package project.gamemechanics.interfaces;

import project.gamemechanics.components.properties.ListProperty;
import project.gamemechanics.components.properties.MapProperty;
import project.gamemechanics.components.properties.Property;

import java.util.Set;

/**
 * An interface providing access to entity's properties
 *
 * @see Property
 */
public interface PropertyProvider {
    /**
     * check if entity has any {@link Property} registered
     * under by given ID
     *
     * @param propertyKind property ID to check
     * @return true if there's some property registered under such ID or false otherwise
     */
    Boolean hasProperty(Integer propertyKind);

    /**
     * get all registered property IDs for the entity
     *
     * @return set of registered properties' IDs
     */
    Set<Integer> getAvailableProperties();

    /**
     * get single value from multi-value property by its ID
     *
     * @param propertyKind  ID of the property to get value from
     * @param propertyIndex index of value to get from the property
     * @return requested value if both IDs are valid
     * or special constant if either at least one of IDs is invalid
     * or the requested property is not a {@link ListProperty}
     * or {@link MapProperty}
     * @see Property
     * @see ListProperty
     * @see MapProperty
     */
    Integer getProperty(Integer propertyKind, Integer propertyIndex);

    /**
     * get a single-value property's value by iD
     *
     * @param propertyIndex ID of the property to get the value from
     * @return requested property's value if ID is valid
     * and the property is {@link project.gamemechanics.components.properties.SingleValueProperty}
     * or a special constant otherwise
     * @see Property
     * @see project.gamemechanics.components.properties.SingleValueProperty
     */
    Integer getProperty(Integer propertyIndex);
}
