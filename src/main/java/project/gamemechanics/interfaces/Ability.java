package gamemechanics.interfaces;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import gamemechanics.battlefield.actionresults.events.TurnEvent;
import gamemechanics.battlefield.map.actions.AggregatedAbilityAction;
import gamemechanics.components.affectors.Affector;
import gamemechanics.components.properties.Property;
import gamemechanics.effects.IngameEffect;
import gamemechanics.flyweights.abilities.IngameAbility;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * interface for various spells and combat abilities that may be used in the battle
 * abilities're flyweights that actually exist in single instance per one concrete
 * Abilities' action and properties're scaled to the caster's level on every use.
 *
 * @see GameEntity
 * @see PropertyProvider
 * @see AffectorProvider
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(IngameAbility.class),
})
public interface Ability extends GameEntity, PropertyProvider, AffectorProvider {
    /**
     * execute an ability
     *
     * @param action {@link AggregatedAbilityAction} - action data needed to perform the ability
     * @return list of {@link TurnEvent}s caused by ability. This list shall be added
     * to {@link gamemechanics.battlefield.actionresults.ActionResult}'s event list
     */
    List<TurnEvent> execute(@NotNull AggregatedAbilityAction action);

    Map<Integer, Property> getPropertiesMap();

    Map<Integer, Affector> getAffectorsMap();

    List<IngameEffect.EffectModel> getAppliedEffects();
}
