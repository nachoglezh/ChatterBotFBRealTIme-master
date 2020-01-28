package com.example.chatterbot.model.data;

import java.util.HashMap;
import java.util.Map;

public class Message {
    private boolean outcoming;
    private String message, time;

    public Message(){

    }

    public Message(boolean outcoming, String message, String time) {
        this.outcoming = outcoming;
        this.message = message;
        this.time = time;
    }


    public boolean isOutcoming() {
        return outcoming;
    }

    public String getMessage() {
        return message;
    }

    public String getTime() {
        return time;
    }

    public void setOutcoming(boolean outcoming) {
        this.outcoming = outcoming;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
