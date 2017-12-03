package gamemechanics.effects;

import gamemechanics.components.affectors.Affector;
import gamemechanics.components.affectors.AffectorCategories;
import gamemechanics.globals.Constants;
import gamemechanics.globals.DigitsPairIndices;
import gamemechanics.interfaces.Effect;

import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class IngameEffect implements Effect {
    private static final AtomicInteger instanceCounter = new AtomicInteger(0);
    private final Integer effectID = instanceCounter.getAndIncrement();

    private final String name;
    private final String description;

    private Integer duration;

    private final Map<Integer, Affector> affectors;

    public static class EffectModel {
        public String name;
        public String description;

        public Integer duration;

        public Map<Integer, Affector> affectors;

        public EffectModel(String name, String description, Integer duration,
                           Map<Integer, Affector> affectors) {
            this.name = name;
            this.description = description;
            this.duration = duration;
            this.affectors = affectors;
        }
    }

    public IngameEffect(EffectModel model) {
        name = model.name;
        description = model.description;
        duration = model.duration;
        affectors = model.affectors;
    }

    @Override
    public Integer getInstancesCount() {
        return instanceCounter.get();
    }

    @Override
    public Integer getID() {
        return effectID;
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
    public Integer getDuration() {
        return duration;
    }

    @Override
    public Boolean isExpired() {
        return duration > 0 && !isPerpetual();
    }

    @Override
    public Boolean isPerpetual() {
        return duration != Constants.PERPETUAL_EFFECT_DURATION;
    }

    @Override
    public Set<Integer> getAvailableAffectors() {
        return affectors.keySet();
    }

    @Override
    public Boolean hasAffector(Integer affectorKind) {
        return affectors.containsKey(affectorKind);
    }

    @Override
    public Integer getAffection(Integer affectorKind, Integer affectionIndex) {
        if ((affectorKind & AffectorCategories.AC_MULTI_VALUE_AFFECTORS) == 0
                || !affectors.containsKey(affectorKind)) {
            return Integer.MIN_VALUE;
        }
        return affectors.get(affectorKind).getAffection(affectionIndex);
    }

    @Override
    public Integer getAffection(Integer affectorKind) {
        if (!affectors.containsKey(affectorKind)) {
            return Integer.MIN_VALUE;
        }
        if ((affectorKind & AffectorCategories.AC_REDUCABLE_AFFECTORS) != 0) {
            Random random = new Random(System.currentTimeMillis());
            Affector affector = affectors.get(affectorKind);
            return affector.getAffection(DigitsPairIndices.MIN_VALUE_INDEX)
                    + random.nextInt(affector.getAffection(DigitsPairIndices.MAX_VALUE_INDEX
                    - DigitsPairIndices.MIN_VALUE_INDEX));
        }
        if ((affectorKind & AffectorCategories.AC_SINGLE_VALUE_AFFECTORS) != 0) {
            return affectors.get(affectorKind).getAffection();
        }
        return Integer.MIN_VALUE;
    }

    @Override
    public void tick() {
        if (duration > 0) {
            --duration;
        }
    }
}
