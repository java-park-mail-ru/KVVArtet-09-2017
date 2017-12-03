package gamemechanics.battlefield.map.actions;

import gamemechanics.battlefield.actionresults.ActionResult;
import gamemechanics.battlefield.actionresults.BattleActionResult;
import gamemechanics.battlefield.actionresults.events.EventsFactory;
import gamemechanics.battlefield.map.helpers.PathfindingAlgorithm;
import gamemechanics.interfaces.Ability;
import gamemechanics.interfaces.MapNode;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;

public class BattleAction extends AbstractAction {
    private final MapNode sender;
    private final MapNode target;
    private final Ability ability;
    private final PathfindingAlgorithm pathfinder;

    public BattleAction(@NotNull MapNode sender, @NotNull MapNode target,
                        @NotNull Ability ability, PathfindingAlgorithm pathfinder) {
        this.sender = sender;
        this.target = target;
        this.ability = ability;
        this.pathfinder = pathfinder;
    }

    @Override
    public MapNode getSender() {
        return sender;
    }

    @Override
    public MapNode getTarget() {
        return target;
    }

    @Override
    public Ability getAbility() {
        return ability;
    }

    @Override
    public ActionResult execute() {
        ActionResult result;
        if (isSenderValid()) {
            result = new BattleActionResult(getID(), sender, target, ability,
                    ability.execute(aggregateActionData()));
        } else {
            result = new BattleActionResult(getID(), sender, target, ability, new ArrayList<>());
            result.addEvent(EventsFactory.makeRollbackEvent());
        }
        return result;
    }

    private Boolean isSenderValid() {
        return sender.isOccupied();
    }

    private AggregatedAbilityAction aggregateActionData() {
        return new AggregatedAbilityAction(sender, target, ability.getID(),
                ability.getAffectorsMap(), ability.getPropertiesMap(), ability.getAppliedEffects(), pathfinder);
    }
}
