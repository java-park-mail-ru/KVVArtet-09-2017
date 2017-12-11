package project.websocket.messages;

public class ErrorMessage extends Message {
    public ErrorMessage(String message){
        status = message;
    }

    ErrorMessage(){
        status = "some error occured";
    }
}
