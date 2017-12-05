package project.gamemechanics.components.properties;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import project.gamemechanics.globals.Constants;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ListProperty implements Property {
    private final List<Integer> properties;

    public ListProperty(@JsonProperty("properties") @NotNull List<Integer> properties) {
        this.properties = properties;
    }

    @Override
    @JsonIgnore
    public Integer getProperty() {
        return 0;
    }

    @Override
    public Integer getProperty(@NotNull Integer propertyIndex) {
        if (propertyIndex == null) {
            return Constants.WRONG_INDEX;
        }
        if (propertyIndex < 0 || propertyIndex >= properties.size()) {
            return Constants.WRONG_INDEX;
        }
        return properties.get(propertyIndex);
    }

    @Override
    @JsonProperty("properties")
    public List<Integer> getPropertyList() {
        return properties;
    }

    @Override
    @JsonSetter("properties")
    public void setPropertyList(@NotNull List<Integer> property) {
        if (property == null) {
            return;
        }
        if (property.size() != properties.size()) {
            return;
        }
        for (Integer i = 0; i < properties.size(); ++i) {
            properties.set(i, property.get(i));
        }
    }

    @Override
    @JsonIgnore
    public void setSingleProperty(@NotNull Integer proeprtyValue) {
    }

    @Override
    @JsonIgnore
    public void setSingleProperty(@NotNull Integer propertyIndex, @NotNull Integer property) {
        if (propertyIndex < 0 || property >= properties.size()) {
            return;
        }
        properties.set(propertyIndex, property);
    }

    @Override
    public Boolean modifyByPercentage(Float percentage) {
        for (Integer propertyIndex = 0; propertyIndex < properties.size(); ++propertyIndex) {
            final Boolean result = modifyByPercentage(propertyIndex, percentage);
            if (!result) {
                return result;
            }
        }
        return true;
    }

    @Override
    public Boolean modifyByPercentage(Integer propertyIndex, Float percentage) {
        if (propertyIndex < 0 || propertyIndex >= properties.size()) {
            return false;
        }
        final Integer oldValue = properties.get(propertyIndex);
        final Float resultPercentage = percentage + Constants.PERCENTAGE_CAP_FLOAT;
        properties.set(propertyIndex, Math.round(oldValue * resultPercentage));
        return true;
    }

    @Override
    public void modifyByAddition(Integer toAdd) {
        for (Integer propertyIndex = 0; propertyIndex < properties.size(); ++propertyIndex) {
            final Boolean result = modifyByAddition(propertyIndex, toAdd);
            if (!result) {
                return;
            }
        }
    }

    @Override
    public Boolean modifyByAddition(Integer propertyIndex, Integer toAdd) {
        if (propertyIndex < 0 || propertyIndex >= properties.size()) {
            return false;
        }
        properties.set(propertyIndex, properties.get(propertyIndex) + toAdd);
        return true;
    }

    @Override
    @JsonIgnore
    public Map<Integer, Integer> getPropertyMap() {
        return null;
    }

    @SuppressWarnings("ParameterHidesMemberVariable")
    @Override
    @JsonIgnore
    public Boolean setPropertyMap(@NotNull Map<Integer, Integer> properties) {
        return null;
    }

    @Override
    @JsonIgnore
    public Set<Integer> getPropertySet() {
        return null;
    }

    @SuppressWarnings("ParameterHidesMemberVariable")
    @Override
    @JsonIgnore
    public Boolean setPropertySet(@NotNull Set<Integer> properties) {
        return false;
    }
}
