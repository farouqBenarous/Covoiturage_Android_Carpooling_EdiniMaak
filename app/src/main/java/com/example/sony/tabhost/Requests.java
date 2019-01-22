package com.example.sony.tabhost;


import android.app.NotificationManager;
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
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.squareup.picasso.Picasso;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * A simple {@link Fragment} subclass.
 */
public class Requests extends Fragment {
    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);
    RecyclerView recyclerView;
FirebaseRecyclerAdapter<modeluser, Requests.BlogHolder> firebaseRecyclerAdapter;
modeluser user ,cuser;
FirebaseDatabase database;
DatabaseReference reference,reference1,reference2,reference3,reference4,reference5;
FirebaseAuth auth;
FirebaseUser firebaseUser;
ImageView profileimageView;
TextView fullname;
String CUid,UID;
String SetUri,cusername,cemail,curi,username,email;



    public Requests() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_requests, container, false);
        profileimageView = view.findViewById(R.id.profileimage);
        fullname = view.findViewById(R.id.fullname);



        recyclerView = (RecyclerView) view.findViewById(R.id.rr);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));



        // Inflate the layout for this fragment

        return view;
    }


    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("User");
       reference3 = database.getReference("User");
       reference2 = database.getReference("User");
       reference1 = database.getReference("User");
        reference4 = database.getReference("User");
        reference5 = database.getReference("User");

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        assert firebaseUser != null;
        cemail = firebaseUser.getEmail();
        cusername = firebaseUser.getDisplayName();
        CUid = firebaseUser.getUid();
        curi = firebaseUser.getPhotoUrl().toString();
        cuser = new modeluser(cemail,cusername,curi);

        reference = reference.child(CUid).child("requestlist").getRef();
        Query query = FirebaseDatabase.getInstance().getReference().child("User").child(CUid).child("requestlist").getRef();
        FirebaseRecyclerOptions<modeluser> options =
                new FirebaseRecyclerOptions.Builder<modeluser>()
                        .setQuery(query,modeluser.class)
                        .build();
        firebaseRecyclerAdapter = new
                FirebaseRecyclerAdapter<modeluser,Requests.BlogHolder>
                        (options) {
                    @NonNull
                    @Override
                    public Requests.BlogHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        // Create a new instance of the ViewHolder, in this case we are using a custom
                        // layout called R.layout.message for each item
                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.requestrow, parent, false);
                        return new Requests.BlogHolder(view);
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull Requests.BlogHolder viewHolder, final int position, @NonNull final modeluser model) {
                        viewHolder.setUsername(model.getfullName());

                        viewHolder.setImage(getContext(), model.getPhoto_profile());
                            SetUri = model.getPhoto_profile();


                        viewHolder.setUsername(model.getfullName());

                        viewHolder.setemail(model.getEmail());

                        email =model.getEmail();
                        username =  model.getfullName();
                        email =model.getEmail();
                        user = new modeluser(email,username,SetUri);


                        viewHolder.Accept.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

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
                                reference3.child(CUid).child("friendlist").push().setValue(user);

                                //Delete from Requestsended list and add to the friend list the other user

                                reference3.orderByChild("fullName").equalTo(username).addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                        UID = dataSnapshot.getKey();

                                        reference3.child(UID).child("requestsended").orderByChild("fullName").equalTo(cusername).
                                                addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                              String u = dataSnapshot.getKey();reference3.child(UID).child("requestsended").child(u).removeValue();
                              reference3.child(UID).child("friendlist").push().setValue(cuser);

                                    //Notifications
                                    String desp = cusername+ "  Have Accepted Your Friend Request ";
                                    String type  = "accepted";
                         modelNotifications not = new modelNotifications(String.valueOf(generateViewId()),cusername,curi,desp,type);
                         reference5.child(UID).child("notificaions").push().setValue(not);


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


                                Toast.makeText(getContext(),"Request Accepted ",Toast.LENGTH_SHORT).show();

                                }});

                        viewHolder.Deny.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                 // Delete from the request list
                                reference3.child(CUid).child("requestlist").orderByChild("fullName").equalTo(username).addChildEventListener(new ChildEventListener() {
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

                                // Delete from the requestSended
                                reference.orderByChild("fullName").equalTo(username).addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                    UID =dataSnapshot.getKey();
                reference.child(UID).child("Requestsended").orderByChild("fullName").equalTo(cusername).addChildEventListener(new ChildEventListener() {
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
                                        Toast.makeText(getContext(),"Request Denyed ",Toast.LENGTH_SHORT);

                                        }
                                    @Override
                                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                                    @Override
                                    public void onChildRemoved(DataSnapshot dataSnapshot) {}
                                    @Override
                                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {}});
                            }});

                    viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getContext(),showprofile.class);
                            intent.putExtra("query",username);
                            startActivity(intent);

                        };
                    });

                    } } ;
        recyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }
    public class BlogHolder extends RecyclerView.ViewHolder {
        View mView;
        Button Accept,Deny;

        public BlogHolder(View itemView) {
            super(itemView);
            mView = itemView;
            Accept = mView.findViewById(R.id.accept);
            Deny= mView.findViewById(R.id.delete);

        }
        public void setImage(Context context, String image) {
            ImageView Profilimage = mView.findViewById(R.id.user_image);
            Picasso.with(context).
                    load(image).
                    into(Profilimage);
        }
        public void setUsername(String username) {
            TextView fullname = mView.findViewById(R.id.Userna);
            fullname.setText(username);
        }

        public void setemail(String email) {
            TextView Email = mView.findViewById(R.id.email);
            Email.setText(email);
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
