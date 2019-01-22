package com.example.sony.tabhost;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.squareup.picasso.Picasso;

public class SearchtoMessage extends AppCompatActivity {
    RecyclerView recyclerView;
    FirebaseRecyclerAdapter<modeluser, SearchtoMessage.userdHolder> firebaseRecyclerAdapter;
    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    String CUid;
    String SetUri,cusername,cemail,curi,username,email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchto_message);

    }

    @Override
    protected void onStart() {
        super.onStart();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("User");


        recyclerView = findViewById(R.id.search);
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        assert firebaseUser != null;
        cemail = firebaseUser.getEmail();
        cusername = firebaseUser.getDisplayName();
        CUid = firebaseUser.getUid();
        curi = firebaseUser.getPhotoUrl().toString();
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        Query query = FirebaseDatabase.getInstance().getReference().child("infouserse").getRef();
        FirebaseRecyclerOptions<modeluser> options =
                new FirebaseRecyclerOptions.Builder<modeluser>()
                        .setQuery(query,modeluser.class)
                        .build();
        firebaseRecyclerAdapter = new
                FirebaseRecyclerAdapter<modeluser,SearchtoMessage.userdHolder>
                        (options) {
                    @NonNull
                    @Override
                    public SearchtoMessage.userdHolder  onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        // Create a new instance of the ViewHolder, in this case we are using a custom
                        // layout called R.layout.message for each item
                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.friendrow, parent, false);
                        return new SearchtoMessage.userdHolder(view);
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull SearchtoMessage.userdHolder viewHolder, final int position, @NonNull final modeluser model) {

                        viewHolder.setImage(getApplicationContext(), model.getPhoto_profile());
                        SetUri = model.getPhoto_profile();
                        final String uri = model.getPhoto_profile();

                        viewHolder.setUsername(model.getfullName());
                        username =  model.getfullName();
                        final String name = model.getfullName();
                        viewHolder.setemail(model.getEmail());

                        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(getApplicationContext(),Conv.class);
                                intent.putExtra("username",name);
                                intent.putExtra("SetUri",uri);
                                startActivity(intent);
                                }
                        });

                    } } ;
        recyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();

    }


    public class userdHolder extends RecyclerView.ViewHolder {
        View mView;

        public userdHolder(View itemView) {
            super(itemView);
            mView = itemView;
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







}

