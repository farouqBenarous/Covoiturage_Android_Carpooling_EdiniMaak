package com.example.sony.tabhost;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import  android.support.v7.app.AlertDialog.Builder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class ProfileShowBlogBigger extends AppCompatActivity {
    String Uid,SetUri,friend,dép,arv,date,time,nbplace,price,ph,id,username,email,idcar,type;
   FirebaseDatabase database;
   DatabaseReference reference;
   FirebaseAuth auth;
   FirebaseUser firebaseUser;
   ImageView userimage;
   TextView Username,dep,Arv,datee,Time,place,Price,phone,Email,Type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_show_blog_bigger);
        Username = findViewById(R.id.Username);
        dep = findViewById(R.id.dép);
        Arv= findViewById(R.id.arv);
        datee = findViewById(R.id.datee);
        Time = findViewById(R.id.time);
        place = findViewById(R.id.place);
        Price = findViewById(R.id.price);
        phone = findViewById(R.id.phone);
        userimage = findViewById(R.id.user_single_image);
        Email = findViewById(R.id.email);
        Type = findViewById(R.id.type);
        database = FirebaseDatabase.getInstance();
        reference =database.getReference();
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();



        Intent intent = getIntent();

        idcar = intent.getStringExtra("idcar");
        SetUri  = intent.getStringExtra("Uri");
        username  = intent.getStringExtra("username");
        email = intent.getStringExtra("email");
        dép  = intent.getStringExtra("dép");
        arv  = intent.getStringExtra("arv");
        date  = intent.getStringExtra("date");
        time  = intent.getStringExtra("time");
        nbplace  = String.valueOf(intent.getStringExtra("nbplace"));
        price  =String.valueOf( intent.getStringExtra("price"));
        ph  = intent.getStringExtra("phone");
        Uid = intent.getStringExtra("Uid");
        id  = intent.getStringExtra("id");
        type = intent.getStringExtra("Type");
        String Uri = "https://firebasestorage.googleapis.com/v0/b/convoiturage-81bdb.appspot.com/o/photoscars%2Fcar-icon.jpg?alt=media&token=00cf0730-3e88-4f7f-a1ff-5c92fda9dcae";
       if (SetUri.equals(Uri)) { userimage.setImageResource(R.drawable.profile_vide);}
       else  { Picasso.with(getApplicationContext()).load(SetUri).into(userimage); }
        Username.setText(username);
        Email.setText(email);
        dep.setText(" Departure City : "+dép);
        Arv.setText(" Arrived City : "+arv);
        datee.setText(" Departure Date : "+date);
        Time.setText(" Departure Time : "+time);
        place.setText(" Avalaible place : "+nbplace);
        Price.setText(" Price : "+price);
        phone.setText(ph);
        Type.setText("Type : "+type);
    }
    public  void  Delete(View view) {
        AlertDialog.Builder adb = new AlertDialog.Builder(ProfileShowBlogBigger.this);
        adb.setTitle("Confirmation");
        adb.setMessage("Are you sure  you want to Delete This Trip  ? ");
        adb.setCancelable(true);

        adb.setPositiveButton(" yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

reference.child("User").child(Uid).child("MyTrips").orderByChild("id").equalTo(id).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        String uid = dataSnapshot.getKey();
                        reference.child("User").child(Uid).child("MyTrips").child(uid).removeValue();
                        startActivity(new Intent(ProfileShowBlogBigger.this,MainActivity.class));
                        finish();

                    }
                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) { }
                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) { }
                    @Override
                    public void onCancelled(DatabaseError databaseError) { }});
reference.child("Trips").orderByChild("id").equalTo(id).addChildEventListener(new ChildEventListener() {
    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        String uid = dataSnapshot.getKey();
        reference.child("Trips").child(uid).removeValue();
        startActivity(new Intent(ProfileShowBlogBigger.this,MainActivity.class));
        finish();
    }
    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {}
    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
    @Override
    public void onCancelled(DatabaseError databaseError) {}});}});

        adb.setNegativeButton(" No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                }});

        adb.show();
    }

        public void Change (View view) {
             //Stocke the variabeles without the begining
             Intent myIntent = new Intent(getApplicationContext(), Change_Trip.class);
            myIntent.putExtra("dép", dép);
            myIntent.putExtra("arv", arv);
            myIntent.putExtra("date", date);
            myIntent.putExtra("time",time );

            myIntent.putExtra("idcar",idcar);
            myIntent.putExtra("Uid",Uid);
            myIntent.putExtra("email",email);
            myIntent.putExtra("username",username);
            myIntent.putExtra("Uri", SetUri);
            myIntent.putExtra("nbplace",nbplace);
            myIntent.putExtra("price",price);
            myIntent.putExtra("phone",ph);
            myIntent.putExtra("id",id);
            startActivity(myIntent);
            }


}
