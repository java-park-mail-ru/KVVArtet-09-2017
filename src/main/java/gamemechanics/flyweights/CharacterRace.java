package gamemechanics.flyweights;

import gamemechanics.components.affectors.Affector;
import gamemechanics.interfaces.AffectorProvider;
import gamemechanics.interfaces.GameEntity;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class CharacterRace implements GameEntity, AffectorProvider {
    private static final AtomicInteger instanceCounter = new AtomicInteger(0);
    private final Integer raceID = instanceCounter.getAndIncrement();

    private final String name;
    private final String description;

    public Map<Integer, Affector> affectors;

    public static class CharacterRaceModel {
        public String name;
        public String description;
        public Map<Integer, Affector> affectors;

        public CharacterRaceModel(String name, String description, Map<Integer, Affector> affectors) {
            this.name = name;
            this.description = description;
            this.affectors = affectors;
        }
    }

    public CharacterRace(CharacterRaceModel model) {
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
        return raceID;
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
    public Integer getAffection(Integer affectorKind, Integer affectionIndex) {
        if (!hasAffector(affectorKind)) {
            return Integer.MIN_VALUE;
        }
        return affectors.get(affectorKind).getAffection(affectionIndex);
    }

    @Override
    public Integer getAffection(Integer affectorKind) {
        return Integer.MIN_VALUE;
    }
}
