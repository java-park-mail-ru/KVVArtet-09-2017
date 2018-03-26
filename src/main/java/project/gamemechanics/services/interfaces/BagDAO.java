package project.gamemechanics.services.interfaces;

import javafx.util.Pair;
import org.springframework.stereotype.Service;
import project.gamemechanics.services.dbmodels.BagDatabaseModel;

import javax.validation.constraints.NotNull;
import java.util.List;

@SuppressWarnings("unused")
@Service
public interface BagDAO {

    BagDatabaseModel getBagById(@NotNull Integer id);

    @NotNull
    Integer setBag(@NotNull BagDatabaseModel newBag);

    void updateSlotsInBag(@NotNull Integer bagId, @NotNull List<Pair<Integer, Integer>> indexToIdList);

    void deleteBag(@NotNull Integer id);
}
