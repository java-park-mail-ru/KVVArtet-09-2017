package project.gamemechanics.components.actionmakers;

import project.gamemechanics.battlefield.map.BattleMap;
import project.gamemechanics.battlefield.map.actions.BattleAction;
import project.gamemechanics.interfaces.Ability;
import project.gamemechanics.interfaces.ActionMaker;
import project.gamemechanics.interfaces.AliveEntity;

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
