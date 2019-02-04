package com.nomercy.meetly.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.nomercy.meetly.Model.User;
import com.nomercy.meetly.R;
import com.nomercy.meetly.api.APIInterface;
import com.nomercy.meetly.api.Constants;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AboutMeet extends AppCompatActivity {
    private int meet_id;
    private TextView aboutMeet;
    private ImageButton toBack;
    Retrofit retrofit;
    private  RecyclerView membersRecycler;
    GroupMemberAdapter mAdapter;
    private SwipeRefreshLayout membersSwipe;
    private User user = new User();
    ArrayList<User> membersOfGroup;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_meet);

        aboutMeet = findViewById(R.id.txtGroup);
        toBack = findViewById(R.id.imageButton);
        membersRecycler = findViewById(R.id.membersRecyclerView);
        membersSwipe = findViewById(R.id.membersSwipeToRefresh);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        //The logging interceptor will be added to the http client
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        retrofit = new Retrofit.Builder()
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Constants.BaseUrl)
                .build();


        String name = getIntent().getStringExtra("meetName2");
        meet_id = getIntent().getIntExtra("idMembersOfMeet2",0);
    //    Toast.makeText(this, ": " + meet_id, Toast.LENGTH_SHORT).show();
        aboutMeet.setText(name);
        getMembersOfMeet();

        membersSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMembersOfMeet();
            }
        });

        toBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public void getMembersOfMeet() {
        APIInterface service = retrofit.create(APIInterface.class);
        Call<User> call = service.getMembersOfMeet(meet_id);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.body().getMembers() != null) {
                    generateMembers(response.body().getMembers());
                }

            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    public void generateMembers(ArrayList<String> arrayList) {
        //  Toast.makeText(this, "size: " + arrayList.size(), Toast.LENGTH_LONG).show();
//        for(int i=0; i< arrayList.size(); i++) {
//            user = new User(arrayList.get(i));
//            membersOfGroup.add(user);
//        }

        mAdapter = new GroupMemberAdapter(arrayList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        membersRecycler.setLayoutManager(mLayoutManager);
        membersRecycler.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        membersRecycler.setAdapter(mAdapter);
        membersSwipe.setRefreshing(false);


    }
}
