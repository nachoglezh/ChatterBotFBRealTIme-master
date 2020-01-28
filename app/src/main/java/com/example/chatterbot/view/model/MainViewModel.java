package com.example.chatterbot.view.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.chatterbot.model.data.Message;
import com.example.chatterbot.model.data.TranslationResponse;
import com.example.chatterbot.model.repository.Repository;
import com.example.chatterbot.view.adapter.BackupAdapter;
import com.example.chatterbot.view.adapter.MessagesAdapter;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class MainViewModel extends AndroidViewModel {
    private static final String WELCOME = "Hola";
    private Repository repository;
    private boolean waitingResponse;
    private boolean waitingBotTranslation;
    private String translateCountryCode = "es";
    private boolean tts = true;

    private MessagesAdapter messagesAdapter;
    private BackupAdapter backupAdapter;

    private boolean started;
    private boolean loading;
    private FirebaseUser user;
    private FirebaseDatabase database;
    private DatabaseReference reference;

    public MainViewModel(@NonNull Application application) {
        super(application);

        messagesAdapter = new MessagesAdapter();
        backupAdapter = new BackupAdapter();
        messagesAdapter.addMessage(new Message(false, WELCOME, getShortTime()));
        repository = new Repository();

        database = FirebaseDatabase.getInstance();
        try {
            database.setPersistenceEnabled(true);
        }
        catch(Exception e) {

        }

    }

    public MessagesAdapter getMessagesAdapter() {
        return messagesAdapter;
    }

    public BackupAdapter getBackupAdapter() {
        return backupAdapter;
    }

    public String getTranslateCountryCode() {
        return translateCountryCode;
    }

    public void setTranslateCountryCode(String translateCountryCode) {
        this.translateCountryCode = translateCountryCode;
    }

    public void translate(String fromLang, String text, String to) {
        repository.translate(fromLang, text, to);
    }

    public boolean isWaitingResponse() {
        return waitingResponse;
    }

    public void setWaitingResponse(boolean waitingResponse) {
        this.waitingResponse = waitingResponse;
    }

    public MutableLiveData<List<TranslationResponse>> getTranslationResponses() {
        return repository.getTranslationResponses();
    }

    public boolean isWaitingBotTranslation() {
        return waitingBotTranslation;
    }

    public void setWaitingBotTranslation(boolean waitingBotTranslation) {
        this.waitingBotTranslation = waitingBotTranslation;
    }
    public boolean isTts()
    {
        return tts;
    }

    public void setTts(boolean tts)
    {
        this.tts = tts;
    }

    public FirebaseUser getUser()
    {
        return user;
    }

    public boolean isStarted()
    {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public void setUser(FirebaseUser user)
    {
        this.user = user;
    }

    public FirebaseDatabase getDatabase()
    {
        return database;
    }

    public DatabaseReference getReference()
    {
        return reference;
    }

    public boolean isLoading()
    {
        return loading;
    }

    public void setLoading(boolean loading)
    {
        this.loading = loading;
    }

    public void setReference(DatabaseReference reference)
    {
        this.reference = reference;
    }

    public static String getShortTime()
    {
        DateFormat dateFormat = DateFormat.getTimeInstance(DateFormat.SHORT);
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String getLongTime()
    {
        DateFormat dateFormat = DateFormat.getTimeInstance(DateFormat.DEFAULT);
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static void LongTimeToShort(String lonTime){


    }
}
