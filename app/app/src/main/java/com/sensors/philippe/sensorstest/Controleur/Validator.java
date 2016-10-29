package com.sensors.philippe.sensorstest.Controleur;


public class Validator {

    private static String[] emergencyListNumber = new String[10];

    public static void setEmergencyListNumber() {
        emergencyListNumber[0] = "000";
        emergencyListNumber[1] = "108";
        emergencyListNumber[2] = "110";
        emergencyListNumber[3] = "111";
        emergencyListNumber[4] = "112";
        emergencyListNumber[5] = "117";
        emergencyListNumber[6] = "119";
        emergencyListNumber[7] = "120";
        emergencyListNumber[8] = "911";
        emergencyListNumber[9] = "999";
    }

    /**
     * Détermine si le numéro de téléphone passée en paramètre est un téléphone d'urgence valide.
     * @param phoneNumber Le numéro de téléphone à valider.
     * @return Retourne vrai si le numéro est valide, sinon retourne faux.
     * */
    public static boolean validatePhoneNumber(String phoneNumber) {
        if (emergencyListNumber[0] == null){
            setEmergencyListNumber();
        }

        boolean numberIsValid = false;

        for (int i =0;i< emergencyListNumber.length;i++ ){
            if(phoneNumber.equals(emergencyListNumber[i])){
                numberIsValid = true;
            }
        }
        return numberIsValid;
    }

    /**
     * Détermine si l'identifiant passé en paramètre est valide ou non.
     * L'identifiant est valide si il n'est pas présent dans la base de données.
     * @param id L'identifiant à valider.
     * @return Retourne vrai si l'identifiant est valide, sinon retourne faux.
     * */
    public static boolean validateID(String id) {
        return true;
    }
}
