package com.example.sony.tabhost;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class Change_Password extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    final String[] pass = new String[1];
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change__password);
        ProgressBar progressBar = findViewById(R.id.progressBar4);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("User");
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        final String email = firebaseUser.getEmail();
        reference.orderByChild("email").equalTo(email).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                pass[0] =  dataSnapshot.child("password").getValue().toString();
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });




    }

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public void save (View view) {
    final ProgressBar progressBar = findViewById(R.id.progressBar4);
    database = FirebaseDatabase.getInstance();
    reference = database.getReference("User");
    auth = FirebaseAuth.getInstance();
    firebaseUser = auth.getCurrentUser();
   final FirebaseUser user = auth.getCurrentUser();
    final EditText oldpass =(EditText) findViewById(R.id.oldpassword);
    EditText newpass = (EditText) findViewById(R.id.newpassword);
    EditText confirmnewpass = (EditText) findViewById(R.id.confirmnewpassword);
    final String username = firebaseUser.getEmail();
    final String Soldpass = oldpass.getText().toString();
    String snewpass = newpass.getText().toString();
    final String sconfirmpass = confirmnewpass.getText().toString();

    if (TextUtils.isEmpty(Soldpass)) {
        oldpass.setError("Enter Old password ! ");
        Toast.makeText(getApplicationContext(), "Enter Old  password ! ", Toast.LENGTH_SHORT).show();
        return;
    }
    if (Soldpass.length() < 6) {
        oldpass.setError("Password too short, enter minimum 6 characters!");
        Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
        return;
    }

    //
    //
/*
    synchronized (this) {
        try { wait(10000);
        }
        catch (InterruptedException e) { e.printStackTrace();  }
    }
*/
    if (!Objects.equals(Soldpass, pass[0])) {
        oldpass.setError("Password Don't match with your Old password");
        Toast.makeText(getApplicationContext(),"Password Don't match with your Old password",Toast.LENGTH_SHORT).show();
    return;
    }
    if (TextUtils.isEmpty(snewpass)) {
        newpass.setError("Enter New password ! ");
        return;
    }
    if (snewpass.length() < 6) {
        newpass.setError("Password too short, enter minimum 6 characters!");
        Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
        return;
    }

    //
    //
    if (TextUtils.isEmpty(sconfirmpass)) {
        confirmnewpass.setError("Enter New password ! ");
        Toast.makeText(getApplicationContext(), "Enter New  password ! ", Toast.LENGTH_SHORT).show();
        return;
    }
    if (sconfirmpass.length() < 6) {
        confirmnewpass.setError("Password too short, enter minimum 6 characters!");
        Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
        return;
    }
    if (!Objects.equals(snewpass,sconfirmpass)) {
        newpass.setError("Password don't match ");
        confirmnewpass.setError("password don't match");
        Toast.makeText(getApplicationContext(), "Password match  ", Toast.LENGTH_SHORT).show();
        return;
    }
         progressBar.setVisibility(View.VISIBLE);
                        firebaseUser.updatePassword(sconfirmpass).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    reference.orderByChild("email").equalTo(username).addChildEventListener(new ChildEventListener() {
                                        @Override
                                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                            reference.child(dataSnapshot.getKey()).child("password").setValue(sconfirmpass);
                                            Toast.makeText(getApplicationContext(), "Password Successfuly Updated ", Toast.LENGTH_SHORT).show();
                                            progressBar.setVisibility(View.GONE);
                                            AlertDialog.Builder adb = new AlertDialog.Builder(Change_Password.this);
                                            adb.setTitle("Password Updated");
                                            adb.setMessage("  Password Successfuly Updated  ");
                                            adb.setCancelable(false);
                                            adb.setPositiveButton(" OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                            startActivity(new Intent(Change_Password.this,AccountSetting.class));                                                   finish();
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
                                } else {
                                    Toast.makeText(getApplicationContext(), "Faild to update 2nd  password  "+task, Toast.LENGTH_LONG).show();
                                }
                            }

                        });

                    }


}







