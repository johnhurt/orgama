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
import org.orgama.server.config.OrgamaTestEnv;
import org.orgama.server.config.OrgamaTstModule;
import org.orgama.server.config.ServerSideConstants;
import org.orgama.shared.ICookieHandler;
import org.orgama.shared.auth.model.AuthState;
import org.orgama.shared.auth.model.ICompleteAuthState;
import org.orgama.shared.auth.source.AuthServiceName;

/**
 * Unit tests for AuthBootstrapper that test the behavior for users registering
 * and authenticating successfully
 * @author kguthrie
 */
@RunWith(JukitoRunner.class)
public class AuthBootstrapperPositiveAuthenticationWithGoogleTest {
	
	public static class Module extends OrgamaTstModule {

		@Override
		protected boolean isAuthenticated() {
			return true;
		}

		@Override
		protected String getAuthServiceName() {
			return AuthServiceName.googleAccounts;
		}
		
	}
	
	@Inject AuthBootstrapper bootstrapper;
	@Inject ICookieHandler cookieHandler;
	@Inject ServerSideConstants constants;
	@Inject HttpSession httpSession;
	@Inject OrgamaTestEnv env;
	@Inject AuthUserService userService;
	
	public AuthBootstrapperPositiveAuthenticationWithGoogleTest() {
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
	public void testSuccessfulRegistrationWithGoogleAccounts() {
		AuthInitialization authInitialization = new AuthInitialization();
		authInitialization.setAuthServiceName(AuthServiceName.googleAccounts);
		authInitialization.setEmailAddress(env.getEmailAddress());
		authInitialization.setState(AuthInitializationState.registering);
		
		httpSession.setAttribute(constants.getAuthInitializationKey(), 
				authInitialization);
		
		ICompleteAuthState completeAuthState = bootstrapper.bootstrap();
		
		assertNotNull(completeAuthState);
		assertEquals(AuthState.authenticated, completeAuthState.getAuthState());
		assertEquals(AuthServiceName.googleAccounts, 
				completeAuthState.getAuthServiceName());
	}
	
	
	@Test
	public void testSuccessfulAuthenticationWithGoogleAccounts() {
		
		AuthInitialization authInitialization = new AuthInitialization();
		authInitialization.setAuthServiceName(AuthServiceName.googleAccounts);
		authInitialization.setEmailAddress(env.getEmailAddress());
		authInitialization.setState(AuthInitializationState.authenticating);
		authInitialization.setServiceSpecificUserId(env.getEmailAddress());
		
		//Register the user with the given auth Initialization
		userService.registerNewUser(authInitialization);
		
		httpSession.setAttribute(constants.getAuthInitializationKey(), 
				authInitialization);
		
		ICompleteAuthState completeAuthState = bootstrapper.bootstrap();
		
		assertNotNull(completeAuthState);
		assertEquals(AuthState.authenticated, completeAuthState.getAuthState());
		assertEquals(AuthServiceName.googleAccounts, 
				completeAuthState.getAuthServiceName());
	}
}