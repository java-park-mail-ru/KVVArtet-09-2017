package gamemechanics.interfaces;

import java.util.List;

public interface Ability extends GameEntity, PropertyProvider, AffectorProvider {
    Boolean execute(AliveEntity sender, MapNode target, List<Effect> effectList);
}
