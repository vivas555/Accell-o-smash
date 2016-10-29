package com.sensors.philippe.sensorstest.Vue;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sensors.philippe.sensorstest.Controleur.Chronometer;
import com.sensors.philippe.sensorstest.Controleur.ChronometerListener;
import com.sensors.philippe.sensorstest.Modele.Account;
import com.sensors.philippe.sensorstest.Modele.Collision;
import com.sensors.philippe.sensorstest.Modele.DatabaseManager;
import com.sensors.philippe.sensorstest.Modele.DatabaseManagerListener;
import com.sensors.philippe.sensorstest.Modele.ForcesCalculator;
import com.sensors.philippe.sensorstest.Modele.RequestType;
import com.sensors.philippe.sensorstest.R;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ChronometerListener, SensorEventListener, DatabaseManagerListener{

    public static final String COLLISION_STRENGTH = "COLLISION_STRENGTH";
    public static final String COLLISION_CALLDONE = "COLLISION_CALLDONE";
    public static final int ALERT_REQUEST_CODE = 1;
    public static final String ACCOUNT_INFOS = "ACCOUNT_KEY";
    public static final String ACCOUNT_WEIGHT = "ACCOUNT_WEIGHT";
    public static final String ACCOUNT_SEATBELT = "ACCOUNT_SEATBELT";

    private TextView tv_X;
    private TextView tv_Y;
    private TextView tv_Z;
    private TextView login;

    private Button btnLogin;
    private Button btnRegister;

    private Chronometer chronometer;
    private boolean sensorToUpdate;

    private SensorManager smanager;
    private Sensor accelerometer;

    private Account account;

    //HIC>300 appeller, car c'est la limite supportable avant d'avoir de gros risque pour un adulte moyen
    private static int HIC =700;
    //3300 Newtons est la force nécessaire pour avoir une chance sur 4 de briser une cote et
    //une quasi certidude de la félée.
    private static int FORCE_NEED_TO_CALL = 3300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String a = String.valueOf(R.string.alert_alertText);

        tv_X = (TextView)findViewById(R.id.tv_X);
        tv_Y = (TextView)findViewById(R.id.tv_Y);
        tv_Z = (TextView)findViewById(R.id.tv_Z);
        login = (TextView)findViewById(R.id.tvLogin);

        btnLogin = (Button)findViewById(R.id.btnLogin);
        btnRegister = (Button)findViewById(R.id.btnRegister);

        this.chronometer = new Chronometer("updateRateTimer", 150, 150, Chronometer.ChronometerType.INFINITE, this);
        sensorToUpdate = true;

        smanager = (SensorManager)getSystemService(SENSOR_SERVICE);
        accelerometer = smanager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        Bundle extras = this.getIntent().getExtras();

        if (extras != null) {
            String accountAsString = extras.getString("ACCOUNT");
            if (accountAsString != null) {
                ObjectMapper mapper = new ObjectMapper();
                try {
                    this.account = mapper.readValue(accountAsString, Account.class);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                this.account = null;
            }
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(this.account == null){
            ArrayList<String> accountInfos = savedInstanceState.getStringArrayList(ACCOUNT_INFOS);
            account.setWeight( savedInstanceState.getFloat(ACCOUNT_WEIGHT));
            account.setSeatBeltAlwaysOn(savedInstanceState.getBoolean(ACCOUNT_SEATBELT));


            if (accountInfos != null) {
                account.setAccountID(accountInfos.get(0));
                account.setPassword(accountInfos.get(1));
                account.setName(accountInfos.get(2));
                account.setFirstName(accountInfos.get(3));
                account.setEmergencyNumber(accountInfos.get(4));
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
      ArrayList <String> accountInfos = new ArrayList<>(5);
        accountInfos.add(0,account.getAccountID());
        accountInfos.add(1, account.getPassword());
        accountInfos.add(2, account.getName());
        accountInfos.add(3, account.getFirstName());
        accountInfos.add(4, account.getEmergencyNumber());
        outState.putStringArrayList(ACCOUNT_INFOS,accountInfos);
        outState.putFloat(ACCOUNT_WEIGHT,account.getWeight());
        outState.putBoolean(ACCOUNT_SEATBELT,account.isSeatBeltAlwaysOn());
    }

    @Override
    protected void onStart() {
        super.onStart();
        //TODO Retirer ceci.
        //startActivity(new Intent(getBaseContext(), AlertActivity.class));
        //TODO Retirer ceci.
        //this.account = new Account("Awe", "AsYouCommand", "Tremblay", "Philippe", "00000000", 100, true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        smanager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_FASTEST);
        this.chronometer.start();
        refreshView();
    }

    @Override
    protected void onPause() {
        super.onPause();
        smanager.unregisterListener(this);
    }

    public void onClickBtnLogin(View view) {

        if (this.btnLogin.getText().toString().equals(getResources().getString(R.string.main_connectionBtnText_connected))) {
            this.account = null;
        } else if (this.btnLogin.getText().toString().equals(getResources().getString(R.string.main_connectionBtnText_notConnected))) {
            startActivity(new Intent(getBaseContext(), LoginActivity.class));
        }
        refreshView();
    }

    public void onClickBtnRegister(View view) {
        startActivity(new Intent(getApplicationContext(), Inscription.class));
    }

    public void onClickBtnCollisions(View view) {
        DatabaseManager.requestDatabase(this, RequestType.GET_COLLISIONS, this.account.getAccountID());
    }

    private void refreshView(){
        if (this.account != null) {
            this.login.setText(this.account.getAccountID());
            this.btnLogin.setText(R.string.main_connectionBtnText_connected);
            this.btnRegister.setVisibility(View.INVISIBLE);
        } else {
            this.login.setText(R.string.main_idText_notConnected);
            this.btnLogin.setText(R.string.main_connectionBtnText_notConnected);
            this.btnRegister.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void update(String id, long millisUntilFinished) {
        if (millisUntilFinished == 0)
            sensorToUpdate = true;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        String XForce = Float.toString(event.values[0]);
        String YForce = Float.toString(event.values[1]);
        String ZForce = Float.toString(event.values[2]);

        String[] valueOfAccelerometer = new String[3];
        valueOfAccelerometer[0] = XForce;
        valueOfAccelerometer[1] = YForce;
        valueOfAccelerometer[2] = ZForce;

        if (sensorToUpdate) {
            tv_X.setText("X: " + XForce);
            tv_Y.setText("Y: " + YForce);
            tv_Z.setText("Z: " + ZForce);
            sensorToUpdate = false;
        }

        float weight;
        double nForce;
        double hic;
        boolean seatBeltAlwaysOn;
        boolean callAlert = false;
        if (this.account != null) {
            weight = account.getWeight();
            seatBeltAlwaysOn = account.isSeatBeltAlwaysOn();
        } else {
            weight = 70;
            seatBeltAlwaysOn = false;
        }

        nForce = ForcesCalculator.calculateNforceOnBody(weight, valueOfAccelerometer, seatBeltAlwaysOn);
        hic = ForcesCalculator.calculateHeadInjuryCriterion(valueOfAccelerometer);

        if(nForce >= FORCE_NEED_TO_CALL || nForce == -1)
            callAlert = true;
        else if(hic >= HIC || hic ==-1  )
            callAlert = true;

        if (callAlert) {
            Intent alertIntent = new Intent(getBaseContext(), AlertActivity.class);
            Bundle b = new Bundle();
            b.putDouble(MainActivity.COLLISION_STRENGTH, nForce);
            startActivityForResult(alertIntent, ALERT_REQUEST_CODE);
        }
    }

    private void saveCollision (float strength, boolean callDone) {
        Collision newCollision = new Collision(new java.sql.Date(System.currentTimeMillis()),
                                               strength, this.account.getAccountID(), callDone);
        ObjectMapper mapper = new ObjectMapper();
        String accountAsString = "";
        try {
            accountAsString = mapper.writeValueAsString(newCollision);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        DatabaseManager.requestDatabase(this, RequestType.CREATE_COLLISION, accountAsString);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ALERT_REQUEST_CODE) {
            Bundle extras = data.getExtras();
            saveCollision(extras.getFloat(COLLISION_STRENGTH), extras.getBoolean(COLLISION_CALLDONE));
        }
    }

    @Override
     public void onFinish(String id) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    //TODO Supprimer ceci
    public void pretendCollision(View view) {
        Collision falseCollision = new Collision(new java.sql.Date(1L), 765, "Awe", true);
        ObjectMapper mapper = new ObjectMapper();
        String accountAsString = "";
        try {
            accountAsString = mapper.writeValueAsString(falseCollision);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        DatabaseManager.requestDatabase(this, RequestType.CREATE_COLLISION, accountAsString);
    }

    @Override
    public void requestResult(RequestType requestType, Object object) {
        if (requestType == RequestType.GET_COLLISIONS) {
            if (object != null) {
                String collisions = (String) object;
                Intent intent = new Intent(getBaseContext(), CollisionHistory.class);
                Bundle extras = new Bundle();
                ObjectMapper mapper = new ObjectMapper();

                String accountAsString = null;
                try {
                    accountAsString = mapper.writeValueAsString(this.account);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }

                extras.putString("COLLISIONS", collisions);
                extras.putString(CollisionHistory.ACCOUNT, accountAsString);
                intent.putExtras(extras);
                startActivity(intent);
            }
        }
        else if (requestType == RequestType.CREATE_COLLISION) {
            int i = 0;
        }
    }

    private void onCollision(){
        Intent alertIntent = new Intent(getApplicationContext(), AlertActivity.class);
        alertIntent.putExtra("PHONE_NUMBER", account.getEmergencyNumber());
        startActivity(alertIntent);
    }


}
