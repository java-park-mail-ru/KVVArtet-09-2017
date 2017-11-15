package gamemechanics.interfaces;

import gamemechanics.flyweights.PerkBranch;

import java.util.Map;
import java.util.Set;

/**
 * interface for entities describing various {@link AliveEntity} roles:
 * user characters' and friendly NPCs' classes, NPC monsters' roles etc.
 * Implementations shall always provide access to the {@link Ability} list of that role.
 */
public interface CharacterRole extends GameEntity, AffectorProvider {
    /**
     * get an ability from available abilities list
     * @param abilityID ID of ability to get
     * @return null if there's no {@link Ability} with such ID or {@link Ability} otherwise
     */
    Ability getAbility(Integer abilityID);

    /**
     * get all abilities available for that role
     * @return all available abilities
     */
    Map<Integer, Ability> getAllAbilities();

    /**
     * get {@link PerkBranch} by ID
     * (only for user characters' classes)
     * @param branchID ID of branch to get
     * @return null if ID is invalid or it's not a user character class
     * or {@link PerkBranch} otherwise
     * @see PerkBranch
     */
    default PerkBranch getBranch(Integer branchID) {
        return null;
    }

    /**
     * get {@link Perk} from the role's {@link PerkBranch}
     * (only for user character classes)
     * @param branchID ID of the branch with the wanted perk
     * @param perkID ID of the perk to get
     * @return null if at least one of IDs is invalid or it's not a user character class
     * or {@link Perk} otherwise
     * @see PerkBranch
     * @see Perk
     */
    default Perk getPerk(Integer branchID, Integer perkID) {
        return null;
    }

    /**
     * override for corresponding {@link AffectorProvider}'s method
     * (as only NPC roles have affectors)
     * @param affectorKind affector ID to get
     * @return true if that role has such affector
     * or false if it isn't an NPC role or there's no such affector
     * @see AffectorProvider
     * @see gamemechanics.components.affectors.Affector
     */
    @Override
    default Boolean hasAffector(Integer affectorKind) {
        return false;
    }

    /**
     * override for corresponding {@link AffectorProvider}'s method
     * (as only NPC roles have affectors)
     * @return all available affectors' IDs or null if it isn't an NPC role
     * @see AffectorProvider
     */
    @Override
    default Set<Integer> getAvailableAffectors() {
        return null;
    }

    /**
     * override for corresponding {@link AffectorProvider}'s method
     * (as only NPC roles have affectors)
     * @param affectorKind affector ID to get
     * @return requested affector's value
     * or 0 if it's not an NPC role or invalid affector was requested
     * @see AffectorProvider
     */
    @Override
    default Integer getAffection(Integer affectorKind) {
        return 0;
    }

    /**
     * override for corresponding {@link AffectorProvider}'s method
     * (as only NPC roles have affectors)
     * @param affectorKind affector ID
     * @param affectionIndex value index in affector
     * @return value of the requested multi-value affector,
     * special constant if either affector ID or affection index was invalid
     * or 0 if it isn't an NPC role
     * @see AffectorProvider
     */
    @Override
    default Integer getAffection(Integer affectorKind, Integer affectionIndex) {
        return 0;
    }
}