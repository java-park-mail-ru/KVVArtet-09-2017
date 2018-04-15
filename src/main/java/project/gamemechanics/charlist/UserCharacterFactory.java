package project.gamemechanics.charlist;

import project.gamemechanics.smartcontroller.SmartController;
import project.websocket.messages.charlist.CreateCharacterRequestMessage;
import project.websocket.messages.charlist.DeleteCharacterRequestMessage;

public interface UserCharacterFactory {
    void createUserCharacter(CreateCharacterRequestMessage requestMessage, SmartController activeSmart);

    void deleteUserCharacter(DeleteCharacterRequestMessage requestMessage, SmartController activeSmart);
}
