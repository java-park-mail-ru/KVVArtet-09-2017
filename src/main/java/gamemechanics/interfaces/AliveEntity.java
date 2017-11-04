package gamemechanics.interfaces;

import gamemechanics.battlefield.Action;

public interface AliveEntity extends Levelable, ModifiablePropertyProvider, Updateable {
    Boolean isAlive();
    void affectHitpoints(Integer amount);

    default Boolean isControlledByAI() { return false; }

    default Action makeDecision() {
        return null;
    }

    Integer getDamage();
    Integer getDefense();

    Integer getCash();
    /* TODO - think about some Reward getReward() method */

    Integer getInitiative();
    Integer getSpeed();

    Ability getAbility(Integer abilityIndex);

    Ability useAbility(Integer abilityIndex);

    Boolean addEffect(Effect effect);
    Boolean removeEffect(Integer effectIndex);
    Boolean removeAllEffects();

    Bag getBag(Integer bagIndex);
}
