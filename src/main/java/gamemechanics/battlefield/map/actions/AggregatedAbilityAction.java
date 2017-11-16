package gamemechanics.battlefield.map.actions;

import gamemechanics.battlefield.map.helpers.PathfindingAlgorithm;
import gamemechanics.components.affectors.Affector;
import gamemechanics.components.properties.Property;
import gamemechanics.interfaces.MapNode;

import javax.validation.constraints.NotNull;
import java.util.Map;

public class AggregatedAbilityAction {
    public final MapNode sender;
    public final MapNode target;
    public final Map<Integer, Affector> abilityAffectors;
    public final Map<Integer, Property> abilityProperties;
    public final PathfindingAlgorithm pathfinder;

    public AggregatedAbilityAction(@NotNull MapNode sender, @NotNull MapNode target,
                                   @NotNull Map<Integer, Affector> abilityAffectors,
                                   @NotNull Map<Integer, Property> abilityProperties,
                                   PathfindingAlgorithm pathfinder) {
        this.sender = sender;
        this.target = target;
        this.abilityAffectors = abilityAffectors;
        this.abilityProperties = abilityProperties;
        this.pathfinder = pathfinder;
    }
}
