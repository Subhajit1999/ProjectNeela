package com.subhajitkar.commercial.project_neela.utils;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIManager {

    //news api call
    @GET("everything")
    Call<JsonObject> getDailyNews (
            @Query("q") String query,
            @Query("language") String lang,
            @Query("sortBy") String sortBy,
            @Query("from") String fromDate,
            @Query("apiKey") String auth_key);
}
