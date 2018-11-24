package com.nomercy.meetly;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.nomercy.meetly.api.APIInterface;
import com.nomercy.meetly.api.Constants;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewPlace extends AppCompatActivity {
    private List<Place> placeList = new ArrayList<>();
    private RecyclerView recyclerView;
    private PlaceAdapter mAdapter;
    FloatingActionButton fab;
    int user_id;
    Retrofit retrofit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_place);


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
        recyclerView = (RecyclerView) findViewById(R.id.feed);
        fab = findViewById(R.id.fabBtn);

        mAdapter = new PlaceAdapter(placeList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
       // recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);


        preparePlaceData();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewPlace();

            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Place place = placeList.get(position);
                Intent intent = new Intent();
                intent.putExtra("name", place.getPlaceName());
                intent.putExtra("id", place.getId());
                setResult(RESULT_OK, intent);
                finish();
                //Toast.makeText(getApplicationContext(), place.getPlaceName() + " is selected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    public void createNewPlace() {
        AlertDialog.Builder builder = new AlertDialog.Builder(NewPlace.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_place, null);
        final EditText namePlace = mView.findViewById(R.id.inputNamePlace);
        final EditText addressPlace = mView.findViewById(R.id.inputAddressPlace);
        Button ok = mView.findViewById(R.id.btnOk);
        builder.setView(mView);

        final AlertDialog dialog = builder.create();
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               final String  placeName = namePlace.getText().toString();
               final String  placeAddress = addressPlace.getText().toString();
                    APIInterface service = retrofit.create(APIInterface.class);
                    Call<Place> call = service.postPlace(placeName, placeAddress);
                    call.enqueue(new Callback<Place>() {
                        @Override
                        public void onResponse(Call<Place> call, Response<Place> response) {
                            response.body();
                            Intent intent = new Intent();
                            intent.putExtra("another_place", placeName);
                            intent.putExtra("address", placeAddress);
                            intent.putExtra("another_id", response.body().getId());
                            setResult(RESULT_OK, intent);
                            finish();
                            dialog.dismiss();
                           // Toast.makeText(GroupsMain.this, "Группа успешно создана. Обновите страницу", Toast.LENGTH_LONG).show();
                        }
                        @Override
                        public void onFailure(Call<Place> call, Throwable t) {
                        }
                    });


            }
        });

        dialog.show();

    }



    private void preparePlaceData() {
        Place place = new Place(1,"Merry Berry", "ул. Горького, 5а, Симферополь", R.drawable.merry_berry_place);
        placeList.add(place);
        place = new Place(2,"ШашлыкоFF", "Симферополь, просп. Кирова 24", R.drawable.shashlik);
        placeList.add(place);
        place = new Place(3,"Garry's", "ул. Карла Маркса, Симферополь", R.drawable.garry);
        placeList.add(place);
        place = new Place(4,"Ресторан Мартини", "ул. Ушинского, 4, Симферополь", R.drawable.martini);
        placeList.add(place);
        place = new Place(5,"Shen", "ул. Генерала Васильева, 40, Симферополь", R.drawable.shen);
        placeList.add(place);
        place = new Place(6,"Кофейня Rabbit Burrow", "просп. Кирова, 11, Симферополь", R.drawable.rabbit);
        placeList.add(place);

    }
}