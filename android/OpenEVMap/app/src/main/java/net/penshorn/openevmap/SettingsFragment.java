package net.penshorn.openevmap;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import net.penshorn.openevmap.R;

public class SettingsFragment extends Fragment implements View.OnClickListener {
    private static int MY_PERMISSION_ACCESS_COURSE_LOCATION = 1;
    Button button_login;
    EditText editview_username;
    EditText editview_password;
    TextView textview_location_status;
    Button button_toggle_location;
    boolean service_state = false;

    Intent locationService;

    RestClient client;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        client = new RestClient(container.getContext());



        View view =  inflater.inflate(R.layout.settings_fragment, container, false);

        button_login = (Button) view.findViewById(R.id.button_login);

        editview_username = (EditText) view.findViewById(R.id.editText_username);
        editview_password = (EditText) view.findViewById(R.id.editText_password);

        textview_location_status = (TextView) view.findViewById(R.id.textview_current_location_service);
        button_toggle_location = (Button) view.findViewById(R.id.button_togggle_location);


        locationService = new Intent(getContext(), LocationService.class);

        service_state = isMyServiceRunning(LocationService.class);


        update_service_gui();

        button_login.setOnClickListener(this);
        button_toggle_location.setOnClickListener(this);


        return view;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.button_togggle_location:
                toggle_service();
                break;
            case R.id.button_stop_service:
                //stop_service();
                break;
            case R.id.button_login:
                login();
                break;
            case R.id.button_send_test_loc:
                //send_test_location();
        }
    }

    private void update_service_gui()
    {
        if(service_state)
            textview_location_status.setText("Enabled");
        else
            textview_location_status.setText("Diabled");
    }
    private void login()
    {
        client.login(editview_username.getText().toString(),editview_password.getText().toString());
    }
    @TargetApi(Build.VERSION_CODES.M)
    private void toggle_service()
    {
        if (!service_state)
        {
            if ( ContextCompat.checkSelfPermission( this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {

                ActivityCompat.requestPermissions( this.getActivity(), new String[] {  Manifest.permission.ACCESS_FINE_LOCATION },
                        MY_PERMISSION_ACCESS_COURSE_LOCATION );
            }
            getActivity().startService(locationService);

            service_state = true;
        }
        else
        {
            getActivity().stopService(locationService);
            service_state = false;
        }

        update_service_gui();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) this.getContext().getSystemService(this.getContext().ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}