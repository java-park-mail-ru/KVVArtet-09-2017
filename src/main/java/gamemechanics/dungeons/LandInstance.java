package gamemechanics.dungeons;

import javax.validation.constraints.NotNull;

public abstract class LandInstance extends AbstractInstance {
    public LandInstance(@NotNull LandInstanceModel model) {
        super(model);
    }
}
