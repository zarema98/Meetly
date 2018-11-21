package com.nomercy.meetly;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nomercy.meetly.api.APIInterface;
import com.nomercy.meetly.api.Constants;
import com.nomercy.meetly.api.User;

import java.text.DateFormat;
import java.util.Calendar;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class NewMeetingFragment extends Fragment  {

    Button button5, createMeet, btnDate;
    Retrofit retrofit;
    EditText name, description, dateInput;
    String nameS, descriptionS;
    ProgressBar createMeetProgressBar;
    DBHelper mDbHelper;
    int id;
    String message;
    Calendar c;
    DatePickerDialog datePickerDialog;
    private FragmentActivity myContext;



    public interface onSomeEventListener {
        void someEvent(String s);
    }

    AuthorizationFragment.onSomeEventListener someEventListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity;

        if (context instanceof Activity){
            activity = (Activity) context;
            try {
                someEventListener = (AuthorizationFragment.onSomeEventListener) activity;
            } catch (ClassCastException e) {
                throw new ClassCastException(activity.toString() + " must implement onSomeEventListener");
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v2 =  inflater.inflate(R.layout.fragment_new_meeting, container, false);

        button5 = v2.findViewById(R.id.button5);
        btnDate = v2.findViewById(R.id.btnDate);
        createMeet = v2.findViewById(R.id.btn_createMeet);
        name = v2.findViewById(R.id.nameInput);
        description = v2.findViewById(R.id.descriptionInput);
        dateInput = v2.findViewById(R.id.dateInput);
        createMeetProgressBar = v2.findViewById(R.id.createMeetProgressBar);
        createMeetProgressBar.setVisibility(View.GONE);
        mDbHelper = new DBHelper(getContext());

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

        addListenerOnButton();
        return v2;
    }

    // Сканер косаний:
    public void addListenerOnButton() {
        button5.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        someEventListener.someEvent("backToMain"); // глвынй экран
                    }
                }
        );

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //chooseDate();
            }
        });

        createMeet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createMeet();
            }
        });
    }


    public void chooseDate() {
        c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);

//        datePickerDialog = new DatePickerDialog(getContext() , new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
//
//            }
//        });

    }

   public void createMeet() {
       APIInterface service = retrofit.create(APIInterface.class);
       nameS = name.getText().toString();
       descriptionS = description.getText().toString();
       id = mDbHelper.getId();

       if (nameS.length() <= 0) {
           Toast.makeText(getContext(), "Название встречи должно быть заполнено", Toast.LENGTH_LONG).show();
       } else {
           createMeetProgressBar.setVisibility(View.VISIBLE);
           createMeetProgressBar.setProgress(20);
           createMeetProgressBar.setMax(70);
       Call<User> call = service.post(nameS, "2018-12-12", "12:12", descriptionS, "photo", 1, new int[]{62, 63, 64}, id);
       call.enqueue(new Callback<User>() {
           @Override
           public void onResponse(Call<User> call, Response<User> response) {
               response.body();
               message = response.body().getMessage();
               Toast.makeText(getContext(), message, Toast.LENGTH_LONG);
               someEventListener.someEvent("backToMain");
           }

           @Override
           public void onFailure(Call<User> call, Throwable t) {
           }
       });
   }


    }

}
