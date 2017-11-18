package gamemechanics.flyweights.abilities;

import gamemechanics.battlefield.BattlefieldObjectsCategories;
import gamemechanics.battlefield.actionresults.events.EventsFactory;
import gamemechanics.battlefield.actionresults.events.TurnEvent;
import gamemechanics.battlefield.map.tilesets.AreaEffectTileset;
import gamemechanics.battlefield.map.tilesets.Tileset;
import gamemechanics.components.affectors.AffectorCategories;
import gamemechanics.components.properties.PropertyCategories;
import gamemechanics.effects.IngameEffect;
import gamemechanics.interfaces.Effect;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class AbilityBehaviors {
    private static final Map<Integer, IngameAbility.AbilityBehavior> behaviors = initializeBehaviors();

    public static final int BASIC_ABILITY_BEHAVIOR = 0;

    public static IngameAbility.AbilityBehavior getBehavior(Integer behaviorIndex) {
        return behaviors.getOrDefault(behaviorIndex, null);
    }

    private static Map<Integer, IngameAbility.AbilityBehavior> initializeBehaviors() {
        Map<Integer, IngameAbility.AbilityBehavior> behaviorMap = new HashMap<>();

        behaviorMap.put(BASIC_ABILITY_BEHAVIOR, aggregatedAbilityAction -> {
            List<TurnEvent> causedEvents = new ArrayList<>();

            if (aggregatedAbilityAction.sender.isOccupied()) {
                if (!aggregatedAbilityAction.target.isOccupied() && !aggregatedAbilityAction.abilityProperties
                        .get(PropertyCategories.PC_INFLICTED_CATEGORIES).getPropertySet()
                        .contains(BattlefieldObjectsCategories.BO_EARTH)) {
                    causedEvents.add(EventsFactory.makeRollbackEvent());
                    return causedEvents;
                }
                Tileset affectedArea = new AreaEffectTileset(aggregatedAbilityAction.abilityID,
                        aggregatedAbilityAction.sender,
                        aggregatedAbilityAction.target,
                        aggregatedAbilityAction.abilityProperties.get(PropertyCategories.PC_AREA_SHAPE).getProperty(),
                        aggregatedAbilityAction.abilityProperties.get(PropertyCategories.PC_AREA).getProperty(),
                        makeEffects(aggregatedAbilityAction.abilityEffects),
                        aggregatedAbilityAction.abilityAffectors.get(AffectorCategories.AC_ABILITY_HEALTH_AFFECTOR)
                                .getAffectionsList(),
                        aggregatedAbilityAction.abilityProperties.get(PropertyCategories.PC_INFLICTED_CATEGORIES)
                                .getPropertySet());
                affectedArea.applyEffects(causedEvents);
            } else {
                causedEvents.add(EventsFactory.makeRollbackEvent());
            }

            return causedEvents;
        });

        return behaviorMap;
    }

    private static List<Effect> makeEffects(@NotNull List<IngameEffect.EffectModel> effectModels) {
        List<Effect> effects = new ArrayList<>(effectModels.size());
        for (Integer i = 0; i < effectModels.size(); ++i) {
            effects.set(i, new IngameEffect(effectModels.get(i)));
        }
        return effects;
    }
}
