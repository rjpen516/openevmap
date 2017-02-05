package net.penshorn.openevmap;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static int MY_PERMISSION_ACCESS_COURSE_LOCATION = 1;
    Button button_start;
    Button button_stop;

    Intent locationService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button_start = (Button) findViewById(R.id.button_start_service);
        button_stop = (Button) findViewById(R.id.button_stop_service);

        button_start.setOnClickListener(this);
        button_stop.setOnClickListener(this);

        locationService = new Intent(MainActivity.this, LocationReporting.class);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.button_start_service:
                enable_service();
                break;
            case R.id.button_stop_service:
                stop_service();
        }

    }


    private void enable_service()
    {
        if ( ContextCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {

            ActivityCompat.requestPermissions( this, new String[] {  Manifest.permission.ACCESS_FINE_LOCATION },
                    MY_PERMISSION_ACCESS_COURSE_LOCATION );
        }
            startService(locationService);
    }

    private void stop_service()
    {
        stopService(locationService);
    }
}
