package project.gamemechanics.dungeons;

import project.gamemechanics.battlefield.actionresults.ActionResult;
import project.gamemechanics.battlefield.aliveentitiescontainers.CharactersParty;
import project.gamemechanics.interfaces.Levelable;
import project.gamemechanics.interfaces.Updateable;

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

    @SuppressWarnings("SameReturnValue")
    CharactersParty getParty(@NotNull Integer partyIndex);

    Boolean isInstanceCleared();

    Boolean isInstanceFailed();

    @SuppressWarnings("EmptyMethod")
    void giveRewards();

    @SuppressWarnings("SameReturnValue")
    Boolean handlePacket(/* JSON packet here as arg */);
}
