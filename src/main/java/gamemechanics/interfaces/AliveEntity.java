package gamemechanics.interfaces;

import gamemechanics.battlefield.Action;

import java.util.List;

public interface AliveEntity extends Levelable {
    Boolean isAlive();
    List<Integer> getHitpoints();
    Integer getHitpointsPercentage();
    void heal(Integer amount);
    void wound(Integer amount);

    Boolean isControlledByAI();

    default Action decision() {
        return null;
    }

    Integer getInitiative();

    Integer getDamage();
    List<Integer> getDamageMinMax();
    Integer getDefense();

    Integer getCash();

    void move(Integer targetX, Integer targetY);
    void move(List<Integer> targetCoords);

    void getAbility(Integer abilityIndex);

    void useAbility(Integer abilityIndex, Integer targetX, Integer targetY);
    void useAbility(Integer abilityIndex, List<Integer> targetCoords);

    Boolean addEffect(Effect effect);
    Boolean removeEffect(Integer effectIndex);
    Boolean removeAllEffects();

    void setActionMaker(ActionMaker actionMaker);
    void resetActionMaker();
}
