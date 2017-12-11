package gamemechanics.components.actionmakers;

import gamemechanics.battlefield.map.BattleMap;
import gamemechanics.battlefield.map.actions.BattleAction;
import gamemechanics.interfaces.Ability;
import gamemechanics.interfaces.ActionMaker;
import gamemechanics.interfaces.AliveEntity;

import javax.validation.constraints.NotNull;
import java.util.List;

public class BattleActionMaker implements ActionMaker {
    @SuppressWarnings("FieldCanBeLocal")
    private final BattleMap map;

    public BattleActionMaker(@NotNull BattleMap map) {
        this.map = map;
    }

    @Override
    public BattleAction makeAction(@NotNull Ability ability, @NotNull AliveEntity sender,
                                   @NotNull List<Integer> targetCoords) {
        return null;
    }
}
