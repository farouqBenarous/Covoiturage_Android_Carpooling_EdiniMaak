package com.example.sony.tabhost;

import android.content.Intent;
import android.os.Build;
import android.os.RecoverySystem;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class Firstpage extends AppCompatActivity {
    ProgressBar progressBar;
    Button btnlogin,btnsignup;
    String phone;
    FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstpage);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        final DatabaseReference myref;
        FirebaseUser firebaseUser;
        progressBar = (ProgressBar) findViewById(R.id.progr);
        btnlogin = findViewById(R.id.goLogine);
        btnsignup = findViewById(R.id.gosignup);
        progressBar.setVisibility(View.VISIBLE);
        database = FirebaseDatabase.getInstance();
        myref = database.getReference("User");

        if (auth.getCurrentUser() == null) {
            progressBar.setVisibility(View.GONE);
            btnsignup.setVisibility(View.VISIBLE);
            btnlogin.setVisibility(View.VISIBLE);}
        else {
            FirebaseUser User = FirebaseAuth.getInstance().getCurrentUser();
            assert User != null;
            String username = User.getEmail();
            myref.orderByChild("email").equalTo(username).addChildEventListener(new ChildEventListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    String Uid = dataSnapshot.getKey();
                    String  phonenumber = dataSnapshot.child("phone_number").getValue().toString();

                    if (Objects.equals(phonenumber, "5")) {
                        Intent intent = new Intent(Firstpage.this,validation_emailphonenumber.class);
                        progressBar.setVisibility(View.GONE);
                        startActivity(intent);
                        finish();
                    }
                    else  {
                        Intent intent = new Intent(Firstpage.this,MainActivity.class);
                        progressBar.setVisibility(View.GONE);
                        startActivity(intent);
                        finish();}


                }
                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {}
                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                @Override
                public void onCancelled(DatabaseError databaseError) {}});
        }
    }




    public void   goLogine (View view) {
        Intent intent = new Intent(Firstpage.this,LoginActivity.class);
        startActivity(intent);
    }
    public void gosignup (View view) {
        Intent intent = new Intent(Firstpage.this,SignupActivity.class);
        startActivity(intent);}


    public Boolean emailexist (String email , DataSnapshot dataSnapshot ) {
        // check if the email is already exist or not
        modeluser user = new modeluser();

        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            user.setEmail(ds.getValue(modeluser.class).getEmail());

            if (user.getEmail().equals(email) ) {return true;}

        }

        return false;
    }

}



