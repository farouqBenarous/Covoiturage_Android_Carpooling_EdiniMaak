package com.example.sony.tabhost;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
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
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class showprofile extends AppCompatActivity {
    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);
    Query query;
String username,Cusername,cemail,curi,email,Uri,UID,CUid;
String Uid,SetUri,friend,dép,arv,date,time,nbplace,price,ph,id,Type;
TextView fullname,Email,textview1;
ImageView profilimage;
ImageButton badd,bdelete,bwaiting,brequested;
FirebaseDatabase database;
DatabaseReference reference,reference1,reference2,reference3,reference4,reference5;
FirebaseAuth auth;
FirebaseUser firebaseUser;
modeluser user,cuser;
RecyclerView recyclerView;
FirebaseRecyclerAdapter<Trip,BlogHolder> firebaseRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showprofile);
        fullname = findViewById(R.id.fullname);
        Email = findViewById(R.id.email);
        profilimage = findViewById(R.id.profile);

        textview1 = findViewById(R.id.textview1);

        badd = findViewById(R.id.add);
        bdelete = findViewById(R.id.delete);
        bwaiting = findViewById(R.id.waiting);
        brequested = findViewById(R.id.requested);

        Intent intent = getIntent();
        username = intent.getStringExtra("query");
        fullname.setText(username);
       auth = FirebaseAuth.getInstance();
       firebaseUser = auth.getCurrentUser();
        CUid = firebaseUser.getUid();
        Cusername = firebaseUser.getDisplayName();
        cemail = firebaseUser.getEmail();
        curi = firebaseUser.getPhotoUrl().toString();
        cuser = new modeluser(cemail,Cusername,curi);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("User");
        reference1 = database.getReference("User");
        reference2 = database.getReference("User");
        reference3 = database.getReference("User");
        reference4 = database.getReference("User");
        reference5   = database.getReference("User");
        recyclerView = findViewById(R.id.Trips);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        reference.orderByChild("fullName").equalTo(username).addChildEventListener(new ChildEventListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                UID = dataSnapshot.getKey();
                query = FirebaseDatabase.getInstance().getReference().child("User").child(UID).child("MyTrips").getRef();
                email = dataSnapshot.child("email").getValue(String.class);
                Email.setText(email);
                Uri = dataSnapshot.child("photo_profile").getValue(String.class);
                user = new modeluser(email,username,Uri);
                if (!Objects.equals(Uri, "vide")) {
                    Picasso.with(getApplicationContext()).load(Uri).into(profilimage); }


                FirebaseRecyclerOptions<Trip> options =
                        new FirebaseRecyclerOptions.Builder<Trip>()
                                .setQuery(
                                        query
                                        ,Trip.class)
                                .build();
                firebaseRecyclerAdapter = new
                        FirebaseRecyclerAdapter<Trip,BlogHolder>
                                (options) {
                            @NonNull
                            @Override
            public BlogHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                                // Create a new instance of the ViewHolder, in this case we are using a custom
                                // layout called R.layout.message for each item
                                View view = LayoutInflater.from(parent.getContext())
                                        .inflate(R.layout.blogrow, parent, false);
                                return new BlogHolder(view);
                            }

                            @Override
            protected void onBindViewHolder(@NonNull BlogHolder viewHolder, final int position, @NonNull final Trip model) {
                                viewHolder.setUsername(model.getUsernameUser());
                                if (model.getUriPhoto() == null || model.getUriPhoto().equals("vide")) {
                                    String Uri = "https://firebasestorage.googleapis.com/v0/b/convoiturage-81bdb.appspot.com/o/photoscars%2Fcar-icon.jpg?alt=media&token=00cf0730-3e88-4f7f-a1ff-5c92fda9dcae";
                                    SetUri = Uri;
                                    viewHolder.setImage(getApplicationContext(), Uri); }
                                else { viewHolder.setImage(getApplicationContext(), model.getUriPhoto());
                                    SetUri = model.getUriPhoto(); }

                                viewHolder.setdép(" Departure City : " + model.getDép());
                                dép =model.getDép();
                                viewHolder.setarv(" Arrived City  : " + model.getArv());
                                arv =model.getArv();
                                final String dat = " " + model.getDay()+"/"+model.getMonth()+"/"+model.getYear() ;
                                viewHolder.setdate(" " + model.getDay()+"/"+model.getMonth()+"/"+model.getYear() );
                                viewHolder.settime("Departure Time : "+model.getHour()+" : "+model.getMinute());
                                time = model.getHour()+" : "+model.getMinute();
                                viewHolder.setplace("Avalaible place : "+model.getNbPlace());
                                nbplace = String.valueOf(model.getNbPlace());
                                viewHolder.setprice("Price : "+model.getPrice());
                                price = String.valueOf(model.getPrice());
                                viewHolder.settype(model.getType());
                                Type = model.getType();
                                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                                    @RequiresApi(api = Build.VERSION_CODES.N)
                                    @Override
                                    public void onClick(View view) {
                                        ph = model.getPhonenumber();
                                        id = model.getId();
                                        Car car =   model.getCar();
                                        String idcar =car.getId();

                                        Intent myIntent = new Intent(getApplicationContext(),TripShowBlogBigger.class);
                                        myIntent.putExtra("email",email);
                                        myIntent.putExtra("username",username);
                                        myIntent.putExtra("Uid",Uid);
                                        myIntent.putExtra("Uri", SetUri);
                                        myIntent.putExtra("dép",dép);
                                        myIntent.putExtra("arv",arv );
                                        myIntent.putExtra("date", dat );
                                        myIntent.putExtra("time",time );
                                        myIntent.putExtra("nbplace",nbplace);
                                        myIntent.putExtra("price",price);
                                        myIntent.putExtra("phone",ph);
                                        myIntent.putExtra("id",id);
                                        myIntent.putExtra("idcar",idcar);
                                        myIntent.putExtra("Type",Type);
                                        startActivity(myIntent);
                                    } } ) ; }
                };

                recyclerView.setAdapter(firebaseRecyclerAdapter);
                firebaseRecyclerAdapter.startListening(); }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onCancelled(DatabaseError databaseError) {}});

    // Add a test here if the user exist in the friend list or request list or request sended
        checkexistFriendlist(username);
        checkexistRequestList(username);
        checkexistRequestSended(username);
    }


    @SuppressLint("ShowToast")
    public void AddFriend(View view) {
        //add the other user
        reference.child(CUid).child("requestsended").push().setValue(user);

        // add the currente user here
        reference.child(UID).
                child("requestlist").
                push().
                setValue(cuser);

        Toast.makeText(getApplicationContext(),"Request sended ",Toast.LENGTH_SHORT);

        badd.setVisibility(View.GONE);
        bwaiting.setVisibility(View.VISIBLE);
        textview1.setText("Request Sended"); }

    public void waiting(View view){

        //delte the other user email by intent
        reference.child(CUid).child("requestsended").orderByChild("email").equalTo(email).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String u = dataSnapshot.getKey();
                reference1.child(CUid).child("requestsended").child(u).removeValue();

            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onCancelled(DatabaseError databaseError) { }});


        // delete te current user

        reference.child(UID).child("requestlist").orderByChild("email").equalTo(cemail).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String u = dataSnapshot.getKey();
                reference1.child(UID).child("requestlist").child(u).removeValue();
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onCancelled(DatabaseError databaseError) { }});

        Toast.makeText(getApplicationContext(),"Request Canceled ",Toast.LENGTH_SHORT);

        bwaiting.setVisibility(View.GONE);
        textview1.setText("Add friend");
        badd.setVisibility(View.VISIBLE);



    }

    public void Delete(View view){
        reference2.child(CUid).child("friendlist").orderByChild("fullName").equalTo(username).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String u = dataSnapshot.getKey();
                reference2.child(CUid).child("friendlist").child(u).removeValue();

            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onCancelled(DatabaseError databaseError) {}});

        reference2.child(UID).child("friendlist").orderByChild("fullName").equalTo(Cusername).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String u = dataSnapshot.getKey();
                reference2.child(UID).child("friendlist").child(u).removeValue();

            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onCancelled(DatabaseError databaseError) {}});

        Toast.makeText(getApplicationContext(),"User Deleted Successfully",Toast.LENGTH_SHORT).show();

        textview1.setText("Add friend");
        bdelete.setVisibility(View.GONE);
        badd.setVisibility(View.VISIBLE);
    }

    public void requested(View view) {

        PopupMenu popup = new PopupMenu(showprofile.this,brequested);
        popup.getMenuInflater().inflate(R.menu.popuplist,popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if ( Objects.equals("Accept Request" , menuItem.getTitle())) {
                    // Delete from Requsst list and add to the friend list Current User
                    reference3.child(CUid).child("requestlist").orderByChild("fullName").equalTo(username).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            String u = dataSnapshot.getKey();
                            reference3.child(CUid).child("requestlist").child(u).removeValue();

                        }
                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {}
                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                        @Override
                        public void onCancelled(DatabaseError databaseError) {}});
// IM HERE
                    // Add the same UID
                    reference3.child(CUid).child("friendlist").child(UID).setValue(user);

                    //Delete from Requestsended list and add to the friend list the other user
                    reference3.child(UID).child("requestsended").orderByChild("fullName").equalTo(Cusername).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            String u = dataSnapshot.getKey();
                            reference3.child(UID).child("requestsended").child(u).removeValue();
                            }
                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {}
                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                        @Override
                        public void onCancelled(DatabaseError databaseError) {}});
                    reference3.child(UID).child("friendlist").push().setValue(cuser);

                    // Notifications
                    String desp = Cusername+ "  Have Accepted Your Friend Request ";
                    String type  = "accepted";
                    modelNotifications not = new modelNotifications(String.valueOf(generateViewId()),Cusername,curi,desp,type);
                   reference5.child(UID).child("notificaions").push().setValue(not);



                    textview1.setText("Friend");
                    Toast.makeText(getApplicationContext(),"Request Accepted",Toast.LENGTH_SHORT ).show();
                    brequested.setVisibility(View.GONE);
                    bdelete.setVisibility(View.VISIBLE);
                    }
                if (Objects.equals("Deny Request" , menuItem.getTitle())) {
                    reference4.child(CUid).child("requestlist").orderByChild("email").equalTo(email).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            String u = dataSnapshot.getKey();
                            reference4.child(CUid).child("requestlist").child(u).removeValue();

                        }
                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {}
                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                        @Override
                        public void onCancelled(DatabaseError databaseError) { }});


                    reference.child(UID).child("requestsended").orderByChild("email").equalTo(cemail).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            String u = dataSnapshot.getKey();
                            reference1.child(UID).child("requestsended").child(u).removeValue();
                        }
                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {}
                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                        @Override
                        public void onCancelled(DatabaseError databaseError) { }});

                    Toast.makeText(getApplicationContext(),"Request Denyed ",Toast.LENGTH_SHORT);

                    textview1.setText("Add friend");
                    brequested.setVisibility(View.GONE);
                    badd.setVisibility(View.VISIBLE);
                    }

                return false; }
        });
 popup.show();
    }


    public void Message(View view){
        Intent intent = new Intent(getApplicationContext(),Conv.class);
        intent.putExtra("username",username);
        intent.putExtra("SetUri",Uri);
        startActivity(intent);
        }

    public void About(View view) {
        Intent intent =    new Intent(getApplicationContext(),Aboutprofile.class);
        intent.putExtra("email",email);
        startActivity(intent);   }

        public void checkexistFriendlist (String username) {
        FirebaseDatabase database;
        DatabaseReference reference;
        FirebaseAuth auth;
        FirebaseUser firebaseUser;
        String CUID;

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        CUID = firebaseUser.getUid();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("User").child(CUID).child("friendlist").getRef();

            reference.orderByChild("fullName").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                       badd.setVisibility(View.GONE);
                       textview1.setText("Friend");
                        bdelete.setVisibility(View.VISIBLE);
                    } }
                @Override
                public void onCancelled(DatabaseError databaseError) {}});

    }

        public void checkexistRequestSended (String username) {
            FirebaseDatabase database;
            DatabaseReference reference;
            FirebaseAuth auth;
            FirebaseUser firebaseUser;
            String CUID;

            auth = FirebaseAuth.getInstance();
            firebaseUser = auth.getCurrentUser();
            CUID = firebaseUser.getUid();
            database = FirebaseDatabase.getInstance();
            reference = database.getReference("User").child(CUID).child("requestsended").getRef();

            reference.orderByChild("fullName").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        badd.setVisibility(View.GONE);
                        textview1.setText("Cancel Request");
                        bwaiting.setVisibility(View.VISIBLE); } }
                @Override
                public void onCancelled(DatabaseError databaseError) {}});


          }


        public void checkexistRequestList (String username) {
            final FirebaseDatabase database;
            DatabaseReference reference;
            FirebaseAuth auth;
            FirebaseUser firebaseUser;
            String CUID;

            auth = FirebaseAuth.getInstance();
            firebaseUser = auth.getCurrentUser();
            CUID = firebaseUser.getUid();
            database = FirebaseDatabase.getInstance();
            reference = database.getReference("User").child(CUID).child("requestlist").getRef();

         reference.orderByChild("fullName").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
             @Override
             public void onDataChange(DataSnapshot dataSnapshot) {
             if (dataSnapshot.exists()) {
                 badd.setVisibility(View.GONE);
                 textview1.setText("Request ");
                 brequested.setVisibility(View.VISIBLE); } }
             @Override
             public void onCancelled(DatabaseError databaseError) {}});

            }

    public class BlogHolder extends RecyclerView.ViewHolder {
        View mView;

        public BlogHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setImage(Context context, String image) {
            ImageView carImage = mView.findViewById(R.id.user_single_image);
            Picasso.with(context).
                    load(image).
                    into(carImage);
        }


        public void setUsername(String username) {
            TextView brandTextView = mView.findViewById(R.id.Username);
            brandTextView.setText(username);
        }

        // Set of its a friend or not
        public void setfriend(String fr) {
            TextView modelTextView = mView.findViewById(R.id.friend);
            modelTextView.setText(fr);
        }

        public void setdép(String dép) {
            TextView matriculeTextView = mView.findViewById(R.id.dép);
            matriculeTextView.setText(dép);
        }

        public void setarv(String arv) {
            TextView numcartegriseTextView = mView.findViewById(R.id.arv);
            numcartegriseTextView.setText(arv);
        }
        public void setdate(String date) {
            TextView Date = mView.findViewById(R.id.datee);
            Date.setText(date);
        }
        public void settime(String time) {
            TextView Time = mView.findViewById(R.id.time);
            Time.setText(time);
        }

        public void setplace(String place) {
            TextView Place = mView.findViewById(R.id.place);
            Place.setText(place);
        }

        public void setprice(String price) {
            TextView Price = mView.findViewById(R.id.time);
            Price.setText(price);
        }


        public void settype(String type) {
            TextView Type = mView.findViewById(R.id.Type);
            Type.setText(type);
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
