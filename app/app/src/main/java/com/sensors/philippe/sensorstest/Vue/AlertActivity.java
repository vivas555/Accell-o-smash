package com.sensors.philippe.sensorstest.Vue;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sensors.philippe.sensorstest.Controleur.Chronometer;
import com.sensors.philippe.sensorstest.Controleur.ChronometerListener;
import com.sensors.philippe.sensorstest.R;

import java.util.ArrayList;
import java.util.List;

public class AlertActivity extends AppCompatActivity implements ChronometerListener {

    public static final String AUTO_CALL_TIMER = "autoCallTimer";
    public static final String FLASHING_TIMER = "flashingTimer";
    public static final int MILLIS_IN_FUTURE = 30000;
    public static final int COUNT_DOWN_INTERVAL = 1000;
    public static final String TIMER_TIME_LEFT = "TIMER_TIME_LEFT";

    private TextView tv_timer;
    private LinearLayout lLayout_flashing;

    private Chronometer callTimer;

    private Chronometer backGroundTimer;
    private int bgColorCode;
    private int bgColorCodeDirection;
    private List<Integer> bgcolors;

    private String phoneNumber ="";
    private Boolean callMade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);

        tv_timer = (TextView)findViewById(R.id.tv_timer);
        if (tv_timer != null) {
            tv_timer.setText("30");
        }

        lLayout_flashing = (LinearLayout)findViewById(R.id.llayout_flashing);
        lLayout_flashing.setBackgroundColor(Color.parseColor("#ffffff"));

        if(callTimer == null) {
            callTimer = new Chronometer(AUTO_CALL_TIMER, MILLIS_IN_FUTURE, COUNT_DOWN_INTERVAL,
                    Chronometer.ChronometerType.FINITE, this);
        }
        backGroundTimer = new Chronometer(FLASHING_TIMER, 50, 50, Chronometer.ChronometerType.INFINITE, this);
        bgColorCode = 0;
        bgColorCodeDirection = -1;
        bgcolors = new ArrayList<>();

        initializeColorsList();
        refreshView();
        updateBgColorCode();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
             phoneNumber = extras.getString("PHONE_NUMBER");
        }
        callMade = false;
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        long timeToShipToTimer = savedInstanceState.getLong(TIMER_TIME_LEFT);
        callTimer = new Chronometer(AUTO_CALL_TIMER, timeToShipToTimer, COUNT_DOWN_INTERVAL, Chronometer.ChronometerType.FINITE, this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        long timeLeftToTimer = callTimer.save();
        outState.putLong(TIMER_TIME_LEFT,timeLeftToTimer);
    }

    @Override
    protected void onResume() {
        super.onResume();

        callTimer.start();
        backGroundTimer.start();
    }

    @Override
    public void update(String id, long millisUntilFinished) {
        if (id.equals(AUTO_CALL_TIMER)) {
            long secondsUntilFinished = millisUntilFinished / 1000;
            int secondsLeft = Integer.valueOf(String.valueOf(secondsUntilFinished));
            tv_timer.setText(String.valueOf(secondsLeft));
        }
    }

    @Override
    public void onFinish(String id) {
        if (id.equals(FLASHING_TIMER)) {
            refreshView();
            updateBgColorCode();
        } else if (id.equals(AUTO_CALL_TIMER)) {
            setTextToCallingNow();
            makeCall();
        }
    }

    public void onClickCallNowBtn(View view) {
        this.callTimer.stop();
        setTextToCallingNow();
        makeCall();
    }


    /**
     * #region Gestion du flash de l'écran
     * */
    public void onClickCancelBtn(View view) {
        goBack(RESULT_CANCELED);
    }

    private void refreshView(){
        lLayout_flashing.setBackgroundColor(this.bgcolors.get(this.bgColorCode));
    }

    private void updateBgColorCode() {
        if (bgColorCode <= 0 || bgColorCode >= 7) {
            this.bgColorCodeDirection *= -1;
        }
        this.bgColorCode += this.bgColorCodeDirection;
    }

    private void initializeColorsList() {
        this.bgcolors.add(0, Color.parseColor("#ef9a9a"));
        this.bgcolors.add(1, Color.parseColor("#e57373"));
        this.bgcolors.add(2, Color.parseColor("#ef5350"));
        this.bgcolors.add(3, Color.parseColor("#f44336"));
        this.bgcolors.add(4, Color.parseColor("#e53935"));
        this.bgcolors.add(5, Color.parseColor("#d32f2f"));
        this.bgcolors.add(6, Color.parseColor("#c62828"));
        this.bgcolors.add(7, Color.parseColor("#b71c1c"));
    }
    /**
     * #endregion
     * */

    private void setTextToCallingNow() {
        ViewGroup.LayoutParams timerLayout = tv_timer.getLayoutParams();
        timerLayout.height = ViewGroup.LayoutParams.MATCH_PARENT;
        tv_timer.setLayoutParams(timerLayout);
        tv_timer.setText(R.string.alert_callingNow);
    }

    private void makeCall(){
        setCollisionCallMade();

        Intent in = new Intent(Intent.ACTION_CALL, Uri.parse(phoneNumber));
        try{
            startActivity(in);
        } catch (android.content.ActivityNotFoundException ex){
            ex.printStackTrace();
        }
        goBack(RESULT_OK);
    }

    private void setCollisionCallMade(){
        callMade = true;
    }

    private void goBack(int resultCode) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(MainActivity.COLLISION_STRENGTH, this.getIntent().getExtras().getDouble(MainActivity.COLLISION_STRENGTH));
        resultIntent.putExtra(MainActivity.COLLISION_CALLDONE, callMade);
        setResult(resultCode, resultIntent);
        finish();
    }
}

