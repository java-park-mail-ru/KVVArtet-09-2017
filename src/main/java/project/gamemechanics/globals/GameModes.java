package project.gamemechanics.globals;

import javax.validation.constraints.NotNull;

public final class GameModes {
    public static final int GM_COOP_PVE = 0;
    public static final int GM_SOLO_PVE = 1;

    public static final int GM_COOP_PVP = 2;
    public static final int GM_SQUAD_PVP = 3;

    public static Boolean isPve(@NotNull Integer gameMode) {
        return gameMode == GM_COOP_PVE || gameMode == GM_SOLO_PVE;
    }

    public static Boolean isPvp(@NotNull Integer gameMode) {
        return gameMode == GM_COOP_PVP || gameMode == GM_SQUAD_PVP;
    }
}
