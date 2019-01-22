package com.example.sony.tabhost;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class AccountSetting extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_setting);

        ListView listView1 = findViewById(R.id.listview1);

        AdapterView.OnItemClickListener itemClickListener1 =new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View v, int position, long id) {
                if (position==0) {
                    Intent intent = new Intent(AccountSetting.this,Change_Password.class);
                    startActivity(intent);
                }
                if (position==1) {
                    Intent intent = new Intent(AccountSetting.this,Change_User_Name.class);
                    startActivity(intent);
                }
                if (position==2) {
                    Intent intent = new Intent(AccountSetting.this,Change_Phone_number.class);
                    startActivity(intent);
                }
                if (position==3) {
                    Intent intent = new Intent(AccountSetting.this,Block_User.class);
                    startActivity(intent);
                }
                if (position==4) {
                    Intent intent = new Intent(AccountSetting.this,Delete_This_Account.class);
                    startActivity(intent);
                }

            }
        };

        listView1.setOnItemClickListener(itemClickListener1);
    }
}
