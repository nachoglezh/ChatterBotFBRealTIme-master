package com.example.chatterbot.model.rest;

import com.example.chatterbot.model.data.TranslationResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Api
{
    @POST("ttranslatev3")
    @FormUrlEncoded
    Call<List<TranslationResponse>> postTranslator(@Field("fromLang") String fromLang, @Field("text") String text, @Field("to") String to);
}
