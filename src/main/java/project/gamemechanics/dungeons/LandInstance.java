package project.gamemechanics.dungeons;

import javax.validation.constraints.NotNull;

@SuppressWarnings("AbstractClassNeverImplemented")
public abstract class LandInstance extends AbstractInstance {
    public LandInstance(@NotNull LandInstanceModel model) {
        super(model);
    }
}
