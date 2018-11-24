package com.nomercy.meetly.api;

import com.nomercy.meetly.Model.GroupList;
import com.nomercy.meetly.Model.Groups;
import com.nomercy.meetly.Model.Meet;
import com.nomercy.meetly.Model.MeetList;
import com.nomercy.meetly.Model.Place;
import com.nomercy.meetly.Model.User;

import java.security.acl.Group;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface APIInterface {
    @FormUrlEncoded
    @POST("/api/auth/authenticate")
    Call<User>post(
            @Field("telephone") String method
    );
    @FormUrlEncoded
    @POST("/api/auth/registerConfirmSms")
    Call<User>post(
            @Field("code") String code,
            @Field("telephone") String telephone
    );
    @FormUrlEncoded
    @POST("/api/auth/loginConfirmSms")
    Call<User>postlogin(
            @Field("code") String code,
            @Field("telephone") String telephone
    );
    @FormUrlEncoded
    @POST("/api/auth/add_register")
    Call<User>post(
            @Field("name") String name,
            @Field("surname") String surname,
            @Field("photo") String photo,
            @Field("user_id") int user_id
    );

    @FormUrlEncoded
    @POST("/api/meets/createMeet")
    Call<User>post(
            @Field("name") String name,
            @Field("date") String date,
            @Field("time") String time,
            @Field("description") String description,
            @Field("photo") String photo,
            @Field("place_id") int place,
            @Field("user_array") int []participant,
            @Field("user_id") int user_id

    );

    @GET("/api/meets/getMeets/{id}")
    Call<MeetList>get(
            @Path("id") int id
    );

    @GET("/api/auth/logout/{id}")
    Call<User>logout(
            @Path("id") int id
    );

    @FormUrlEncoded
    @POST("/api/groups/createGroup")
    Call<Groups>post(
            @Field("name") String name,
            @Field("user_id") int user_id
    );

    @GET("/api/groups/getGroups/{id}")
    Call<GroupList>getGroup(
            @Path("id") int id
    );


    @FormUrlEncoded
    @POST("/api/meets/anotherPlace")
    Call<Place>postPlace(
            @Field("name") String name,
            @Field("adress") String adress


    );

    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "/api/meets/deleteMeet", hasBody = true)
    Call<Meet>deleteMeet(
            @Field("user_id") int user_id,
            @Field("meet_id") int meet_id
    );


    @FormUrlEncoded
    @POST("/api/meets/restoreMeet")
    Call<Meet>restoreMeet(
            @Field("user_id") int user_id,
            @Field("meet_id") int meet_id
    );

    @GET("/api/groups/addMembersToGroup")
    Call<Group>addMembers(
            @Field("members_array") int []participant,
            @Field("group_id") int group_id
    );
}

