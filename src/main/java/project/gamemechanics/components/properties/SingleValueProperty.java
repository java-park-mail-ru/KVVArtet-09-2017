package project.gamemechanics.components.properties;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import project.gamemechanics.globals.Constants;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("RedundantSuppression")
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
    public Integer getProperty(@NotNull Integer propertyIndex) {
        return 0;
    }
    // CHECKSTYLE:OFF

    @Override
    @JsonSetter("property")
    public void setSingleProperty(@NotNull Integer property) {
        if (property == null) {
            return;
        }
        this.property = property;
    }

    @JsonIgnore
    @Override
    public void setSingleProperty(@NotNull Integer propertyIndex, @NotNull Integer propertyValue) {
    }

    @Override
    public Boolean modifyByPercentage(@NotNull Float percentage) {
        property = Math.round(property * (percentage + Constants.PERCENTAGE_CAP_FLOAT));
        return true;
    }

    @Override
    public Boolean modifyByPercentage(@NotNull Integer propertyIndex, @NotNull Float percentage) {
        return false;
    }

    @Override
    @SuppressWarnings("ParameterHidesMemberVariable")
    public void modifyByAddition(@NotNull Integer property) {
        this.property += property;
    }
    // CHECKSTYLE:ON

    @Override
    public Boolean modifyByAddition(@NotNull Integer propertyIndex, @NotNull Integer toAdd) {
        return false;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    @JsonIgnore
    public Map<Integer, Integer> getPropertyMap() {
        return null;
    }

    @JsonIgnore
    @Override
    public Boolean setPropertyMap(@NotNull Map<Integer, Integer> properties) {
        return false;
    }

    @SuppressWarnings("ConstantConditions")
    @JsonIgnore
    @Override
    public List<Integer> getPropertyList() {
        return null;
    }

    @JsonIgnore
    @Override
    public void setPropertyList(@NotNull List<Integer> properties) {
    }

    @SuppressWarnings("ConstantConditions")
    @JsonIgnore
    @Override
    public Set<Integer> getPropertySet() {
        return null;
    }

    @JsonIgnore
    @Override
    public Boolean setPropertySet(@NotNull Set<Integer> properties) {
        return false;
    }
}
