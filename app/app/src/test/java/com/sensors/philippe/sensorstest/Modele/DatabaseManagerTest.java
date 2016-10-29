package com.sensors.philippe.sensorstest.Modele;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class DatabaseManagerTest {
    DatabaseManagerListener listener = null;
    RequestDatabaseTask task =null;


    @Before
    public void initialise(){
        listener = mock(DatabaseManagerListener.class);
        task = mock(RequestDatabaseTask.class);

    }

    @Test
    public void canAskToCreateAccount() throws Exception {
        initialise();
        RequestType type = RequestType.CREATE_ACCOUNT;
        DatabaseManager.requestDatabaseForTest(listener,type,task);
        verify(task).execute(listener,RequestType.CREATE_ACCOUNT);
    }

    @Test
    public void canAskToGetAccount() throws Exception {
        Object object = null;
        RequestType type = RequestType.GET_ACCOUNT;
        DatabaseManager.requestDatabaseForTest(listener,type,task);
        verify(task).execute(listener,RequestType.GET_ACCOUNT);
    }

    @Test
    public void canCallListener() throws Exception {
        initialise();
        Object object = null;
        RequestType type = RequestType.CREATE_ACCOUNT;
        DatabaseManager.requestResult(listener,type,object);
        verify(listener).requestResult(type,object);
    }
}