package net.penshorn.openevmap;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Created by satri on 12/31/2017.
 */



public class HomeFragment extends Fragment implements restCallback{
    private TextView numberofpoints;
    RestClient client;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view =  inflater.inflate(R.layout.home_fragment, container, false);
        numberofpoints = (TextView) view.findViewById(R.id.numberofPoints);
        client = new RestClient(container.getContext(),this);
        client.getEvPoints();
        return view;
    }

    @Override
    public void onRestCallback(String data) {


        JsonParser parser = new JsonParser();
        JsonArray o = parser.parse(data).getAsJsonArray();
        numberofpoints.setText("" + o.size());

    }
}
