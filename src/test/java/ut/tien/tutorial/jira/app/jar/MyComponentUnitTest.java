package ut.tien.tutorial.jira.app.jar;

import org.junit.Test;
import tien.tutorial.jira.app.jar.api.MyPluginComponent;
import tien.tutorial.jira.app.jar.impl.MyPluginComponentImpl;

import static org.junit.Assert.assertEquals;

public class MyComponentUnitTest
{
    @Test
    public void testMyName()
    {
        MyPluginComponent component = new MyPluginComponentImpl(null);
        assertEquals("names do not match!", "myComponent",component.getName());
    }
}