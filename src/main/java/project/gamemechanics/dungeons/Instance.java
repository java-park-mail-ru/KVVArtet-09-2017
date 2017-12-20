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

@SuppressWarnings("unused")
public interface Instance extends Levelable, Updateable {
    @SuppressWarnings("unused")
    Integer getRoomsCount();

    @SuppressWarnings("unused")
    Integer getClearedRoomsCount();

    @SuppressWarnings("unused")
    Integer getBattleLogLength();

    @SuppressWarnings("unused")
    List<ActionResult> getBattleLog();

    @SuppressWarnings("unused")
    ActionResult getBattleLogEntry(@NotNull Integer entryIndex);

    @SuppressWarnings("unused")
    Integer getGameMode();

    @SuppressWarnings("unused")
    List<Integer> getBattlingSquadsIds();

    @SuppressWarnings("unused")
    @Nullable CharactersParty getParty(@NotNull Integer partyIndex);

    @SuppressWarnings("unused")
    Boolean isInstanceCleared();

    @SuppressWarnings("unused")
    Boolean isInstanceFailed();

    @SuppressWarnings("unused")
    void giveRewards();

    @SuppressWarnings("unused")
    List<Long> encodeCurrentRoomMap();

    Message handleMessage(ActionRequestMessage message);
}
