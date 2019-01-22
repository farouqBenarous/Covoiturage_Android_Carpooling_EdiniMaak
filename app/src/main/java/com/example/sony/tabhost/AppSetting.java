package com.example.sony.tabhost;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.intellij.lang.annotations.Language;

public class AppSetting extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_setting);
        ListView listView = findViewById(R.id.listview);

        AdapterView.OnItemClickListener itemClickListener =new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View v, int position, long id) {
                if (position==0) {
                    Intent intent = new Intent(AppSetting.this, com.example.sony.tabhost.Language.class);
                    startActivity(intent);
                }
                if (position==1) {
                    Intent intent = new Intent(AppSetting.this,ContactUs.class);
                    startActivity(intent);
                }
                if (position==2) {
                    Intent intent = new Intent(AppSetting.this,About.class);
                    startActivity(intent);
                }
            }
        };

        listView.setOnItemClickListener( itemClickListener);

    }
}
