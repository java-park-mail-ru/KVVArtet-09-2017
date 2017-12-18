package project.gamemechanics.dungeons;

import org.jetbrains.annotations.Nullable;
import project.gamemechanics.battlefield.actionresults.ActionResult;
import project.gamemechanics.battlefield.aliveentitiescontainers.CharactersParty;
import project.gamemechanics.interfaces.Levelable;
import project.gamemechanics.interfaces.Updateable;
import project.websocket.messages.ActionRequestMessage;
import project.websocket.messages.Message;
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

    List<Long> encodeCurrentRoomMap();

    Message handleMessage(ActionRequestMessage message);
}
