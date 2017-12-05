package project.gamemechanics.components.properties;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import java.util.Map;

public class AbilitiesCooldownProperty extends MapProperty {
    public AbilitiesCooldownProperty(@JsonProperty("propertyMap") @NotNull Map<Integer, Integer> properties) {
        super(properties);
    }

    @Override
    public void modifyByAddition(@NotNull Integer toAdd) {
        for (Integer key : getPropertyMap().keySet()) {
            if (getPropertyMap().get(key) > 0) {
                getPropertyMap().replace(key, getPropertyMap().get(key) + toAdd);
            }
        }
    }
}
