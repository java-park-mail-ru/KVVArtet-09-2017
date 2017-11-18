package gamemechanics.flyweights.abilities;

import gamemechanics.battlefield.actionresults.events.TurnEvent;
import gamemechanics.battlefield.map.actions.AggregatedAbilityAction;
import gamemechanics.components.affectors.Affector;
import gamemechanics.components.properties.Property;
import gamemechanics.effects.IngameEffect;
import gamemechanics.interfaces.Ability;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

public class IngameAbility implements Ability {
    private static final AtomicInteger instanceCounter = new AtomicInteger(0);
    private final Integer abilityID = instanceCounter.getAndIncrement();

    private final String name;
    private final String description;

    private final Map<Integer, Property> properties;
    private final Map<Integer, Affector> affectors;
    private final List<IngameEffect.EffectModel> appliedEffects;
    private final AbilityBehavior perform;

    interface AbilityBehavior extends Function<AggregatedAbilityAction, List<TurnEvent>> {}

    public static class AbilityModel {
        public String name;
        public String description;
        public Map<Integer, Property> properties;
        public Map<Integer, Affector> affectors;
        public List<IngameEffect.EffectModel> appliedEffects;
        public AbilityBehavior perform;

        public AbilityModel(@NotNull String name, @NotNull String description,
                            @NotNull Map<Integer, Property> properties,
                            @NotNull Map<Integer, Affector> affectors,
                            @NotNull List<IngameEffect.EffectModel> appliedEffects,
                            @NotNull AbilityBehavior perform) {
            this.name = name;
            this.description = description;
            this.properties = properties;
            this.affectors = affectors;
            this.appliedEffects = appliedEffects;
            this.perform = perform;
        }
    }

    public IngameAbility(AbilityModel model) {
        name = model.name;
        description = model.description;
        properties = model.properties;
        affectors = model.affectors;
        appliedEffects = model.appliedEffects;
        perform = model.perform;
    }

    @Override
    public Integer getInstancesCount() {
        return instanceCounter.get();
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
