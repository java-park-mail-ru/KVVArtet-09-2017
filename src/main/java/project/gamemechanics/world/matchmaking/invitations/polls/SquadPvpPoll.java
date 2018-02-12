package project.gamemechanics.world.matchmaking.invitations.polls;

import com.fasterxml.jackson.annotation.JsonProperty;
import project.gamemechanics.battlefield.aliveentitiescontainers.CharactersParty;
import project.gamemechanics.globals.GameModes;

import javax.validation.constraints.NotNull;
import java.util.Map;

public class SquadPvpPoll extends AbstractPvpPoll {
    public SquadPvpPoll(@NotNull Map<Integer, CharactersParty> parties) {
        super(parties);
    }

    @Override
    @JsonProperty("mode")
    public @NotNull Integer getGameMode() {
        return GameModes.GM_SQUAD_PVP;
    }
}
