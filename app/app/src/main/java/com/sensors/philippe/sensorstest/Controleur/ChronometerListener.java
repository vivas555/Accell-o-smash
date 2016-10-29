package com.sensors.philippe.sensorstest.Controleur;

/**
 * Created by philippe on 2016-04-24.
 */
public interface ChronometerListener {
    void update(String id, long millisUntilFinished);
    void onFinish(String id);
}
