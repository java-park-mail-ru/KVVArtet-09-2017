package gamemechanics.battlefield.map.actions;

import gamemechanics.interfaces.Ability;
import gamemechanics.interfaces.MapNode;

import javax.validation.constraints.NotNull;

public class BattleAction extends AbstractAction {
    private final MapNode sender;
    private final MapNode target;
    private final Ability ability;

    public BattleAction(@NotNull MapNode sender, @NotNull MapNode target, @NotNull Ability ability) {
        this.sender = sender;
        this.target = target;
        this.ability = ability;
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
    public Boolean execute() {
        Boolean isValid = isSenderValid();
        if (isValid) {
            ability.execute(sender.getInhabitant(), target);
        }
        return isValid;
    }

    private Boolean isSenderValid() {
        return sender.isOccupied();
    }
}
