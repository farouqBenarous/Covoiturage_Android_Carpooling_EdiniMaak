package com.example.sony.tabhost;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class Show_result_friend_search extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_result_friend_search);
        Intent intent = getIntent();
        String a ;
        a = intent.getStringExtra("query");
        Toast.makeText(getApplicationContext(),a,Toast.LENGTH_SHORT).show();
    }
}
