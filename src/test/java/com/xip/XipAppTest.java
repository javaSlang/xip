package com.xip;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class XipAppTest {

    @Test(expected = IllegalArgumentException.class)
    public void testMoreThan3Args() throws IOException {
        try {
            new XipAppImpl(new String[]{"login", "password", "tag", "extraArg"});
        } catch (IOException e) {
            assertEquals("Failed to run :: the app requires exactly 3 args :: login, password, key tag :: but actually passed 4 args.", e.getMessage());
            throw e;
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLessThan3Args() throws IOException {
        try {
            new XipAppImpl(new String[]{"login", "password"});
        } catch (IOException e) {
            assertEquals("Failed to run :: the app requires exactly 3 args :: login, password, key tag :: but actually passed 2 args.", e.getMessage());
            throw e;
        }
    }
}
