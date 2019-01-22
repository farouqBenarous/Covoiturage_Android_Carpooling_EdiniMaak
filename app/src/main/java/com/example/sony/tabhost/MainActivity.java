package com.example.sony.tabhost;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";
    private boolean mVerificationInProgress = false;
    private StorageReference storageReference;
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference reference,reff;
    FirebaseUser firebaseUser;
    String Cusername,Uid;
    RecyclerView Searchrecycler;
    modeluser modeluser = new modeluser();
    int z,o;
    MaterialSearchView materialsearchview;
    String[] list;
    FirebaseAuth.AuthStateListener authStateListener;
    private static final  int GALLERY_INTENT = 1;
    private TextView mTextMessage;
    String profileImageUrl;
    private ImageView profileimageView ;
    ProgressBar progressbar1;
    FragmentManager fragmentManager = getSupportFragmentManager();
    Notification_fragment notification_fragment = new Notification_fragment();
    Profile_fragment profile_fragment = new Profile_fragment();
    Message_fragment message_fragment = new Message_fragment();
    Trajets_Fragment trajets_fragment = new Trajets_Fragment();
    Requests request_fragment = new Requests();
    //array of strings used to populate the spinner
    final String[] spin = {"","Change Profile Picture","Delete Profile picture"};


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_home:
                fragmentManager.beginTransaction().replace(R.id.frameLayout, trajets_fragment, trajets_fragment.getTag()).commit();
                return true;
                case R.id.message:
                fragmentManager.beginTransaction().replace(R.id.frameLayout, message_fragment, message_fragment.getTag()).commit();
                    return true;
                case R.id.navigation_notifications: fragmentManager.beginTransaction().replace(R.id.frameLayout, notification_fragment,
                            notification_fragment.getTag()).commit();
                    return true;
                case R.id.profile: fragmentManager.beginTransaction().replace(R.id.frameLayout, profile_fragment, profile_fragment.getTag()).commit();
                    return true;
                case R.id.invitations: fragmentManager.beginTransaction().replace(R.id.frameLayout, request_fragment,request_fragment.getTag()).commit();
                     return true;

            }
            return false; }
    };




    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        database = FirebaseDatabase.getInstance();
        reff = database.getReference("User");
        // Populate the array list
        materialsearchview = (MaterialSearchView) findViewById(R.id.mysearch);
        materialsearchview.setHint("Search Friends");

        reff.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // get the users names
                o = (int) dataSnapshot.getChildrenCount();
                list = new String[o];
                z=0;
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    modeluser.setfullName(ds.child("fullName").getValue(String.class));
                    list [z] =ds.child("fullName").getValue(String.class);
                    z ++; }
                materialsearchview.setSuggestions(list);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}});



        Toolbar toolbar =(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //profile fragment attributes
        assert firebaseUser != null;
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        auth = FirebaseAuth.getInstance();
        profileimageView  =(ImageView) findViewById(R.id.profileimage);
       storageReference= FirebaseStorage.getInstance().getReference();
       progressbar1= (ProgressBar) findViewById(R.id.progressBar1);
        auth = FirebaseAuth.getInstance();
        FirebaseUser user3 = auth.getCurrentUser();
        Cusername = user3.getDisplayName();
        fragmentManager.beginTransaction().replace(R.id.frameLayout, profile_fragment,profile_fragment.getTag()).commit();


        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (auth.getCurrentUser() == null) {
                    startActivity(new Intent(getApplication(),LoginActivity.class));}}};
    }

    @Override
    protected void onResume() {
        super.onResume();
        reff.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // get the users names
                o = (int) dataSnapshot.getChildrenCount();
                list = new String[o];
                z=0;
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    modeluser.setfullName(ds.child("fullName").getValue(String.class));
                    list [z] =ds.child("fullName").getValue(String.class);
                    z ++; }
                materialsearchview.setSuggestions(list);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}});

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu, menu);

        MenuItem item = menu.findItem(R.id.search);
        materialsearchview.setMenuItem(item);
        materialsearchview.clearFocus();
        materialsearchview.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public boolean onQueryTextSubmit(String query) {
              materialsearchview.setVisibility(View.VISIBLE);
             // Add a test if the username exist in the list or not ;
               if (checkexist(list,o,query)) {
                   if (Objects.equals(query,Cusername)) {
                       Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                       startActivity(intent);

                       }
                   else {
                       Intent intent = new Intent(getApplicationContext(),showprofile.class);
                       intent.putExtra("query",query);
                       startActivity(intent);

                   }
                   }
               else {
                   Toast.makeText(getApplicationContext(),"Username doesn't Exist",Toast.LENGTH_SHORT).show();
                   }


                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                materialsearchview.setVisibility(View.VISIBLE);
                return false;
            }
        });

        return true;
    }

    // profile fragment
  public  void logout (View view) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signOut();
        Toast.makeText(this, "Logout succesfull", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this,LoginActivity.class));
        finish();


    }
  public void upload_photo(View view) {

        final Spinner spinner = (Spinner) findViewById(R.id.addpic);
        spinner.performClick();

        // Create an ArrayAdapter using the string array and a default spinner layout
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.Add_pic, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        //Register a callback to be invoked when an item in this AdapterView has been selected


        spinner.setOnItemSelectedListener (new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinner.setSelection( 0 );
                String valuespin= "";
                  valuespin = spin[position];
                switch ( position ) {
                    case 1:
                        progressbar1= (ProgressBar) findViewById(R.id.progressBar1);
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType("image/*");
                        progressbar1.setVisibility(View.VISIBLE);
                        startActivityForResult(intent,GALLERY_INTENT);
                        break;
                    case 2:
                        progressbar1= (ProgressBar) findViewById(R.id.progressBar1);
                        progressbar1.setVisibility(View.VISIBLE);
                        deletephotoprofile();
                        break;

                    default:
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getApplicationContext(),"nothing",Toast.LENGTH_SHORT).show();
               }});
    }
  @Override
  protected void onActivityResult(int requestCode, int resultCode, final Intent data) {

        progressbar1= findViewById(R.id.progressBar1);
        profileimageView  = findViewById(R.id.profileimage);
        storageReference= FirebaseStorage.getInstance().getReference();
        if (requestCode == GALLERY_INTENT & resultCode == RESULT_OK && data != null && data.getData()!=null) {
            final Uri uriprofileimage = data.getData();

            StorageReference filepath = storageReference.child("photos").child(uriprofileimage.getLastPathSegment());
            filepath.putFile(uriprofileimage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //download the pic before set it in the imageview
                    auth = FirebaseAuth.getInstance();
                    final FirebaseUser user=auth.getCurrentUser();
                    profileImageUrl=taskSnapshot.getDownloadUrl().toString();
                    UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                            .setPhotoUri(Uri.parse(profileImageUrl)).build();
                    assert user != null;
                    user.updateProfile(profileUpdate)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        //Set the uri in the realtime
                                        final Uri uri =   user.getPhotoUrl();
                                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                                        final DatabaseReference ref = database.getReference();
                                        final DatabaseReference reference = database.getReference();
                                        FirebaseUser user=auth.getCurrentUser();
                                        assert user != null;
                                        String username = user.getEmail();
                                        ref.child("User").orderByChild("email").equalTo(username).addChildEventListener(new ChildEventListener() {
                                            @Override
                                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                                String Uid= dataSnapshot.getKey();
                                                reference.child("User").child(Uid).child("photo_profile").setValue(uri.toString());}
                                            @Override
                                            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                                            @Override
                                            public void onChildRemoved(DataSnapshot dataSnapshot) {}
                                            @Override
                                            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                                Toast.makeText(getApplicationContext(),"probleme",Toast.LENGTH_SHORT).show();
                                            }});
                                        Picasso.with(MainActivity.this).load(uri).into(profileimageView);
                                        progressbar1.setVisibility(View.GONE);
                                        Toast.makeText(MainActivity.this,"image successfully uploaded ",Toast.LENGTH_LONG).show();

                                    }
                                }
                            });
                }
            });


        }

    }
  private void deletephotoprofile() {
        //Delete photo from Storge and real time and Firebaseuser and the imagview

      progressbar1= (ProgressBar) findViewById(R.id.progressBar1);
progressbar1.setVisibility(View.VISIBLE);
      profileimageView  =(ImageView) findViewById(R.id.profileimage);
      storageReference= FirebaseStorage.getInstance().getReference();
      final FirebaseDatabase database = FirebaseDatabase.getInstance();
      DatabaseReference ref1 = database.getReference();
      final DatabaseReference ref2 = database.getReference();
      auth = FirebaseAuth.getInstance();
      FirebaseUser user=auth.getCurrentUser();
      assert user != null;
      String username = user.getEmail();

ref1.child("User").orderByChild("email").equalTo(username).addChildEventListener(new ChildEventListener() {
    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        ref2.child("User").child(dataSnapshot.getKey().toString()).child("photo_profile").setValue("vide");
        profileimageView.setImageResource(R.drawable.profile_vide);
        progressbar1.setVisibility(View.GONE);
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
    public void gotoappseting(View view) {
        startActivity(new Intent(getApplication(),AppSetting.class));
    }
    public void gotoaccountsetting(View view) {
        startActivity(new Intent(getApplication(),AccountSetting.class));
    }
    public void gotofriends(View view) {
        startActivity(new Intent(getApplication(),Friends.class));
    }
    public void gotomycars(View view) {
        startActivity(new Intent(getApplication(),myCar.class));
    }

// Trajets Fragments

    public  void AddTrip (View view) {

        startActivity(new Intent(getApplication(),AddTrip.class));

    }

    public  void Search_A_trip (View view) {

        startActivity(new Intent(getApplication(),SearchATrip.class));

    }


    // Message Fragment
    public void newmsg (View view) {
        startActivity(new Intent(getApplicationContext(),SearchtoMessage.class));

    }








@TargetApi(Build.VERSION_CODES.KITKAT)
@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public  boolean checkexist (String[] list, int a, String username) {
    int i ;
        for (i = 0 ; i<a;i++) {
            if (Objects.equals(list[i],username) ) {
                return true;
              }
            }
            return false;

}





}



