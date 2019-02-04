package com.nomercy.meetly.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.nomercy.meetly.R;

import org.w3c.dom.Text;

public class ResultActivity extends AppCompatActivity {
    private TextView txtDate, txtTime;
    String date, time;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_notification);

        txtDate = findViewById(R.id.dateOfMeet);
        txtTime = findViewById(R.id.timaOfMeet);


        Toast.makeText(this, date, Toast.LENGTH_SHORT).show();
//        txtDate.setText(date);
//        txtTime.setText(time);
}

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        date = intent.getStringExtra("dateOfMeet222");
        time = intent.getStringExtra("timeOfMeet222");
        setIntent(intent);
    }


}
