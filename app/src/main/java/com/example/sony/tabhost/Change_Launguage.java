package com.example.sony.tabhost;

import android.app.Application;
import android.content.Context;

import com.example.sony.tabhost.Helper.Helper;

public class Change_Launguage extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(Helper.onAttache(base,"en"));


    }
}
