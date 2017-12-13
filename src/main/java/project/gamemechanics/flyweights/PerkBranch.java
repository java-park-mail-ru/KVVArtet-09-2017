package project.gamemechanics.flyweights;

import project.gamemechanics.interfaces.GameEntity;
import project.gamemechanics.interfaces.Perk;

import javax.validation.constraints.NotNull;
import java.util.List;

public class PerkBranch implements GameEntity {
    private final Integer branchID;

    private final String name;
    private final String description;

    private final List<Perk> perks;

    public static class PerkBranchModel {
        public final Integer id;
        public final String name;
        public final String description;
        @SuppressWarnings("PublicField")
        public final List<Perk> perks;

        public PerkBranchModel(@NotNull Integer id, @NotNull String name,
                               @NotNull String description, @NotNull List<Perk> perks) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.perks = perks;
        }
    }

    public PerkBranch(@NotNull PerkBranchModel model) {
        branchID = model.id;
        name = model.name;
        description = model.description;
        perks = model.perks;
    }

    @Override
    public Integer getInstancesCount() {
        return 0;
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
