package project.gamemechanics.resources.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.jetbrains.annotations.Nullable;
import project.gamemechanics.components.affectors.Affector;
import project.gamemechanics.components.properties.Property;
import project.gamemechanics.interfaces.GameEntity;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.Set;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(BasicModel.class),
        @JsonSubTypes.Type(ForeignKeyModel.class),
        @JsonSubTypes.Type(HybridModel.class),
        @JsonSubTypes.Type(HybridModelWithInlays.class),
        @JsonSubTypes.Type(InstanceNameDescription.class),
})
public interface GameResource extends GameEntity {
    default Boolean hasProperty(@NotNull Integer propertyKind) {
        return false;
    }

    @JsonIgnore
    default @Nullable Set<Integer> getAvailableProperties() {
        return null;
    }

    @Nullable Map<Integer, Property> getAllProperties();

    default @Nullable Property getProperty(@NotNull Integer propertyIndex) {
        return null;
    }

    default Boolean hasAffector(@NotNull Integer propertyKind) {
        return false;
    }

    @JsonIgnore
    default @Nullable Set<Integer> getAvailableAffectors() {
        return null;
    }

    @Nullable Map<Integer, Affector> getAllAffectors();

    default @Nullable Affector getAffector(@NotNull Integer affectorIndex) {
        return null;
    }

    default void scaleToLevel(@NotNull Integer level) {
    }

    default Boolean hasMapping(@NotNull Integer mappingIndex) {
        return false;
    }

    @JsonIgnore
    default @Nullable Set<Integer> getAvailableMappings() {
        return null;
    }

    default @Nullable List<Integer> getMapping(@NotNull Integer mappingIndex) {
        return null;
    }

    @Nullable Map<Integer, List<Integer>> getAllMappings();

    default @Nullable GameResource getInlaid(@NotNull Integer inlaidIndex) {
        return null;
    }

    @Nullable List<GameResource> getAllInlaid();
}
