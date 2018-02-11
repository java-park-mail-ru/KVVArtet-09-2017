package project.gamemechanics.battlefield.map.helpers;

import org.jetbrains.annotations.Nullable;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface PathfindingAlgorithm {
    @Nullable Route getPath(@NotNull List<Integer> sourceCoordinates, @NotNull List<Integer> goalCoordinates);
}
