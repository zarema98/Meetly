package com.nomercy.meetly.Controller;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nomercy.meetly.R;

import java.util.ArrayList;

public class ScoreActivity extends AppCompatActivity {
    EditText name1, name2;
    TextView tv;
    ImageButton addItem;
    int matchParent1 = LinearLayout.LayoutParams.MATCH_PARENT;
    RecyclerView feed;
    int positionInfo = 0;
    final ArrayList<String> list = new ArrayList<>();
    final ArrayList<String> list2 = new ArrayList<>();

    ScoreAdapter adapter;
    LinearLayoutManager manager;
    LinearLayout LR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(ScoreActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_app_apdates, null);
        CheckBox mCheckBox = mView.findViewById(R.id.checkBox);
        mBuilder.setTitle("Подсказка");
        mBuilder.setMessage("Данная функция позволяет удобно оплатить счет в ресторанах кафе и тд. Вам лишь необходимо ввести наименование товара и его цену, при необходимости вы можете добавить еще поля для ввода!");
        mBuilder.setView(mView);
        mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){
                    storeDialogStatus(true);
                    //По идеи это нажати чекБокса
                }else{
                    storeDialogStatus(false);
                }
            }
        });

        if(getDialogStatus()){
            mDialog.hide();
        }else{
            mDialog.show();
        }


        //LR = findViewById(R.id.server);
        addItem = findViewById(R.id.btnAddItem);
        name1 = findViewById(R.id.Name1);
        name2 = findViewById(R.id.Name2);
        feed = findViewById(R.id.recyclerViewS);
        tv = findViewById(R.id.textView3);
        addListenerOnButton();
    }
    private void storeDialogStatus(boolean isChecked){
        SharedPreferences mSharedPreferences = getSharedPreferences("CheckItem", MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putBoolean("item", isChecked);
        mEditor.apply();
    }

    private boolean getDialogStatus(){
        SharedPreferences mSharedPreferences = getSharedPreferences("CheckItem", MODE_PRIVATE);
        return mSharedPreferences.getBoolean("item", false);
    }

    public void addListenerOnButton() {

        addItem.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (name1.length() > 0 && name2.length() > 0 && isNumeric(name2.getText().toString())) {
                            vivod();
                            name1.setText("");
                            name2.setText("");
                        } else if(!isNumeric(name2.getText().toString())) {
                                Toast.makeText(ScoreActivity.this, "Введите корректные данные!", Toast.LENGTH_LONG).show();

                        }
                        else{
                            Toast.makeText(ScoreActivity.this,"Запоните поля",Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );
    }
    void vivod(){
        manager = new LinearLayoutManager(this);
    positionInfo += Integer.parseInt(name2.getText().toString());
    tv.setText(String.valueOf(positionInfo));

        list.add(name1.getText().toString());
        //final ArrayList<String> list2 = new ArrayList<>();
        list2.add(name2.getText().toString());



        adapter = new ScoreAdapter(list,list2, this);
        feed.setAdapter(adapter);
        feed.setLayoutManager(manager);
        feed.setVisibility(View.VISIBLE);
    }

    public static boolean isNumeric(String str)
    {
        return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
    }
}


