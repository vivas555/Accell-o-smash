package com.sensors.philippe.sensorstest.Modele;

public class DatabaseManager {

    public DatabaseManager() {
    }

    public static void requestDatabase(DatabaseManagerListener listener, RequestType requestType, Object... params) {
        RequestDatabaseTask task = new RequestDatabaseTask();
        task.execute(listener, requestType, params);
    }

    public static void requestDatabaseForTest(DatabaseManagerListener listener, RequestType requestType,RequestDatabaseTask task) {
        task.execute(listener, requestType);
    }

    public static void requestResult(DatabaseManagerListener listener, RequestType requestType, Object object) {
        listener.requestResult(requestType, object);
    }
}
