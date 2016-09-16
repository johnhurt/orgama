package org.orgama.server.auth;

import com.google.inject.Inject;
import static org.junit.Assert.*;

import org.jukito.JukitoRunner;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.orgama.server.config.TstModule;

@RunWith(JukitoRunner.class)
public class DevModeFacebookAuthServiceTest {
    
	public static class Module extends TstModule{}
	
	@Inject DevModeFacebookAuthService authService;
	
    public DevModeFacebookAuthServiceTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    /**
     * Test something
     */
    @Test
    public void testLogin() throws Exception {
        authService.login(
				"Any client id will work as long as there is one", 
				"r/facebook/code", "any state at all will work");
    }
}
