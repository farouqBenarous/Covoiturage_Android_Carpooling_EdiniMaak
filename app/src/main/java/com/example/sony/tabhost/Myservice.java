package com.example.sony.tabhost;

import android.app.Notification;
import android.app.TaskStackBuilder;
import android.support.annotation.Nullable;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;


public class Myservice  extends IntentService {

        public  static  final  String MESSAGE = "message";
        public  static  final  int NOTIFICATION_ID=1356;

        public Myservice() {
            super("Myservice");
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        private void showText (final String text) {
            Intent intent = new Intent(Myservice.this,MainActivity.class);
            TaskStackBuilder stackBuilder =  TaskStackBuilder.create(this);
            stackBuilder.addParentStack(MainActivity.class);
            stackBuilder.addNextIntent(intent);
            PendingIntent pendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);

            Notification notificationt =  new Notification.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle ( "Hello")
                    .setAutoCancel(true)
                    .setPriority(Notification.PRIORITY_MAX)
                    .setDefaults(Notification.DEFAULT_VIBRATE)
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setContentIntent(pendingIntent)
                    .setContentText(text)
                    .build();
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            assert notificationManager != null;
            notificationManager.notify(NOTIFICATION_ID,notificationt);


        }


        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        protected void onHandleIntent(@Nullable Intent intent) {

            synchronized (this) {
                try { wait(100);  }
                catch (InterruptedException e) { e.printStackTrace();  }
            }

            String text = intent.getStringExtra(MESSAGE);
            showText(text);

        }
    }


