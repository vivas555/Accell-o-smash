package com.sensors.philippe.sensorstest.Modele;

import android.content.res.Resources;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sensors.philippe.sensorstest.R;

import java.util.Date;

public class Collision {

    private java.sql.Date collisionDate;
    private double collisionStrength;
    private String collisionOwner;
    private boolean callDone;

    public Collision() {
    }

    public Collision(java.sql.Date collisionDate, double collisionStrength, String collisionOwner, boolean callDone) {

        this.collisionDate = collisionDate;
        this.collisionStrength = collisionStrength;
        this.collisionOwner = collisionOwner;
        this.callDone = callDone;
    }

    public Date getcollisionDate() {
        return collisionDate;
    }

    public void setcollisionDate(java.sql.Date collisionDate) {
        this.collisionDate = collisionDate;
    }

    public double getcollisionStrength() {
        return collisionStrength;
    }


    public String toString(Resources res) {
        String toReturn = res.getString(R.string.collisionFrag_collisionOwner) + collisionOwner +"\n" +
                res.getString(R.string.collisionFrag_collisionDate) + collisionDate +"\n" +
                res.getString(R.string.collisionFrag_collisionStrength) + collisionStrength +"\n" +
                res.getString(R.string.collisionFrag_collisionEmergency);

        if(callDone){
            toReturn+= res.getString(R.string.yes);
        }
        else{
            toReturn+= res.getString(R.string.no);
        }

        return toReturn;
    }

    public void setcollisionStrength(double collisionStrength) {
        this.collisionStrength = collisionStrength;
    }

    public String getcollisionOwner() {
        return collisionOwner;
    }

    public void setcollisionOwner(String collisionOwner) {
        this.collisionOwner = collisionOwner;
    }

    public boolean isCallDone() {
        return callDone;
    }

    public void setCallDone(boolean callDone) {
        this.callDone = callDone;
    }


}
