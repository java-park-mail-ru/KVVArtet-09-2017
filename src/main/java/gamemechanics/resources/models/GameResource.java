package gamemechanics.resources.models;

import gamemechanics.components.affectors.Affector;
import gamemechanics.components.properties.Property;
import gamemechanics.interfaces.GameEntity;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface GameResource extends  GameEntity {
    default Boolean hasProperty(Integer propertyKind) {
        return false;
    }

    default Set<Integer> getAvailableProperties() {
        return null;
    }

    default Map<Integer, Property> getAllProperties() {
        return null;
    }

    default Property getProperty(@NotNull Integer propertyIndex) {
        return null;
    }

    default Boolean hasAffector(Integer propertyKind) {
        return false;
    }

    default Set<Integer> getAvailableAffectors() {
        return null;
    }

    default Map<Integer, Affector> getAllAffectors() {
        return null;
    }

    default Affector getAffector(@NotNull Integer affectorIndex) {
        return null;
    }

    default void scaleToLevel(Integer level) {
    }

    default Boolean hasMapping(@NotNull Integer mappingIndex) {
        return false;
    }

    default Set<Integer> getAvailableMappings() {
        return null;
    }

    default List<Integer> getMapping(@NotNull Integer mappingIndex) {
        return null;
    }

    default Map<Integer, List<Integer>> getAllMappings() {
        return null;
    }

    default GameResource getInlaid(@NotNull Integer inlaidIndex) {
        return null;
    }

    default List<GameResource> getAllInlaid() {
        return null;
    }
}
