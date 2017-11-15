package gamemechanics.dungeons;

import javax.validation.constraints.NotNull;

public class DungeonInstance extends AbstractInstance {
    public DungeonInstance(@NotNull DungeonInstanceModel model) {
        super(model);
    }
}
