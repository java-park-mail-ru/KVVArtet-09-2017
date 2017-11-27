package gamemechanics.flyweights.perks;

import gamemechanics.components.affectors.Affector;
import gamemechanics.components.affectors.AffectorCategories;
import gamemechanics.globals.DigitsPairIndices;
import gamemechanics.interfaces.Perk;

import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class IngamePerk implements Perk {
    private final Integer perkID;

    private final String name;
    private final String description;

    private final Map<Integer, Affector> affectors;

    public static class PerkModel {
        public Integer id;
        public String name;
        public String description;
        Map<Integer, Affector> affectors;

        public PerkModel(@NotNull Integer id,
                         @NotNull String name, @NotNull String description,
                         @NotNull Map<Integer, Affector> affectors) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.affectors = affectors;
        }
    }

    public IngamePerk(@NotNull PerkModel model) {
        perkID = model.id;
        name = model.name;
        description = model.description;
        affectors = model.affectors;
    }

    @Override
    public Integer getInstancesCount() {
        return 0;
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
