package gamemechanics.interfaces;

import gamemechanics.battlefield.Action;

import java.util.List;

public interface AliveEntity extends Levelable, ModifiablePropertyProvider, Updateable {
    Boolean isAlive();
    void affectHitpoints(Integer amount);

    default Boolean isControlledByAI() { return false; }

    default Action makeDecision() {
        return null;
    }

    Integer getDamage();
    List<Integer> getDamageMinMax();
    Integer getDefense();

    Integer getCash();

    Integer getInitiative();

    Ability getAbility(Integer abilityIndex);

    Ability useAbility(Integer abilityIndex);

    Boolean addEffect(Effect effect);
    Boolean removeEffect(Integer effectIndex);
    Boolean removeAllEffects();

    Bag getBag(Integer bagIndex);
}
