package gamemechanics.interfaces;

import gamemechanics.components.affectors.Affector;

import java.util.List;
import java.util.Map;

public interface AbilityEffect {
    Boolean execute(AliveEntity caster, MapNode target, Map<Integer, Affector> affectors,
                    List<Effect> effectList);
}
