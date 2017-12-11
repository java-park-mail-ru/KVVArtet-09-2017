package project.gamemechanics.resources.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
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
})
public interface GameResource extends GameEntity {
    default Boolean hasProperty(@NotNull Integer propertyKind) {
        return false;
    }

    @JsonIgnore
    default Set<Integer> getAvailableProperties() {
        return null;
    }

    Map<Integer, Property> getAllProperties();

    default Property getProperty(@NotNull Integer propertyIndex) {
        return null;
    }

    default Boolean hasAffector(@NotNull Integer propertyKind) {
        return false;
    }

    @JsonIgnore
    default Set<Integer> getAvailableAffectors() {
        return null;
    }

    Map<Integer, Affector> getAllAffectors();

    default Affector getAffector(@NotNull Integer affectorIndex) {
        return null;
    }

    default void scaleToLevel(Integer level) {
    }

    default Boolean hasMapping(@NotNull Integer mappingIndex) {
        return false;
    }

    @JsonIgnore
    default Set<Integer> getAvailableMappings() {
        return null;
    }

    default List<Integer> getMapping(@NotNull Integer mappingIndex) {
        return null;
    }

    Map<Integer, List<Integer>> getAllMappings();

    default GameResource getInlaid(@NotNull Integer inlaidIndex) {
        return null;
    }

    List<GameResource> getAllInlaid();
}
