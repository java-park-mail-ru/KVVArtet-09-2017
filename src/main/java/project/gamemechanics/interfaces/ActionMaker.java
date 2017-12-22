package project.gamemechanics.interfaces;

import java.util.List;

@SuppressWarnings("unused")
public interface ActionMaker {
    @SuppressWarnings("SameReturnValue")
    Action makeAction(Ability ability, AliveEntity sender, List<Integer> coords);
}
