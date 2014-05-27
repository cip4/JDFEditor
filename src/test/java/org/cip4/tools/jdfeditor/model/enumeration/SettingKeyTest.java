package org.cip4.tools.jdfeditor.model.enumeration;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Method;

/**
 * JUnit test case for SettingKey
 */
public class SettingKeyTest {

    @Test
    public void testInitGenericAttr() throws Exception {

        // arrange
        String expected = "ID Type JobID JobPartID ProductID CustomerID SpawnIDs" + " Class Status PartIDKeys xmlns xmlns:xsi xsi:Type"
                + " SettingsPolicy BestEffortExceptions" + " OperatorInterventionExceptions" + " MustHonorExceptions" + " DocIndex Locked DescriptiveName Brand";

        // act
        Method m = SettingKey.class.getDeclaredMethod("initGenericAttr");
        m.setAccessible(true);
        String actual = (String) m.invoke(null);

        // assert
        Assert.assertEquals("Value GenericAttr is wrong.", expected, actual);

    }
}