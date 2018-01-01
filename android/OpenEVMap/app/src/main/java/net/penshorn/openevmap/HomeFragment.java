package net.penshorn.openevmap;

import android.app.Fragment;
import android.arch.persistence.room.Room;
import android.os.AsyncTask;
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
    private TextView numberofcache;
    RestClient client;
    AppDatabase db;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view =  inflater.inflate(R.layout.home_fragment, container, false);
        numberofpoints = (TextView) view.findViewById(R.id.numberofPoints);
        numberofcache = (TextView) view.findViewById(R.id.cachedpoints);
        client = new RestClient(container.getContext(),this);
        client.getTotalEvPoints();
        db = Room.databaseBuilder(getContext(), AppDatabase.class, "evpoints").fallbackToDestructiveMigration().build();
        new AsyncTask<Void, Void, Integer>() {
            @Override
            protected Integer doInBackground(Void... params) {
                return db.evpointdao().getRowsNotUploaded();

            }
            @Override
            protected void onPostExecute(Integer count)
            {
                update_cached_points_ui(count);
            }
        }.execute();
        return view;
    }
    private void update_cached_points_ui(int count)
    {
        numberofcache.setText("" + count);
    }

    @Override
    public void onRestCallback(String data) {


        JsonParser parser = new JsonParser();
        JsonObject o = parser.parse(data).getAsJsonObject();
        numberofpoints.setText("" + o.get("total"));

    }
}
