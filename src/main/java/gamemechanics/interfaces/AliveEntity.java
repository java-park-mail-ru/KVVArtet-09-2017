package gamemechanics.interfaces;

import gamemechanics.components.properties.PropertyCategories;

public interface AliveEntity extends Levelable, ModifiablePropertyProvider, Updateable {
    Boolean isAlive();
    void affectHitpoints(Integer amount);

    default Integer getOwnerID() {
        return getProperty(PropertyCategories.PC_OWNER_ID);
    }

    default Boolean isControlledByAI() {
        return hasProperty(PropertyCategories.PC_OWNER_ID);
    }

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

    CharacterRole getCharacterRole();
}
