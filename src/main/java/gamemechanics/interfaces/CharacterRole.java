package gamemechanics.interfaces;

import gamemechanics.flyweights.PerkBranch;

import java.util.Map;

public interface CharacterRole extends GameEntity {
    Ability getAbility(Integer abilityID);
    Map<Integer, Ability> getAllAbilities();

    default Action makeDecision() {
        return null;
    }

    default PerkBranch getBranch(Integer branchID) {
        return null;
    }

    default Perk getPerk(Integer branchID, Integer perkID) {
        return null;
    }
}
