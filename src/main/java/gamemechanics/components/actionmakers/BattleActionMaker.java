package gamemechanics.components.actionmakers;

import gamemechanics.battlefield.Action;
import gamemechanics.battlefield.map.BattleMap;
import gamemechanics.components.properties.PropertyCategories;
import gamemechanics.globals.DigitsPairIndices;
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
    public Action makeAction(Ability ability, AliveEntity sender, List<Integer> targetCoords) {
        return new Action(map.getTile(sender.getProperty(PropertyCategories.PC_COORDINATES,
                DigitsPairIndices.X_COORD_INDEX), sender.getProperty(PropertyCategories.PC_COORDINATES,
                DigitsPairIndices.Y_COORD_INDEX)), map.getTile(targetCoords.get(DigitsPairIndices.X_COORD_INDEX),
                targetCoords.get(DigitsPairIndices.Y_COORD_INDEX)), ability);
    }
}
