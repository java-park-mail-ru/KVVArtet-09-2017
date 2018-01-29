package project.gamemechanics.flyweights.abilities;

import project.gamemechanics.battlefield.actionresults.events.TurnEvent;
import project.gamemechanics.battlefield.map.actions.AggregatedAbilityAction;
import project.gamemechanics.components.affectors.Affector;
import project.gamemechanics.components.properties.Property;
import project.gamemechanics.effects.IngameEffect;
import project.gamemechanics.interfaces.Ability;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class IngameAbility implements Ability {
    private final Integer abilityID;

    private final String name;
    private final String description;

    private final Map<Integer, Property> properties;
    private final Map<Integer, Affector> affectors;
    private final List<IngameEffect.EffectModel> appliedEffects;
    private final AbilityBehavior perform;

    public interface AbilityBehavior extends Function<AggregatedAbilityAction, List<TurnEvent>> {
    }

    public static class AbilityModel {
        // CHECKSTYLE:OFF
        public final Integer id;
        final String name;
        final String description;
        final Map<Integer, Property> properties;
        final Map<Integer, Affector> affectors;
        final List<IngameEffect.EffectModel> appliedEffects;
        final AbilityBehavior perform;
        // CHECKSTYLE:ON

        public AbilityModel(@NotNull Integer id,
                            @NotNull String name, @NotNull String description,
                            @NotNull Map<Integer, Property> properties,
                            @NotNull Map<Integer, Affector> affectors,
                            @NotNull List<IngameEffect.EffectModel> appliedEffects,
                            @NotNull AbilityBehavior perform) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.properties = properties;
            this.affectors = affectors;
            this.appliedEffects = appliedEffects;
            this.perform = perform;
        }
    }

    public IngameAbility(@NotNull AbilityModel model) {
        abilityID = model.id;
        name = model.name;
        description = model.description;
        properties = model.properties;
        affectors = model.affectors;
        appliedEffects = model.appliedEffects;
        perform = model.perform;
    }

    @Override
    public @NotNull Integer getInstancesCount() {
        return 0;
    }

    @Override
    public @NotNull Integer getID() {
        return abilityID;
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public @NotNull String getDescription() {
        return description;
    }

    @Override
    public @NotNull Boolean hasProperty(@NotNull Integer propertyKind) {
        return properties.containsKey(propertyKind);
    }

    @Override
    public @NotNull Set<Integer> getAvailableProperties() {
        return properties.keySet();
    }

    @Override
    public @NotNull Integer getProperty(@NotNull Integer propertyKind, @NotNull Integer propertyIndex) {
        if (!hasProperty(propertyKind)) {
            return Integer.MIN_VALUE;
        }
        return properties.get(propertyKind).getProperty(propertyIndex);
    }

    @Override
    public @NotNull Integer getProperty(@NotNull Integer propertyKind) {
        if (!hasProperty(propertyKind)) {
            return Integer.MIN_VALUE;
        }
        return properties.get(propertyKind).getProperty();
    }

    @Override
    public @NotNull Boolean hasAffector(@NotNull Integer affectorKind) {
        return affectors.containsKey(affectorKind);
    }

    @Override
    public @NotNull Set<Integer> getAvailableAffectors() {
        return affectors.keySet();
    }

    @Override
    public @NotNull Integer getAffection(@NotNull Integer affectorKind, @NotNull Integer affectionIndex) {
        if (!hasAffector(affectorKind)) {
            return Integer.MIN_VALUE;
        }
        return affectors.get(affectorKind).getAffection(affectionIndex);
    }

    @Override
    public @NotNull Integer getAffection(@NotNull Integer affectorKind) {
        if (!hasAffector(affectorKind)) {
            return Integer.MIN_VALUE;
        }
        return affectors.get(affectorKind).getAffection();
    }

    @Override
    public @NotNull List<TurnEvent> execute(@NotNull AggregatedAbilityAction action) {
        return perform.apply(action);
    }

    @Override
    public @NotNull Map<Integer, Property> getPropertiesMap() {
        return properties;
    }

    @Override
    public @NotNull Map<Integer, Affector> getAffectorsMap() {
        return affectors;
    }

    @Override
    public @NotNull List<IngameEffect.EffectModel> getAppliedEffects() {
        return appliedEffects;
    }
}
