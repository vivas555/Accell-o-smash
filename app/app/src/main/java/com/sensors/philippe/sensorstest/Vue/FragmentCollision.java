package com.sensors.philippe.sensorstest.Vue;


import android.app.Fragment;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sensors.philippe.sensorstest.R;

import java.util.Date;

public class FragmentCollision extends Fragment {

    private String accountID;
    private Date date;
    private float strength;
    private boolean urgencyCalled;

    public FragmentCollision() {
    }

    public void setDefaultArguements(String accountID, Date date, float strength, boolean urgencyCalled) {
        this.accountID = accountID;
        this.date = date;
        this.strength = strength;
        this.urgencyCalled = urgencyCalled;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_collision, container, false);
    }

}
