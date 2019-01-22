package com.example.sony.tabhost;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class Message_fragment extends Fragment {
RecyclerView recyclerView ;
FirebaseRecyclerAdapter<Chat, Message_fragment.ChatHolder> firebaseRecyclerAdapter;
modeluser user ,cuser;
Chat chat;
FirebaseDatabase database;
DatabaseReference reference,reference1,reference2,reference3,reference4;
FirebaseAuth auth;
FirebaseUser firebaseUser;
ImageView profileimageView;
TextView fullname;
String CUid,UID;
String SetUri,cusername,cemail,curi,username,email,lastmsg;


    public Message_fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_message_fragment, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.chatRecyclerView);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
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
        Query query = FirebaseDatabase.getInstance().getReference().child("User").child(CUid).child("Chat").child("infoconvs").getRef();
        FirebaseRecyclerOptions<Chat> options = new FirebaseRecyclerOptions.Builder<Chat>()
                .setQuery(query,Chat.class)
                .build();
        firebaseRecyclerAdapter = new
                FirebaseRecyclerAdapter<Chat,Message_fragment.ChatHolder>(options) {
                    @NonNull
                    @Override
                    public Message_fragment.ChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        // Create a new instance of the ViewHolder, in this case we are using a custom
                        // layout called R.layout.message for each item
                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.chatrow, parent, false);
                     return new Message_fragment.ChatHolder(view);
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull Message_fragment.ChatHolder viewHolder, final int position, @NonNull final Chat model) {

                        viewHolder.setImage(getContext(), model.getProfilepic());
                        SetUri = model.getProfilepic();



                        viewHolder.setUsername(model.getUSername());
                        username =  model.getUSername();

                        viewHolder.setlastmsg(model.getLastmsg());
                        lastmsg = model.getLastmsg();



                        chat = new Chat(SetUri,username,lastmsg);
                        user = new modeluser(email,username,SetUri);



                        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(getContext(),Conv.class);
                                intent.putExtra("username",username);
                                intent.putExtra("SetUri",SetUri);
                                intent.putExtra("lastmsg",lastmsg);
                                startActivity(intent);

                            };
                        });

                    } } ;
        recyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();


    }




    public class ChatHolder extends RecyclerView.ViewHolder {
        View mView;
        public ChatHolder(View itemView) {
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
            TextView fullname = mView.findViewById(R.id.username);
            fullname.setText(username);
        }

        public void setlastmsg(String lastmsg) {
            TextView lastmsgTextview = mView.findViewById(R.id.lastmessage);
            lastmsgTextview.setText(lastmsg);
        }
        }


}
