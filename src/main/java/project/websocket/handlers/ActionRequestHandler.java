package project.websocket.handlers;

import org.springframework.stereotype.Component;
import project.gamemechanics.components.properties.PropertyCategories;
import project.gamemechanics.world.World;
import project.states.DungeonState;
import project.websocket.messages.ActionRequestMessage;
import project.websocket.messages.Message;
import project.websocket.services.ConnectionPoolService;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;
@Component
public class ActionRequestHandler extends MessageHandler<ActionRequestMessage> {
    @NotNull
    private World world;

    @NotNull
    private ConnectionPoolService connectionPoolService;

    @NotNull
    private DungeonState dungeonState;

    public ActionRequestHandler(@NotNull World world, @NotNull ConnectionPoolService connectionPoolService, @NotNull DungeonState dungeonState) {
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
    public Message handle(@NotNull ActionRequestMessage message, Integer forUser) {
        //TODO some world class method
        Integer dungeonID = connectionPoolService.getSmartController(forUser).getActiveChar().getProperty(PropertyCategories.PC_INSTANCE_ID);
        world.getActiveInstances().get(dungeonID).handleMessage(message);
        return message;
    }
}