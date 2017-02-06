package net.penshorn.openevmap;

import android.content.Context;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;
import com.koushikdutta.ion.bitmap.Transform;

import android.content.SharedPreferences;
import android.util.Log;

import java.util.concurrent.ExecutionException;


/**
 * Created by satri on 2/5/2017.
 */

public class RestClient
{
    private static final String HOSTNAME = "http://10.1.1.124:8000/";
    private static final String LOGIN_ENDPOINT = "api/api-token-auth/";
    private static final String EVPOINT_ENDPOINT = "api/evpoints/";
    private static final String TAG = "RESTCLIENT";
    private SharedPreferences sharedPref;
    private Context c;
    private String token = "";
    public RestClient(Context context)
    {
        c = context;
        sharedPref = c.getSharedPreferences("EVMAP",Context.MODE_PRIVATE);
        if(!sharedPref.getString("jwt_token","").equals(""))
        {
            token = sharedPref.getString("jwt_token","");
        }
    }

    public void login(String username, String password) {



        String current_token = sharedPref.getString("jwt_token","");

        JsonObject json = new JsonObject();
        json.addProperty("username", username);
        json.addProperty("password", password);

        Ion.getDefault(c).getCookieMiddleware().clear();

        Ion.with(c).load(HOSTNAME + LOGIN_ENDPOINT)
                .setJsonObjectBody(json)
                .asJsonObject()
                .withResponse().setCallback(new FutureCallback<Response<JsonObject>>() {
            @Override
            public void onCompleted(Exception e, Response<JsonObject> result) {
                if(result.getHeaders().code() == 200) {
                    //Log.d(TAG,result.getResult().toString());
                    token = result.getResult().get("token").getAsString();
                    sharedPref.edit().putString("jwt_token", token);
                }
                else if(result.getHeaders().code() == 400) {
                    Log.d(TAG, "Invalid username/password");

                }
                else if(result.getHeaders().code() == 403)
                {
                    Log.d(TAG,result.getResult().toString());

                }
                else
                    Log.d(TAG,"Login Error");

            }
        });
    }

    public boolean postPoint(double longitude, double latitue, double speed)
    {
        //First we need to make sure we have a key (If we don't then they need to login)
        if(token == "")
            return false;

        JsonObject json = new JsonObject();
        json.addProperty("longitude", longitude);
        json.addProperty("latitude", latitue);
        json.addProperty("speed", speed);
        json.addProperty("tempature",-1);
        json.addProperty("energy_usage",-1);


        Ion.with(c).load(HOSTNAME + EVPOINT_ENDPOINT).addHeader("Authorization", "JWT " + token)
                .setJsonObjectBody(json)
                .asJsonObject()
                .withResponse().setCallback(new FutureCallback<Response<JsonObject>>() {
            @Override
            public void onCompleted(Exception e, Response<JsonObject> result) {
                if(result.getHeaders().code() == 201)
                    //Log.d(TAG,result.getResult().toString());
                    Log.d(TAG, "Location posted");
                else if(result.getHeaders().code() == 400) {
                    Log.d(TAG, "Invalid username/password");

                }
                else if(result.getHeaders().code() == 403)
                {
                    Log.d(TAG,result.getResult().toString());

                }
                else
                    Log.d(TAG,"Post Error");

            }
        });
        return true;
    }
}
