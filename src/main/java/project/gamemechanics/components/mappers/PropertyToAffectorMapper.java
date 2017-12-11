package project.gamemechanics.components.mappers;

import project.gamemechanics.components.affectors.AffectorCategories;
import project.gamemechanics.components.properties.PropertyCategories;
import project.gamemechanics.globals.Constants;

import java.util.HashMap;
import java.util.Map;

public final class PropertyToAffectorMapper {
    private PropertyToAffectorMapper() {
    }

    private static final Map<Integer, Integer> PROPERTY_AFFECTOR_MAPPING = initMap();

    public static Integer getAffectorKind(Integer propertyKind) {
        if (!PROPERTY_AFFECTOR_MAPPING.containsKey(propertyKind)) {
            return Constants.WRONG_INDEX;
        }
        return PROPERTY_AFFECTOR_MAPPING.get(propertyKind);
    }

    private static Map<Integer, Integer> initMap() {
        final Map<Integer, Integer> mapping = new HashMap<>();
        mapping.put(PropertyCategories.PC_STATS, AffectorCategories.AC_STATS_AFFECTOR);
        mapping.put(PropertyCategories.PC_RATINGS, AffectorCategories.AC_RATINGS_AFFECTOR);
        mapping.put(PropertyCategories.PC_HITPOINTS, AffectorCategories.AC_HEALTH_AFFECTOR);
        mapping.put(PropertyCategories.PC_BASE_DAMAGE, AffectorCategories.AC_DAMAGE_AFFECTOR);
        mapping.put(PropertyCategories.PC_BASE_DEFENSE, AffectorCategories.AC_DEFENSE_AFFECTOR);
        return mapping;
    }
}
