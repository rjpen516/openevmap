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
    private static final String PROD_HOSTNAME = "https://evmap.penshorn.net/";
    private static final String DEV_HOSTNAME = "http://10.1.1.124/";
    private static final boolean IS_DEV = false;
    private static final String HOSTNAME = IS_DEV ? DEV_HOSTNAME : PROD_HOSTNAME;
    private static final String LOGIN_ENDPOINT = "api/api-token-auth/";
    private static final String EVPOINT_ENDPOINT = "api/evpoints/";
    private static final String REFRESH_ENDPOINT = "api/api-token-refresh/";
    private static final String TAG = "RESTCLIENT";
    private SharedPreferences sharedPref;
    private Context c;
    private String token = "";
    private String host;
    private boolean isDev;
    public RestClient(Context context)
    {
        c = context;
        sharedPref = c.getSharedPreferences("EVMAP",Context.MODE_APPEND);
            token = sharedPref.getString("jwt_token","");
    }

    public void login(String username, String password) {



        String current_token = sharedPref.getString("jwt_token","");

        JsonObject json = new JsonObject();
        json.addProperty("username", username);
        json.addProperty("password", password);

        Ion.getDefault(c).getCookieMiddleware().clear();
        Log.d(TAG,"Making request to " + HOSTNAME + LOGIN_ENDPOINT);
        Ion.with(c).load(HOSTNAME + LOGIN_ENDPOINT)
                .setJsonObjectBody(json)
                .asJsonObject()
                .withResponse().setCallback(new FutureCallback<Response<JsonObject>>() {
            @Override
            public void onCompleted(Exception e, Response<JsonObject> result) {
                if(result == null)
                {
                    Log.d(TAG,"Network Error");
                }
                else if(result.getHeaders().code() == 200) {
                    Log.d(TAG,result.getResult().toString());
                    token = result.getResult().get("token").getAsString();
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("jwt_token",token);
                    editor.commit();
                    Log.d(TAG,"We just saved the token");
                    //sharedPref.
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
        refresh();
        //First we need to make sure we have a key (If we don't then they need to login)
        if(token == "") {
            Log.d(TAG,"I don't have a token");
            return false;
        }
        Log.d(TAG, "Going to post point");
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
                    Log.d(TAG, "Invalid username/password" + result.getResult().toString());

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

    private void refresh()
    {

        JsonObject json = new JsonObject();

        json.addProperty("token", token);

        Ion.with(c).load(HOSTNAME + REFRESH_ENDPOINT).addHeader("Authorization", "JWT " + token)
                .setJsonObjectBody(json)
                .asJsonObject()
                .withResponse().setCallback(new FutureCallback<Response<JsonObject>>() {
            @Override
            public void onCompleted(Exception e, Response<JsonObject> result) {
                if(result.getHeaders().code() == 201) {
                    Log.d(TAG,"We got a new token");
                    token = result.getResult().get("token").getAsString();
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("jwt_token",token);
                    editor.commit();
                }
                else if(result.getHeaders().code() == 400)
                {
                    Log.d(TAG,result.getResult().toString());
                }

            }
        });
    }
}
