package project.gamemechanics.components.properties;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.jetbrains.annotations.Nullable;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unused")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(SingleValueProperty.class),
        @JsonSubTypes.Type(ListProperty.class),
        @JsonSubTypes.Type(MapProperty.class),
        @JsonSubTypes.Type(SetProperty.class),
})
public interface Property {
    @NotNull Integer getProperty();

    @NotNull Integer getProperty(@NotNull Integer propertyIndex);

    void setSingleProperty(@NotNull Integer property);

    void setSingleProperty(@NotNull Integer propertyIndex, @NotNull Integer property);

    @Nullable List<Integer> getPropertyList();

    void setPropertyList(@NotNull List<Integer> properties);

    @Nullable Map<Integer, Integer> getPropertyMap();

    @NotNull Boolean setPropertyMap(@NotNull Map<Integer, Integer> property);

    @Nullable Set<Integer> getPropertySet();

    @NotNull Boolean setPropertySet(@NotNull Set<Integer> property);

    @NotNull Boolean modifyByPercentage(@NotNull Float percentage);

    @NotNull Boolean modifyByPercentage(@NotNull Integer itemIndex, @NotNull Float percentage);

    void modifyByAddition(@NotNull Integer toAdd);

    @NotNull Boolean modifyByAddition(@NotNull Integer propertyIndex, @NotNull Integer toAdd);
}

