package com.example.chatterbot.model.repository;


import androidx.lifecycle.MutableLiveData;
import com.example.chatterbot.model.data.TranslationResponse;
import com.example.chatterbot.model.rest.Api;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Repository {
    private Api api;
    private MutableLiveData<List<TranslationResponse>> translationResponses = new MutableLiveData<>();

    public Repository() {
        retrieveClient();
    }

    private void retrieveClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.bing.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(Api.class);
    }

    public void translate(String fromLang, String text, String to) {
        Call<List<TranslationResponse>> call = api.postTranslator(fromLang, text, to);
        call.enqueue(new Callback<List<TranslationResponse>>() {
            @Override
            public void onResponse(Call<List<TranslationResponse>> call, Response<List<TranslationResponse>> response) {
                translationResponses.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<TranslationResponse>> call, Throwable t) {
                translationResponses = new MutableLiveData<>();
            }
        });
    }

    public MutableLiveData<List<TranslationResponse>> getTranslationResponses() {
        return translationResponses;
    }
}
