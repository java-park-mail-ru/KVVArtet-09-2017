package gamemechanics.dungeons;

import gamemechanics.battlefield.actionresults.ActionResult;
import gamemechanics.interfaces.Levelable;

import java.util.List;

public interface Instance extends Levelable {
    Integer getRoomsCount();
    Integer getClearedRoomsCount();

    Integer getBattleLogLength();
    List<ActionResult> getBattleLog();
    ActionResult getBattleLogEntry(Integer entryIndex);

    Integer getGameMode();
    List<Integer> getBattlingSquadsIds();

    Boolean isInstanceCleared();
    Boolean isInstanceFailed();

    void giveRewards();

    /* TODO: place the frontend-package handling code there, and maybe change
     * the return value to some JSON response
     * (throw the incoming package there and handle it working on implementation's internals)
     */
    Boolean handlePacket(/* JSON packet here as arg */);
}
