package project.gamemechanics.flyweights.perks;

import project.gamemechanics.components.affectors.Affector;
import project.gamemechanics.components.affectors.AffectorCategories;
import project.gamemechanics.globals.DigitsPairIndices;
import project.gamemechanics.interfaces.Perk;

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
        // CHECKSTYLE:OFF
        public final Integer id;
        final String name;
        final String description;
        final Map<Integer, Affector> affectors;
        // CHECKSTYLE:ON

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
    public @NotNull Integer getInstancesCount() {
        return 0;
    }

    @Override
    public @NotNull Integer getID() {
        return perkID;
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public @NotNull String getDescription() {
        return description;
    }

    @Override
    public @NotNull Boolean hasAffector(@NotNull Integer affectorKind) {
        return affectors.containsKey(affectorKind);
    }

    @Override
    public @NotNull Set<Integer> getAvailableAffectors() {
        return affectors.keySet();
    }

    @Override
    public @NotNull Integer getAffection(@NotNull Integer affectorKind) {
        if (!hasAffector(affectorKind)) {
            return Integer.MIN_VALUE;
        }
        if ((affectorKind & AffectorCategories.AC_REDUCABLE_AFFECTORS) != 0) {
            final Random random = new Random(System.currentTimeMillis());
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
    public @NotNull Integer getAffection(@NotNull Integer affectorKind, @NotNull Integer affectionIndex) {
        if ((affectorKind & AffectorCategories.AC_MULTI_VALUE_AFFECTORS) == 0 || !hasAffector(affectorKind)) {
            return Integer.MIN_VALUE;
        }
        return affectors.get(affectorKind).getAffection(affectionIndex);
    }

    @Override
    public @NotNull Integer getRankBasedAffection(@NotNull Integer affectorKind,
                                                  @NotNull Integer affectionIndex,
                                                  @NotNull Integer perkRank) {
        Integer baseValue = getAffection(affectorKind, affectionIndex);
        if (baseValue != Integer.MIN_VALUE && perkRank > 0) {
            baseValue *= perkRank;
        }
        return baseValue;
    }

    @Override
    public @NotNull Integer getRankBasedAffection(@NotNull Integer affectorKind,
                                                  @NotNull Integer perkRank) {
        Integer baseValue = getAffection(affectorKind);
        if (baseValue != Integer.MIN_VALUE && perkRank > 0) {
            baseValue *= perkRank;
        }
        return baseValue;
    }
}
