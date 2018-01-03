package net.penshorn.openevmap;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.ListPreference;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import net.penshorn.openevmap.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

public class SettingsFragment extends Fragment implements View.OnClickListener {
    private static int MY_PERMISSION_ACCESS_COURSE_LOCATION = 1;
    public static final String TAG = "SettingsFraggment";
    public static final String BLUETOOTH_LIST_KEY = "BLUETOOTH_LIST_KEY";
    Button button_login;
    EditText editview_username;
    EditText editview_password;
    TextView textview_location_status;
    TextView textview_bluetooth_status;
    Button button_toggle_location;
    Button button_toggle_bluetooth;
    boolean service_state = false;
    boolean service_bluetooth_state = false;
    boolean mIsBound = false;
    SharedPreferences prefs;

    Intent locationService;
    Intent networkService;
    Intent bluetoothService;

    private AbstractGatewayService service;

    RestClient client;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        client = new RestClient(container.getContext());

        prefs = getActivity().getSharedPreferences(TAG, Context.MODE_APPEND);

        View view =  inflater.inflate(R.layout.settings_fragment, container, false);

        button_login = (Button) view.findViewById(R.id.button_login);

        editview_username = (EditText) view.findViewById(R.id.editText_username);
        editview_password = (EditText) view.findViewById(R.id.editText_password);

        textview_location_status = (TextView) view.findViewById(R.id.textview_current_location_service);
        textview_bluetooth_status = (TextView) view.findViewById(R.id.textview_bluetooth_service);
        button_toggle_location = (Button) view.findViewById(R.id.button_togggle_location);
        button_toggle_bluetooth = (Button) view.findViewById(R.id.button_toggle_bluetooth);

        locationService = new Intent(getContext(), LocationService.class);
        networkService = new Intent(getContext(),NetworkService.class);
        bluetoothService = new Intent(getContext(),ObdGatewayService.class);
        service_state = isMyServiceRunning(LocationService.class);
        service_bluetooth_state = isMyServiceRunning(ObdGatewayService.class);

        update_service_gui();

        button_login.setOnClickListener(this);
        button_toggle_location.setOnClickListener(this);
        button_toggle_bluetooth.setOnClickListener(this);

        Spinner dynamicSpinner = (Spinner) view.findViewById(R.id.spinner);



        //do some bluetooth setup

            /*
     * Let's use this device Bluetooth adapter to select which paired OBD-II
     * compliant device we'll use.
     */
        ArrayList<String> items = new ArrayList<String>();
        final ArrayList<BluetoothDevice> items_bluetooth = new ArrayList<BluetoothDevice>();
        final BluetoothAdapter mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBtAdapter == null) {

            return view;
        }
        final String remoteDevice = prefs.getString(SettingsFragment.BLUETOOTH_LIST_KEY, null);
        Log.v(TAG,"We got the following object from save: " + remoteDevice);
        int saved_device = 0;
        BluetoothDevice[] pairedDevices = new BluetoothDevice[mBtAdapter.getBondedDevices().size()];
        pairedDevices = mBtAdapter.getBondedDevices().toArray(pairedDevices);
        if (pairedDevices.length > 0) {
            for (int i = 0; i < pairedDevices.length; i++)
            { //BluetoothDevice device : pairedDevices) {
                items.add(pairedDevices[i].getName() + "\n" + pairedDevices[i].getAddress());
                items_bluetooth.add(pairedDevices[i]);

                if(pairedDevices[i].getAddress().equals(remoteDevice))
                {
                    saved_device = i;
                }
            }
        }

        String[] itemArray = new String[items.size()];
        itemArray = items.toArray(itemArray);




        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(),
                android.R.layout.simple_spinner_item, itemArray);

        dynamicSpinner.setAdapter(adapter);

        dynamicSpinner.setSelection(saved_device);

        dynamicSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Log.v("item", (String) parent.getItemAtPosition(position));
                prefs.edit().putString(SettingsFragment.BLUETOOTH_LIST_KEY,items_bluetooth.get(position).getAddress()).commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

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
            case R.id.button_toggle_bluetooth:
                toggle_bluetooth_service();
        }
    }

    private void update_service_gui()
    {
        if(service_state)
            textview_location_status.setText("Enabled");
        else
            textview_location_status.setText("Diabled");

        if(service_bluetooth_state)
            textview_bluetooth_status.setText("Enable");
        else
            textview_bluetooth_status.setText("Diabled");
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
            getActivity().startService(networkService);

            service_state = true;
        }
        else
        {
            getActivity().stopService(locationService);
            getActivity().stopService(networkService);
            service_state = false;
        }

        update_service_gui();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void toggle_bluetooth_service()
    {
        if (!service_bluetooth_state) {
            textview_bluetooth_status.setText("Connecting to car");
             bluetoothService = new Intent(this.getContext(), ObdGatewayService.class);
            this.getActivity().bindService(bluetoothService, serviceConn, Context.BIND_AUTO_CREATE);
            service_bluetooth_state = true;
            mIsBound = true;
        }
        else
        {
            this.getActivity().stopService(bluetoothService);
            this.getActivity().unbindService(serviceConn);

            service_bluetooth_state = false;
            mIsBound = false;
            update_service_gui();
        }

        //update_service_gui();
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

    private final ServiceConnection serviceConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder binder) {
            Log.d(TAG, className.toString() + " service is bound");
            //isServiceBound = true;
            SettingsFragment.this.service = ((AbstractGatewayService.AbstractGatewayServiceBinder) binder).getService();
            SettingsFragment.this.service.setContext(SettingsFragment.this.getActivity());
            Log.d(TAG, "Starting live data");
            try {
                SettingsFragment.this.service.startService();
                service_bluetooth_state = true;
                if (true)
                    update_service_gui();
            } catch (IOException ioe) {
                Log.e(TAG, "Failure Starting live data");
                //btStatusTextView.setText(getString(R.string.status_bluetooth_error_connecting));
                //if(mIsBound)
                //    SettingsFragment.this.service.unbindService(serviceConn);
            }
        }

        @Override
        protected Object clone() throws CloneNotSupportedException {
            return super.clone();
        }

        // This method is *only* called when the connection to the service is lost unexpectedly
        // and *not* when the client unbinds (http://developer.android.com/guide/components/bound-services.html)
        // So the isServiceBound attribute should also be set to false when we unbind from the service.
        @Override
        public void onServiceDisconnected(ComponentName className) {
            Log.d(TAG, className.toString() + " service is unbound");
            //isServiceBound = false;
        }
    };
}