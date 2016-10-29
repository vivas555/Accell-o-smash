package com.sensors.philippe.sensorstest.Vue;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sensors.philippe.sensorstest.Modele.Account;
import com.sensors.philippe.sensorstest.Modele.DatabaseManager;
import com.sensors.philippe.sensorstest.Modele.DatabaseManagerListener;
import com.sensors.philippe.sensorstest.Modele.RequestType;
import com.sensors.philippe.sensorstest.R;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity implements DatabaseManagerListener {

    private EditText id;
    private EditText pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        id = (EditText)findViewById(R.id.et_login_id);
        pass = (EditText)findViewById(R.id.et_login_password);
    }

    public void onClickBtnBack(View view) {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    public void onClickBtnConfirm(View view) {
        DatabaseManager.requestDatabase(this, RequestType.GET_ACCOUNT, id.getText().toString());
    }

    @Override
    public void requestResult(RequestType requestType, Object object) {
        if (requestType == RequestType.GET_ACCOUNT) {
            boolean validLogin = false;
            Account account = new Account();
            ObjectMapper mapper = new ObjectMapper();

            if (object != null) {
                account = (Account) object;
                if (account.getPassword().equals(pass.getText().toString())) {
                    validLogin = true;
                }
            }

            if (validLogin) {
                Intent intent = new Intent(getBaseContext(), MainActivity.class);

                try {
                    intent.putExtra("ACCOUNT", mapper.writeValueAsString(account));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }

                startActivity(intent);
            } else {
                Toast.makeText(this, R.string.invalidAccountToast,
                        Toast.LENGTH_LONG).show();
                pass.setText("");
            }
        }
    }
}
