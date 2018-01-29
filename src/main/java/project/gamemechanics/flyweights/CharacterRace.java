package project.gamemechanics.flyweights;

import project.gamemechanics.components.affectors.Affector;
import project.gamemechanics.interfaces.AffectorProvider;
import project.gamemechanics.interfaces.GameEntity;

import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.Set;

public class CharacterRace implements GameEntity, AffectorProvider {
    private final Integer raceID;

    private final String name;
    private final String description;

    private final Map<Integer, Affector> affectors;

    public static class CharacterRaceModel {
        // CHECKSTYLE:OFF
        public final Integer id;
        final String name;
        final String description;
        final Map<Integer, Affector> affectors;
        // CHECKSTYLE:ON

        public CharacterRaceModel(@NotNull Integer id,
                                  @NotNull String name, @NotNull String description,
                                  @NotNull Map<Integer, Affector> affectors) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.affectors = affectors;
        }
    }

    public CharacterRace(@NotNull CharacterRaceModel model) {
        raceID = model.id;
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
        return raceID;
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
    public @NotNull Integer getAffection(@NotNull Integer affectorKind, @NotNull Integer affectionIndex) {
        if (!hasAffector(affectorKind)) {
            return Integer.MIN_VALUE;
        }
        return affectors.get(affectorKind).getAffection(affectionIndex);
    }

    @Override
    public @NotNull Integer getAffection(@NotNull Integer affectorKind) {
        return Integer.MIN_VALUE;
    }
}
