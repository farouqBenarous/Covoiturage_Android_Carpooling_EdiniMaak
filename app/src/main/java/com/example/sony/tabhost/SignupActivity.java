package com.example.sony.tabhost;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {
    private EditText inputfullnamme, inputage, inputphonenumber;
    private EditText inputEmail, inputPassword,inputconfirmpassword;
    private Button btnSignIn, btnSignUp, btnResetPassword;
    private ProgressBar progressBar;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    String spin_val;
    String[] spin = {"Female", "Male"};//array of strings used to populate the spinner
    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";
    private boolean mVerificationInProgress = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //Get Firebase auth instance

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("User");
        auth = FirebaseAuth.getInstance();

        btnSignIn = findViewById(R.id.sign_in_button);
        btnSignUp = (Button) findViewById(R.id.sign_up_button);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        inputconfirmpassword = findViewById(R.id.confirmpassword);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnResetPassword = (Button) findViewById(R.id.btn_reset_password);
        inputfullnamme = (EditText) findViewById(R.id.fullname);
        inputage = (EditText) findViewById(R.id.Age);


        final Spinner gender = (Spinner) findViewById(R.id.spinnergender);
        // Create an ArrayAdapter using the string array and a default spinner layout
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.Gender, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        gender.setAdapter(adapter);
        //Register a callback to be invoked when an item in this AdapterView has been selected
        gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long id) {
                // TODO Auto-generated method stub
                spin_val = spin[position];
                //saving the value selected
                }
                @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }});




    }


    public void sign_in_button(View v) {
        finish();
    }
    public void sign_up_button(View v) {

        final String[] email = {inputEmail.getText().toString().trim()};
        String password = inputPassword.getText().toString().trim();
        final String confirmpassword = inputconfirmpassword.getText().toString().trim();
        final String fullname = inputfullnamme.getText().toString().trim();
        final String age = inputage.getText().toString().trim();

        progressBar.setVisibility(View.GONE);


        if (TextUtils.isEmpty(email[0])) {
            inputEmail.setError("Enter email address!");
            Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();

            progressBar.setVisibility(View.GONE);

            return;
        }
        if (!checkemail(email[0])) {
            inputEmail.setError("E-mail badly formed");

            progressBar.setVisibility(View.GONE);

            return;
        }
        if (TextUtils.isEmpty(password)) {
            inputPassword.setError("Enter password ! ");
            Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();

            progressBar.setVisibility(View.GONE);

            return;
        }

        if (TextUtils.isEmpty(confirmpassword)) {
            inputconfirmpassword.setError("Enter confirm  password ! ");
            Toast.makeText(getApplicationContext(), "Enter confirm  password!", Toast.LENGTH_SHORT).show();

            progressBar.setVisibility(View.GONE);

            return;
        }
        if (password.length() < 6) {
            inputPassword.setError("Password too short, enter minimum 6 characters!");
            Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();

            progressBar.setVisibility(View.GONE);

            return;
        }
        if (!password.equals(confirmpassword)) {
            inputPassword.setError("password don't match !");
            inputconfirmpassword.setError("password don't match !");

            progressBar.setVisibility(View.GONE);

            return;
        }

        if (TextUtils.isEmpty(fullname)) {
            inputfullnamme.setError("Enter your Fullname !");
            Toast.makeText(getApplicationContext(), "Enter your Full name !", Toast.LENGTH_SHORT).show();

            progressBar.setVisibility(View.GONE);

            return;
        }
        if (!validateLetters(fullname)) {
            inputfullnamme.setError("Full name Badlly formed");
            Toast.makeText(getApplicationContext(),"Full name Badlly formed",Toast.LENGTH_SHORT).show();

            progressBar.setVisibility(View.GONE);

       return;
        }


        // A have TO add A condition check if th use name exis or not





        if (TextUtils.isEmpty(age)) {
            inputage.setError("Enter your Age !");
            Toast.makeText(getApplicationContext(), "Enter your Age !", Toast.LENGTH_SHORT).show();

            progressBar.setVisibility(View.GONE);

            return;
        }

        if (age.length() > 3) {
            inputage.setError("Age inccorect ! ");
            Toast.makeText(getApplicationContext(), "Age inccorect !!", Toast.LENGTH_SHORT).show();

            progressBar.setVisibility(View.GONE);

            return;
        }


        progressBar.setVisibility(View.VISIBLE);
        //create user
        auth.createUserWithEmailAndPassword(email[0], password)
                .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {


                        if (!task.isSuccessful()) {
                            progressBar.setVisibility(View.GONE);

             Toast.makeText(SignupActivity.this, "" + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                        } else {

                            email[0] = (auth.getCurrentUser().getEmail());
                           String emaill = email[0];
                           String phone="5";
                            modeluser user;
                            String Uri = "https://firebasestorage.googleapis.com/v0/b/convoiturage-81bdb.appspot.com/o/photos%2Fprofile_vide.jpg?alt=media&token=ad6911c1-71b7-40c2-9d07-4b3965b27828";

   user = new modeluser(email[0],fullname,spin_val,age,phone,Uri,"cars",confirmpassword,"empty","empty",
           "empty","vide");
                            FirebaseUser User = FirebaseAuth.getInstance().getCurrentUser();
                            String Uid = User.getUid();
                            databaseReference = database.getReference("User");
                            databaseReference.child(Uid).setValue(user);

                            DatabaseReference  refff = database.getReference();
                            refff.child("infouserse").child(Uid).setValue(user);
                            UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(fullname).
                                    setPhotoUri(android.net.Uri.parse(Uri))
                                    .build();

                            User.updateProfile(profileUpdate)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){}}});





                            final Intent intent = new Intent(SignupActivity.this,validation_emailphonenumber.class);
                            intent.putExtra("Uid",Uid);
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            firebaseUser.sendEmailVerification()
                                    .addOnCompleteListener( new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                               final AlertDialog.Builder adb = new AlertDialog.Builder(SignupActivity.this);
                                                adb.setTitle("E-mail verification");
                              adb.setMessage("An E-mail Verification has been sent to the adress Click on the link to Validate Your Account ");
                                                adb.setCancelable(false);

                                                adb.setPositiveButton(" OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        progressBar.setVisibility(View.GONE);
                                                        startActivity(intent);
                                                        finish();
                                                   }});

                                          adb.show();
                                            }
                                        }
                                    });


                        }
                    }
                });
    }



    public boolean checkemail(String email) {
        Pattern pattern = Pattern.compile(".+@.+\\.[a-z]+");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    public static boolean validateLetters(String txt) {

        String regx = "[a-zA-Z]+\\.?";
        Pattern pattern = Pattern.compile(regx, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(txt);
        return matcher.find();


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
    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }






}