package com.example.sony.tabhost;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.squareup.picasso.Picasso;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class Conv extends AppCompatActivity {
    private static  AtomicInteger sNextGeneratedId = new AtomicInteger(1);
    RecyclerView recyclerView;
    int a = 0;
    String id;
   String username,Uri,Cusername,Cuid,cUri,idconvC="0",idconvO="0",Smesg,ido;
   EditText msgEdittext;
   FirebaseDatabase database;
   DatabaseReference reference,ref1,ref2,ref3,ref4;
   FirebaseAuth auth;
   FirebaseUser firebaseUser;
   Message messageC,messageO;
   Chat chatC,chatO;
   FirebaseRecyclerAdapter<Message, Conv.ConvViewHolder> firebaseRecyclerAdapter;
   Semaphore semaphore = new Semaphore(1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conv);
        Intent intent = getIntent();

        username = intent.getStringExtra("username");
        Uri = intent.getStringExtra("SetUri");


        msgEdittext = findViewById(R.id.msg);

        database =  FirebaseDatabase.getInstance();
        reference = database.getReference("User");
       ref1 = database.getReference("User");
        ref2 = database.getReference("User");
        ref3 = database.getReference("User");
        ref4 = database.getReference("User");
        reference.keepSynced(true);
        ref1.keepSynced(true);
        ref2.keepSynced(true);
        ref3.keepSynced(true);
        ref4.keepSynced(true);

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        Cuid = firebaseUser.getUid();
        Cusername = firebaseUser.getDisplayName();
        cUri = firebaseUser.getPhotoUrl().toString();
        reference.keepSynced(true);

        CheckexistO ();
        CheckexistC ();

        recyclerView = findViewById(R.id.ConvRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {
        super.onStart();
        }

    @Override
    protected void onResume() {
        super.onResume();
        CheckexistO ();
        CheckexistC ();

        Query query = FirebaseDatabase.getInstance().getReference().child("User").child(Cuid).child("Chat").child("messages").
                child(idconvC);
        FirebaseRecyclerOptions<Message> options =
                new FirebaseRecyclerOptions.Builder<Message>()
                        .setQuery(query,Message.class)
                        .build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Message,Conv.ConvViewHolder>
                (options) {
            @NonNull
            @Override
            public Conv.ConvViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.messagerow, parent, false);
                return new Conv.ConvViewHolder(view);
            }

            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            protected void onBindViewHolder (@NonNull Conv.ConvViewHolder viewHolder,final int position,@NonNull Message model) {

                if ( model.getUsername().equals(Cusername)) {
                    viewHolder.seturi(getApplicationContext(),model.getUri());
                    viewHolder.setusername(model.getUsername());
                    viewHolder.setmsg(model.getMessage());
                    //viewHolder.setgravityright();
                }

                else {
                    viewHolder.seturi(getApplicationContext(),model.getUri());
                    viewHolder.setusername(model.getUsername());
                    viewHolder.setmsg(model.getMessage());
                }



            }
        };

        recyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();


    }

    // i stopped here still have a probleme with the replaid msg it bugs the app


    public void send(View view) throws InterruptedException {
        Smesg = msgEdittext.getText().toString();
         id = String.valueOf(generateViewId());
        if (TextUtils.isEmpty(Smesg)) {
       Toast.makeText(getApplicationContext(),"Enter Message !",Toast.LENGTH_SHORT).show();
        return;
        }
           chatC = new Chat(cUri,Cusername,Smesg);
        messageC = new Message(cUri,Cusername,Smesg);


        chatO = new Chat(Uri,username,Smesg);
        messageO = new Message(Uri,username,Smesg);

        //set it in the current user and check if the conv exist or not by the two methode
        if (idconvC.isEmpty() || idconvC == null || idconvC.equals("0")) {
            idconvC =id ;
            reference.child(Cuid).child("Chat").child("infoconvs").child(id).setValue(chatC);
           // reference.child(Cuid).child("Chat").child(id).child("username").setValue(username);
            }
        reference.child(Cuid).child("Chat").child("infoconvs").child(idconvC).child("lastmsg").setValue(Smesg);
        ref1.child(Cuid).child("Chat").child("messages").child(idconvC).push().setValue(messageC);


        if (idconvO.isEmpty() || idconvO == null ||     idconvO.equals("0")) {
            ref2.child(ido).child("Chat").child("infoconvs").child(id).setValue(chatC);
            idconvO = id;
            //reference.child(Cuid).child("Chat").child(id).child("username").setValue(Cusername);
        }

        ref3.child(ido).child("Chat").child("infoconvs").child(idconvO).child("lastmsg").setValue(Smesg);
        ref4.child(ido).child("Chat").child("messages").child(idconvO).push().setValue(messageC);

        msgEdittext.setText("");
        onResume();

       /* reference.child(Cuid).child("Chat").child("infoconvs").orderByChild("username").equalTo(username).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onCancelled(DatabaseError databaseError) {}});
*/

        // set it in the other user and check if the conv exist or not by the two methode
/*
        reference.child(ido).child("Chat").child("infoconvs").orderByChild("username").equalTo(Cusername).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onCancelled(DatabaseError databaseError) {}});
  */
    }

    public  void  CheckexistC () {
        reference.child(Cuid).child("Chat").child("infoconvs").orderByChild("username").equalTo(username).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                idconvC = dataSnapshot.getKey();
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
    public  void  CheckexistO () {
        reference.orderByChild("fullName").equalTo(username).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ido = dataSnapshot.getKey();
reference.child(dataSnapshot.getKey()).child("Chat").child("infoconvs").orderByChild("username").equalTo(Cusername).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        idconvO = dataSnapshot.getKey();
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
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onCancelled(DatabaseError databaseError) {}});


    }


    public  static  class ConvViewHolder extends  RecyclerView.ViewHolder {
        View mView;
        CardView cardView;

        public ConvViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        public  void setgravityright() {

            cardView = mView.findViewById(R.id.msgcard);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    RecyclerView.LayoutParams.WRAP_CONTENT,RecyclerView.LayoutParams.WRAP_CONTENT );
            params.gravity = Gravity.CENTER;
            cardView.setLayoutParams(params);}

        public  void setgravityleft() {

            cardView = mView.findViewById(R.id.msgcard);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);

            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            cardView.setLayoutParams(layoutParams);
        }



        public  void setmsg (String  msg) {
            TextView  msgTextView = mView.findViewById(R.id.message);
            msgTextView.setText(msg);
        }
        public  void setusername (String  username) {
            TextView  usernameTextView = mView.findViewById(R.id.username);
            usernameTextView.setText(username);
        }
        public  void  seturi (Context context , String image) {
            ImageView Image = mView.findViewById(R.id.user_image);
            Picasso.with(context).load(image).into(Image);
        }

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
