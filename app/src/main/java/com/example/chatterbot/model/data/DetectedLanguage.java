package com.example.chatterbot.model.data;

public class DetectedLanguage {

    private String language;


    private int score;

    public DetectedLanguage(String language, int score) {
        this.language = language;
        this.score = score;
    }

    public String getLanguage() {
        return language;
    }
}
