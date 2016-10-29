package com.sensors.philippe.sensorstest.Modele;

import android.hardware.SensorEvent;

import java.math.BigDecimal;

public class ForcesCalculator {

    /**
     * Calcule la force en Newton(N) de l'impact subit par le corps lors de changement d'accélération
     * @param userWeight Le poids de l'utilisateur vu que la formule de la force selon la loi de NEwton est f=m*a
     * @param seatBeltAlwaysOn Un booléen savoir si l'utilisateur porte toujours sa ceinture de sécurité pour savoir si
     *                         une partie de la force est dissipée.
     * @return La force en Newton subit par l'utilisateur
     */
    public static double calculateNforceOnBody (float userWeight, String[] valueOfAccelerometer, boolean seatBeltAlwaysOn){
        double forceOnBody = 0.0d;
        double totalForcesOnBodyMinusGravity = Double.valueOf(valueOfAccelerometer[0]) +
                                   Double.valueOf(valueOfAccelerometer[1]) +
                                   Double.valueOf(valueOfAccelerometer[2])-9.8;


        if ((totalForcesOnBodyMinusGravity)< -1){
            return -1;
        }
        forceOnBody = userWeight * (totalForcesOnBodyMinusGravity);
        if(seatBeltAlwaysOn){
            forceOnBody = forceOnBody *0.5;
        }
        return forceOnBody;
    }
    public static double calculateHeadInjuryCriterion(String[] valueOfAccelerometer){

        BigDecimal totalForcesOnBodyMinusGravity = new BigDecimal( Double.valueOf(valueOfAccelerometer[0]) +
                Double.valueOf(valueOfAccelerometer[1]) +
                Double.valueOf(valueOfAccelerometer[2])-9.8);

        if ((totalForcesOnBodyMinusGravity.longValue())< -1){
            return -1;
        }

        totalForcesOnBodyMinusGravity = totalForcesOnBodyMinusGravity.pow(5);

       BigDecimal hic = BigDecimal.valueOf((0.015)*(((1/(0.015))* Math.sqrt(totalForcesOnBodyMinusGravity.doubleValue()*0.015))));

        return hic.longValue();
    }


}
