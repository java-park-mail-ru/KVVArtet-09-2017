package project.gamemechanics.battlefield.map.actions;

import org.jetbrains.annotations.Nullable;
import project.gamemechanics.battlefield.actionresults.ActionResult;
import project.gamemechanics.battlefield.actionresults.BattleActionResult;
import project.gamemechanics.battlefield.actionresults.events.EventsFactory;
import project.gamemechanics.battlefield.map.helpers.PathfindingAlgorithm;
import project.gamemechanics.interfaces.Ability;
import project.gamemechanics.interfaces.MapNode;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Objects;

public class BattleAction extends AbstractAction {
    private final MapNode sender;
    private final MapNode target;
    private final Ability ability;
    private final PathfindingAlgorithm pathfinder;

    public BattleAction(@NotNull MapNode sender, @NotNull MapNode target,
                        @NotNull Ability ability, @Nullable PathfindingAlgorithm pathfinder) {
        this.sender = sender;
        this.target = target;
        this.ability = ability;
        this.pathfinder = pathfinder;
    }

    @Override
    public @NotNull MapNode getSender() {
        return sender;
    }

    @Override
    public @NotNull MapNode getTarget() {
        return target;
    }

    @Override
    public @NotNull Ability getAbility() {
        return ability;
    }

    @Override
    public @NotNull ActionResult execute() {
        final ActionResult result;
        if (isSenderValid()) {
            result = new BattleActionResult(getID(), sender, target, ability,
                    ability.execute(aggregateActionData()));
            Objects.requireNonNull(sender.getInhabitant()).useAbility(ability.getID());
        } else {
            result = new BattleActionResult(getID(), sender, target, ability, new ArrayList<>());
            result.addEvent(EventsFactory.makeRollbackEvent());
        }
        return result;
    }

    private @NotNull Boolean isSenderValid() {
        return sender.isOccupied();
    }

    private @NotNull AggregatedAbilityAction aggregateActionData() {
        return new AggregatedAbilityAction(sender, target, ability.getID(),
                ability.getAffectorsMap(), ability.getPropertiesMap(),
                ability.getAppliedEffects(), pathfinder);
    }
}
