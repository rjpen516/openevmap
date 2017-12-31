package net.penshorn.openevmap;

/**
 * Created by satri on 12/31/2017.
 *        json.addProperty("longitude", longitude);
 json.addProperty("latitude", latitue);
 json.addProperty("speed", speed);
 json.addProperty("tempature",-1);
 json.addProperty("energy_usage",-1);
 */

public class EVPoint
{
    public double longitude;
    public double latitude;
    public float speed;
    public float tempature;
    public float energy;
    public EVPoint(double longitude, double latitue, float speed, float tempature, float energy)
    {
        this.longitude = longitude;
        this.latitude = latitue;
        this.speed = speed;
        this.tempature = tempature;
        this.energy = energy;

    }
    public String toString()
    {
        return "" + "Long: " + longitude + " Lat: " + latitude + " Speed " + speed;
    }
}
