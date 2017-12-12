package project.websocket.messages;

import org.jetbrains.annotations.NotNull;

public class ErrorMessage extends Message {

    private final String message;

    public ErrorMessage(@NotNull String message){
        this.message = message;
    }

    public ErrorMessage(){
        this.message = "some error occured";
    }

    public String getMessage() {
        return message;
    }
}
