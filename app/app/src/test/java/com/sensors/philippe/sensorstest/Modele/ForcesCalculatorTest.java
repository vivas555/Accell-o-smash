package com.sensors.philippe.sensorstest.Modele;

import junit.framework.Assert;

import org.junit.Test;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ForcesCalculatorTest {




    @Test
    public void canDetectBrokenAccellerometer() throws Exception {
        String[] values = new String[3];
        values[0]= "0";
        values[1]= "4";
        values[2]= "0";

        Assert.assertEquals(-1.0,ForcesCalculator.calculateNforceOnBody(100,values,true));
    }


    @Test
    public void canCalculateforce() throws Exception {
        String[] values = new String[3];
        values[0]= "0";
        values[1]= "10.8";
        values[2]= "0";

        Assert.assertEquals(50.0,ForcesCalculator.calculateNforceOnBody(100,values,true));
    }

    @Test
    public void canClaculateHeadInjuryCriterion() throws Exception {

        String[] values = new String[3];
        values[0]= "0";
        values[1]= "29.8";
        values[2]= "0";
        Assert.assertEquals(219.0,ForcesCalculator.calculateHeadInjuryCriterion(values));

    }

    @Test
    public void canDetectBrokenAccellerometerForHIC() throws Exception {
        String[] values = new String[3];
        values[0]= "0";
        values[1]= "4";
        values[2]= "0";

        Assert.assertEquals(-1.0,ForcesCalculator.calculateHeadInjuryCriterion(values));
    }
}