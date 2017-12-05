package project.gamemechanics.interfaces;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import project.gamemechanics.aliveentities.UserCharacter;
import project.gamemechanics.aliveentities.npcs.NonPlayerCharacter;
import project.gamemechanics.components.properties.Property;
import project.gamemechanics.components.properties.PropertyCategories;
import project.gamemechanics.globals.Constants;

import javax.validation.constraints.NotNull;

/**
 * interface for user characters and various NPCs
 *
 * @see Levelable
 * @see ModifiablePropertyProvider
 * @see Updateable
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(UserCharacter.class),
        @JsonSubTypes.Type(NonPlayerCharacter.class),
})
public interface AliveEntity extends Levelable, ModifiablePropertyProvider, Updateable {
    /**
     * check if entity is alive or not
     *
     * @return true if entity's current HP value is above 0 or false otherwise
     */
    Boolean isAlive();

    /**
     * changes entity's current HP value
     * (note: current HP value can't become bigger than entity's maximum HP)
     *
     * @param amount amount of heal or damage dealt to the entity
     *               (note: values below 0 mean damage, values above 0 mean healing)
     */
    void affectHitpoints(@NotNull Integer amount);

    /**
     * get entity's owner ID (used to define to whom the entity belongs to)
     *
     * @return value of corresponding {@link Property}.
     * for AI-controlled NPCs it will return {@literal Constants.AI_CONTROLLED_NPC_ID}
     * @see Constants
     */
    default Integer getOwnerID() {
        return getProperty(PropertyCategories.PC_OWNER_ID);
    }

    /**
     * checks the value of ownerID property
     *
     * @return true if the property's equal to the {@literal Constants.AI_CONTROLLED_NPC_ID}
     * or false otherwise
     * @see Constants
     */
    default Boolean isControlledByAI() {
        return !hasProperty(PropertyCategories.PC_OWNER_ID)
                || getProperty(PropertyCategories.PC_OWNER_ID)
                == Constants.AI_CONTROLLED_NPC_ID;
    }

    /**
     * get an AI calculated action
     *
     * @return null for user-controlled characters and NPCs, or some AI-generated action for
     * AI-driven NPCs.
     * @see Action
     */
    default Action makeDecision() {
        return null;
    }

    /**
     * gets a randomized value between minimal and maximal values of entity's damage
     *
     * @return damage dealt by entity (without applying critical hit modifier)
     */
    Integer getDamage();

    /**
     * gets entity's defense amount
     *
     * @return entity's defense
     */
    Integer getDefense();

    /**
     * gets entity's cash amount
     *
     * @return cash in character's purse for {@link UserCharacter}
     * or cash reward for defeating the NPC for {@link NonPlayerCharacter}
     */
    Integer getCash();

    /**
     * returns an unmodified entity's intiative. Initiative defines entity's position in
     * the {@link project.gamemechanics.battlefield.Battlefield} turn order.
     *
     * @return entity's initiative
     */
    Integer getInitiative();

    /**
     * get entity's movement speed (maximal distance which it can cover
     * with one movement {@link Action})
     *
     * @return entity's speed
     */
    Integer getSpeed();

    /**
     * gets an {@link Ability} from {@link CharacterRole} abilities list.
     *
     * @param abilityID ID of ability to get
     * @return null if entity's {@link CharacterRole} doesn't have an {@link Ability} with such ID or
     * requested {@link Ability} otherwise
     */
    default Ability getAbility(@NotNull Integer abilityID) {
        if (!getCharacterRole().getAllAbilities().containsKey(abilityID)) {
            return null;
        }
        return getCharacterRole().getAbility(abilityID);
    }

    /**
     * pretty similar to the getAbility(Integer) method,
     * but also sets a cooldown for the asked ability
     *
     * @param abilityID ID of ability to use
     * @return null if ID was invalid or {@link Ability} otherwise
     * @see Ability
     */
    default Ability useAbility(@NotNull Integer abilityID) {
        final Ability ability = getAbility(abilityID);
        if (ability != null) {
            setProperty(PropertyCategories.PC_ABILITIES_COOLDOWN, abilityID,
                    ability.getProperty(PropertyCategories.PC_COOLDOWN));
        }
        return ability;
    }

    /**
     * add an {@link Effect} to the entity
     *
     * @param effect {@link Effect} to add
     * @see Effect
     */
    void addEffect(@NotNull Effect effect);

    /**
     * remove an effect from entity's active effects list by index
     *
     * @param effectIndex index of the {@link Effect} to remove
     * @return true if the removal was successful or false otherwise
     */
    Boolean removeEffect(@NotNull Integer effectIndex);

    /**
     * clear the entity's active effects list
     *
     * @return true if clearing was successful or false otherwise
     */
    Boolean removeAllEffects();

    /**
     * get entity's {@link Bag} by index
     *
     * @param bagIndex index of the bag to get
     * @return null if index is invalid of entity's {@link Bag} otherwise
     */
    Bag getBag(@NotNull Integer bagIndex);

    /**
     * get user character class, NPC monster's role or NPC hero's class
     *
     * @return entity's role
     * @see CharacterRole
     */
    CharacterRole getCharacterRole();

    /**
     * set unit's behavior (only for AI-driven units, DO NOT overload for any others)
     *
     * @param behavior AI to drive unit's behavior
     */
    @JsonIgnore
    default void setBehavior(@NotNull DecisionMaker behavior) {
    }
}
