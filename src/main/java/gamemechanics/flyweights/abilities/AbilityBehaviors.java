package gamemechanics.flyweights.abilities;

import gamemechanics.battlefield.BattlefieldObjectsCategories;
import gamemechanics.battlefield.actionresults.events.EventsFactory;
import gamemechanics.battlefield.actionresults.events.TurnEvent;
import gamemechanics.components.properties.PropertyCategories;

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
                /* TODO: add actual ability processing: affected tiles grabbing and implementing effects
                and affections on them */
            } else {
                causedEvents.add(EventsFactory.makeRollbackEvent());
            }

            return causedEvents;
        });

        return behaviorMap;
    }
}
