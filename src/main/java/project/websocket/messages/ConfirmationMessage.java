package project.websocket.messages;

import org.jetbrains.annotations.NotNull;

public class ConfirmationMessage extends Message {
    private final String message;

    public ConfirmationMessage(@NotNull String message){
        this.message = message;
    }

    public ConfirmationMessage(){
        this.message = "success";
    }

    public String getMessage() {
        return message;
    }

}
