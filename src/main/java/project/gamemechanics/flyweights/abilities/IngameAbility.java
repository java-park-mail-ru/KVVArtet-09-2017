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

    interface AbilityBehavior extends Function<AggregatedAbilityAction, List<TurnEvent>> {
    }

    public static class AbilityModel {
        public final Integer id;
        public final String name;
        public final String description;
        @SuppressWarnings("PublicField")
        public final Map<Integer, Property> properties;
        @SuppressWarnings("PublicField")
        public final Map<Integer, Affector> affectors;
        @SuppressWarnings("PublicField")
        public final List<IngameEffect.EffectModel> appliedEffects;
        @SuppressWarnings("PublicField")
        public final AbilityBehavior perform;

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
    public Integer getInstancesCount() {
        return 0;
    }

    @Override
    public Integer getID() {
        return abilityID;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public Boolean hasProperty(Integer propertyKind) {
        return properties.containsKey(propertyKind);
    }

    @Override
    public Set<Integer> getAvailableProperties() {
        return properties.keySet();
    }

    @Override
    public Integer getProperty(Integer propertyKind, Integer propertyIndex) {
        if (!hasProperty(propertyKind)) {
            return Integer.MIN_VALUE;
        }
        return properties.get(propertyKind).getProperty(propertyIndex);
    }

    @Override
    public Integer getProperty(Integer propertyKind) {
        if (!hasProperty(propertyKind)) {
            return Integer.MIN_VALUE;
        }
        return properties.get(propertyKind).getProperty();
    }

    @Override
    public Boolean hasAffector(Integer affectorKind) {
        return affectors.containsKey(affectorKind);
    }

    @Override
    public Set<Integer> getAvailableAffectors() {
        return affectors.keySet();
    }

    @Override
    public Integer getAffection(Integer affectorKind, Integer affectionIndex) {
        if (!hasAffector(affectorKind)) {
            return Integer.MIN_VALUE;
        }
        return affectors.get(affectorKind).getAffection(affectionIndex);
    }

    @Override
    public Integer getAffection(Integer affectorKind) {
        if (!hasAffector(affectorKind)) {
            return Integer.MIN_VALUE;
        }
        return affectors.get(affectorKind).getAffection();
    }

    @Override
    public List<TurnEvent> execute(AggregatedAbilityAction action) {
        return perform.apply(action);
    }

    @Override
    public Map<Integer, Property> getPropertiesMap() {
        return properties;
    }

    @Override
    public Map<Integer, Affector> getAffectorsMap() {
        return affectors;
    }

    @Override
    public List<IngameEffect.EffectModel> getAppliedEffects() {
        return appliedEffects;
    }
}
