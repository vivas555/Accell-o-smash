package com.sensors.philippe.sensorstest.Controleur;

import android.os.CountDownTimer;

public class Chronometer {

    public enum ChronometerType {FINITE, INFINITE};
    private ChronometerType chronometerType;
    private ChronometerListener chronometerListener;
    private CountDownTimer timer;
    private long timeUntilFinish = 0;
    private long countDownInterval =0;

    public Chronometer(final String id, final long millisInFuture, final long countDownInterval, ChronometerType type, ChronometerListener listener) {
        this.chronometerListener = listener;
        this.chronometerType = type;
        this.timeUntilFinish = millisInFuture;
        this.countDownInterval = countDownInterval;
        this.timer = new CountDownTimer(millisInFuture, countDownInterval) {
            @Override
            public void onTick(long millisUntilFinished) {
                chronometerListener.update(id, millisUntilFinished);
                timeUntilFinish -= countDownInterval;
            }

            @Override
            public void onFinish() {
                chronometerListener.onFinish(id);
                if (chronometerType == ChronometerType.INFINITE)
                    this.start();
            }
        };
    }

    public void start(){
        this.timer.start();
    }

    public void stop() {
        this.timer.cancel();
    }

    public long save(){
        return  this.timeUntilFinish;
    }
}