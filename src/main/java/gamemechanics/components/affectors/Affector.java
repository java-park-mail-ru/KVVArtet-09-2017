package gamemechanics.components.affectors;

import java.util.List;
import java.util.Map;

public interface Affector {
    default Integer getAffection(Integer affectionIndex) {
        return 0;
    }

    default Integer getAffection() {
        return 0;
    }

    default Boolean setSingleAffection(Integer affection) {
        return false;
    }

    default List<Integer> getAffectionsList() {
        return null;
    }

    default Boolean setAffectionsList(List<Integer> affections) {
        return false;
    }

    default Map<Integer, Integer> getAffectionsMap() {
        return null;
    }

    default Boolean setAffectionsMap(Map<Integer, Integer> affections) {
        return false;
    }

    default Boolean modifyByPercentage(Float percentage) {
        return false;
    }

    default Boolean modifyByAddition(Integer toAdd) {
        return false;
    }
}
