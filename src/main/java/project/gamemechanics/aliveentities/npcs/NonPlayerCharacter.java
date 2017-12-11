package project.gamemechanics.aliveentities.npcs;

import project.gamemechanics.aliveentities.AbstractAliveEntity;
import project.gamemechanics.components.properties.PropertyCategories;
import project.gamemechanics.interfaces.Action;
import project.gamemechanics.interfaces.DecisionMaker;

import javax.validation.constraints.NotNull;
import java.util.concurrent.atomic.AtomicInteger;

public class NonPlayerCharacter extends AbstractAliveEntity {
    private static final AtomicInteger INSTANCE_COUNTER = new AtomicInteger(0);
    private final Integer npcID = INSTANCE_COUNTER.getAndIncrement();

    private DecisionMaker behavior;

    public NonPlayerCharacter(@NotNull NPCModel model) {
        super(model);
        behavior = model.behavior;
    }

    @Override
    public Integer getInstancesCount() {
        return INSTANCE_COUNTER.get();
    }

    @Override
    public Integer getID() {
        return npcID;
    }

    @Override
    public Integer getDamage() {
        return getProperty(PropertyCategories.PC_BASE_DAMAGE);
    }

    @Override
    public Integer getDefense() {
        return getProperty(PropertyCategories.PC_BASE_DEFENSE);
    }

    @Override
    public Action makeDecision() {
        return behavior.makeDecision();
    }

    @Override
    public void setBehavior(@NotNull DecisionMaker behavior) {
        this.behavior = behavior;
    }
}
