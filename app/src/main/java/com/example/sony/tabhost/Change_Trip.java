package com.example.sony.tabhost;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class Change_Trip extends AppCompatActivity {
 private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);
 String [] wilaya = {"ADRAR","AIN DEFLA ","AIN TEMOUCHENT ","AL TARF ","ALGER ","ANNABA ","B.B.ARRERIDJ ","BATNA"
            ,"BECHAR ","BEJAIA ","BISKRA ","BLIDA ","BOUIRA","BOUMERDES ","CHLEF ","CONSTANTINE ","DJELFA"
            ,"EL BAYADH ","EL OUED ","GHARDAIA ","GUELMA ","ILLIZI ","JIJEL ","KHENCHELA ","LAGHOUAT",
            "MASCARA ","MEDEA ","MILA ","MOSTAGANEM","M’SILA","NAAMA ","ORAN ","OUARGLA ","OUM ELBOUAGHI","RELIZANE",
            "SAIDA","SETIF","SIDI BEL ABBES ","SKIKDA","SOUKAHRAS","TAMANGHASSET","TEBESSA","TIARET","TINDOUF",
            "TIPAZA ","TISSEMSILT","TIZI OUZOU","TLEMCEN"};
    String [] emptyCars = {"You Have No Cars"};
    FirebaseDatabase database;
    DatabaseReference reference1,reference2;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    Trip trip;
    Car car;
    Car car1 = new Car();
    String [] Cars ;
    int Day,Month,Year;
    int Hour,Mintute;
    int I,z;
    Calendar calendar;
    String D,A,da,ti,Uuser,nbp,pri,id,idcar;
    String email,Username,Phonenumber,UriPhoto,spin_val,Uid,date,format,Sdep,Sarv,time;
    Spinner cars;
    TextView datetextview,TimeTextView,tt;
    AutoCompleteTextView dep,arv;
    EditText nbplace,price;
    int Iprice,Inbplace;
    DatePickerDialog.OnDateSetListener MdateSetListner;
    TimePickerDialog.OnTimeSetListener timeSetListener;
    String TypeTrip;
    String[] spin = {"Public", "Friends"};//array of strings used to populate the spinner

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change__trip);

        final Spinner TypeSpin = (Spinner) findViewById(R.id.Type);
        // Create an ArrayAdapter using the string array and a default spinner layout
        final ArrayAdapter<CharSequence> Adapter = ArrayAdapter.createFromResource(this,
                R.array.TypeTrip, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        TypeSpin.setAdapter(Adapter);
        //Register a callback to be invoked when an item in this AdapterView has been selected
        TypeSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long id) {
                // TODO Auto-generated method stub
                TypeTrip = spin[position];
                //saving the value selected
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }});





        Intent intent = getIntent();
        D  = intent.getStringExtra("dép");
        A  = intent.getStringExtra("arv");
        da  = intent.getStringExtra("date");
        ti  = intent.getStringExtra("time");

        idcar = intent.getStringExtra("idcar");

        Uuser = intent.getStringExtra("Uid");
        nbp  = intent.getStringExtra("nbplace");
        pri  = intent.getStringExtra("price");
        id  = intent.getStringExtra("id");

        // TIme Picker Code
        TimeTextView = findViewById(R.id.Time);
        calendar  =Calendar.getInstance();
        Hour = calendar.get(Calendar.HOUR_OF_DAY);
        Mintute = calendar.get(Calendar.MINUTE);
        TimeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Selecttimeformat(Hour);
                TimePickerDialog dialog =new TimePickerDialog(Change_Trip.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int h, int m) {
                        Hour = h;
                        Mintute = m;
                        time = String.valueOf(h+":"+m);
                        TimeTextView.setText(time
                        );
                    }
                } ,Hour,Mintute,true);
                dialog.show();
            }
        });


        // Date Picker Code
        datetextview = findViewById(R.id.date);
        datetextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();

                Year  = cal.get(Calendar.YEAR);
                Month  = cal.get(Calendar.MONTH);
                Day  = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(Change_Trip.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        MdateSetListner,Year,Month,Day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }});
        MdateSetListner = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                Day = day;
                Year = year;
                Month = month+1;
                date =Day+"/"+Month+"/"+Year;
                String string= Month+"/"+Day+"/"+Year;
                datetextview.setText(date);
            }};

        // On Create and Refrences
        nbplace = findViewById(R.id.nbplace);
        price = findViewById(R.id.price);
        cars =  findViewById(R.id.cars);
        final ArrayAdapter<String> adapter =new ArrayAdapter<String>(this,android.R.layout.select_dialog_item,wilaya);
        dep = findViewById(R.id.départ);
        arv = findViewById(R.id.arv);
        arv.setThreshold(1);
        arv.setAdapter(adapter);
        dep.setThreshold(1);
        dep.setAdapter(adapter);
        database =FirebaseDatabase.getInstance();
        reference1 = database.getReference("User");
        reference2 = database.getReference("Trips");
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        assert firebaseUser != null;
        email = firebaseUser.getEmail();
        Username = firebaseUser.getDisplayName();

        reference1.orderByChild("email").equalTo(email).addChildEventListener(new ChildEventListener() {

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Uid = dataSnapshot.getKey();
                Phonenumber = dataSnapshot.child("phone_number").getValue(String.class);
                UriPhoto = dataSnapshot.child("photo_profile").getValue(String.class);
                if (Objects.equals(dataSnapshot.child("cars").getValue(), "cars") ||
                        Objects.equals(dataSnapshot.child("cars").getValue(),null ) ) {
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>
                            (getApplicationContext(),
                                    android.R.layout.simple_spinner_dropdown_item, emptyCars);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    cars.setAdapter(adapter);

                }
                else {

                    reference1.child(Uid).child("cars").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            int o = (int) dataSnapshot.getChildrenCount();
                            Cars = new String[o];
                            z=0;

                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                // add a ListenerForSingleValueEvent
                                // How To Stop this Fuck'in Iteration ( Random Add)
                                car1.setBrand( ds.child("brand").getValue(String.class));
                                Cars [z] =car1.getBrand();
                                z ++;
                            }
                            final ArrayAdapter<String> adapter1 = new ArrayAdapter<>(getApplicationContext(),
                                    android.R.layout.simple_spinner_item, Cars);
                            adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            cars.setAdapter(adapter1);
                            cars.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    I = i;}
                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {
                                }});
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {}});
                }}
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
    protected void onStart() {
        super.onStart();
        dep.setText(D);
        arv.setText(A);
        datetextview.setText(da);
        date = da;
        TimeTextView.setText(ti);
        time = ti;
        // Car
        nbplace.setText(nbp );
        price.setText(pri);
    }






    @RequiresApi(api = Build.VERSION_CODES.N)
    public  void  saveTrip(View view) {
// Sart The saving
        Sdep = String.valueOf(dep.getText());
        Sarv = String.valueOf(arv.getText());

        if (TextUtils.isEmpty(Sdep)) {
            dep.setError("Enter The Departure City !");
            Toast.makeText(getApplicationContext(), "Enter Departure City ", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Arrays.asList(wilaya).contains(Sdep)) {
            dep.setError("Departure City Don't Exist !");
            Toast.makeText(getApplicationContext(), "Departure City Don't Exist", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(Sarv)) {
            arv.setError("Enter The Departure City !");
            Toast.makeText(getApplicationContext(), "Enter Departure City ", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!Arrays.asList(wilaya).contains(Sarv)) {
            arv.setError("Arrived City Don't Exist !");
            Toast.makeText(getApplicationContext(), "Arrived  City Don't Exist", Toast.LENGTH_SHORT).show();
            return;
        }

        if (Objects.equals(Sarv,Sdep)) {
            arv.setError("Departure city should not equal to the Arrived !");
            dep.setError("Departure city should not equal to the Arrived !");
            return;
        }

        if (TextUtils.isEmpty(datetextview.getText()) || datetextview.getText() == null) {
            datetextview.setError("Enter The Departure Date !");
            Toast.makeText(getApplicationContext(), "Enter Departure Date ", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            if (!checkDate(date)) {
                datetextview.setError("Set A Date bigger than current Date !");
                Toast.makeText(getApplicationContext(), "Set A Date bigger than current Date ", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


        // Check of Time
        if (TextUtils.isEmpty(TimeTextView.getText()) || TimeTextView.getText() == null) {
            TimeTextView.setError("Enter The Departure Time !");
            Toast.makeText(getApplicationContext(), "Enter Departure Time ", Toast.LENGTH_SHORT).show();
            return;
        }


        try {
            if (!checkTime(time)) {
                TimeTextView.setError("Set A Time bigger than current Time !");
                Toast.makeText(getApplicationContext(), "Set A Time bigger than current Time ", Toast.LENGTH_LONG).show();
                return;
            }
        } catch (ParseException e) {
            e.printStackTrace();}

 if (Cars == null) {
            Toast.makeText(getApplicationContext(), "You Have To select A Car ", Toast.LENGTH_LONG).show();
            return;
        }


        if (Objects.equals(nbplace.getText(),0) ||TextUtils.isEmpty(nbplace.getText()) || nbplace.getText() == null ) {
            nbplace.setError("Set The number Of place Availble");
            Toast.makeText(getApplicationContext(), "Set The number Of place Availble", Toast.LENGTH_LONG).show();
            return;
        }

        if (Integer.parseInt(String.valueOf(nbplace.getText())) > 30) {
            nbplace.setError("number Of place Too big");
            Toast.makeText(getApplicationContext(), "number Of place Too big", Toast.LENGTH_LONG).show();
            return;
        }


        if (Objects.equals(price.getText(),0) ||TextUtils.isEmpty(price.getText()) || price.getText() == null ) {
            price.setError("Set The Price Please");
            Toast.makeText(getApplicationContext(), "Set The Price Please", Toast.LENGTH_LONG).show();
            return;
        }


        if (Integer.parseInt(String.valueOf(price.getText())) < 10) {
            price.setError("Price verry Low");
            Toast.makeText(getApplicationContext(), "Price verry Low", Toast.LENGTH_LONG).show();
            return;
        }
        final int iprice = Integer.parseInt(String.valueOf(price.getText()));
        final int inbplace = Integer.parseInt(String.valueOf(nbplace.getText()));


        reference1.child(Uid).child("cars").orderByChild("brand").equalTo(Cars[I]).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String photo = dataSnapshot.child("Photo").getValue(String.class);
                String model = dataSnapshot.child("model").getValue(String.class);
                String matricule = dataSnapshot.child("matricule").getValue(String.class);
                String numcartegrise = dataSnapshot.child("numcartegrise").getValue(String.class);
                String brand = dataSnapshot.child("brand").getValue(String.class);
                car =  new Car(idcar,photo,brand,model,matricule,numcartegrise);

               reference2.orderByChild("id").equalTo(id).addChildEventListener(new ChildEventListener() {
                   @Override
                   public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                       String ID = dataSnapshot.getKey();
                       reference2.child(ID).child("dép").setValue(Sdep);
                       reference2.child(ID).child("arv").setValue(Sarv);
                       reference2.child(ID).child("Year").setValue(Year);
                       reference2.child(ID).child("Month").setValue(Month);
                       reference2.child(ID).child("Day").setValue(Day);
                       reference2.child(ID).child("Hour").setValue(Hour);
                       reference2.child(ID).child("Mintute").setValue(Mintute);
                       reference2.child(ID).child("price").setValue(iprice);
                       reference2.child(ID).child("nbPlace").setValue(inbplace);
                       reference2.child(ID).child("price").setValue(iprice);
                       reference2.child(ID).child("Uid").setValue(Uid);
                       reference2.child(ID).child("Username").setValue(Username);
                       reference2.child(ID).child("Phonenumber").setValue(Phonenumber);
                       reference2.child(ID).child("UriPhoto").setValue(UriPhoto);
                       reference2.child(ID).child("car").setValue(car);
                       reference2.child(ID).child("Type").setValue(TypeTrip);



                   }

                   @Override
                   public void onChildChanged(DataSnapshot dataSnapshot, String s) { }
                   @Override
                   public void onChildRemoved(DataSnapshot dataSnapshot) { }
                   @Override
                   public void onChildMoved(DataSnapshot dataSnapshot, String s) { }
                   @Override
                   public void onCancelled(DatabaseError databaseError) { }});

                reference1.child(Uid).child("MyTrips").orderByChild("id").equalTo(id).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        String IDD = dataSnapshot.getKey();

                        reference1.child(Uid).child("MyTrips").child(IDD).child("dép").setValue(Sdep);
                        reference1.child(Uid).child("MyTrips").child(IDD).child("arv").setValue(Sarv);
                        reference1.child(Uid).child("MyTrips").child(IDD).child("Year").setValue(Year);
                        reference1.child(Uid).child("MyTrips").child(IDD).child("Month").setValue(Month);
                        reference1.child(Uid).child("MyTrips").child(IDD).child("Day").setValue(Day);
                        reference1.child(Uid).child("MyTrips").child(IDD).child("Hour").setValue(Hour);
                        reference1.child(Uid).child("MyTrips").child(IDD).child("Mintute").setValue(Mintute);
                        reference1.child(Uid).child("MyTrips").child(IDD).child("price").setValue(iprice);
                        reference1.child(Uid).child("MyTrips").child(IDD).child("nbPlace").setValue(inbplace);
                        reference1.child(Uid).child("MyTrips").child(IDD).child("price").setValue(iprice);
                        reference1.child(Uid).child("MyTrips").child(IDD).child("Uid").setValue(Uid);
                        reference1.child(Uid).child("MyTrips").child(IDD).child("Username").setValue(Username);
                        reference1.child(Uid).child("MyTrips").child(IDD).child("Phonenumber").setValue(Phonenumber);
                        reference1.child(Uid).child("MyTrips").child(IDD).child("UriPhoto").setValue(UriPhoto);
                        reference1.child(Uid).child("MyTrips").child(IDD).child("car").setValue(car);
                        reference1.child(Uid).child("MyTrips").child(IDD).child("Type").setValue(TypeTrip);



                    }
                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) { }
                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) { }
                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) { }
                    @Override
                    public void onCancelled(DatabaseError databaseError) { }});


                //Use The update method and not Tec create

                Toast.makeText(getApplicationContext(),"Trip Succesfuly Shared  ", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplication(),MainActivity.class));

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

    public void  addCar(View view) {

        startActivity(new Intent(getApplication(),Mycars.class));
        finish();
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


    public  void Selecttimeformat(int Hour) {
        if ( Hour == 0) {
            Hour +=12;
            format = "AM";
        }
        else  if (Hour == 12 ) {
            format = "PM";
        }
        else if (Hour >12) {
            format = "PM";

        }
        else {
            format = "AM";

        }


    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public boolean checkDate (String date) throws ParseException {
        //Give The Entred Date  Date
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
        Date strDate = sdf.parse(date);
        String scurrent = sdf.format(new Date());
        Date current = sdf.parse(scurrent);



        if (current.after(strDate)) {
            return false;
        }
        else{
            return true;
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public boolean checkTime (String time) throws ParseException {
        //Give The Entred Date  Date
        Calendar c = Calendar.getInstance();
        java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("HH:mm");

        String SCurenntetime = df.format(c.getTime());
        Date Currenttime = df.parse(SCurenntetime);
        Date GivenTime = df.parse(time);

        if (Currenttime.after(GivenTime)) {return false;  }
        else{ return true;  }
    }


}
