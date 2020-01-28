package com.example.chatterbot.model.data;

public class Container {
    private Translation translationResponse;
    private DetectedLanguage detectedLanguage;

    public Translation getTranslationResponse() {
        return translationResponse;
    }

    public Container setTranslationResponse(Translation translationResponse) {
        this.translationResponse = translationResponse;
        return this;
    }

    public DetectedLanguage getDetectedLanguage() {
        return detectedLanguage;
    }

    public Container setDetectedLanguage(DetectedLanguage detectedLanguage) {
        this.detectedLanguage = detectedLanguage;
        return this;
    }
}
