package project.gamemechanics.effects;

import project.gamemechanics.components.affectors.Affector;
import project.gamemechanics.components.affectors.AffectorCategories;
import project.gamemechanics.globals.Constants;
import project.gamemechanics.globals.DigitsPairIndices;
import project.gamemechanics.interfaces.Effect;

import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class IngameEffect implements Effect {
    private static final AtomicInteger INSTANCE_COUNTER = new AtomicInteger(0);
    private final Integer effectID = INSTANCE_COUNTER.getAndIncrement();

    private final String name;
    private final String description;

    private Integer duration;

    private final Map<Integer, Affector> affectors;

    public static class EffectModel {
        public final String name;
        public final String description;

        public final Integer duration;

        @SuppressWarnings("PublicField")
        public final Map<Integer, Affector> affectors;

        public EffectModel(@NotNull String name, @NotNull String description,
                           @NotNull Integer duration,
                           @NotNull Map<Integer, Affector> affectors) {
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
        return INSTANCE_COUNTER.get();
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
            final Random random = new Random(System.currentTimeMillis());
            final Affector affector = affectors.get(affectorKind);
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
