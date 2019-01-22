package com.example.sony.tabhost;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ContactUs extends AppCompatActivity {

    String emailceo;
    String phoneceo;
    Button email, phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        email = findViewById(R.id.emailceo);
        phone = findViewById(R.id.callceo);
        phoneceo = phone.getText().toString();
        emailceo = email.getText().toString();

    }

    public void CallCeo(View view) {

        Intent i = new Intent(Intent.ACTION_CALL);
        i.setData(Uri.parse("tel:"+phoneceo));
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(),"Please Grant The Permission To call",Toast.LENGTH_SHORT).show();
            RequestPermissions();
        }
        else {
        startActivity(i);}

    }
    public void RequestPermissions() {
        ActivityCompat.requestPermissions(this,new String[] {android.Manifest.permission.CALL_PHONE},1);



    }



    public void MsgCeo (View view) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setData(Uri.parse(emailceo));
        i.putExtra(Intent.EXTRA_EMAIL,emailceo);
        i.putExtra(Intent.EXTRA_SUBJECT,"Carpooling Android Application");
        i.putExtra(Intent.EXTRA_TEXT,"Hello Mr.Benarous \n");
        i.setType("message/rfc822");
        Intent chooser = Intent.createChooser(i,"Launch Email App");
        startActivity(chooser);



}

}
