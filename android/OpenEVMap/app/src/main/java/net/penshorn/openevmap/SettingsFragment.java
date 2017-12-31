package net.penshorn.openevmap;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import net.penshorn.openevmap.R;

public class SettingsFragment extends Fragment implements View.OnClickListener {

    Button button_login;
    EditText editview_username;
    EditText editview_password;

    RestClient client;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        client = new RestClient(container.getContext());



        View view =  inflater.inflate(R.layout.settings_fragment, container, false);

        button_login = (Button) view.findViewById(R.id.button_login);

        editview_username = (EditText) view.findViewById(R.id.editText_username);
        editview_password = (EditText) view.findViewById(R.id.editText_password);


        button_login.setOnClickListener(this);


        return view;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.button_start_service:
                //enable_service();
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
    private void login()
    {
        client.login(editview_username.getText().toString(),editview_password.getText().toString());
    }
}