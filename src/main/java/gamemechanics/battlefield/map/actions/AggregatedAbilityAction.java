package gamemechanics.battlefield.map.actions;

import gamemechanics.battlefield.map.helpers.PathfindingAlgorithm;
import gamemechanics.components.affectors.Affector;
import gamemechanics.components.properties.Property;
import gamemechanics.effects.IngameEffect;
import gamemechanics.interfaces.MapNode;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

public class AggregatedAbilityAction {
    public final MapNode sender;
    public final MapNode target;
    public final Integer abilityID;
    public final Map<Integer, Affector> abilityAffectors;
    public final Map<Integer, Property> abilityProperties;
    public final List<IngameEffect.EffectModel> abilityEffects;
    public final PathfindingAlgorithm pathfinder;

    public AggregatedAbilityAction(@NotNull MapNode sender, @NotNull MapNode target, @NotNull Integer abilityID,
                                   @NotNull Map<Integer, Affector> abilityAffectors,
                                   @NotNull Map<Integer, Property> abilityProperties,
                                   @NotNull List<IngameEffect.EffectModel> abilityEffects,
                                   PathfindingAlgorithm pathfinder) {
        this.sender = sender;
        this.target = target;
        this.abilityID = abilityID;
        this.abilityAffectors = abilityAffectors;
        this.abilityProperties = abilityProperties;
        this.abilityEffects = abilityEffects;
        this.pathfinder = pathfinder;
    }
}
