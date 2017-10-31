package gamemechanics.interfaces;

import gamemechanics.battlefield.Tile;

public interface Ability extends GameEntity, PropertyProvider, AffectorProvider {
    Boolean execute(AliveEntity sender, Tile target);
}
