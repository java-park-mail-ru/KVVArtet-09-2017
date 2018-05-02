package project.websocket.messages.charlist;

import project.websocket.messages.Message;

public class DeleteCharacterRequestMessage extends Message {
    private Integer charIndexInCharist;

    public DeleteCharacterRequestMessage(Integer charIndexInCharist) {
        this.charIndexInCharist = charIndexInCharist;
    }

    public Integer getCharIndexInCharist() {
        return charIndexInCharist;
    }

    @SuppressWarnings("unused")
    public void setCharIndexInCharist(Integer charIndexInCharist) {
        this.charIndexInCharist = charIndexInCharist;
    }
}
