package gamemechanics.aliveentities.npcs;

import gamemechanics.aliveentities.AbstractAliveEntity;
import gamemechanics.components.properties.PropertyCategories;
import gamemechanics.interfaces.Action;
import gamemechanics.interfaces.DecisionMaker;

import javax.validation.constraints.NotNull;
import java.util.concurrent.atomic.AtomicInteger;

public class NonPlayerCharacter extends AbstractAliveEntity {
    private static final AtomicInteger instanceCounter = new AtomicInteger(0);
    private final Integer npcID = instanceCounter.getAndIncrement();

    private DecisionMaker behavior;

    public NonPlayerCharacter(@NotNull NPCModel model) {
        super(model);
        behavior = model.behavior;
    }

    @Override
    public Integer getInstancesCount() {
        return instanceCounter.get();
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
}
