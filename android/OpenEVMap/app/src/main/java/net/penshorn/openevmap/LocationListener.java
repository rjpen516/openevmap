package net.penshorn.openevmap;

import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;



/**
 * Created by satri on 2/4/2017.
 */

public class LocationListener implements android.location.LocationListener {
    Location mLastLocation;
    private static final String TAG = "LocationListener";
    private AppDatabase db;
    public LocationListener(String provider, AppDatabase db) {
        this.db = db;
        Log.e(TAG, "LocationListener " + provider);
        mLastLocation = new Location(provider);
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.e(TAG, "onLocationChanged: " + location);
        mLastLocation.set(location);
        final Location location2 = location;

        //In here we will add to the queue for locations, sample the current usage of eletricity etc
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                db.evpointdao().insert(new EVPoint(location2.getLongitude(),location2.getLatitude(), location2.getSpeed(), -1,-1));

                return null;
            }
        }.execute();
        //db.evpointdao().insert(new EVPoint(location.getLongitude(),location.getLatitude(), location.getSpeed(), -1,-1));
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.e(TAG, "onProviderDisabled: " + provider);
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.e(TAG, "onProviderEnabled: " + provider);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.e(TAG, "onStatusChanged: " + provider);
    }
}
