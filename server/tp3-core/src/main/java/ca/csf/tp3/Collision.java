package ca.csf.tp3;

public class Collision {

    public static final String TABLE_COLUMNS = "COL_ID, COL_Date, COL_Force, COL_HelpCalled, COL_User";

    private java.sql.Date collisionDate;
    private float collisionStrength;
    private boolean callDone;
    private String collisionOwner;

    public Collision() {
    }

    public Collision(java.sql.Date collisionDate, float collisionStrength, boolean callDone, String collisionOwner) {
        this.collisionDate = collisionDate;
        this.collisionStrength = collisionStrength;
        this.callDone = callDone;
        this.collisionOwner = collisionOwner;
    }

    //--------------- Getters -----------------------

    public java.sql.Date getCollisionDate() {
        return collisionDate;
    }

    public float getCollisionStrength() {
        return collisionStrength;
    }

    public boolean isCallDone() {
        return callDone;
    }

    public int isCallDoneAsInteger() {
        int i = 0;
        if (this.callDone)
            i = 1;
        return i;
    }

    public String getCollisionOwner() {
        return collisionOwner;
    }

    //--------------- Setters -----------------------


    public void setCollisionDate(java.sql.Date collisionDate) {
        this.collisionDate = collisionDate;
    }

    public void setCollisionStrength(float collisionStrength) {
        this.collisionStrength = collisionStrength;
    }

    public void setCollisionCallDone(boolean callDone) {
        this.callDone = callDone;
    }

    public void setCollisionOwner(String collisionOwner) {
        this.collisionOwner = collisionOwner;
    }

    //--------------- Extras -----------------------

    public static String getTableColumns() {return TABLE_COLUMNS;}
}
