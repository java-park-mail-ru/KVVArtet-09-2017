package project.gamemechanics.interfaces;

import org.jetbrains.annotations.Nullable;

import javax.validation.constraints.NotNull;
import java.util.List;

@SuppressWarnings("unused")
public interface ActionMaker {
    @SuppressWarnings("SameReturnValue")
    @NotNull Action makeAction(@Nullable Ability ability, @NotNull AliveEntity sender, @NotNull List<Integer> coords);
}
