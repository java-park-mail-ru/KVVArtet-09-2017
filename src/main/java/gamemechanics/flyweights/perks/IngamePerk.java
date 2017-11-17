package gamemechanics.flyweights.perks;

import gamemechanics.components.affectors.Affector;
import gamemechanics.components.affectors.AffectorCategories;
import gamemechanics.globals.DigitsPairIndices;
import gamemechanics.interfaces.Perk;

import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class IngamePerk implements Perk {
    private final AtomicInteger instanceCounter = new AtomicInteger(0);
    private final Integer perkID = instanceCounter.getAndIncrement();

    private final String name;
    private final String description;

    private final Map<Integer, Affector> affectors;

    public static class PerkModel {
        public String name;
        public String description;
        Map<Integer, Affector> affectors;

        public PerkModel(String name, String description, Map<Integer, Affector> affectors) {
            this.name = name;
            this.description = description;
            this.affectors = affectors;
        }
    }

    public IngamePerk(PerkModel model) {
        name = model.name;
        description = model.description;
        affectors = model.affectors;
    }

    @Override
    public Integer getInstancesCount() {
        return instanceCounter.get();
    }

    @Override
    public Integer getID() {
        return perkID;
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
    public Boolean hasAffector(Integer affectorKind) {
        return affectors.containsKey(affectorKind);
    }

    @Override
    public Set<Integer> getAvailableAffectors() {
        return affectors.keySet();
    }

    @Override
    public Integer getAffection(Integer affectorKind) {
        if (!hasAffector(affectorKind)) {
            return Integer.MIN_VALUE;
        }
        if ((affectorKind & AffectorCategories.AC_REDUCABLE_AFFECTORS) != 0) {
            Random random = new Random(System.currentTimeMillis());
            return affectors.get(affectorKind).getAffection(DigitsPairIndices.MIN_VALUE_INDEX)
                    + random.nextInt(affectors.get(affectorKind).getAffection(DigitsPairIndices.MAX_VALUE_INDEX)
                    - affectors.get(affectorKind).getAffection(DigitsPairIndices.MIN_VALUE_INDEX));
        }
        if ((affectorKind & AffectorCategories.AC_SINGLE_VALUE_AFFECTORS) != 0) {
            return affectors.get(affectorKind).getAffection();
        }
        return Integer.MIN_VALUE;
    }

    @Override
    public Integer getAffection(Integer affectorKind, Integer affectionIndex) {
        if ((affectorKind & AffectorCategories.AC_MULTI_VALUE_AFFECTORS) == 0 || !hasAffector(affectorKind)) {
            return Integer.MIN_VALUE;
        }
        return affectors.get(affectorKind).getAffection(affectionIndex);
    }

    @Override
    public Integer getRankBasedAffection(Integer affectorKind, Integer affectionIndex, Integer perkRank) {
        Integer baseValue = getAffection(affectorKind, affectionIndex);
        if (baseValue != Integer.MIN_VALUE && perkRank > 0) {
            baseValue *= perkRank;
        }
        return baseValue;
    }

    @Override
    public Integer getRankBasedAffection(Integer affectorKind, Integer perkRank) {
        Integer baseValue = getAffection(affectorKind);
        if (baseValue != Integer.MIN_VALUE && perkRank > 0) {
            baseValue *= perkRank;
        }
        return baseValue;
    }
}
