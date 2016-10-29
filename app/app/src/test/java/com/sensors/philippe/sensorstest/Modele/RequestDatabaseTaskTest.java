package com.sensors.philippe.sensorstest.Modele;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import static org.powermock.api.easymock.PowerMock.mockStatic;
import static org.powermock.api.easymock.PowerMock.replayAll;
import static org.powermock.api.easymock.PowerMock.verifyAll;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import org.powermock.reflect.Whitebox;

import static org.mockito.Mockito.times;

@PrepareForTest({DatabaseManager.class})
@RunWith(PowerMockRunner.class)

public class RequestDatabaseTaskTest  {

    @Test
    public void canGetTheRightSwitchCase() throws Exception {
        RequestDatabaseTask task = new RequestDatabaseTask();
             Object bool = task.doInBackground(null,RequestType.TEST,null,null);
        Assert.assertTrue((Boolean)bool);
    }
}