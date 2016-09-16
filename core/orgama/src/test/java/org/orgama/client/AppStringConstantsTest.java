/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orgama.client;

import com.google.inject.Inject;
import org.jukito.JukitoRunner;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.orgama.client.config.ClientSideConstants;
import org.orgama.server.config.OrgamaTstModule;

/**
 *
 * @author kguthrie
 */
@RunWith(JukitoRunner.class)
public class AppStringConstantsTest {
	
	//Boiler plate to make injections work using OrgamaTstEnvModule
    public static class Module extends OrgamaTstModule {}
	
	@Inject 
	ClientSideConstants constants;
	
    public AppStringConstantsTest() {
    }
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
    public void testGettingConstantsWithInjectedValues() {
        
        assertEquals(constants.getAppDescription(), OrgamaTstModule.description);
        assertEquals(constants.getAppTitle(), OrgamaTstModule.title);
        assertEquals(constants.getAppKeywords(), OrgamaTstModule.keywords);
    }
}
