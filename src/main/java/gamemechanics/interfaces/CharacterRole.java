package gamemechanics.interfaces;

import gamemechanics.flyweights.PerkBranch;

import java.util.Map;
import java.util.Set;

public interface CharacterRole extends GameEntity, AffectorProvider {
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

    @Override
    default Boolean hasAffector(Integer affectorKind) {
        return false;
    }

    @Override
    default Set<Integer> getAvailableAffectors() {
        return null;
    }

    @Override
    default Integer getAffection(Integer affectorKind) {
        return 0;
    }

    @Override
    default Integer getAffection(Integer affectorKind, Integer affectionIndex) {
        return 0;
    }
}
