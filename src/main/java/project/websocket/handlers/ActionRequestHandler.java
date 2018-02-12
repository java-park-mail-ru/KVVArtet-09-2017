package project.websocket.handlers;

import org.springframework.stereotype.Component;
import project.gamemechanics.components.properties.PropertyCategories;
import project.gamemechanics.world.World;
import project.states.DungeonState;
import project.websocket.messages.Message;
import project.websocket.messages.battle.ActionRequestMessage;
import project.websocket.services.ConnectionPoolService;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Component
public class ActionRequestHandler extends MessageHandler<ActionRequestMessage> {
    private final @NotNull World world;

    private final @NotNull ConnectionPoolService connectionPoolService;

    private final @NotNull DungeonState dungeonState;

    public ActionRequestHandler(@NotNull World world,
                                @NotNull ConnectionPoolService connectionPoolService,
                                @NotNull DungeonState dungeonState) {
        super(ActionRequestMessage.class);
        this.world = world;
        this.connectionPoolService = connectionPoolService;
        this.dungeonState = dungeonState;
    }

    @PostConstruct
    private void init() {
        dungeonState.registerHandler(ActionRequestMessage.class, this);
    }

    @Override
    public @NotNull Message handle(@NotNull ActionRequestMessage message, @NotNull Integer forUser) {
        final Integer dungeonID =
                Objects.requireNonNull(connectionPoolService.getSmartController(forUser)
                        .getActiveChar()).getProperty(PropertyCategories.PC_INSTANCE_ID);
        return world.getActiveInstances().get(dungeonID).handleMessage(message);
    }
}