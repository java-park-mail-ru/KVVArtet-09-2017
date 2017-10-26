package gamemechanics.interfaces;

import gamemechanics.battlefield.Tile;

import java.util.List;

public interface AbilityEffect {
    Boolean execute(AliveEntity caster, Tile target, List<Integer> effect);
}
