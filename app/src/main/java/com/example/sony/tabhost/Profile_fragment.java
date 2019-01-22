package com.example.sony.tabhost;


import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class Profile_fragment extends Fragment {
    private FirebaseAuth auth;
    ProgressBar progressbar1;
    TextView fullname;
    FirebaseDatabase database;
    DatabaseReference ref, reference;
    FirebaseUser firebaseUser;
    RecyclerView recyclerView;

    String Uid,SetUri,friend,dép,arv,date,time,nbplace,price,ph,id,username,email,Type;

    ImageView profileimageView;
    FirebaseRecyclerAdapter<Trip, BlogHolder> firebaseRecyclerAdapter;

    public Profile_fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
     View view =inflater.inflate(R.layout.fragment_profile, container, false);
        profileimageView = view.findViewById(R.id.profileimage);
        fullname = view.findViewById(R.id.fullname);
        // Inflate the layout for this fragment
        recyclerView = (RecyclerView) view.findViewById(R.id.rr);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("User");
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        assert firebaseUser != null;
       email = firebaseUser.getEmail();
        username = firebaseUser.getDisplayName();
        fullname.setText(username);
        ref = database.getReference();
        ref.child("User").orderByChild("email").equalTo(email).addChildEventListener(new ChildEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //set the profile image to the imageview
                String photo = dataSnapshot.child("photo_profile").getValue(String.class);
                if (Objects.equals(photo,"vide")) {
                    profileimageView.setImageResource(R.drawable.profile_vide);

                } else {
                    Picasso.with(getContext()).load(photo).into(profileimageView);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        Uid = firebaseUser.getUid();
        reference = reference.child(Uid).child("MyTrips").getRef();
        Query query = FirebaseDatabase.getInstance().getReference().child("User").child(Uid).child("MyTrips").getRef();
        FirebaseRecyclerOptions<Trip> options =
                new FirebaseRecyclerOptions.Builder<Trip>()
                        .setQuery(query,Trip.class)
                        .build();
        firebaseRecyclerAdapter = new
                FirebaseRecyclerAdapter<Trip, BlogHolder>
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
                        username = model.getUsernameUser();

                        viewHolder.setImage(getContext(), model.getUriPhoto());
                         SetUri = model.getUriPhoto();

                        viewHolder.setdép(" Departure City : " + model.getDép());
                        dép =model.getDép();

                        viewHolder.setarv(" Arrived City  : " + model.getArv());
                        arv =model.getArv();
                        final String dat = " " + model.getDay()+"/"+model.getMonth()+"/"+model.getYear() ;
                        viewHolder.setdate(" Departure date :" + model.getDay()+"/"+model.getMonth()+"/"+model.getYear() );

                        viewHolder.settime("Departure Time : "+model.getHour()+" : "+model.getMinute());
                        time = model.getHour()+" : "+model.getMinute();

                        viewHolder.setplace("Avalaible place : "+model.getNbPlace());
                        nbplace = String.valueOf(model.getNbPlace());

                        viewHolder.setprice("Price : "+model.getPrice());
                        price = String.valueOf(model.getPrice());

                        viewHolder.settype("Type : "+model.getType());
                        Type = model.getType();

                        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.N)
                            @Override
                            public void onClick(View view) {
                                ph = model.getPhonenumber();
                                id = model.getId();
                             Car car =   model.getCar();
                            String idcar =car.getId();

                                Intent myIntent = new Intent(getContext(), ProfileShowBlogBigger.class);
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
                            } } ) ; } } ;

        recyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();





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
            TextView Price = mView.findViewById(R.id.price);
            Price.setText(price);
        }


        public void settype(String type) {
            TextView Type = mView.findViewById(R.id.Type);
            Type.setText(type);
        }
        }



}
