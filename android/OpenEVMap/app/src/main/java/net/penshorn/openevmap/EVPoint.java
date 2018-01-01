package net.penshorn.openevmap;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by satri on 12/31/2017.
 *        json.addProperty("longitude", longitude);
 json.addProperty("latitude", latitue);
 json.addProperty("speed", speed);
 json.addProperty("tempature",-1);
 json.addProperty("energy_usage",-1);
 */

@Entity
public class EVPoint
{
    @PrimaryKey(autoGenerate = true)
    public int uid;
    @ColumnInfo(name = "longitude")
    public double longitude;
    @ColumnInfo(name = "Latitude")
    public double latitude;
    @ColumnInfo(name = "Speed")
    public float speed;
    @ColumnInfo(name = "Temp")
    public float tempature;
    @ColumnInfo(name = "Energy")
    public float energy;
    @ColumnInfo(name = "upload")
    public boolean upload;

    @Ignore
    public EVPoint(double longitude, double latitude, float speed, float tempature, float energy)
    {
        this.longitude = longitude;
        this.latitude = latitude;
        this.speed = speed;
        this.tempature = tempature;
        this.energy = energy;
        this.upload = false;

    }
    public EVPoint(double longitude, double latitude, float speed, float tempature, float energy,boolean upload)
    {
        this.longitude = longitude;
        this.latitude = latitude;
        this.speed = speed;
        this.tempature = tempature;
        this.energy = energy;
        this.upload = upload;

    }
    public String toString()
    {
        return "" + "Long: " + longitude + " Lat: " + latitude + " Speed " + speed;
    }
}
