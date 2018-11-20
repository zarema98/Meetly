package com.nomercy.meetly.api;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface APIInterface {
    @FormUrlEncoded
    @POST("/api/auth/authenticate")
    Call<User>post(
            @Field("telephone") String method
    );
    @FormUrlEncoded
    @POST("api/auth/registerConfirmSms")
    Call<User>post(
            @Field("code") String code,
            @Field("telephone") String telephone
    );
    @FormUrlEncoded
    @POST("api/auth/loginConfirmSms")
    Call<User>postlogin(
            @Field("code") String code,
            @Field("telephone") String telephone
    );
    @FormUrlEncoded
    @POST("api/auth/add_register")
    Call<User>post(
            @Field("name") String name,
            @Field("surname") String surname,
            @Field("photo") String photo,
            @Field("user_id") int user_id
    );

    @FormUrlEncoded
    @POST("api/meets/createMeet")
    Call<User>post(
            @Field("name") String name,
            @Field("date") String date,
            @Field("time") String time,
            @Field("description") String description,
            @Field("photo") String photo,
            @Field("place_id") int place,
            @Field("user_array") int []participant,
            @Field("user_id") String user_id

    );


}

