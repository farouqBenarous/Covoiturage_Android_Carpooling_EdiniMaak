package com.example.sony.tabhost;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import  android.support.v7.app.AlertDialog.Builder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import java.util.concurrent.atomic.AtomicInteger;

public class TripShowBlogBigger extends AppCompatActivity {
    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);
    String Cuid,Cusername,Curi,Uid,SetUri,friend,dép,arv,date,time,nbplace,price,ph,id,username,email,idcar,type;
    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    ImageView userimage;
    TextView Username,dep,Arv,datee,Time,place,Price,phone,Email,Type;
    Dialog ConfirmDialogue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trip_show_blog_bigger);


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
        Cuid = firebaseUser.getUid();
        Cusername = firebaseUser.getDisplayName();
        Curi = firebaseUser.getDisplayName();
        reference.keepSynced(true);



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
        type = intent.getStringExtra("type");

        Picasso.with(getApplicationContext()).load(SetUri).into(userimage);
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
    @SuppressLint("SetTextI18n")
    public void Confirm (View view) {
       //Stocke the variabeles without the begining

     AlertDialog.Builder mBuilder = new AlertDialog.Builder(TripShowBlogBigger.this);
        View mView = getLayoutInflater().inflate(R.layout.dialogue_confirm_trip, null);

        TextView départure = (TextView) mView.findViewById(R.id.départure);
       départure.setText(dép);

       TextView arrivé = (TextView) mView.findViewById(R.id.arrivé);
       arrivé.setText(arv);

       TextView pricec    = (TextView) mView.findViewById(R.id.prix);
       pricec.setText(price+" Da ");

       final EditText place = (EditText) mView.findViewById(R.id.place);
       place.setHint("place left "+nbplace);

       Button reserver = (Button) mView.findViewById(R.id.reserver);
       reserver.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
              final String placereserved = place.getText().toString();
               if (TextUtils.isEmpty(placereserved)) {
                   Toast.makeText(getApplicationContext(),"Enter Place Number !",Toast.LENGTH_SHORT).show();
                   return; }
               if (Integer.parseInt(placereserved )>Integer.parseInt(nbplace) ) {
                Toast.makeText(getApplicationContext(),"Only   "+nbplace+"  left",Toast.LENGTH_LONG).show();
                return;
               }
               reference.child("Trips").orderByChild("id").equalTo(id).addChildEventListener(new ChildEventListener() {
                   @Override
                   public void onChildAdded(DataSnapshot dataSnapshot, String s) {
           reference.child("Trips").child(dataSnapshot.getKey()).child("nbPlace")
                   .setValue(Integer.parseInt(nbplace)-Integer.parseInt(placereserved)).addOnCompleteListener(new OnCompleteListener<Void>() {

               @Override
               public void onComplete(@NonNull Task<Void> task) {
                   // Add in the User notifications

                   //  Set The type of the notifications 
                   id =String.valueOf( generateViewId());
                   String typeI = "reservation";
                   String CurrentDes ="You Have reserved a Trip "+dép+" --> "+arv+"\n"+"Contact the User for more informations" ;
                   modelNotifications I = new modelNotifications(id,username,SetUri,CurrentDes,typeI);

                   String typeO = "reserved";
                   String Des = Cusername + " Have Reserved  "+placereserved+" place in Your  Trip "+dép+" --> "+arv ;
                   modelNotifications O = new modelNotifications(id,Cusername,Curi,Des,typeO);


                   reference.child("User").child(Cuid).child("notificaions").push().setValue(I);
                   reference.child("User").child(Uid).child("notificaions").push().setValue(O);


                   android.support.v7.app.AlertDialog.Builder adb = new android.support.v7.app.AlertDialog.Builder(TripShowBlogBigger.this);
                   adb.setTitle(" Trip successfully Reserved    ");
                   adb.setMessage("You Have Reserved  "+placereserved+" Place in The Trip "+dép+" --> "+arv+"\n"+"Total Price : "+
                   placereserved+" * "+price+ " = "+String.valueOf( Integer.parseInt(price)*Integer.parseInt(placereserved))+" Da");
                   adb.setCancelable(true);

                   adb.setPositiveButton(" Ok", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {
                     dialog.cancel();
                     startActivity(new Intent(getApplicationContext(),MainActivity.class));
                       }});


                   adb.show();

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

           }
       });

       mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();


    }
    public void Call(View view) {

        Intent i = new Intent(Intent.ACTION_CALL);
        i.setData(Uri.parse("tel:"+ph));
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
