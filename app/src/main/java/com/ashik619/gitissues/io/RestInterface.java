package com.ashik619.gitissues.io;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by ashik619 on 11-03-2017.
 */
public interface RestInterface {

    @GET("issues")
    Call<JsonArray> getAllIssues();

    @GET("issues/{number}/comments")
    Call<JsonArray> getComments(@Path("number") String number);


}
