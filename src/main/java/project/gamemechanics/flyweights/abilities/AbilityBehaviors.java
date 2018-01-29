package project.gamemechanics.flyweights.abilities;

import org.jetbrains.annotations.Nullable;
import project.gamemechanics.battlefield.BattlefieldObjectsCategories;
import project.gamemechanics.battlefield.actionresults.events.EventsFactory;
import project.gamemechanics.battlefield.actionresults.events.TurnEvent;
import project.gamemechanics.battlefield.map.tilesets.AreaEffectTileset;
import project.gamemechanics.battlefield.map.tilesets.Tileset;
import project.gamemechanics.components.affectors.AffectorCategories;
import project.gamemechanics.components.properties.PropertyCategories;
import project.gamemechanics.effects.IngameEffect;
import project.gamemechanics.globals.DigitsPairIndices;
import project.gamemechanics.interfaces.Effect;

import javax.validation.constraints.NotNull;
import java.util.*;

public final class AbilityBehaviors {
    private static final Map<Integer, IngameAbility.AbilityBehavior> BEHAVIORS = initializeBehaviors();

    private static final int BASIC_ABILITY_BEHAVIOR = 0;

    public static @Nullable IngameAbility.AbilityBehavior getBehavior(@NotNull Integer behaviorIndex) {
        return BEHAVIORS.getOrDefault(behaviorIndex, null);
    }

    private static @NotNull Map<Integer, IngameAbility.AbilityBehavior> initializeBehaviors() {
        final Map<Integer, IngameAbility.AbilityBehavior> behaviorMap = new HashMap<>();

        behaviorMap.put(BASIC_ABILITY_BEHAVIOR, aggregatedAbilityAction -> {
            final List<TurnEvent> causedEvents = new ArrayList<>();

            if (aggregatedAbilityAction.sender.isOccupied()) {
                if (!aggregatedAbilityAction.target.isOccupied()
                        && !Objects.requireNonNull(aggregatedAbilityAction.abilityProperties
                        .get(PropertyCategories.PC_INFLICTED_CATEGORIES).getPropertySet())
                        .contains(BattlefieldObjectsCategories.BO_EARTH)) {
                    causedEvents.add(EventsFactory.makeRollbackEvent());
                    return causedEvents;
                }
                final List<Integer> damage = new ArrayList<>(Objects.requireNonNull(
                        aggregatedAbilityAction.abilityAffectors
                                .get(AffectorCategories.AC_ABILITY_HEALTH_AFFECTOR).getAffectionsList()));
                final Integer senderDamage = Objects.requireNonNull(
                        aggregatedAbilityAction.sender.getInhabitant()).getDamage();
                damage.set(DigitsPairIndices.MIN_VALUE_INDEX, damage.get(DigitsPairIndices.MIN_VALUE_INDEX)
                        + senderDamage);
                damage.set(DigitsPairIndices.MAX_VALUE_INDEX, damage.get(DigitsPairIndices.MAX_VALUE_INDEX)
                        + senderDamage);

                final List<IngameEffect.EffectModel> effectModels = aggregatedAbilityAction.abilityEffects == null
                        ? new ArrayList<>() : aggregatedAbilityAction.abilityEffects;

                final Tileset affectedArea = new AreaEffectTileset(aggregatedAbilityAction.abilityID,
                        aggregatedAbilityAction.sender,
                        aggregatedAbilityAction.target,
                        aggregatedAbilityAction.abilityProperties.get(PropertyCategories.PC_AREA_SHAPE).getProperty(),
                        aggregatedAbilityAction.abilityProperties.get(PropertyCategories.PC_AREA).getProperty(),
                        makeEffects(effectModels), damage,
                        Objects.requireNonNull(aggregatedAbilityAction.abilityProperties
                                .get(PropertyCategories.PC_INFLICTED_CATEGORIES).getPropertySet()));
                affectedArea.applyEffects(causedEvents);
            } else {
                causedEvents.add(EventsFactory.makeRollbackEvent());
            }

            return causedEvents;
        });

        return behaviorMap;
    }

    private static @NotNull List<Effect> makeEffects(@NotNull List<IngameEffect.EffectModel> effectModels) {
        final List<Effect> effects = new ArrayList<>(effectModels.size());
        for (Integer i = 0; i < effectModels.size(); ++i) {
            effects.set(i, new IngameEffect(effectModels.get(i)));
        }
        return effects;
    }
}
