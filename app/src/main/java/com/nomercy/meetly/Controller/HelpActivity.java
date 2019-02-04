package com.nomercy.meetly.Controller;

import android.support.v7.app.AppCompatActivity;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.nomercy.meetly.Model.HelpDatabase;
import com.nomercy.meetly.R;

import java.io.IOException;
import java.util.ArrayList;

public class HelpActivity extends AppCompatActivity {

    RecyclerView feed;
    TextView textView, headInfo, statusName;
    ScrollView helpInfoSV;
    ImageButton back;
    final ArrayList<String> list = new ArrayList<>();
    final ArrayList<String> list2 = new ArrayList<>();

    String idCursor, helpNameCursor, helpInfoCursor;
    String screenStatus = "finish";

    HelpAdapter helpAdapter;
    LinearLayoutManager manager;

    HelpDatabase HDB;
    SQLiteDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        feed = findViewById(R.id.helpName);
        textView = findViewById(R.id.helpInfo);
        helpInfoSV = findViewById(R.id.helpInfoSV);
        headInfo = findViewById(R.id.headInfo);
        statusName = findViewById(R.id.statusName);
        back = findViewById(R.id.back);

        manager = new LinearLayoutManager(this);

        HDB = new HelpDatabase(this);

        try {
            HDB.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }

        try {
            mDb = HDB.getWritableDatabase();
        } catch (SQLException mSQLException) {
            throw new Error("UnableToWritableDatabase");
        }

        Cursor cursor = mDb.rawQuery("SELECT * FROM help", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            idCursor = cursor.getString(0);
            helpNameCursor = cursor.getString(1);
            //checkPageNumber = Integer.parseInt(checkPageNumberCursor);

            list.add(idCursor);
            list2.add(helpNameCursor);

            cursor.moveToNext();
        }
        cursor.close();

        helpAdapter = new HelpAdapter(list, list2, this);
        feed.setAdapter(helpAdapter);
        feed.setLayoutManager(manager);

        helpAdapter.setOnItemClickListener(new HelpAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String name = list.get(position);

                Cursor cursor = mDb.rawQuery("SELECT * FROM help", null);
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    idCursor = cursor.getString(0);

                    if(name.equals(idCursor)) {
                        helpNameCursor = cursor.getString(1);
                        helpInfoCursor = cursor.getString(2);
                        screenStatus = "info";
                    }
                    cursor.moveToNext();
                }
                cursor.close();

                textView.setText(helpInfoCursor);
                feed.setVisibility(View.GONE);
                helpInfoSV.setVisibility(View.VISIBLE);
                headInfo.setText("Вопрос");
                statusName.setText(helpNameCursor);
            }
        });

        addListenerOnButton();
    }


    public void addListenerOnButton() {
        back.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(screenStatus.equals("finish")) {
                            finish();
                        }
                        else if(screenStatus.equals("info")) {
                            screenStatus = "finish";
                            feed.setVisibility(View.VISIBLE);
                            helpInfoSV.setVisibility(View.GONE);
                            headInfo.setText("Помощь");
                            statusName.setText("Общие вопросы");
                        }
                    }
                }
        );
    }
}
