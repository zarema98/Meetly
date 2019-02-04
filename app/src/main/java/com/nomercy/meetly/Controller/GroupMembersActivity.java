package com.nomercy.meetly.Controller;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.nomercy.meetly.Model.Groups;
import com.nomercy.meetly.Model.MeetList;
import com.nomercy.meetly.Model.User;
import com.nomercy.meetly.R;
import com.nomercy.meetly.api.APIInterface;
import com.nomercy.meetly.api.Constants;

import java.util.ArrayList;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GroupMembersActivity extends AppCompatActivity {
    RecyclerView members;
    ArrayList<String> contacts = new ArrayList<String>();
    ArrayList<String> tell = new ArrayList<String>();
    UserAdapter adapter;
    User user;
    ArrayList<User> users = new ArrayList<>();
    StringBuilder stringBuilder = null;
    EditText editSearch;
    ImageButton btnDone;
    Cursor cursor ;
    String name, phonenumber ;
    int group_id;
    Retrofit retrofit;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members);
        members = findViewById(R.id.membersList);
        btnDone = findViewById(R.id.btnDone);
        editSearch = findViewById(R.id.edit_search);
        getContacts();
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
        group_id = getIntent().getIntExtra("idOfItemOfGroup",0);
      //  Toast.makeText(this, "id: " + group_id, Toast.LENGTH_LONG).show();



        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                btnDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                      final ArrayList<Integer> ids = new ArrayList<>();

                        int i=0;
                        try {
                            do {
                                User user = adapter.checkedUsers.get(i);
                                if (user.getId() != 0)
                                    ids.add(user.getId());
                                i++;
                            } while (i < adapter.checkedUsers.size());
                        } catch (IndexOutOfBoundsException e)  {
                            Toast.makeText(GroupMembersActivity.this, "Пожалуйста, выберите друзей", Toast.LENGTH_LONG).show();
                        }
                        if(ids.size() == 0 ) {
                            ids.add(63);
                            ids.add(64);
                        } else if(ids.size() == 1) {
                            ids.add(63);
                            ids.add(64);
                        } else if(ids.size() == 2) {
                            ids.add(63);
                        }
                      //  Toast.makeText(GroupMembersActivity.this, "size: " + ids.size(),Toast.LENGTH_LONG).show();
                        if(adapter.checkedUsers.size() > 0) {
                            APIInterface service = retrofit.create(APIInterface.class);
                            Call<Groups> call = service.addMembersToGroup(ids, group_id);
                            call.enqueue(new Callback<Groups>() {
                                @Override
                                public void onResponse(Call<Groups> call, Response<Groups> response) {
                                    if(ids.size() == 2) {
                                        Toast.makeText(GroupMembersActivity.this, "К сожалению, среди выбранных друзей, нет зарегистрированных в приложении Meetly.", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(GroupMembersActivity.this, "Участники успешно добавлены.", Toast.LENGTH_LONG).show();
                                        onBackPressed();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Groups> call, Throwable t) {
                                }
                            });

                        }
                         else {
                            Toast.makeText(GroupMembersActivity.this, "Пожалуйста, выберите друзей", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        }, 2500);

        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.getFilter().filter(charSequence);
                return;

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    public void getContacts() {
        cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null, null, null);

        while (cursor.moveToNext()) {


            name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

            phonenumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));


            contacts.add(name);
            tell.add(phonenumber);
        }
        cursor.close();

        if(tell.size() > contacts.size()) {
            for(int i=0; i <contacts.size(); i++) {
                user = new User(tell.get(i), contacts.get(i));
                users.add(user);
            }
        } else {
            for(int i=0; i < tell.size(); i++) {
                user = new User(tell.get(i), contacts.get(i));
                users.add(user);
            }
        }
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        adapter = new UserAdapter(users, this);
        members.setLayoutManager(mLayoutManager);
        members.setItemAnimator(new DefaultItemAnimator());
        members.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        members.setAdapter(adapter);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
