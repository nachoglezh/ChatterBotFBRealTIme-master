package com.example.chatterbot.model.data;

import java.util.List;

public class TranslationResponse {
    private DetectedLanguage detectedLanguage;
    private List<Translation> translations;

    public TranslationResponse(DetectedLanguage detectedLanguage, List<Translation> translations) {
        this.detectedLanguage = detectedLanguage;
        this.translations = translations;
    }

    public DetectedLanguage getDetectedLanguage() {
        return detectedLanguage;
    }

    public List<Translation> getTranslations() {
        return translations;
    }

    @Override
    public String toString() {
        return "TranslationResponse{" +
                "detectedLanguage=" + detectedLanguage +
                ", translations=" + translations +
                '}';
    }
}
