package project.gamemechanics.components.properties;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.Objects;

public class AbilitiesCooldownProperty extends MapProperty {
    public AbilitiesCooldownProperty(@JsonProperty("propertyMap") @NotNull Map<Integer, Integer> properties) {
        super(properties);
    }

    @Override
    public void modifyByAddition(@NotNull Integer toAdd) {
        for (Integer key : Objects.requireNonNull(getPropertyMap()).keySet()) {
            if (getPropertyMap().get(key) > 0) {
                getPropertyMap().replace(key, getPropertyMap().get(key) + toAdd);
            }
        }
    }
}
