package project.gamemechanics.battlefield.map.actions;

import org.jetbrains.annotations.Nullable;
import project.gamemechanics.battlefield.map.helpers.PathfindingAlgorithm;
import project.gamemechanics.components.affectors.Affector;
import project.gamemechanics.components.properties.Property;
import project.gamemechanics.effects.IngameEffect;
import project.gamemechanics.interfaces.MapNode;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"PublicField", "unused"})
// CHECKSTYLE:OFF
public class AggregatedAbilityAction {
    public final MapNode sender;
    public final MapNode target;
    public final Integer abilityID;
    public final Map<Integer, Affector> abilityAffectors;
    public final Map<Integer, Property> abilityProperties;
    public final List<IngameEffect.EffectModel> abilityEffects;
    @SuppressWarnings({"WeakerAccess"})
    public final PathfindingAlgorithm pathfinder;

    public AggregatedAbilityAction(@NotNull MapNode sender, @NotNull MapNode target, @NotNull Integer abilityID,
                                   @NotNull Map<Integer, Affector> abilityAffectors,
                                   @NotNull Map<Integer, Property> abilityProperties,
                                   @NotNull List<IngameEffect.EffectModel> abilityEffects,
                                   @Nullable PathfindingAlgorithm pathfinder) {
        this.sender = sender;
        this.target = target;
        this.abilityID = abilityID;
        this.abilityAffectors = abilityAffectors;
        this.abilityProperties = abilityProperties;
        this.abilityEffects = abilityEffects;
        this.pathfinder = pathfinder;
    }
}
// CHECKSTYLE:ON