package com.nomercy.meetly.Controller;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.nomercy.meetly.Model.User;
import com.nomercy.meetly.R;

import java.util.ArrayList;
import java.util.Objects;

public class MembersActivity extends AppCompatActivity {
    Cursor cursor ;
    String name, phonenumber ;

    RecyclerView members;
    ArrayList<String> contacts = new ArrayList<String>();
    ArrayList<String> tell = new ArrayList<String>();
    UserAdapter adapter;
    User user;
   ArrayList<User> users = new ArrayList<>();
    ImageButton btnDone;
    StringBuilder stringBuilder = null;
    EditText editSearch;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members);
        //getSupportLoaderManager().initLoader(1, null, this);
        members = findViewById(R.id.membersList);
        btnDone = findViewById(R.id.btnDone);
        editSearch = findViewById(R.id.edit_search);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getContacts();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                btnDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        stringBuilder = new StringBuilder();
                        int i=0;
                        ArrayList<Integer> ids = new ArrayList<>();
                        String [] telephones = new String[adapter.checkedUsers.size()];
                        char ch = ',';
                        do {
                            User user = adapter.checkedUsers.get(i);
                            if(user.getId() != 0)
                                ids.add(user.getId());
                            telephones[i] = user.getTelephone();
                            if(i == adapter.checkedUsers.size()-1) ch = '.';
                            stringBuilder.append(user.getName() + ch);
                            if(i != adapter.checkedUsers.size() -1) {
                                stringBuilder.append("\n");
                            }
                            i++;

                        } while (i < adapter.checkedUsers.size());
                        if(adapter.checkedUsers.size() > 0) {
                            Intent intent = new Intent();
                            intent.putExtra("names", stringBuilder.toString());
                            // Toast.makeText(MembersActivity.this, "tel: " + telephones[0], Toast.LENGTH_LONG).show();
//                    if(ids.size() > 0) {
//                        Toast.makeText(MembersActivity.this, "size: " + ids.size(), Toast.LENGTH_LONG).show();
//                    }
                            intent.putExtra("ids", ids);

                            setResult(RESULT_OK, intent);
                            finish();


                        } else {
                            Toast.makeText(MembersActivity.this, "Пожалуйста, выберите друзей", Toast.LENGTH_LONG).show();

                        }
                    }
                });


            }
        },2500);

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
//            Toast.makeText(MembersActivity.this, "size cont: " + contacts.size(), Toast.LENGTH_LONG).show();
//            Toast.makeText(MembersActivity.this, "size TEl: " + tell.size(), Toast.LENGTH_LONG).show();

        }

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        adapter = new UserAdapter(users, this);
        members.setLayoutManager(mLayoutManager);
        members.setItemAnimator(new DefaultItemAnimator());
        members.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        members.setAdapter(adapter);
        //contactList.setAdapter(adapter2);


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
