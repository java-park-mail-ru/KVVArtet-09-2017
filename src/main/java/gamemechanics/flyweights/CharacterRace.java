package gamemechanics.flyweights;

import gamemechanics.interfaces.GameEntity;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class CharacterRace implements GameEntity {
    private static final AtomicInteger instanceCounter = new AtomicInteger(0);
    private final Integer raceID = instanceCounter.getAndIncrement();

    private final String name;
    private final String description;

    private final List<Integer> statBonuses;
    private final List<Integer> ratingBonuses;

    public static class CharacterRaceModel {
        public String name;
        public String description;
        public List<Integer> statBonuses;
        public List<Integer> ratingBonuses;

        public CharacterRaceModel(String name, String description,
                                  List<Integer> statBonuses, List<Integer> ratingBonuses) {
            this.name = name;
            this.description = description;
            this.statBonuses = statBonuses;
            this.ratingBonuses = ratingBonuses;
        }
    }

    public CharacterRace(CharacterRaceModel model) {
        name = model.name;
        description = model.description;
        statBonuses = model.statBonuses;
        ratingBonuses = model.ratingBonuses;
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

    public Integer getStatBonus(Integer statIndex) {
        if (statIndex < 0 || statIndex >= statBonuses.size()) {
            return Integer.MIN_VALUE;
        }
        return statBonuses.get(statIndex);
    }

    public List<Integer> getStatBonuses() {
        return statBonuses;
    }

    public Integer getRatingBonus(Integer ratingIndex) {
        if (ratingIndex < 0 || ratingIndex >= ratingBonuses.size()) {
            return Integer.MIN_VALUE;
        }
        return ratingBonuses.get(ratingIndex);
    }

    public List<Integer> getRatingBonuses() {
        return ratingBonuses;
    }
}
