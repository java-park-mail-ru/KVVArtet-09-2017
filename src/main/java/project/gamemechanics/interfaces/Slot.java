package project.gamemechanics.interfaces;

import project.gamemechanics.components.properties.SingleValueProperty;

import javax.validation.constraints.NotNull;
import java.util.Map;

@SuppressWarnings("unused")
public interface Slot extends PropertyProvider {

    Map<Integer, SingleValueProperty> getAllProperties();

    @NotNull Integer getPropertyValueByKind(@NotNull Integer propertyKind);

    void resetProperties(Integer itemId, Integer itemQuantity);

}
