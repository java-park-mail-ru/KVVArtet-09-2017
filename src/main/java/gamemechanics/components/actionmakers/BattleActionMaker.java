package gamemechanics.components.actionmakers;

import gamemechanics.battlefield.Action;
import gamemechanics.battlefield.Tile;
import gamemechanics.battlefield.map.BattleMap;
import gamemechanics.globals.DigitsPairIndices;
import gamemechanics.interfaces.Ability;
import gamemechanics.interfaces.ActionMaker;
import gamemechanics.interfaces.AliveEntity;

import java.util.List;
import java.util.Map;

public class BattleActionMaker implements ActionMaker {
    private final BattleMap map;
    private final Map<AliveEntity, Tile> battlersMap;

    public BattleActionMaker(BattleMap map, Map<AliveEntity, Tile> battlersMap) {
        this.map = map;
        this.battlersMap = battlersMap;
    }

    @Override
    public Action makeAction(Ability ability, AliveEntity sender, List<Integer> targetCoords) {
        return new Action(battlersMap.get(sender), map.getTile(targetCoords.get(DigitsPairIndices.X_COORD_INDEX),
                targetCoords.get(DigitsPairIndices.Y_COORD_INDEX)), ability);
    }
}
