package com.example.sony.tabhost;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class Setinformationcar extends AppCompatActivity {
    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);
    private static final  int GALLERY_INTENT = 1;
    String spin_val;
    String[] spin = {"Sedan ", "Truck ","motorbike,scooter","van","bus"};//array of strings used to populate the spinner
    EditText inputbrand,inputmatricule,inputcartegrise;
    ImageView carimage;
    Uri uricarimage;
    ProgressBar progressBar;
    Car car;
    modeluser user;
    FirebaseUser firebaseUser;
    FirebaseAuth auth;
    StorageReference storage;
    FirebaseDatabase database;
    DatabaseReference reference,reference1;
    String numcartegrise;
    String matricule;
    String brand;
    @Override
    //  LIMITE THE CARS AT  MAIMUM
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setinformationcar);
        carimage = findViewById(R.id.carimage);
        inputbrand = findViewById(R.id.brand);
        inputmatricule = findViewById(R.id.matricule);
        inputcartegrise = findViewById(R.id.numcartegrise);
        progressBar = findViewById(R.id.progressBar3);

        final Spinner cartype = (Spinner) findViewById(R.id.cartype);
        // Create an ArrayAdapter using the string array and a default spinner layout
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.cartype, android.R.layout.simple_spinner_dropdown_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        cartype.setAdapter(adapter);
        //Register a callback to be invoked when an item in this AdapterView has been selected
        cartype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long id) {// TODO Auto-generated method stub
                spin_val = spin[position];
                //saving the value selected
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }});


    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void save(View view) throws InterruptedException {
//add the check format and the autocomplete Edittext in the brand
        brand = inputbrand.getText().toString();
        matricule = inputmatricule.getText().toString();
        numcartegrise = inputcartegrise.getText().toString();
        progressBar = findViewById(R.id.progressBar3);
        String   tag =  carimage.getTag().toString();



        if (TextUtils.isEmpty(brand)) {
            inputbrand.setError("Enter The Brand !");
            Toast.makeText(getApplicationContext(), "Enter the Brand !", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(matricule)) {
            inputmatricule.setError("Enter The Matricule !");
            Toast.makeText(getApplicationContext(), "Enter The Matricule !", Toast.LENGTH_SHORT).show();
            return;
        }

        if (matricule.length() !=10 ) {
            inputmatricule.setError("Invalide Matricule !");
            Toast.makeText(getApplicationContext(), "Invalide Matricule !", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(numcartegrise)) {
            inputcartegrise.setError("Enter The cartegrise number !");
            Toast.makeText(getApplicationContext(), "Enter the cartegrise number !", Toast.LENGTH_SHORT).show();
            return;
        }

        if (numcartegrise.length() != 10) {
            inputcartegrise.setError("Invalide  cartegrise number !");
            Toast.makeText(getApplicationContext(), "Invalide  cartegrise number !", Toast.LENGTH_SHORT).show();
            return;
        }


// Check if The User added the Pic Or not
        if (Objects.equals(tag,"standardcar")) {

            progressBar.setVisibility(View.VISIBLE);

            uploadwithoutimage();


        }
        else {

        progressBar.setVisibility(View.VISIBLE);
       uploadtothefirebase();

        }


    }


    public void choosepic(View view) {
        progressBar=  findViewById(R.id.progressBar3);
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,GALLERY_INTENT);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        progressBar= (ProgressBar) findViewById(R.id.progressBar1);
        carimage = findViewById(R.id.carimage);
        if (requestCode == GALLERY_INTENT & resultCode == RESULT_OK && data != null && data.getData()!=null) {
        uricarimage = data.getData();
        carimage.setTag(uricarimage.toString());
            Picasso.with(Setinformationcar.this).load(uricarimage).into(carimage);
         }
    }

    public  void  uploadwithoutimage () {
        progressBar = findViewById(R.id.progressBar3);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("User");



                progressBar = findViewById(R.id.progressBar3);
                auth = FirebaseAuth.getInstance();
                database = FirebaseDatabase.getInstance();
                reference = database.getReference("User");
                reference1 = database.getReference();
                assert firebaseUser != null;
                firebaseUser = auth.getCurrentUser();

                reference.orderByChild("email").equalTo(firebaseUser.getEmail()).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        String Uid= dataSnapshot.getKey();
                        brand = inputbrand.getText().toString();
                        matricule = inputmatricule.getText().toString();
                        numcartegrise = inputcartegrise.getText().toString();
                        progressBar = findViewById(R.id.progressBar3);

                        String idcar=  String.valueOf(generateViewId());
                        car = new Car(idcar,brand,spin_val,matricule,numcartegrise);
                        reference1.child("User").child(Uid).child("cars").push().setValue(car);

                    }
                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {}
                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                    @Override
                    public void onCancelled(DatabaseError databaseError) {}});






                progressBar.setVisibility(View.GONE);
                final AlertDialog.Builder adb = new AlertDialog.Builder(Setinformationcar.this);
                adb.setTitle("The Car has Added ");
                adb.setMessage("The Car has Added To Your List You can Use it to Create Trip ");
                adb.setCancelable(false);

                adb.setPositiveButton(" OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(getApplication(),myCar.class));
                        finish();
                    }});

                adb.show();

            }





    public void uploadtothefirebase() {

        progressBar = findViewById(R.id.progressBar3);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("User");


        // Upload the pic to the Firebase storage and save the Uri and set it in the Realtime
        storage= FirebaseStorage.getInstance().getReference();
        StorageReference filepath = storage.child("photoscars").child(uricarimage.getLastPathSegment());
        filepath.putFile(uricarimage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                auth = FirebaseAuth.getInstance();
               final String downloadeduri= taskSnapshot.getDownloadUrl().toString();

                progressBar = findViewById(R.id.progressBar3);
                auth = FirebaseAuth.getInstance();
                database = FirebaseDatabase.getInstance();
                reference = database.getReference("User");
                reference1 = database.getReference();
                assert firebaseUser != null;
                firebaseUser = auth.getCurrentUser();

                reference.orderByChild("email").equalTo(firebaseUser.getEmail()).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        String Uid= dataSnapshot.getKey();
                        brand = inputbrand.getText().toString();
                        matricule = inputmatricule.getText().toString();
                        numcartegrise = inputcartegrise.getText().toString();
                        progressBar = findViewById(R.id.progressBar3);

                       String idcar=  String.valueOf(generateViewId());
                        car = new Car(idcar,downloadeduri,brand,spin_val,matricule,numcartegrise);
                        reference1.child("User").child(Uid).child("cars").push().setValue(car);

                    }
                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {}
                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                    @Override
                    public void onCancelled(DatabaseError databaseError) {}});

                progressBar.setVisibility(View.GONE);
                final AlertDialog.Builder adb = new AlertDialog.Builder(Setinformationcar.this);
                adb.setTitle("The Car has Added ");
                adb.setMessage("The Car has Added To Your List You can Use it to Create Trip ");
                adb.setCancelable(false);

                adb.setPositiveButton(" OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(getApplication(),myCar.class));
                        finish();
                    }});

                adb.show();

            }
        });

    }


    public static int generateViewId() {
        for (;;) {
            final int result = sNextGeneratedId.get();
            // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
            int newValue = result + 1;
            if (newValue > 0x00FFFFFF) newValue = 1; // Roll over to 1, not 0.
            if (sNextGeneratedId.compareAndSet(result, newValue)) {
                return result;
            }
        }
    }




    }



