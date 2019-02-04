package com.nomercy.meetly.Controller;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class UpdateProfile extends AppCompatActivity {
    Retrofit retrofit;
    private EditText name, surname, phone;
    private DBHelper mDBHelper;
    private Button change;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_profile);

        mDBHelper = new DBHelper(this);

        name = findViewById(R.id.enterNameUpdate);
        surname = findViewById(R.id.enterSurnameUPdate);
        phone = findViewById(R.id.enterPhoneUPdate);
        change = findViewById(R.id.btnChange);

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

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile();
            }
        });


    }

    public void updateProfile () {
        APIInterface service = retrofit.create(APIInterface.class);
        int id = mDBHelper.getId();
        String userName = name.getText().toString();
        String userSurname = surname.getText().toString();
        String userPhone = phone.getText().toString();
        if(userName.length() == 0) {
            name.setText("");
        } else if(userSurname.length() == 0) {
            surname.setText("");
        } else if(userPhone.length() == 0) {
            phone.setText("");
        }

        final Call<User> call = service.updateProfile(id, userName, userSurname, userPhone);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Toast.makeText(getBaseContext(), "Ваши данные успешно обновлены", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });

    }
}
