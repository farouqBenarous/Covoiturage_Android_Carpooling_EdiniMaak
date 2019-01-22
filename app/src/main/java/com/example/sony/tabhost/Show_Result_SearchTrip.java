package com.example.sony.tabhost;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class Show_Result_SearchTrip extends AppCompatActivity {
    private FirebaseAuth auth;
    ProgressBar progressbar1;
    TextView fullname;
    FirebaseDatabase database;
    DatabaseReference ref, reference;
    FirebaseUser firebaseUser;
    String CUID;
    RecyclerView recyclerView;

    FirebaseRecyclerAdapter<Trip,Show_Result_SearchTrip.BlogHolder> firebaseRecyclerAdapter;
    String Uid,Cusername,SetUri,friend,dép,arv,date,time,nbplace,price,ph,id,username,email,type,Friend;
    int  o;
    String [] friendlist ;
    String [] empty_friendlist;
    modeluser modeluser = new modeluser();
   String Sdép, Sarv ,Stime ,Sdate,Stype;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show__result__search_trip);

        Intent intent = getIntent();
        Sdép = intent.getStringExtra("départ");
        Sarv = intent.getStringExtra("arrivé");
        Stime = intent.getStringExtra("time");
        Sdate = intent.getStringExtra("date");
        Stype = intent.getStringExtra("TypeTrip");

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("User");
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        assert firebaseUser != null;
        Uid = firebaseUser.getUid();
        Cusername = firebaseUser.getDisplayName();
        reference = reference.child(Uid).child("MyTrips").getRef();

        ref = database.getReference("User");


        ref.child(Uid).child("friendlist").addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (Objects.equals(dataSnapshot.getValue(), "empty") || Objects.equals(dataSnapshot.getValue(),null ) ) {

                    empty_friendlist = new String[]{"You Have No friend "};
                }
                else  {
                    o = (int) dataSnapshot.getChildrenCount();
                    friendlist = new String[o];
                    int z=0;

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        // add a ListenerForSingleValueEvent
                        // How To Stop this Fuck'in Iteration ( Random Add)
                        modeluser.setfullName( ds.child("fullName").getValue(String.class));
                        friendlist [z] =modeluser.getfullName();
                        z ++;
                    }
                } }
            @Override
            public void onCancelled(DatabaseError databaseError) {}});



        recyclerView = (RecyclerView)findViewById(R.id.searchRecyclerView);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


    }

    @Override
    protected void onStart() {
        super.onStart();
        Query query = FirebaseDatabase.getInstance().getReference().child("Trips").getRef();
        FirebaseRecyclerOptions<Trip> options =
                new FirebaseRecyclerOptions.Builder<Trip>()
                        .setQuery(query,Trip.class)
                        .build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Trip, Show_Result_SearchTrip.BlogHolder>(options) {
            @NonNull
            @Override
            public Show_Result_SearchTrip.BlogHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.blogrow, parent, false);
                return new Show_Result_SearchTrip.BlogHolder(view);
            }

            @TargetApi(Build.VERSION_CODES.KITKAT)
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            protected void onBindViewHolder(@NonNull final Show_Result_SearchTrip.BlogHolder viewHolder, final int position, @NonNull final Trip model) {
                // Add an if here check if the user is in the friend list or not and if the type of the trip is ppublic or friendd only
                //  Friends and ( )
              if (Objects.equals(model.getArv(),Sarv)
                   &&Objects.equals(model.getDép(),Sdép)
                    && Objects.equals(model.getDay()+"/"+model.getMonth()+"/"+model.getYear(),Sdate)
                     && Objects.equals(model.getHour()+":"+model.getMinute(),Stime)
                      ) {

                  if (Objects.equals(Stype,"Friends")&&!checkexistfirnelist(friendlist,o,model.getUsernameUser()))
                  { viewHolder.setheight(); }
                  else  {         viewHolder.setUsername(model.getUsernameUser());
                      username = model.getUsernameUser();

                      viewHolder.setImage(getApplicationContext(), model.getUriPhoto());
                      SetUri = model.getUriPhoto();

                      // Add an if to check the friend list
                      if (checkexistfirnelist(friendlist,o,model.getUsernameUser()) ) { viewHolder.setfriend("Friend");  }
                      else  {
                          if (Objects.equals(model.getUsernameUser(),Cusername)) {viewHolder.setfriend("");}
                          else {  viewHolder.setfriend("Not Friend"); }
                      }


                      viewHolder.setdép(" Departure City : " + model.getDép());
                      dép =model.getDép();

                      viewHolder.setarv(" Arrived City  : " + model.getArv());
                      arv =model.getArv();

                      final String dat = " " + model.getDay()+"/"+model.getMonth()+"/"+model.getYear() ;
                      viewHolder.setdate("Departure date :  " + model.getDay()+"/"+model.getMonth()+"/"+model.getYear() );

                      viewHolder.settime("Departure Time : "+model.getHour()+" : "+model.getMinute());
                      time = model.getHour()+" : "+model.getMinute();

                      viewHolder.setplace("Avalaible place : "+model.getNbPlace());
                      nbplace = String.valueOf(model.getNbPlace());

                      viewHolder.setprice("Price : "+model.getPrice());
                      price = String.valueOf(model.getPrice());

                      viewHolder.settype("Type : "+model.getType());
                      type= model.getType();

                      viewHolder.profile.setOnClickListener(new View.OnClickListener() {
                          @Override
                          public void onClick(View view) {
                              if (Objects.equals(model.getUsernameUser(),Cusername)) {
                                  Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                  startActivity(intent);

                              }
                              else {
                                  Intent intent = new Intent(getApplicationContext(),showprofile.class);
                                  intent.putExtra("query",model.getUsernameUser());
                                  startActivity(intent);

                              }

                          }} );

                      viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                          @RequiresApi(api = Build.VERSION_CODES.N)
                          @Override
                          public void onClick(View view) {
                              ph = model.getPhonenumber();
                              id = model.getId();
                              Car car =   model.getCar();
                              String idcar =car.getId();
                              username = model.getUsernameUser();
                              type= model.getType();
                              price = String.valueOf(model.getPrice());
                              nbplace = String.valueOf(model.getNbPlace());
                              time = model.getHour()+" : "+model.getMinute();
                              arv =model.getArv();
                              dép =model.getDép();
                              SetUri = model.getUriPhoto();

                              Intent myIntent = new Intent(getApplicationContext(), TripShowBlogBigger.class);
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
                              myIntent.putExtra("type",type);
                              startActivity(myIntent); } } ) ;  }
                }
                else  { viewHolder.setheight(); }

            }

        };

        recyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();




    }
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public  boolean checkexistfirnelist (String[] list, int a, String username) {
        int i ;
        for (i = 0 ; i<a;i++) {
            if (Objects.equals(list[i],username) ) {
                return true;
            }
        }
        return false;

    }

    public class BlogHolder extends RecyclerView.ViewHolder {
        View mView;
        ImageView profile;
        CardView cardView;

        public BlogHolder(View itemView) {
            super(itemView);
            mView = itemView;
            profile = mView.findViewById(R.id.user_single_image);
            cardView = mView.findViewById(R.id.cardview);
        }

        public  void  setheight () {
            cardView = mView.findViewById(R.id.cardview);
            cardView.setLayoutParams(new FrameLayout.LayoutParams(0,0));

            /*            CardView.LayoutParams layoutParams = (CardView.LayoutParams)
                    cardView.getLayoutParams();
            layoutParams.height = 0;

      */
            //cardView.removeView(mView);
            //cardView.setCardElevation(5);
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
            TextView Price = mView.findViewById(R.id.price);
            Price.setText(price);
        }
        public void settype(String type) {
            TextView Type = mView.findViewById(R.id.Type);
            Type.setText(type);
        }


    }



}
