package com.example.sony.tabhost;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.sony.tabhost.Helper.Helper;

import io.paperdb.Paper;

public class About extends AppCompatActivity {

    TextView textView;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

       getMenuInflater().inflate(R.menu.language,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.en) {
            Paper.book().write("language","en");
            updateView ((String)Paper.book().read("language") );
        }
        else if (item.getItemId() == R.id.fr) {
            Paper.book().write("language","fr");
            updateView ((String)Paper.book().read("language") );
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    textView = findViewById(R.id.text);

    //in Paper first
        Paper.init(this);


    //default language
        String language = Paper.book().read("language");
        if (language== null)
            Paper.book().write("language","en");

        updateView ((String)Paper.book().read("language") );

    }

    private void updateView(String laug) {
    Context context = Helper.setLocale(this,laug);
        Resources resources = context.getResources();
        //textView.setText(resources.getString(R.string.hello));

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(Helper.onAttache(newBase,"en"));
    }


}
