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

import com.nomercy.meetly.Model.MeetList;
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

public class AboutGroup extends AppCompatActivity {
    TextView aboutGroup;
    ImageButton addMembers;
    RecyclerView membersRecycler;
    SwipeRefreshLayout membersSwipe;
    ImageButton toBack;
    Retrofit retrofit;
    int id;
    GroupMemberAdapter mAdapter;
    ArrayList<User> membersOfGroup;
    User user = new User();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_group);

        aboutGroup = findViewById(R.id.txtGroup);
        addMembers = findViewById(R.id.buttonAddMembersToGroup);
        membersRecycler = findViewById(R.id.membersRecyclerView);
        membersSwipe = findViewById(R.id.membersSwipeToRefresh);
        toBack = findViewById(R.id.imageButton);

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

        String name = getIntent().getStringExtra("groupName1");
        id = getIntent().getIntExtra("idMemberOfGroup1",0);
        aboutGroup.setText(name);
        addMembers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AboutGroup.this, GroupMembersActivity.class);
                intent.putExtra("idOfItemOfGroup", id);
                intent.setFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                startActivity(intent);
            }
        });

       getMembersOfGroup();
        membersSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMembersOfGroup();


            }
        });
        toBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AboutGroup.this, GroupsMain.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    public void getMembersOfGroup() {
        APIInterface service = retrofit.create(APIInterface.class);
        Call<User> call = service.getMembersOfGroup(id);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
              //  Toast.makeText(AboutGroup.this, "size: " + response.body().getMembers().size(), Toast.LENGTH_LONG).show();
                if(response.body().getMembers() != null) {
                    generateMembers(response.body().getMembers());
                }

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                // handle execution failures like no internet connectivity
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

