package com.example.sony.tabhost;


import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */

public class Notification_fragment extends Fragment {
    RecyclerView recyclerView ;
    FirebaseRecyclerAdapter<modelNotifications, NotHolder> firebaseRecyclerAdapter;
    modeluser user ,cuser;
    modelNotifications Notification;
    FirebaseDatabase database;
    DatabaseReference reference,reference1,reference2,reference3,reference4;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    ImageView profileimageView;
    TextView fullname;
    String CUid,UID;
    String SetUri,cusername,cemail,curi,username,email,descreptios;



    public Notification_fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        // Inflate the layout for this fragment
        View v =inflater.inflate(R.layout.fragment_notification_fragment, container, false);


        recyclerView = v.findViewById(R.id.not);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return v;
        }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("User");
        reference3 = database.getReference("User");
        reference2 = database.getReference("User");
        reference1 = database.getReference("User");
        reference4 = database.getReference("User");



        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        assert firebaseUser != null;
        cemail = firebaseUser.getEmail();
        cusername = firebaseUser.getDisplayName();
        CUid = firebaseUser.getUid();
        curi = firebaseUser.getPhotoUrl().toString();
        cuser = new modeluser(cemail,cusername,curi);

        reference = reference.child(CUid).child("Chat").getRef();
        Query query = FirebaseDatabase.getInstance().getReference().child("User").child(CUid).child("notificaions").getRef();
        FirebaseRecyclerOptions<modelNotifications> options = new FirebaseRecyclerOptions.Builder<modelNotifications>()
                .setQuery(query,modelNotifications.class)
                .build();
        firebaseRecyclerAdapter = new
                FirebaseRecyclerAdapter<modelNotifications,NotHolder>(options) {
                    @NonNull
                    @Override
                    public NotHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        // Create a new instance of the ViewHolder, in this case we are using a custom
                        // layout called R.layout.message for each item
                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.notificationrow, parent, false);
                        return new NotHolder(view);
                    }

                    @TargetApi(Build.VERSION_CODES.KITKAT)
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    protected void onBindViewHolder(@NonNull NotHolder viewHolder, final int position, @NonNull final modelNotifications model) {


                        viewHolder.setImage(getContext(), model.getPhoto_profile());
                        SetUri = model.getPhoto_profile();



                        viewHolder.setUsername(model.getFullname());
                        username =  model.getFullname();

                        viewHolder.setDes(model.getDescreptios());
                        descreptios = model.getDescreptios();

                        NotificationCompat.Builder  mBuilder =
                                (NotificationCompat.Builder) new NotificationCompat.Builder(getContext())
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentTitle(model.getFullname())
                                .setContentText(model.getDescreptios());
                        Context context = getContext();
                        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                        notificationManager.notify(0,mBuilder.build());

                        // Check the type to set evry type its own click listner
                        //when you reserve a trip take to the chat or trip
                        if (Objects.equals(model.getType(),"reservation")) {
                            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(getContext(),Conv.class);
                                    intent.putExtra("username",username);
                                    intent.putExtra("SetUri",SetUri);
                                    intent.putExtra("lastmsg",descreptios);
                                    startActivity(intent);

                                };
                            });
                              }

                        //when someone reserve your trip takes to chat too
                        if (Objects.equals(model.getType(),"reserved")) {
                            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(getContext(),Conv.class);
                                    intent.putExtra("username",username);
                                    intent.putExtra("SetUri",SetUri);
                                    intent.putExtra("lastmsg",descreptios);
                                    startActivity(intent);

                                };
                            });
                            }
                            //when someone accepet your  request take to the profile

                        if (Objects.equals(model.getType(),"accepted")) {
                            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(getContext(),showprofile.class);
                                    intent.putExtra("query",username);
                                    intent.putExtra("SetUri",SetUri);
                                    intent.putExtra("lastmsg",descreptios);
                                    startActivity(intent);

                                };
                            });

                        }
                        // when someone of your friend post a Trip  take to the Profile
                        //CHANGE IT LATER TAKE TO THE TRIP CHANGE THE TRIP INTENT
                        if (Objects.equals(model.getType(),"Trip")) {
                            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(getContext(),Conv.class);
                                    intent.putExtra("query",username);
                                    intent.putExtra("SetUri",SetUri);
                                    intent.putExtra("lastmsg",descreptios);
                                    startActivity(intent);

                                };
                            });
                        }



                    } } ;
        recyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();


    }


    public class NotHolder extends RecyclerView.ViewHolder {
        View mView;
        public NotHolder(View itemView) {
            super(itemView);
            mView = itemView;

        }
        public void setImage(Context context, String image) {
            ImageView Profilimage = mView.findViewById(R.id.userimage);
            Picasso.with(context).
                    load(image).
                    into(Profilimage);
        }
        public void setUsername(String username) {
            TextView fullname = mView.findViewById(R.id.username);
            fullname.setText(username);
        }

        public void setDes(String descreptios) {
            TextView Descreptios = mView.findViewById(R.id.des);
            Descreptios.setText(descreptios);
        }
    }


}
