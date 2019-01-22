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
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class Delete_This_Account extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    final String[] pass = new String[1];
    String email;
    EditText inputpassword;
    String  password;
    String username;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete__this__account);
        progressBar = findViewById(R.id.progressBar17);
        inputpassword = findViewById(R.id.password);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("User");
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        username = firebaseUser.getEmail();
        reference.orderByChild("email").equalTo(username).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                pass[0] =  dataSnapshot.child("password").getValue().toString();
                email = dataSnapshot.child("email").getValue().toString();
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
    public void delete (View view) {
        password = inputpassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            inputpassword.setError("Enter  password ! ");
            Toast.makeText(getApplicationContext(), "Enter  password ! ", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() < 6) {
            inputpassword.setError("Password too short, enter minimum 6 characters!");
            Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!Objects.equals(password, pass[0])) {
            inputpassword.setError("Password Don't match with your Old password");
            Toast.makeText(getApplicationContext(),"Password Don't match with your Old password",Toast.LENGTH_SHORT).show();
            return;
        }
        AlertDialog.Builder adb = new AlertDialog.Builder(Delete_This_Account.this);
        adb.setTitle("Password Updated");
        adb.setMessage("  Password Successfuly Updated  ");
        adb.setCancelable(false);
        adb.setPositiveButton(" yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AuthCredential credential = EmailAuthProvider.getCredential(email,password);
                firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        progressBar.setVisibility(View.VISIBLE);
                        firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    reference.orderByChild("email").equalTo(username).addChildEventListener(new ChildEventListener() {
                                        @Override
                                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                            progressBar.setVisibility(View.GONE);
                                            String uid = dataSnapshot.getKey();
                                            reference.child(uid).removeValue();
                                            startActivity(new Intent(Delete_This_Account.this,LoginActivity.class));
                                            finish();
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
                                else {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(getApplicationContext(),"Faild To Delete Account"+task.getException(),Toast.LENGTH_SHORT).show();
                                }}});
                    }
                    else  {

                        Toast.makeText(getApplicationContext(),"Faild To Reauthenticate User"+task.getException(),Toast.LENGTH_SHORT).show();
                    }
                    }});
            }});
        adb.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        adb.show();
}
}
