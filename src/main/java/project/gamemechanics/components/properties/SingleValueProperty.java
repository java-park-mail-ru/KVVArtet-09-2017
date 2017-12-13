package project.gamemechanics.components.properties;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import project.gamemechanics.globals.Constants;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SingleValueProperty implements Property {
    private Integer property;

    public SingleValueProperty(@JsonProperty("property") @NotNull Integer property) {
        this.property = property;
    }

    @Override
    @JsonProperty("property")
    public Integer getProperty() {
        return property;
    }

    @Override
    @JsonSetter("property")
    public void setSingleProperty(@NotNull Integer property) {
        if (property == null) {
            return;
        }
        this.property = property;
    }

    @Override
    public Boolean modifyByPercentage(@NotNull Float percentage) {
        property = Math.round(property * (percentage + Constants.PERCENTAGE_CAP_FLOAT));
        return true;
    }

    @SuppressWarnings("ParameterHidesMemberVariable")
    @Override
    public void modifyByAddition(@NotNull Integer property) {
        this.property += property;
    }

    @Override
    @JsonIgnore
    public void setSingleProperty(@NotNull Integer propertyIndex, @NotNull Integer propertyValue) {
    }

    @Override
    @JsonIgnore
    public Map<Integer, Integer> getPropertyMap() {
        return null;
    }

    @Override
    @JsonIgnore
    public Boolean setPropertyMap(@NotNull Map<Integer, Integer> properties) {
        return false;
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
    public Integer getProperty(@NotNull Integer propertyIndex) {
        return 0;
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

    @Override
    public Boolean modifyByPercentage(@NotNull Integer propertyIndex, @NotNull Float percentage) {
        return false;
    }

    @Override
    public Boolean modifyByAddition(@NotNull Integer propertyIndex, @NotNull Integer toAdd) {
        return false;
    }
}
