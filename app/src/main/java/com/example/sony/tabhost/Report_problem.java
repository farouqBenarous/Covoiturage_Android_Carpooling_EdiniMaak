package com.example.sony.tabhost;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.crash.FirebaseCrash;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Report_problem extends AppCompatActivity {
EditText inputemail,inputreporttext;
String   email,reporttext;
ProgressBar progressBar;
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_problem);
        progressBar = findViewById(R.id.pb);
        inputemail = findViewById(R.id.email);
        inputreporttext = findViewById(R.id.reprottext);
    }

    public void  send (View view) {
    email = inputemail.getText().toString();
    reporttext = inputreporttext.getText().toString();
        if (TextUtils.isEmpty(email)) {
            inputemail.setError("Enter your Email");
            Toast.makeText(getApplicationContext(), "Enter  your Email ", Toast.LENGTH_SHORT).show();
            return;
        }
        if (checkemail(email) ==false) {
            inputemail.setError("Email Badly Formed");
            Toast.makeText(getApplicationContext(), "Email Badly Formed ", Toast.LENGTH_SHORT).show();
            return;
        }


        if (TextUtils.isEmpty(reporttext)) {
            inputreporttext.setError("Enter The Report Text");
            Toast.makeText(getApplicationContext(), "Enter The  Report Text ", Toast.LENGTH_SHORT).show();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);

        FirebaseCrash.report(new Exception("Probleme"+reporttext));
        progressBar.setVisibility(View.GONE);


Toast.makeText(getApplicationContext(),"Your Crash Has been Reported",Toast.LENGTH_SHORT).show();
        startActivity(new Intent(Report_problem.this,AppSetting.class));

    }


    public boolean checkemail(String email) {
        Pattern pattern = Pattern.compile(".+@.+\\.[a-z]+");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();}

}
