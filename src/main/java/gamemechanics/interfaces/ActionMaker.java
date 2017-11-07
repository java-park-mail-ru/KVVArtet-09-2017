package gamemechanics.interfaces;

import java.util.List;

public interface ActionMaker {
    Action makeAction(Ability ability, AliveEntity sender, List<Integer> coords);
}
