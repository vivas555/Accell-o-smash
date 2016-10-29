package ca.csf.tp3;

public class Account {

    public static final String TABLE_COLUMNS = "USER_Pseudo, USER_Password, USER_Name, USER_FirstName, USER_EmergencyNumber, USER_Weight, USER_SeatBeltAlwaysOn";

    /**
     * Pseudonyme du compte.
     * */
    private String accountID;

    /**
     * Mot de passe de l'utilisateur.
     * */
    private String password;

    /**
     * Nom du propriétaire.
     * */
    private String name;
    /**
     * Prénom du propriétaire.
     * */
    private String firstName;

    /**
     * Le numéro d'urgence à appeler en cas d'accident majeur.
     * */
    private String emergencyNumber;

    /**
     * Poids en kg du proriétaire de l'accompte.
     * */
    private float weight;

    /**
     * Valeur booléenne indiquant si l'utilisateur porte toujour sa ceinture de sécurité.
     */
    private boolean seatBeltAlwaysOn;

    public Account() {
    }

    public Account(String accountID, String password, String name, String firstName, String emergencyNumber, float weight, boolean seatBeltAlwaysOn) {
        this.accountID = accountID;
        this.password = password;
        this.name = name;
        this.firstName = firstName;
        this.emergencyNumber = emergencyNumber;
        this.weight = weight;
        this.seatBeltAlwaysOn = seatBeltAlwaysOn;
    }

    //--------------- Getters -----------------------

    public String getAccountID() {
        return accountID;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getEmergencyNumber() {
        return emergencyNumber;
    }

    public float getWeight() {
        return weight;
    }

    public boolean isSeatBeltAlwaysOn() {
        return seatBeltAlwaysOn;
    }


    //--------------- Setters -----------------------


    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setEmergencyNumber(String emergencyNumber) {
        this.emergencyNumber = emergencyNumber;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public void setSeatBeltAlwaysOn(boolean seatBeltAlwaysOn) {
        this.seatBeltAlwaysOn = seatBeltAlwaysOn;
    }

    //--------------- Extra -----------------------

    public static String getTableColumns() {return TABLE_COLUMNS;}

    public String getValues(int valueid) {
        switch (valueid) {
            case 1:
                return getAccountID();
            case 2:
                return getPassword();
            case 3:
                return getName();
            case 4:
                return getFirstName();
            case 5:
                return getEmergencyNumber();
            case 6:
                return String.valueOf(getWeight());
            case 7:
                int i = 0;
                if (isSeatBeltAlwaysOn())
                    i = 1;
                return String.valueOf(i);
            default:
                return "NULL";
        }
    }
}
