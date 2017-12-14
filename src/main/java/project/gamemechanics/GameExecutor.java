package project.gamemechanics;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import project.gamemechanics.smartcontroller.SmartController;
import project.gamemechanics.world.World;
import project.gamemechanics.world.WorldImpl;
import project.websocket.ConnectionPool;
import project.websocket.services.ConnectionPoolService;

import java.util.Map;

@Service
public class GameExecutor implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameExecutor.class);
    @NotNull
    private final World world;

    @Autowired
    public GameExecutor(@NotNull World world) {
        this.world = world;
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

        while(true){
            try {
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
