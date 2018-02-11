package project.gamemechanics.components.properties;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.jetbrains.annotations.Nullable;
import project.gamemechanics.globals.Constants;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("RedundantSuppression")
public class MapProperty implements Property {
    private final Map<Integer, Integer> properties;

    public MapProperty(@JsonProperty("properties") @NotNull Map<Integer, Integer> properties) {
        this.properties = properties;
    }

    @Override
    @JsonIgnore
    public @NotNull Integer getProperty() {
        return 0;
    }

    @Override
    public @NotNull Integer getProperty(@NotNull Integer propertyIndex) {
        if (!properties.containsKey(propertyIndex)) {
            return Constants.WRONG_INDEX;
        }
        return properties.get(propertyIndex);
    }

    @Override
    @JsonProperty("properties")
    public @NotNull Map<Integer, Integer> getPropertyMap() {
        return properties;
    }
    // CHECKSTYLE:OFF
    @SuppressWarnings("ParameterHidesMemberVariable")
    @Override
    @JsonSetter("properties")
    public @NotNull Boolean setPropertyMap(@NotNull Map<Integer, Integer> properties) {
        if (properties.isEmpty()) {
            return false;
        }
        this.properties.clear();
        for (Integer key : properties.keySet()) {
            this.properties.put(key, properties.get(key));
        }
        return true;
    }

    @Override
    @JsonIgnore
    public void setSingleProperty(@NotNull Integer propertyIndex, @NotNull Integer propertyValue) {
        if (properties.containsKey(propertyIndex)) {
            properties.replace(propertyIndex, propertyValue);
        } else {
            properties.put(propertyIndex, propertyValue);
        }
    }

    @Override
    @JsonIgnore
    public void setSingleProperty(@NotNull Integer propertyValue) {
    }

    @Override
    public void modifyByAddition(@NotNull Integer toAdd) {
        for (Integer key : properties.keySet()) {
            final Boolean result = modifyByAddition(key, toAdd);
            if (!result) {
                return;
            }
        }
    }

    @Override
    public @NotNull Boolean modifyByAddition(@NotNull Integer propertyIndex, @NotNull Integer toAdd) {
        if (!properties.containsKey(propertyIndex)) {
            return false;
        }
        properties.replace(propertyIndex, properties.get(propertyIndex) + toAdd);
        return true;
    }

    @Override
    public @NotNull Boolean modifyByPercentage(@NotNull Integer propertyIndex, @NotNull Float percentage) {
        if (!properties.containsKey(propertyIndex)) {
            return false;
        }
        properties.replace(propertyIndex, Math.round(properties.get(propertyIndex).floatValue()
                * (Constants.PERCENTAGE_CAP_FLOAT + percentage)));
        return true;
    }

    @Override
    public @NotNull Boolean modifyByPercentage(@NotNull Float percentage) {
        for (Integer key : properties.keySet()) {
            final Boolean result = modifyByPercentage(key, percentage);
            if (!result) {
                return result;
            }
        }
        return true;
    }


    @SuppressWarnings("ConstantConditions")
    @Override
    @JsonIgnore
    public @Nullable List<Integer> getPropertyList() {
        return null;
    }

    @Override
    @JsonIgnore
    public void setPropertyList(@NotNull List<Integer> properties) {
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    @JsonIgnore
    public @Nullable Set<Integer> getPropertySet() {
        return null;
    }

    @SuppressWarnings("ParameterHidesMemberVariable")
    @Override
    @JsonIgnore
    public @NotNull Boolean setPropertySet(@NotNull Set<Integer> properties) {
        return false;
    }
}
// CHECKSTYLE:ON