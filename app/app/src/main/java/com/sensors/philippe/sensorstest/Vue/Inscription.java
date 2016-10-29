package com.sensors.philippe.sensorstest.Vue;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sensors.philippe.sensorstest.Controleur.Validator;
import com.sensors.philippe.sensorstest.Modele.Account;
import com.sensors.philippe.sensorstest.Modele.DatabaseManager;
import com.sensors.philippe.sensorstest.Modele.DatabaseManagerListener;
import com.sensors.philippe.sensorstest.Modele.RequestType;
import com.sensors.philippe.sensorstest.R;

public class Inscription extends AppCompatActivity implements DatabaseManagerListener {

    private EditText et_Identifiant;
    private EditText et_Password;
    private EditText et_Name;
    private EditText et_FirstName;
    private EditText et_Weight;
    private EditText et_phoneNumber;
    private CheckBox et_SeatbeltAlwaysOn;

    private Button btnBack;
    private Button btnConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        et_Identifiant = (EditText)findViewById(R.id.et_id);
        et_Password = (EditText)findViewById(R.id.et_password);
        et_Name = (EditText)findViewById(R.id.et_name);
        et_FirstName = (EditText)findViewById(R.id.et_firstName);
        et_Weight = (EditText)findViewById(R.id.et_weight);
        et_phoneNumber = (EditText)findViewById(R.id.et_phoneNumber);
        et_SeatbeltAlwaysOn = (CheckBox)findViewById(R.id.seatbelt);
    }

    public void onClickBtnBack(View view) {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    public void onClickBtnConfirm(View view) {
        String id = et_Identifiant.getText().toString();
        String password = et_Password.getText().toString();
        String name = et_Name.getText().toString();
        String firstName = et_FirstName.getText().toString();
        String phoneNumber = et_phoneNumber.getText().toString();
        Boolean seatBeltAlwaysOn = et_SeatbeltAlwaysOn.isChecked();
        float weight = Float.parseFloat(et_Weight.getText().toString());

        if (Validator.validateID(id)) {
            if (Validator.validatePhoneNumber(phoneNumber)) {
                if ((name.length() > 0) && (firstName.length() > 0)) {
                    if (weight > 0) {
                        try {
                            ObjectMapper mapper = new ObjectMapper();
                            String accountAsString = mapper.writeValueAsString(new Account(id, password, name, firstName, phoneNumber, weight, seatBeltAlwaysOn));
                            DatabaseManager.requestDatabase(this, RequestType.CREATE_ACCOUNT, accountAsString);
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    @Override
    public void requestResult(RequestType requestType, Object object) {
        if (requestType == RequestType.CREATE_ACCOUNT) {
            if (object != null) {
                String accountAsString = (String)object;
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("ACCOUNT", accountAsString);
                intent.putExtras(bundle);
                startActivity(intent);
            } else {
                Toast invalidInscriptionToast = Toast.makeText(getBaseContext(), R.string.inscription_invalidInscriptionToast, Toast.LENGTH_LONG);
                invalidInscriptionToast.show();
            }
        }
    }
}
