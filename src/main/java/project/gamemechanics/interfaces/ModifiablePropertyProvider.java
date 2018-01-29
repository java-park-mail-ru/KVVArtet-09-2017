package project.gamemechanics.interfaces;

import project.gamemechanics.components.properties.ListProperty;
import project.gamemechanics.components.properties.MapProperty;
import project.gamemechanics.components.properties.Property;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * {@link PropertyProvider} extension allowing to add, set and modify properties.
 * after their initialization and assigning to the object
 *
 * @see PropertyProvider
 * @see Property
 */
@SuppressWarnings("unused")
public interface ModifiablePropertyProvider extends PropertyProvider {

    /**
     * add new {@link Property} under a given index.
     *
     * @param propertyKind index to register the property under
     * @param property     property to add
     *                     or false if there's already a property registered under such index
     * @see Property
     */
    void addProperty(@NotNull Integer propertyKind, @NotNull Property property);

    /**
     * remove {@link Property} by index.
     *
     * @param propertyKind property index to remove
     *                     or false if there're no properties registered under such index
     * @see Property
     */
    void removeProperty(@NotNull Integer propertyKind);

    /**
     * set a new value for the single-value {@link Property} by its index.
     *
     * @param propertyKind  index of property to set a new value to
     * @param propertyValue value to set
     *                      or false if there's either no property registered under such index
     *                      or the property registered by that index
     *                      is not a {@link project.gamemechanics.components.properties.SingleValueProperty}
     * @see Property
     * @see project.gamemechanics.components.properties.SingleValueProperty
     */
    void setProperty(@NotNull Integer propertyKind, @NotNull Integer propertyValue);

    /**
     * set a new value for the multi-value {@link Property} by the property ID and
     * the value index in it.
     *
     * @param propertyKind  property ID to change
     * @param propertyIndex index of the value in the chosen property
     * @param propertyValue value to set
     *                      or false if there's no property registered under such index
     *                      or the property registered under such index is not
     *                      a {@link ListProperty}
     *                      or {@link MapProperty}
     * @see Property
     * @see ListProperty
     * @see MapProperty
     */
    void setProperty(@NotNull Integer propertyKind,
                     @NotNull Integer propertyIndex,
                     @NotNull Integer propertyValue);

    /**
     * set a {@link List} of values to the multi-value property by its index.
     *
     * @param propertyKind  property ID to set the list to
     * @param propertyValue values list to set
     *                      or false if there's either no property registered under such index
     *                      or the property registered under such index is not a {@link ListProperty}
     * @see Property
     * @see ListProperty
     */
    void setProperty(@NotNull Integer propertyKind, @NotNull List<Integer> propertyValue);

    /**
     * set a {@link Map} of values to the multi-value property by its index.
     *
     * @param propertyKind  property ID to set the map to
     * @param propertyValue values map to set
     * @return true if the operation was successful
     *     or false if there's either no such property registered under such index
     *     or the property registered under such index is not a {@link MapProperty}
     * @see Property
     * @see MapProperty
     */
    @NotNull Boolean setProperty(@NotNull Integer propertyKind,
                                 @NotNull Map<Integer, Integer> propertyValue);

    /**
     * modify property value(s) by the given percentage.
     *
     * @param propertyKind ID of the property to modify
     * @param percentage   percentage to modify the property's values on
     *                     (note: there's a 1.0f + percentage value used to modify the property value(s)
     *                     so if you want e.g. to increase the property value on 3% you shall pass
     *                     0.03f as an argument, not a 1.03f)
     * @return true if the operation was successful
     *     or false if there's no property registered under such index
     * @see Property
     */
    @NotNull Boolean modifyPropertyByPercentage(@NotNull Integer propertyKind,
                                                @NotNull Float percentage);

    /**
     * modify a single value in multi-value property by the given percentage.
     *
     * @param propertyKind  ID of the property to modify
     * @param propertyIndex index of the value in the property to modify
     * @param percentage    percentage to modify the property's value on
     *                      (note: there's a 1.0f + percentage value used to modify the property value(s)
     *                      so if you want e.g. to increase the property value on 3% you shall pass
     *                      0.03f as an argument, not a 1.03f)
     * @return true if the operation was successful
     *     or false if there's either no property registered under such index
     *     or the property registered under such index if not a {@link ListProperty}
     *     or {@link MapProperty}
     * @see Property
     * @see ListProperty
     * @see MapProperty
     */
    @NotNull Boolean modifyPropertyByPercentage(@NotNull Integer propertyKind,
                                                @NotNull Integer propertyIndex,
                                                @NotNull Float percentage);

    /**
     * modify property value(s) by the given percentage.
     *
     * @param propertyKind ID of the property to modify
     * @param toAdd        amount to modify property's value(s) on
     *                     or false if there's no property registered under such index
     * @see Property
     */
    void modifyPropertyByAddition(@NotNull Integer propertyKind, @NotNull Integer toAdd);

    /**
     * modify property value by the given percentage.
     *
     * @param propertyKind  ID of the property to modify
     * @param propertyIndex index of the value in the property to modify
     * @param toAdd         amount to modify property's value on
     *                      or false if there's either no property registered under such index
     *                      or the property registered under such index is not a {@link ListProperty}
     *                      or {@link MapProperty}
     * @see Property
     * @see ListProperty
     * @see MapProperty
     */
    void modifyPropertyByAddition(@NotNull Integer propertyKind,
                                  @NotNull Integer propertyIndex,
                                  @NotNull Integer toAdd);
}
