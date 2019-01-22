package com.example.sony.tabhost;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
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

public class myCar extends AppCompatActivity {
    FirebaseDatabase database ;
    DatabaseReference ref,reference;
    FirebaseAuth auth;
    FirebaseStorage storage;
    FirebaseUser user;
    RecyclerView recyclerView;
    FirebaseRecyclerAdapter<Car, CarViewHolder> firebaseRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mycars);
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("User");
        ref.keepSynced(true);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        recyclerView = (RecyclerView) findViewById(R.id.CarsRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }

    @Override
    protected void onStart() {
        super.onStart();
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("User");
        ref.keepSynced(true);
        reference = database.getReference("User");
        reference.keepSynced(true);
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        assert user != null;
        String username = user.getEmail();
        ref.orderByChild("email").equalTo(username).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                final String Uid = dataSnapshot.getKey();
                reference.child(Uid).child("cars").addChildEventListener(new ChildEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                        if (Objects.equals(dataSnapshot.getValue().toString(), "cars") || dataSnapshot.getValue() == null) {
                         /*   recyclerView.setVisibility(View.GONE);
                            TextView msg = (TextView) findViewById(R.id.msg);
                            msg.setVisibility(View.VISIBLE);
                            msg.setText("You Have No Cars");
   */
                        } else {
                            reference = reference.child(Uid).child("cars").getRef();
                            Query query = FirebaseDatabase.getInstance().getReference().child("User").child(Uid).child("cars").getRef();
                            FirebaseRecyclerOptions<Car> options =
                                    new FirebaseRecyclerOptions.Builder<Car>()
                                            .setQuery(query,Car.class)
                                            .build();


                            firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Car, CarViewHolder>
                                    (options) {
                                @NonNull
                                @Override
                                public CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                                    // Create a new instance of the ViewHolder, in this case we are using a custom
                                    // layout called R.layout.message for each item
                                    View view = LayoutInflater.from(parent.getContext())
                                            .inflate(R.layout.carrow, parent, false);

                                    return new CarViewHolder(view);
                                }

                                @Override
                                protected void onBindViewHolder(@NonNull CarViewHolder viewHolder, final int position, @NonNull Car model) {
                                    viewHolder.setBrand("Brand : " + model.getBrand());
                                    viewHolder.setModel("Car Type : " + model.getModel());
                                    viewHolder.setmatricule("Matircule : " + model.getMatricule());
                                    viewHolder.setnumcartegrise("Numéro CarteGrise : " + model.getNumcartegrise());
                                    if (model.getPhoto() == null) {
                                        //im here set the empty image car to the image view
                                        String Uri = "https://firebasestorage.googleapis.com/v0/b/convoiturage-81bdb.appspot.com/o/photoscars%2Fcar-icon.jpg?alt=media&token=00cf0730-3e88-4f7f-a1ff-5c92fda9dcae";
                                        viewHolder.setCarImage(getApplicationContext(), Uri);
                                    } else {
                                        viewHolder.setCarImage(getApplicationContext(), model.getPhoto());
                                    }

                                    viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            final AlertDialog.Builder adb = new AlertDialog.Builder(myCar.this);
                                            adb.setTitle("Confirmation");
                                            adb.setMessage("Do you want to Delete This Car  ? ");
                                            adb.setCancelable(true);

                                            adb.setPositiveButton(" yes", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    firebaseRecyclerAdapter.getRef(position).removeValue();
                                                    firebaseRecyclerAdapter.notifyItemRemoved(position);
                                                    recyclerView.invalidate();
                                                    onStart();

                                                }});
                                            adb.setNegativeButton(" No", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    dialogInterface.cancel();

                                                }
                                            });

                                            adb.show();




                                        }
                                    });



                                }
                            };
                            recyclerView.setAdapter(firebaseRecyclerAdapter);
                            firebaseRecyclerAdapter.startListening();


                        }
                    }
                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {}
                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                    @Override
                    public void onCancelled(DatabaseError databaseError) {}});}
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onCancelled(DatabaseError databaseError) {}});

    }

    public void addcar(View view) {
        startActivity(new Intent(getApplication(),Setinformationcar.class));

    }

    public  void   deleteCar (View view) {
        Toast.makeText(getApplicationContext(),"Delett car",Toast.LENGTH_SHORT).show();

    }




    public  static  class CarViewHolder extends  RecyclerView.ViewHolder {
        View mView;
        public CarViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public  void setBrand (String  brand) {
            TextView brandTextView = mView.findViewById(R.id.BrandShow);
            brandTextView.setText(brand);
        }
        public  void setModel (String  model) {
            TextView  modelTextView = mView.findViewById(R.id.CarShowType);
            modelTextView.setText(model);
        }
        public  void setmatricule (String  matricule) {
            TextView  matriculeTextView = mView.findViewById(R.id.matriculeShow);
            matriculeTextView.setText(matricule);
        }
        public  void setnumcartegrise (String  numcartegrise) {
            TextView  numcartegriseTextView = mView.findViewById(R.id.Shownumcartegrise);
            numcartegriseTextView.setText(numcartegrise);
        }
        public  void  setCarImage (Context context , String image) {
            ImageView carImage = mView.findViewById(R.id.CarShowImage);
            Picasso.with(context).load(image).into(carImage);
        }

    }

}
