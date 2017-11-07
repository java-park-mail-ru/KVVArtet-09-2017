package gamemechanics.components.actionmakers;

import gamemechanics.battlefield.BattleAction;
import gamemechanics.battlefield.map.BattleMap;
import gamemechanics.interfaces.Ability;
import gamemechanics.interfaces.ActionMaker;
import gamemechanics.interfaces.AliveEntity;

import java.util.List;

public class BattleActionMaker implements ActionMaker {
    private final BattleMap map;

    public BattleActionMaker(BattleMap map) {
        this.map = map;
    }

    @Override
    public BattleAction makeAction(Ability ability, AliveEntity sender, List<Integer> targetCoords) {
        return null;
    }
}
