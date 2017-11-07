package gamemechanics.battlefield;

import gamemechanics.interfaces.Ability;
import gamemechanics.interfaces.Action;
import gamemechanics.interfaces.Effect;
import gamemechanics.interfaces.MapNode;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class BattleAction implements Action {
    private static final AtomicInteger instanceCounter = new AtomicInteger(0);
    private final Integer actionID = instanceCounter.getAndIncrement();

    private final List<Effect> effectsList;

    private final MapNode sender;
    private final MapNode target;
    private final Ability ability;

    public BattleAction(MapNode sender, MapNode target, Ability ability, List<Effect> effectsList) {
        this.sender = sender;
        this.target = target;
        this.ability = ability;
        this.effectsList = effectsList;
    }

    public BattleAction(MapNode sender, MapNode target, List<Effect> effectsList) {
        this(sender, target, null, effectsList);
    }

    @Override
    public Integer getInstancesCount() {
        return instanceCounter.get();
    }

    @Override
    public Integer getID() {
        return actionID;
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

    public Boolean isMovement() {
        return ability == null;
    }

    public Boolean execute() {
        Boolean isValid = isSenderValid();
        if (isValid) {
            if (isMovement()) {
                isValid = target.isOccupied();
                if (isValid) {
                    if (target.occupy(sender.getInhabitant())) {
                        isValid = sender.free();
                    } else {
                        isValid = false;
                    }
                }
            } else {
                return ability.execute(sender.getInhabitant(), target, effectsList);
            }
        }
        return isValid;
    }

    private Boolean isSenderValid() {
        return sender.isOccupied();
    }
}
