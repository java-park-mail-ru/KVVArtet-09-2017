package project.gamemechanics.components.properties;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import project.gamemechanics.globals.Constants;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SetProperty implements Property {
    private final Set<Integer> properties;

    public SetProperty(@JsonProperty("properties") @NotNull Set<Integer> properties) {
        this.properties = properties;
    }

    @Override
    public Integer getProperty(@NotNull Integer propertyIndex) {
        if (!properties.contains(propertyIndex)) {
            return Constants.WRONG_INDEX;
        }
        return propertyIndex;
    }

    @Override
    @JsonIgnore
    public void setSingleProperty(@NotNull Integer propertyValue) {
    }

    @Override
    @JsonIgnore
    public void setSingleProperty(@NotNull Integer propertyIndex, @NotNull Integer propertyValue) {
    }

    @Override
    @JsonProperty("properties")
    public Set<Integer> getPropertySet() {
        return properties;
    }

    @SuppressWarnings("ParameterHidesMemberVariable")
    @Override
    @JsonSetter("properties")
    public Boolean setPropertySet(@NotNull Set<Integer> properties) {
        if (properties == null) {
            return false;
        }
        this.properties.clear();
        this.properties.addAll(properties);
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
    public List<Integer> getPropertyList() {
        return null;
    }

    @Override
    @JsonIgnore
    public void setPropertyList(@NotNull List<Integer> properties) {
    }

    @Override
    @JsonIgnore
    public Integer getProperty() {
        return 0;
    }

    @Override
    @JsonIgnore
    public Boolean modifyByPercentage(@NotNull Float percentage) {
        return false;
    }

    @Override
    @JsonIgnore
    public Boolean modifyByPercentage(@NotNull Integer propertyIndex, @NotNull Float percentage) {
        return false;
    }

    @Override
    @JsonIgnore
    public void modifyByAddition(@NotNull Integer toAdd) {
    }

    @Override
    @JsonIgnore
    public Boolean modifyByAddition(@NotNull Integer propertyIndex, @NotNull Integer toAdd) {
        return false;
    }
}
