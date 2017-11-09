package gamemechanics.interfaces;

import gamemechanics.components.properties.PropertyCategories;
import gamemechanics.globals.Constants;

public interface AliveEntity extends Levelable, ModifiablePropertyProvider, Updateable {
    Boolean isAlive();
    void affectHitpoints(Integer amount);

    default Integer getOwnerID() {
        return getProperty(PropertyCategories.PC_OWNER_ID);
    }

    default Boolean isControlledByAI() {
        return !hasProperty(PropertyCategories.PC_OWNER_ID)
                || getProperty(PropertyCategories.PC_OWNER_ID)
                == Constants.AI_CONTROLLED_NPC_ID;
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

    default Ability getAbility(Integer abilityID) {
        if (!getCharacterRole().getAllAbilities().containsKey(abilityID)) {
            return null;
        }
        return getCharacterRole().getAbility(abilityID);
    }

    default Ability useAbility(Integer abilityID) {
        if (getCharacterRole().getAbility(abilityID) != null) {
            setProperty(PropertyCategories.PC_ABILITIES_COOLDOWN, abilityID,
                    getCharacterRole().getAbility(abilityID).getProperty(PropertyCategories.PC_COOLDOWN));
        }
        return getCharacterRole().getAbility(abilityID);
    }

    Boolean addEffect(Effect effect);
    Boolean removeEffect(Integer effectIndex);
    Boolean removeAllEffects();

    Bag getBag(Integer bagIndex);

    CharacterRole getCharacterRole();
}
