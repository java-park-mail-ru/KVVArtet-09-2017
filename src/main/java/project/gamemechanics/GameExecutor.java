package project.gamemechanics;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import project.gamemechanics.world.World;
import project.websocket.services.ConnectionPoolService;

final class GameExecutor implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameExecutor.class);
    private final @NotNull World world;
    private final @NotNull ConnectionPoolService connectionPoolService;

    private GameExecutor(@NotNull World world, @NotNull ConnectionPoolService connectionPoolService) {
        this.world = world;
        this.connectionPoolService = connectionPoolService;
    }

    @Override
    public void run() {
        try {
            mainCycle();
        } finally {
            LOGGER.warn("Mechanic executor terminated");
        }
    }

    private void mainCycle() {
        while (true) {
            try {
                connectionPoolService.tick();
                world.tick();
                if (Thread.currentThread().isInterrupted()) {
                    world.reset();
                    return;
                }
            } catch (RuntimeException e) {
                LOGGER.error("Game executor was reseted due to exception", e);
                world.reset();
            }
        }
    }
}
