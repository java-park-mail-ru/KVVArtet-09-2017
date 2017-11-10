package gamemechanics.interfaces;

import gamemechanics.components.affectors.Affector;

import java.util.List;
import java.util.Map;

/**
 * interface to store {@link Ability}'s behavior in
 * @see Ability
 */
public interface AbilityEffect {
    /**
     * execute the behavior
     * @param caster ability caster
     * @param target target map node
     * @param affectors various ability properties' modificators
     * @param effectList actual effects' list to apply on target(s)
     * @return true if the execution was successful or false otherwise
     */
    Boolean execute(AliveEntity caster, MapNode target, Map<Integer, Affector> affectors,
                    List<Effect> effectList);
}
