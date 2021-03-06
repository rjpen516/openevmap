package net.penshorn.openevmap;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.arch.persistence.room.Room;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.view.LayoutInflater;

public class HomeActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private HomeFragment home;
    //private AppDatabase db;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home: {
                    Bundle data = new Bundle();
                    //data.put
                    FragmentTransaction transation = getFragmentManager().beginTransaction();
                    transation.replace(R.id.fragment2, home);
                    transation.addToBackStack(null);
                    transation.commit();
                    return true;
                }
                case R.id.navigation_dashboard: {
                    FragmentTransaction transation = getFragmentManager().beginTransaction();

                    transation.replace(R.id.fragment2, new SettingsFragment());
                    transation.addToBackStack(null);
                    transation.commit();
                    return true;
                }
                case R.id.navigation_notifications: {
                    // mTextMessage.setText(R.string.title_notifications);
                    return true;
                }
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        home = new HomeFragment();
        FragmentTransaction transation = getFragmentManager().beginTransaction();
        transation.replace(R.id.fragment2, home);
        transation.addToBackStack(null);
        transation.commit();

        //AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "evpoints").fallbackToDestructiveMigration().build();

        //mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}


