package net.penshorn.openevmap;

import android.content.Context;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;
import com.koushikdutta.ion.bitmap.Transform;
import android.util.Log;

import java.util.concurrent.ExecutionException;

/**
 * Created by satri on 2/5/2017.
 */

public class RestClient
{
    private static final String HOSTNAME = "http://10.1.1.124:8000/";
    private static final String LOGIN_ENDPOINT = "api/api-auth/login/";
    private static final String TAG = "RESTCLIENT";
    private Context c;
    Ion ion;
    public RestClient(Context context)
    {
        c = context;
        ion.getDefault(c);
    }

    public void login(String username, String password)
    {

        //First lets get so we can get the fucking stupid cookie
        try {
            Response response;
            response = ion.
                    .load(HOSTNAME + LOGIN_ENDPOINT)
                    .asString()
                    .withResponse()
                    .get();
            Log.d(TAG,response.getHeaders().getHeaders().toString());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        JsonObject json = new JsonObject();
        json.addProperty("username", username);
        json.addProperty("password", password);

        Ion.with(c).load(HOSTNAME + LOGIN_ENDPOINT)
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        Log.d(TAG,e.toString());
                    }
                });
    }
}
