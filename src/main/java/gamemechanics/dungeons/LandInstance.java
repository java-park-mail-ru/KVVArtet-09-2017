package gamemechanics.dungeons;

import javax.validation.constraints.NotNull;

public class LandInstance extends AbstractInstance {
    public LandInstance(@NotNull LandInstanceModel model) {
        super(model);
    }
}
