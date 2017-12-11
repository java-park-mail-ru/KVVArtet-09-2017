package project.gamemechanics.interfaces;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import project.gamemechanics.aliveentities.npcs.NonPlayerCharacterRole;
import project.gamemechanics.flyweights.CharacterClass;
import project.gamemechanics.flyweights.PerkBranch;
import project.gamemechanics.globals.Constants;
import org.jetbrains.annotations.Nullable;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * interface for entities describing various {@link AliveEntity} roles:
 * user characters' and friendly NPCs' classes, NPC monsters' roles etc.
 * Implementations shall always provide access to the {@link Ability} list of that role.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(CharacterClass.class),
        @JsonSubTypes.Type(NonPlayerCharacterRole.class),
})
public interface CharacterRole extends GameEntity, PropertyProvider, AffectorProvider {
    /**
     * get an ability from available abilities list
     *
     * @param abilityID ID of ability to get
     * @return null if there's no {@link Ability} with such ID or {@link Ability} otherwise
     */
    Ability getAbility(@NotNull Integer abilityID);

    /**
     * get all abilities available for that role
     *
     * @return all available abilities
     */
    Map<Integer, Ability> getAllAbilities();

    /**
     * get {@link PerkBranch} by ID
     * (only for user characters' classes)
     *
     * @param branchID ID of branch to get
     * @return null if ID is invalid or it's not a user character class
     * or {@link PerkBranch} otherwise
     * @see PerkBranch
     */
    default PerkBranch getBranch(@NotNull Integer branchID) {
        return null;
    }

    /**
     * get {@link Perk} from the role's {@link PerkBranch}
     * (only for user character classes)
     *
     * @param branchID ID of the branch with the wanted perk
     * @param perkID   ID of the perk to get
     * @return null if at least one of IDs is invalid or it's not a user character class
     * or {@link Perk} otherwise
     * @see PerkBranch
     * @see Perk
     */
    default Perk getPerk(@NotNull Integer branchID, @NotNull Integer perkID) {
        return null;
    }

    /**
     * override for corresponding {@link AffectorProvider}'s method
     * (as only NPC roles have affectors)
     *
     * @param affectorKind affector ID to get
     * @return true if that role has such affector
     * or false if it isn't an NPC role or there's no such affector
     * @see AffectorProvider
     * @see project.gamemechanics.components.affectors.Affector
     */
    @Override
    default Boolean hasAffector(@NotNull Integer affectorKind) {
        return false;
    }

    /**
     * override for corresponding {@link AffectorProvider}'s method
     * (as only NPC roles have affectors)
     *
     * @return all available affectors' IDs or null if it isn't an NPC role
     * @see AffectorProvider
     */
    @Override
    @JsonIgnore
    default Set<Integer> getAvailableAffectors() {
        return null;
    }

    /**
     * override for corresponding {@link AffectorProvider}'s method
     * (as only NPC roles have affectors)
     *
     * @param affectorKind affector ID to get
     * @return requested affector's value
     * or 0 if it's not an NPC role or invalid affector was requested
     * @see AffectorProvider
     */
    @Override
    default Integer getAffection(@NotNull Integer affectorKind) {
        return 0;
    }

    /**
     * override for corresponding {@link AffectorProvider}'s method
     * (as only NPC roles have affectors)
     *
     * @param affectorKind   affector ID
     * @param affectionIndex value index in affector
     * @return value of the requested multi-value affector,
     * special constant if either affector ID or affection index was invalid
     * or 0 if it isn't an NPC role
     * @see AffectorProvider
     */
    @Override
    default Integer getAffection(@NotNull Integer affectorKind, @NotNull Integer affectionIndex) {
        return 0;
    }

    @JsonIgnore
    default @Nullable List<Integer> getBehaviorIds() {
        return null;
    }

    @Override
    default Boolean hasProperty(@NotNull Integer propertyKind) {
        return false;
    }

    @Override
    @JsonIgnore
    default @Nullable Set<Integer> getAvailableProperties() {
        return null;
    }

    @Override
    default Integer getProperty(@NotNull Integer propertyKind) {
        return 0;
    }

    @Override
    default Integer getProperty(@NotNull Integer propertyKind, @NotNull Integer propertyIndex) {
        return Constants.WRONG_INDEX;
    }

    default Boolean canEquip(@NotNull Integer equipmentKindId) {
        return false;
    }

    @JsonIgnore
    default @Nullable Set<Integer> getEquipableKinds() {
        return null;
    }
}
