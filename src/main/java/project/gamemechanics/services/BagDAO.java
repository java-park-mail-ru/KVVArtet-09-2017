package project.gamemechanics.services;

import project.gamemechanics.items.containers.StorageBag;

import javax.validation.constraints.NotNull;

@SuppressWarnings("unused")
public interface BagDAO {
    StorageBag.FilledBagModel getSerializeBagById(@NotNull Integer id);

    void setFilledBag(@NotNull StorageBag.FilledBagModel newBag);
}
