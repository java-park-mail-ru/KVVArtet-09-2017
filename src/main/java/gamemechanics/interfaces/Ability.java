package gamemechanics.interfaces;

import gamemechanics.battlefield.Tile;

public interface Ability extends GameEntity {
    Integer getMaxDistance();
    Integer getArea();
    Integer getCooldown();
    Integer getCategories();
    Boolean execute(AliveEntity sender, Tile target);
}
