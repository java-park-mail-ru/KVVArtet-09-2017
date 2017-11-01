package gamemechanics.interfaces;

public interface Ability extends GameEntity, PropertyProvider, AffectorProvider {
    Boolean execute(AliveEntity sender, MapNode target);
}
