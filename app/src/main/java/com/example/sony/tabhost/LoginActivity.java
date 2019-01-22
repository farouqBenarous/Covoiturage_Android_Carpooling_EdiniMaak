package com.example.sony.tabhost;

import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";
    private boolean mVerificationInProgress = false;
    private EditText inputEmail, inputPassword;
    private FirebaseDatabase database;
    private  FirebaseAuth auth;
    private DatabaseReference myref,myref1;
    private  FirebaseUser firebaseUser;
    private ProgressBar progressBar;
    private Button btnSignup, btnLogin, btnReset;
    private  String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnSignup = (Button) findViewById(R.id.btn_signup);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnReset = (Button) findViewById(R.id.btn_reset_password);
        final String email = inputEmail.getText().toString();
        final String password = inputPassword.getText().toString();

        database = FirebaseDatabase.getInstance();
        myref = database.getReference("User");
        myref1 = database.getReference("User");
        auth = FirebaseAuth.getInstance();

    }



    public void btn_signup (View v) {

        startActivity(new Intent(LoginActivity.this, com.example.sony.tabhost.SignupActivity.class));
    }

    public void btnReset(View v) {
        startActivity(new Intent(LoginActivity.this, Repass.class));
    }

    public void btnLogin(View v) {

        database = FirebaseDatabase.getInstance();
        myref = database.getReference();
        auth = FirebaseAuth.getInstance();

        final String email = inputEmail.getText().toString();
        final String password = inputPassword.getText().toString();




        if (TextUtils.isEmpty(email)) {
            inputEmail.setError("Enter email adress");
            Toast.makeText(getApplicationContext(), "Enter email address !", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!checkemail(email)) {
            inputEmail.setError("E-mail badly formed");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            inputPassword.setError("Enter password ! ");
            Toast.makeText(getApplicationContext(), "Enter password ! ", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() < 6) {
            inputPassword.setError("Password too short, enter minimum 6 characters!");
        return;
        }


        progressBar.setVisibility(View.VISIBLE);

        //authenticate user
        auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {


                        if (!task.isSuccessful()) {
                            progressBar.setVisibility(View.GONE);
                            /*add an i to custum the exception messages( Snakebar for the expetions about the Conection ) if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            progressBar.setVisibility(View.GONE);
                            codeinput.setError("Invalid verification Code");
                            Toast.makeText(getApplicationContext(), "Invalid verification Code ! ", Toast.LENGTH_SHORT).show();
                            return;

                        }*/
                            Toast.makeText(LoginActivity.this, ""+task.getException(), Toast.LENGTH_LONG).show();

                        } else {
                            FirebaseUser User = auth.getCurrentUser();
                            assert User != null;
                            String username = User.getEmail();
                            final boolean[] check = new boolean[1];
                            myref.child("User").orderByChild("email").equalTo(username).addChildEventListener(new ChildEventListener() {
                                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                @Override
                                public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                                if (Objects.equals(dataSnapshot.child("phone_number").getValue(),"5")) {
                                    startActivity(new Intent(getApplicationContext(),validation_emailphonenumber.class));
                                    finish();
                                }
                                else {
                                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                    finish();

                                }

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
                });
    }



    public boolean checkemail(String email) {
        Pattern pattern = Pattern.compile(".+@.+\\.[a-z]+");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();}
    public Boolean emailexist (String email , DataSnapshot dataSnapshot ) {
        // check if the email is already exist or not
        modeluser user = new modeluser();

        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            user.setEmail(ds.getValue(modeluser.class).getEmail());

            if (user.getEmail().equals(email) ) {return true;}

        }

        return false;
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_VERIFY_IN_PROGRESS, mVerificationInProgress);
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mVerificationInProgress = savedInstanceState.getBoolean(KEY_VERIFY_IN_PROGRESS);
    }

    private boolean isValidMobile(String phone) {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }




}




