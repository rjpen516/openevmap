package net.penshorn.openevmap;

import android.util.Log;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by satri on 12/31/2017.
 */


public class PointUpload
{
    private static final String TAG = "LocationListener";
    private Queue<EVPoint> queue;
    private int uploaded_points;
    public PointUpload()
    {
        queue = new LinkedBlockingQueue<EVPoint>();
        uploaded_points = 0;

    }

    public void addPoint(EVPoint point)
    {
        Log.d(TAG, "We got a point " + point.toString());
        queue.add(point);
    }

    public int currentSize()
    {
        return queue.size();
    }

    public int getUploadedPoints()
    {
        return uploaded_points;
    }


}
