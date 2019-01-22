package com.example.sony.tabhost;

import android.annotation.SuppressLint;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
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

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Change_Phone_number extends AppCompatActivity {
EditText inputoldphone;
EditText inputnewphone;
EditText intputcode;
TextView t1,t2;
String oldphone;
String newphone;
String code;
String username;
String oldphonenumber;
String email;
String password;
Button send_btn;
private String mPhoneVerificationId;
Fragment_changephone Fragment_changephone = new Fragment_changephone();
FragmentManager fragmentManager = getSupportFragmentManager();
PhoneAuthProvider.ForceResendingToken token;
private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";
private boolean mVerificationInProgress = false;
FirebaseDatabase database;
DatabaseReference reference;
FirebaseAuth auth;
FirebaseUser firebaseUser;
ProgressBar progressBar;
ProgressBar progressBarFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change__phone_number);

        t1 = findViewById(R.id.textview1);
        t2 = findViewById(R.id.textview2);

        progressBar =findViewById(R.id.progress7);
        inputoldphone = findViewById(R.id.oldphone);
        inputnewphone = findViewById(R.id.newphone);
        send_btn= findViewById(R.id.send_btn);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("User");
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        assert firebaseUser != null;
        username = firebaseUser.getEmail();

        reference.orderByChild("email").equalTo(username).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String uid = dataSnapshot.getKey();
                oldphonenumber = dataSnapshot.child("phone_number").getValue().toString();
                email = dataSnapshot.child("email").getValue().toString();
                password = dataSnapshot.child("password").getValue().toString();
                inputoldphone.setText(oldphonenumber);
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onCancelled(DatabaseError databaseError) {}});}

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void  verify (View view) {
        oldphone = inputoldphone.getText().toString();
        newphone = inputnewphone.getText().toString();
        if (TextUtils.isEmpty(oldphone) || oldphone.equals("+213") ) {
            inputoldphone.setError("Enter Old Phone Number  ! ");
            Toast.makeText(getApplicationContext(), "Enter Old Phone Number  ! ", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!isValidMobile(oldphone)) {
            inputoldphone.setError("Phone number Badly formed ! ");
            Toast.makeText(getApplicationContext(), "Phone number Badly formed !", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!Objects.equals(oldphone,oldphonenumber)) {
            inputoldphone.setError("Phone Number  don't match with your Old one ");
            Toast.makeText(getApplicationContext(), "Phone Number  don't match with your Old one  ", Toast.LENGTH_SHORT).show();
            return;
        }


        if (TextUtils.isEmpty(newphone)|| oldphone.equals("+213")) {
            inputnewphone.setError("Enter your new  Phone Number  ! ");
            Toast.makeText(getApplicationContext(), "Enter your new Phone Number  ! ", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isValidMobile(newphone)) {
            inputnewphone.setError("Phone number Badly formed ! ");
            Toast.makeText(getApplicationContext(), "Phone number Badly formed !", Toast.LENGTH_SHORT).show();
            return;
        }
OnPhoneNumberSubmitted(newphone);
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
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        if (e instanceof FirebaseTooManyRequestsException) {
                            // The SMS quota for the project has been exceeded
                            Toast.makeText(getApplicationContext()," The SMS quota for the project has been exceeded", Toast.LENGTH_LONG).show();
                        }

                        if (e instanceof FirebaseAuthInvalidCredentialsException) {

                            // Invalid request
                            Toast.makeText(getApplicationContext(),"Invalid request", Toast.LENGTH_LONG).show();
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
                        Toast.makeText(getApplicationContext(),"The verification code has been sent ", Toast.LENGTH_LONG).show();
                        //stating the pinCodee Fragment
                        inputnewphone.setVisibility(View.GONE);
                        inputoldphone.setVisibility(View.GONE);
                        t1.setVisibility(View.GONE);
                        t2.setVisibility(View.GONE);
                        send_btn.setVisibility(View.GONE);


                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frameLayout,Fragment_changephone)
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                .addToBackStack(null)
                                .commit();
                    }});}


    private void signInWithPhoneAuthCredential(final PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(),"signIn Success",Toast.LENGTH_LONG).show();
                            // add this number to the real time in the child contain this email !


                            UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(username)
                                    .build();
                            if (firebaseUser != null) {
                                firebaseUser.updateProfile(profileUpdate)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                }}});
                            }


                            reference.orderByChild("fullName").equalTo(username).addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                    String Uid = dataSnapshot.getKey();
                                    reference.child(Uid).child("phone_number").setValue(newphone);

                                    final AuthCredential credential = EmailAuthProvider.getCredential(email,password);
                                    auth.signOut();
                                    assert firebaseUser != null;
                                    auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                AlertDialog.Builder adb = new AlertDialog.Builder(Change_Phone_number.this);
                                                adb.setTitle("Phone Number  Updated");
                                                adb.setMessage("  Phone number  Successfuly Updated  ");
                                                adb.setCancelable(false);
                                                adb.setPositiveButton(" OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        startActivity(new Intent(getApplicationContext(),AccountSetting.class));
                                                        finish();
                                                    }});
                                                adb.show();
                                            }
                                            else  {
                                                Toast.makeText(getApplicationContext(),"faild to set Phone number"+task.getException(),Toast.LENGTH_LONG).show();
                                            }}});}
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
                                intputcode.setError("Invalid verification Code");
                                Toast.makeText(getApplicationContext(), "Invalid verification Code ! "+task.getException(), Toast.LENGTH_SHORT).show();
                                return;
                            }
                            Toast.makeText(getApplicationContext(),"signInWithCredential:failure",Toast.LENGTH_LONG).show();
                        }}});}

    // Fragment
    public void Checkcode (View view) {
        intputcode= findViewById(R.id.pinCode);
        code = intputcode.getText().toString();
        progressBarFragment = findViewById(R.id.progressBar13);

        if (TextUtils.isEmpty(code)) {
            intputcode.setError("Enter your Sms Code  ! ");
            Toast.makeText(getApplicationContext(), "Enter your Sms Code  ! ", Toast.LENGTH_SHORT).show();
            return;
        }
        if (code.length() != 6) {
            intputcode.setError("Sms Code too short, enter minimum 6 characters!");
            Toast.makeText(getApplicationContext(), "Sms Code too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
            return;
        }
        onPinCodeSubmitted(code);

    }
    public void onPinCodeSubmitted(String codePin) {
        if (validateCredentials(codePin)) {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codePin, mPhoneVerificationId);
            signInWithPhoneAuthCredential(credential);
        }}



    boolean validateCredentials(@NonNull String ...credentials){
        boolean validText ;
        int i = credentials.length-1;
        do{
            validText = (credentials[i] != null)&&(credentials[i].length()>0) ;
            i--;
        }while (validText && i != -1);
        return validText;}






    private boolean isValidMobile(String phone) {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }
}
