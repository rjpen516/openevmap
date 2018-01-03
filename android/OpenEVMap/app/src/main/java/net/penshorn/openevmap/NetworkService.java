package net.penshorn.openevmap;

import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import static java.lang.Thread.sleep;

public class NetworkService extends Service {
    private static final String TAG = "NetworkService";
    private AppDatabase db;
    private NotificationManager mNotificationManager;
    private RestClient client;


    LocationListener mLocationListeners =
            null;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);


        while(true)
        {
            if(db.evpointdao().getRowsNotUploaded() > 2)
            {
                //todo we will lose data if the call fails
                EVPoint[] points = db.evpointdao().getToUpload();

                boolean restStatus = client.postPointBulk(points);

                if(restStatus)
                {
                    Log.d(TAG, "Removing Points from database");
                    for(int i = 0; i < points.length; i++)
                    {
                        points[i].upload = true;
                        db.evpointdao().setUploadBit(points[i]);
                    }
                }
            }
            if(db.evpointdao().getRowsNotUploaded() > 0 && db.evpointdao().getRowsNotUploaded() <= 2)
            {
                EVPoint evpoint = db.evpointdao().getNextToUpload();
                boolean restStatus = client.postPoint(evpoint);
                if (restStatus)
                {
                    evpoint.upload = true;
                    db.evpointdao().setUploadBit(evpoint);
                    Log.d(TAG,"Uploaded point");
                }
            }
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }




        //return START_STICKY;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onCreate() {
        client = new RestClient(this.getApplicationContext());
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "evpoints").allowMainThreadQueries().fallbackToDestructiveMigration().build();





    }




    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        super.onDestroy();

    }


}