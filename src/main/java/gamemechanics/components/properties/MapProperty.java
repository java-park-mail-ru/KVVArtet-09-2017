package gamemechanics.components.properties;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import gamemechanics.globals.Constants;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MapProperty implements Property {
    private final Map<Integer, Integer> properties;

    public MapProperty(@JsonProperty("properties") @NotNull Map<Integer, Integer> properties) {
        this.properties = properties;
    }

    @Override
    @JsonIgnore
    public Integer getProperty() {
        return 0;
    }

    @Override
    public Integer getProperty(@NotNull Integer propertyIndex) {
        if (!properties.containsKey(propertyIndex)) {
            return Constants.WRONG_INDEX;
        }
        return properties.get(propertyIndex);
    }

    @Override
    @JsonProperty("properties")
    public Map<Integer, Integer> getPropertyMap() {
        return properties;
    }

    @Override
    @JsonSetter("properties")
    public Boolean setPropertyMap(@NotNull Map<Integer, Integer> properties) {
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
    public Boolean setSingleProperty(@NotNull Integer propertyIndex, @NotNull Integer propertyValue) {
        if (properties.containsKey(propertyIndex)) {
            properties.replace(propertyIndex, propertyValue);
        } else {
            properties.put(propertyIndex, propertyValue);
        }
        return true;
    }

    @Override
    @JsonIgnore
    public Boolean setSingleProperty(@NotNull Integer propertyValue) {
        return false;
    }

    @Override
    public Boolean modifyByAddition(@NotNull Integer toAdd) {
        for (Integer key : properties.keySet()) {
            Boolean result = modifyByAddition(key, toAdd);
            if (!result) {
                return result;
            }
        }
        return true;
    }

    @Override
    public Boolean modifyByAddition(@NotNull Integer propertyIndex, @NotNull Integer toAdd) {
        if (!properties.containsKey(propertyIndex)) {
            return false;
        }
        properties.replace(propertyIndex, properties.get(propertyIndex) + toAdd);
        return true;
    }

    @Override
    public Boolean modifyByPercentage(@NotNull Integer propertyIndex, @NotNull Float percentage) {
        if (!properties.containsKey(propertyIndex)) {
            return false;
        }
        properties.replace(propertyIndex, Math.round(properties.get(propertyIndex).floatValue()
                * (Constants.PERCENTAGE_CAP_FLOAT + percentage)));
        return true;
    }

    @Override
    public Boolean modifyByPercentage(@NotNull Float percentage) {
        for (Integer key : properties.keySet()) {
            Boolean result = modifyByPercentage(key, percentage);
            if (!result) {
                return result;
            }
        }
        return true;
    }


    @Override
    @JsonIgnore
    public List<Integer> getPropertyList() {
        return null;
    }

    @Override
    @JsonIgnore
    public Boolean setPropertyList(@NotNull List<Integer> properties) {
        return null;
    }

    @Override
    @JsonIgnore
    public Set<Integer> getPropertySet() {
        return null;
    }

    @Override
    @JsonIgnore
    public Boolean setPropertySet(@NotNull Set<Integer> properties) {
        return false;
    }
}
