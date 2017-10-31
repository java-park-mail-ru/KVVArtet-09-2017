package gamemechanics.interfaces;

import gamemechanics.battlefield.Tile;
import gamemechanics.components.affectors.Affector;

import java.util.Map;

public interface AbilityEffect {
    Boolean execute(AliveEntity caster, Tile target, Map<Integer, Affector> affectors);
}
