package net.penshorn.openevmap;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;



/**
 * Created by satri on 2/4/2017.
 */

public class LocationListener implements android.location.LocationListener {
    Location mLastLocation;
    private static final String TAG = "LocationListener";
    public LocationListener(String provider) {
        Log.e(TAG, "LocationListener " + provider);
        mLastLocation = new Location(provider);
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.e(TAG, "onLocationChanged: " + location);
        mLastLocation.set(location);
        //In here we will add to the queue for locations, sample the current usage of eletricity etc
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
