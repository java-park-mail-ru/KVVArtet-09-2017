package gamemechanics.dungeons;

import gamemechanics.battlefield.actionresults.ActionResult;
import gamemechanics.battlefield.aliveentitiescontainers.CharactersParty;
import gamemechanics.interfaces.Levelable;
import gamemechanics.interfaces.Updateable;
import org.jetbrains.annotations.Nullable;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface Instance extends Levelable, Updateable {
    Integer getRoomsCount();

    Integer getClearedRoomsCount();

    Integer getBattleLogLength();

    List<ActionResult> getBattleLog();

    ActionResult getBattleLogEntry(@NotNull Integer entryIndex);

    Integer getGameMode();

    List<Integer> getBattlingSquadsIds();

    @Nullable CharactersParty getParty(@NotNull Integer partyIndex);

    Boolean isInstanceCleared();

    Boolean isInstanceFailed();

    void giveRewards();

    @SuppressWarnings("SameReturnValue")
    Boolean handlePacket(/* JSON packet here as arg */);
}
