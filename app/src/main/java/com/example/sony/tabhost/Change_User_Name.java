package com.example.sony.tabhost;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class Change_User_Name extends AppCompatActivity {
    EditText oldusername ;
    EditText newusername;
    String soldusername;
    String snewusername;
    String olduser;
    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    ProgressBar progressBar;
String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change__user__name);
        oldusername = findViewById(R.id.oldusername);
        newusername = findViewById(R.id.newusername);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("User");
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        assert firebaseUser != null;
        olduser  = firebaseUser.getDisplayName();
        email = firebaseUser.getEmail();
        oldusername.setText(olduser);
        progressBar = findViewById(R.id.progressBar5);

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public  void Save (View view) {
        soldusername= oldusername.getText().toString();
        snewusername = newusername.getText().toString();
        progressBar = findViewById(R.id.progressBar5);


        if (TextUtils.isEmpty(soldusername)) {
            oldusername.setError("Enter Old User Name  ");
            Toast.makeText(getApplicationContext(), "Enter Old  User Name ! ", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!Objects.equals(soldusername, olduser)) {
            oldusername.setError("User Name Don't match with your Old user Name ");
            Toast.makeText(getApplicationContext(), "User Name Don't match  ! ", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(snewusername)) {
            newusername.setError("Enter New User Name  ");
            Toast.makeText(getApplicationContext(), "Enter New  User Name ! ", Toast.LENGTH_SHORT).show();
            return;
        }

        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                .setDisplayName(snewusername)
                .build();
        if (firebaseUser != null) {
            firebaseUser.updateProfile(profileUpdate)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                progressBar.setVisibility(View.VISIBLE);
                        reference.orderByChild("email").equalTo(email).addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                             String uid = dataSnapshot.getKey().toString();
                             reference.child(uid).child("fullName").setValue(snewusername);
                              progressBar.setVisibility(View.GONE);
                             Toast.makeText(getApplicationContext(),"User Name updated succefuly ",Toast.LENGTH_SHORT).show();
                                AlertDialog.Builder adb = new AlertDialog.Builder(Change_User_Name.this);
                                adb.setTitle("User Name Updated");
                                adb.setMessage("  UserName Successfuly Updated  ");
                                adb.setCancelable(false);

                                adb.setPositiveButton(" OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        startActivity(new Intent(Change_User_Name.this,AccountSetting.class));                                                   finish();

                                    }});
                                adb.show();
                            }
                            @Override
                            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                            @Override
                            public void onChildRemoved(DataSnapshot dataSnapshot) {}
                            @Override
                            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                            @Override
                            public void onCancelled(DatabaseError databaseError) {}});
                            }}});
        }
    }
}
