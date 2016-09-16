/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orgama.client;

import org.jukito.JukitoRunner;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

/**
 *
 * @author kguthrie
 */
@RunWith(JukitoRunner.class)
public class UtilitiesTest {
    
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Test
    public void testConstructorForNoReason() {
        Utilities u = new Utilities();
    }
    
    
    @Test
    public void testGetSysTimeSecs() {
        double d1 = Utilities.getSysTimeSecs();
        double d2 = ((double)System.currentTimeMillis()) / 1000;
        
        assertTrue("system time from get sys secs must be close", 
                Math.abs(d1 - d2) < 1);
    }
}
