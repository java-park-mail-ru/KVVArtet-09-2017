package gamemechanics.flyweights;

import gamemechanics.interfaces.GameEntity;
import gamemechanics.interfaces.Perk;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class PerkBranch implements GameEntity {
    private final AtomicInteger instanceCounter = new AtomicInteger(0);
    private final Integer branchID = instanceCounter.getAndIncrement();

    private final String name;
    private final String description;

    private final List<Perk> perks;

    public static class PerkBranchModel {
        public String name;
        public String description;
        public List<Perk> perks;

        public PerkBranchModel(String name, String description, List<Perk> perks) {
            this.name = name;
            this.description = description;
            this.perks = perks;
        }
    }

    public PerkBranch(PerkBranchModel model) {
        name = model.name;
        description = model.description;
        perks = model.perks;
    }

    @Override
    public Integer getInstancesCount() {
        return instanceCounter.get();
    }

    @Override
    public Integer getID() {
        return branchID;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public Perk getPerk(Integer perkIndex) {
        if (perkIndex < 0 || perkIndex >= perks.size()) {
            return null;
        }
        return perks.get(perkIndex);
    }
}
