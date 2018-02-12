package project.gamemechanics.dungeons;

import org.jetbrains.annotations.Nullable;
import project.gamemechanics.battlefield.actionresults.ActionResult;
import project.gamemechanics.battlefield.aliveentitiescontainers.CharactersParty;
import project.gamemechanics.interfaces.Levelable;
import project.gamemechanics.interfaces.Updateable;
import project.websocket.messages.Message;
import project.websocket.messages.battle.ActionRequestMessage;

import javax.validation.constraints.NotNull;
import java.util.List;

@SuppressWarnings("unused")
public interface Instance extends Levelable, Updateable {
    @SuppressWarnings("unused")
    @NotNull Integer getRoomsCount();

    @SuppressWarnings("unused")
    @NotNull Integer getClearedRoomsCount();

    @SuppressWarnings("unused")
    @NotNull Integer getBattleLogLength();

    @SuppressWarnings("unused")
    @NotNull List<ActionResult> getBattleLog();

    @SuppressWarnings("unused")
    @Nullable ActionResult getBattleLogEntry(@NotNull Integer entryIndex);

    @SuppressWarnings("unused")
    @NotNull Integer getGameMode();

    @SuppressWarnings("unused")
    @NotNull List<Integer> getBattlingSquadsIds();

    @SuppressWarnings("unused")
    @Nullable CharactersParty getParty(@NotNull Integer partyIndex);

    @SuppressWarnings("unused")
    @NotNull Boolean isInstanceCleared();

    @SuppressWarnings("unused")
    @NotNull Boolean isInstanceFailed();

    @SuppressWarnings("unused")
    void giveRewards();

    @SuppressWarnings("unused")
    @NotNull List<Long> encodeCurrentRoomMap();

    @NotNull Message handleMessage(@NotNull ActionRequestMessage message);
}
