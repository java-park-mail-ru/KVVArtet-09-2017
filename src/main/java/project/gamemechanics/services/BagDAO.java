package project.gamemechanics.services;

import project.gamemechanics.items.containers.StorageBag;

import javax.validation.constraints.NotNull;

public interface BagDAO {
    StorageBag.FilledBagModel getSerializeBagById(@NotNull Integer id);

    @NotNull StorageBag.FilledBagModel setFilledBag(@NotNull StorageBag.FilledBagModel newBag);
}
