package com.example.sony.tabhost;

import android.annotation.SuppressLint;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class validation_emailphonenumber extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    FirebaseUser user;
    private FirebaseDatabase database;
    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";
    private boolean mVerificationInProgress = false;
    private EditText Phone,codeinput;
    private String mPhoneVerificationId;
    Button send_pinCode_btn;
    Pincodee pinCodeFragment = new Pincodee();
    FragmentManager fragmentManager = getSupportFragmentManager();
    PhoneAuthProvider.ForceResendingToken token;
    private ProgressBar progressBar;
    String  phonenumber;
    String email;
    String displayname;
    Uri profilUri;
    String em;
    String pass;
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
    protected void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);}

       //PROBLEME :   I HAVE TO CHECK IF THE PHONE NUMBER EXIST OR NOT


        super.onCreate(savedInstanceState);
        setContentView(R.layout.validation);
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("User");
        auth = FirebaseAuth.getInstance();
        FirebaseUser user1 = auth.getCurrentUser();
        assert user1 != null;
        email = user1.getEmail();
        displayname = user1.getDisplayName();
        Phone = findViewById(R.id.phoneNumber_editText);
        send_pinCode_btn = findViewById(R.id.send_pinCode_btn);
        user = auth.getCurrentUser();
        progressBar = (ProgressBar) findViewById(R.id.progress);
        Phone.setVisibility(View.VISIBLE);
        send_pinCode_btn.setVisibility(View.VISIBLE);
        final String username = user.getEmail();
        databaseReference.orderByChild("email").equalTo(email).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                pass =  dataSnapshot.child("password").getValue().toString();
                em = dataSnapshot.child("email").getValue().toString();

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


    public  void verify (View view) {
        phonenumber = Phone.getText().toString();
        if (TextUtils.isEmpty(phonenumber)) {
            Phone.setError("Enter your Phone number");
            Toast.makeText(getApplicationContext(), "Enter your Phone number !", Toast.LENGTH_SHORT).show();
            return;
        }
        if (phonenumber.length() != 13 )  {
            Phone.setError("Phone number to Short or to long ! ");
            Toast.makeText(getApplicationContext(), "Phone number to Short or to long !", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isValidMobile(phonenumber)) {
            Phone.setError("Phone number Badly formed ! ");
            Toast.makeText(getApplicationContext(), "Phone number Badly formed !", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        OnPhoneNumberSubmitted(phonenumber);
}

    boolean validateCredentials(@NonNull  String ...credentials){
        boolean validText ;
        int i = credentials.length-1;
        do{
            validText = (credentials[i] != null)&&(credentials[i].length()>0) ;
            i--;
        }while (validText && i != -1);

        return validText;
    }




    public void OnPhoneNumberSubmitted(String phoneNumber) {
        //problem when i siggnuup without verify the phone and i start the app againe when i click on the send code 'uffortenly stopped'
        //getting firebase phone auth provider instance
        final PhoneAuthProvider phoneAuthProvider = PhoneAuthProvider.getInstance();
        //verifying phone number, using my personne l phone number, setting 1 minute as a time out for the second attempt
        phoneAuthProvider.verifyPhoneNumber(
                phoneNumber, // the phone number of the user
                120,  // the time out
                TimeUnit.SECONDS, // the time out unit
                this,   // maneging the activity life cycle change
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                        //some devices, can detect automatically the sent pinCode
                        signInWithPhoneAuthCredential(phoneAuthCredential);
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        //something went wrong while submitting the pinCode
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(validation_emailphonenumber.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                      if (e instanceof FirebaseTooManyRequestsException) {
                            // The SMS quota for the project has been exceeded
                            Toast.makeText(validation_emailphonenumber.this," The SMS quota for the project has been exceeded", Toast.LENGTH_LONG).show();
                        }

                        if (e instanceof FirebaseAuthInvalidCredentialsException) {

                            // Invalid request
                            Toast.makeText(validation_emailphonenumber.this,"Invalid request", Toast.LENGTH_LONG).show();
                        }



                    }

                    @SuppressLint("CommitTransaction")
                    @Override
                    public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        //the user received the pinCode
                        //super.onCodeSent(verificationId, forceResendingToken);
                        //saving the verification Id to use later
                        mPhoneVerificationId = verificationId;
                        token = forceResendingToken;
                        // Toast the code has been sent
                        Toast.makeText(validation_emailphonenumber.this,"The verification code has been sent "
                                , Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                        //stating the pinCodee Fragment
                        Phone.setVisibility(View.GONE);
                        send_pinCode_btn.setVisibility(View.GONE);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frameLayout,pinCodeFragment)
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                .addToBackStack(null)
                                .commit();
                    }
                }
        );

    }


    private void signInWithPhoneAuthCredential(final PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(),"signIn Success",Toast.LENGTH_LONG).show();
                            // add this number to the real time in the child contain this email !
                            final FirebaseDatabase databas = FirebaseDatabase.getInstance();
                            final DatabaseReference reference    = database.getReference();
                            final DatabaseReference ref = databas.getReference("User");

                            auth = FirebaseAuth.getInstance();
                            final FirebaseUser user = auth.getCurrentUser();

                            UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(displayname)
                                    .build();
                            if (user != null) {
                                user.updateProfile(profileUpdate)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                }}});
                            }


                            ref.orderByChild("email").equalTo(email).addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                    String Uid = dataSnapshot.getKey();
                                    ref.child(Uid).child("phone_number").setValue(phonenumber);

                                    final AuthCredential credential = EmailAuthProvider.getCredential(email,pass);
                                    //auth.signOut();
                                    assert user != null;
                                    auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                Intent intent = new Intent(validation_emailphonenumber.this,MainActivity.class);
                                                startActivity(intent);
                                                finish();

                                            }
                                            else  {
                                                Toast.makeText(getApplicationContext(),"Faild"+task.getException(),Toast.LENGTH_LONG).show();
                                            }

                                        }
                                    });




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
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                progressBar.setVisibility(View.GONE);
                                    codeinput.setError("Invalid verification Code");
                                    Toast.makeText(getApplicationContext(), "Invalid verification Code ! "+task.getException(), Toast.LENGTH_SHORT).show();
                                    return;
                            }
                            Toast.makeText(getApplicationContext(),"signInWithCredential:failure",Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }






    //pinCodee Fragment  Functions
    public void onPinCodeSubmitted(String codePin) {
        if (validateCredentials(codePin)) {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codePin, mPhoneVerificationId);
            signInWithPhoneAuthCredential(credential);
        }

    }

    public void Checkcode (View view) {
        progressBar.setVisibility(View.GONE);
        codeinput = findViewById(R.id.pinCode_editText);
        String codepin = codeinput.getText().toString();

        if (TextUtils.isEmpty(codepin)) {
            codeinput.setError("Enter The verification code");
            Toast.makeText(getApplicationContext(), "Enter The verification code ! ", Toast.LENGTH_SHORT).show();
            return;
        }
        onPinCodeSubmitted(codepin);
    }
    private boolean isValidMobile(String phone) {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }


}