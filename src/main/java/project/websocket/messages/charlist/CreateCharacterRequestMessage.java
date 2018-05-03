package project.websocket.messages.charlist;

import project.websocket.messages.Message;

@SuppressWarnings("unused")
public class CreateCharacterRequestMessage extends Message {
    private String name;
    private String role;
    private String race;

    public CreateCharacterRequestMessage(String name, String role, String race) {
        this.name = name;
        this.role = role;
        this.race = race;
    }

    public CreateCharacterRequestMessage() {

    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
