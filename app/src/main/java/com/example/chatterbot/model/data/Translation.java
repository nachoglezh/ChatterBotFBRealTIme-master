package com.example.chatterbot.model.data;

public class Translation {
    private String text;
    private String to;

    public Translation(String text, String to) {
        this.text = text;
        this.to = to;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
}
