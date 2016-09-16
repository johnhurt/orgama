/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orgama.server.auth;

import com.google.inject.Inject;
import javax.servlet.http.HttpSession;
import org.jukito.JukitoRunner;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.orgama.server.auth.model.AuthInitialization;
import org.orgama.server.auth.model.AuthInitializationState;
import org.orgama.server.config.OrgamaTstModule;
import org.orgama.server.config.ServerSideConstants;

import static org.orgama.server.auth.AuthInitializationService.RequireNew;
import org.orgama.server.config.OrgamaTestEnv;
import org.orgama.shared.auth.source.AuthServiceName;

/**
 *
 * @author kguthrie
 */
@RunWith(JukitoRunner.class)
public class AuthInitializationProviderTest {
	
	//Boiler plate to make injections work using OrgamaTstModule
    public static class Module extends OrgamaTstModule {}
	
	@Inject AuthInitializationService authInitializationProvider;
	@Inject HttpSession httpSession;
	@Inject ServerSideConstants constants;
	@Inject OrgamaTestEnv env;
	@Inject @RequireNew AuthInitializationService requireNewAuthInitService;
	
	public AuthInitializationProviderTest() {
	}
	
	@BeforeClass
	public static void setUpClass() {
	}
	
	@AfterClass
	public static void tearDownClass() {
	}
	
	@Before
	public void setUp() {
	}
	
	@After
	public void tearDown() {
	}
	
	@Test
	public void testGettingDefaultAuthInitialization() {
		//Make sure the http session does not contain an auth initialization;
		assertNull(httpSession.getAttribute(
				constants.getAuthInitializationKey()));
		
		AuthInitialization ai = authInitializationProvider.get();
		
		assertNotNull(ai);
		
		assertEquals(AuthInitializationState.nil, ai.getState());
		
		//Make sure that attempting to get the auth initialization again returns
		//the same pointer to the authinitialization before.
		AuthInitialization ai2 = authInitializationProvider.get();
		
		assertEquals(ai, ai2);
		
		//Make sure the http ssession now contains a copy of the same 
		//auth initialization.  This will not be the same pointer
		AuthInitialization ai3 = (AuthInitialization)httpSession.getAttribute(
				constants.getAuthInitializationKey());
		
		assertNotNull(ai3);
		assertNotSame(ai, ai3);
		assertEquals(ai.getState(), ai3.getState());
	}
	
	@Test
	public void testGettingExistingAuthInitialization() {
		
		String emailAddress = "temp@email.address.com";
		String authSource = "nothing-yet";
		AuthInitializationState state = AuthInitializationState.authenticating;
		
		{
			AuthInitialization ai = new AuthInitialization();
			ai.setEmailAddress(emailAddress);
			ai.setAuthServiceName(authSource);
			ai.setState(state);
			
			httpSession.setAttribute(constants.getAuthInitializationKey(), ai);
		}
		
		AuthInitialization ai = authInitializationProvider.get();
				
		assertNotNull(ai);
		assertEquals(state, ai.getState());
		assertEquals(emailAddress, ai.getEmailAddress());
		assertEquals(authSource, ai.getAuthServiceName());
	}
	
	@Test
	public void testRequireNewAfterNonNew() {
		
		AuthInitialization authInitialization = new AuthInitialization();
		authInitialization.setAuthServiceName(AuthServiceName.googleAccounts);
		authInitialization.setEmailAddress(env.getEmailAddress());
		authInitialization.setState(AuthInitializationState.authenticating);
		authInitialization.setServiceSpecificUserId(env.getEmailAddress());
		
		httpSession.setAttribute(constants.getAuthInitializationKey(), 
				authInitialization);
		
		AuthInitialization authInit = requireNewAuthInitService.get();
		
		assertNotNull(authInit);
		assertEquals(AuthInitializationState.nil, authInit.getState());
	}
}
