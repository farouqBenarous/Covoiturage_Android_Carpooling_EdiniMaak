package com.example.sony.tabhost;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Aboutprofile extends AppCompatActivity {
Button bemail,bphone;
String gender,Email,Phone,UID,username,age;
TextView Gender,Age,Username;
FirebaseDatabase database;
DatabaseReference reference;
FirebaseAuth auth;
FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutprofile);
     database = FirebaseDatabase.getInstance();
     reference = database.getReference("User");
     auth = FirebaseAuth.getInstance();
     firebaseUser = auth.getCurrentUser();
     Intent intent = getIntent();
     UID = intent.getStringExtra("email");
     bemail = findViewById(R.id.emailceo);
     bphone = findViewById(R.id.callceo);
     Gender = findViewById(R.id.gender);
     Age = findViewById(R.id.age);
     Username = findViewById(R.id.username); }

    @Override
    protected void onStart() {
        super.onStart();

        reference.orderByChild("email").equalTo(UID).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Email = dataSnapshot.child("email").getValue(String.class);
                Phone = dataSnapshot.child("phone_number").getValue(String.class);
                gender = dataSnapshot.child("gender").getValue(String.class);
                username = dataSnapshot.child("fullName").getValue(String.class);
                age = dataSnapshot.child("age").getValue(String.class);

                bemail.setText(Email);
                bphone.setText(Phone);
                Gender.setText(gender);
                Age.setText(age);
                Username.setText(username); }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onCancelled(DatabaseError databaseError) {}});

    }

    public void CallCeo(View view) {

        Intent i = new Intent(Intent.ACTION_CALL);
        i.setData(Uri.parse("tel:"+Phone));
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(),"Please Grant The Permission To call",Toast.LENGTH_SHORT).show();
            RequestPermissions();
        }
        else {
            startActivity(i);}

    }
    public void RequestPermissions() {
        ActivityCompat.requestPermissions(this,new String[] {android.Manifest.permission.CALL_PHONE},1);



    }
    public void MsgCeo (View view) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setData(Uri.parse(Email));
        i.putExtra(Intent.EXTRA_EMAIL,Email);
        i.putExtra(Intent.EXTRA_SUBJECT,"Carpooling Android Application");
        i.putExtra(Intent.EXTRA_TEXT,"Hello Mr.Benarous \n");
        i.setType("message/rfc822");
        Intent chooser = Intent.createChooser(i,"Launch Email App");
        startActivity(chooser);



    }

}
