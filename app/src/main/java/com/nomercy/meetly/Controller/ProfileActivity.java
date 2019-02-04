package com.nomercy.meetly.Controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nomercy.meetly.Model.DBHelper;
import com.nomercy.meetly.Model.User;
import com.nomercy.meetly.R;
import com.nomercy.meetly.api.APIInterface;
import com.nomercy.meetly.api.Constants;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileActivity extends AppCompatActivity {
    TextView name, telephone;
    DBHelper mDbHelper;
    Context mContext;
    Retrofit retrofit;
    ImageView edit;
    SwipeRefreshLayout swipeProfile;
    String profileName= "", surname="", profileTelephone="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mDbHelper = new DBHelper(this);
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        name = findViewById(R.id.fullName);
        telephone = findViewById(R.id.profileTelep);
        swipeProfile = findViewById(R.id.swipeToProfile);
        edit = findViewById(R.id.editProfile);

        //The logging interceptor will be added to the http client
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        retrofit = new Retrofit.Builder()
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Constants.BaseUrl)
                .build();
        setProfile();
        swipeProfile.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setProfile();
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, UpdateProfile.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                startActivity(intent);
            }
        });

    //    int id = mDbHelper.getId();


//        String userName = mDbHelper.getUserName(id);
//        String userSurname = mDbHelper.getUserSurname(id);
//        name.setText(userName + " " + userSurname);

    }

    public void setProfile() {

        APIInterface service = retrofit.create(APIInterface.class);
        int id = mDbHelper.getId();

        final Call<User> call = service.myProfile(id);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                profileName = response.body().getName();
                surname = response.body().getSurname();
                name.setText(profileName + " " + surname);
                profileTelephone = response.body().getTelephone();
                telephone.setText(profileTelephone);
                swipeProfile.setRefreshing(false);
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });

    }
}
