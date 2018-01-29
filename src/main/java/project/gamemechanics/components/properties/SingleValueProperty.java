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
public class SingleValueProperty implements Property {
    private Integer property;

    public SingleValueProperty(@JsonProperty("property") @NotNull Integer property) {
        this.property = property;
    }

    @Override
    @JsonProperty("property")
    public @NotNull Integer getProperty() {
        return property;
    }

    @Override
    public @NotNull Integer getProperty(@NotNull Integer propertyIndex) {
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
    public @NotNull Boolean modifyByPercentage(@NotNull Float percentage) {
        property = Math.round(property * (percentage + Constants.PERCENTAGE_CAP_FLOAT));
        return true;
    }

    @Override
    public @NotNull Boolean modifyByPercentage(@NotNull Integer propertyIndex, @NotNull Float percentage) {
        return false;
    }

    @Override
    @SuppressWarnings("ParameterHidesMemberVariable")
    public void modifyByAddition(@NotNull Integer property) {
        this.property += property;
    }
    // CHECKSTYLE:ON

    @Override
    public @NotNull Boolean modifyByAddition(@NotNull Integer propertyIndex, @NotNull Integer toAdd) {
        return false;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    @JsonIgnore
    public @Nullable Map<Integer, Integer> getPropertyMap() {
        return null;
    }

    @JsonIgnore
    @Override
    public @NotNull Boolean setPropertyMap(@NotNull Map<Integer, Integer> properties) {
        return false;
    }

    @SuppressWarnings("ConstantConditions")
    @JsonIgnore
    @Override
    public @Nullable List<Integer> getPropertyList() {
        return null;
    }

    @JsonIgnore
    @Override
    public void setPropertyList(@NotNull List<Integer> properties) {
    }

    @SuppressWarnings("ConstantConditions")
    @JsonIgnore
    @Override
    public @Nullable Set<Integer> getPropertySet() {
        return null;
    }

    @JsonIgnore
    @Override
    public @NotNull Boolean setPropertySet(@NotNull Set<Integer> properties) {
        return false;
    }
}
