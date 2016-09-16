/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orgama.server.auth;

import com.google.inject.Inject;
import java.util.Date;
import javax.servlet.http.HttpSession;
import org.jukito.JukitoRunner;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.orgama.server.Ofy;
import org.orgama.server.auth.model.AuthInitialization;
import org.orgama.server.auth.model.AuthInitializationState;
import org.orgama.server.config.OrgamaTestEnv;
import org.orgama.server.config.OrgamaTstModule;
import org.orgama.server.config.ServerSideConstants;
import org.orgama.server.mock.MockCookieHandler;
import org.orgama.shared.ICookieHandler;
import org.orgama.shared.auth.model.AuthSession;
import org.orgama.shared.auth.model.AuthState;
import org.orgama.shared.auth.model.AuthUser;
import org.orgama.shared.auth.model.ICompleteAuthState;
import org.orgama.shared.auth.source.AuthServiceName;

/**
 *
 * @author kguthrie
 */
@RunWith(JukitoRunner.class)
public class AuthBootstrapperBasicsTest {
	
	public static class Module extends OrgamaTstModule {

	}
	
	@Inject AuthBootstrapper bootstrapper;
	@Inject ICookieHandler cookieHandler;
	@Inject ServerSideConstants constants;
	@Inject HttpSession httpSession;
	@Inject OrgamaTestEnv env;
	
	public AuthBootstrapperBasicsTest() {
	}
	
	@BeforeClass
	public static void setUpClass() {
		Ofy.register(AuthSession.class);
		Ofy.register(AuthUser.class);
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
	public void testInitialBehavior() {
		ICompleteAuthState state = bootstrapper.bootstrap();
		
		assertNotNull(state);
		assertEquals(AuthState.nil, state.getAuthState());
		assertNull(state.getAuthServiceName());
		
	}
	
	@Test
	public void testBehaviorWithValidSession() {
		String sessionId = "seasdfasdfaagagag";
		
		{
			cookieHandler.setValue(constants.getSessionCookieName(), sessionId);
			AuthSession authSession = new AuthSession();
			authSession.setExpirationDate(
					new Date(new Date().getTime() + 1000000));
			authSession.setSessionId(sessionId);
			authSession.setUserId(1);
			Ofy.save().entity(authSession).now();
		}
		
		ICompleteAuthState state = bootstrapper.bootstrap();
		
		assertNotNull(state);
		assertEquals(AuthState.authenticated, state.getAuthState());
		assertNull(state.getAuthServiceName());
		
	}
	
	@Test
	public void testBehaviorWithInvalidSession() {
		
		String sessionId = "seasdfasdfaagagag";
		
		{
			cookieHandler.setValue(constants.getSessionCookieName(), sessionId);
			AuthSession authSession = new AuthSession();
			authSession.setExpirationDate(
					new Date(new Date().getTime() - 1000000)); //Expired
			authSession.setSessionId(sessionId);
			authSession.setUserId(1);
			Ofy.save().entity(authSession).now();
		}
		
		ICompleteAuthState state = bootstrapper.bootstrap();
		
		assertNotNull(state);
		assertEquals(AuthState.nil, state.getAuthState());
		assertNull(state.getAuthServiceName());
	}
	
	@Test
	public void testBehaviorOnActualError() {
		String sessionId = "seasdfasdfaagagag";
		
		{
			cookieHandler.setValue(constants.getSessionCookieName(), sessionId);
			AuthSession authSession = new AuthSession();
			authSession.setExpirationDate(
					new Date(new Date().getTime() + 1000000)); 
			authSession.setSessionId(sessionId);
			authSession.setUserId(1);
			Ofy.save().entity(authSession).now();
		}
		
		//Set the cookie handler to throw an error on get
		((MockCookieHandler)cookieHandler).setThrowErrorOnGet(true);
		
		ICompleteAuthState state = bootstrapper.bootstrap();
		
		assertNotNull(state);
		assertEquals(AuthState.serverError, state.getAuthState());
		assertNull(state.getAuthServiceName());
	}
	
	@Test
	public void testInvalidServiceNameOnRegistration() {
		AuthInitialization authInitialization = new AuthInitialization();
		authInitialization.setAuthServiceName("this is an error");
		authInitialization.setEmailAddress(env.getEmailAddress());
		authInitialization.setState(AuthInitializationState.registering);
		
		httpSession.setAttribute(constants.getAuthInitializationKey(), 
				authInitialization);
		
		
		ICompleteAuthState authState = bootstrapper.bootstrap();
		
		assertEquals(AuthState.serverError, 
				authState.getAuthState());
	}
	
	@Test
	public void testRegistrationWhenNotAuthenticated() {
		AuthInitialization authInitialization = new AuthInitialization();
		authInitialization.setAuthServiceName(AuthServiceName.googleAccounts);
		authInitialization.setEmailAddress(env.getEmailAddress());
		authInitialization.setState(AuthInitializationState.registering);
		
		httpSession.setAttribute(constants.getAuthInitializationKey(), 
				authInitialization);
		
		ICompleteAuthState completeAuthState = bootstrapper.bootstrap();
		
		assertNotNull(completeAuthState);
		assertEquals(AuthState.externalAuthenticationFailed, 
				completeAuthState.getAuthState());
		assertEquals(AuthServiceName.googleAccounts, 
				completeAuthState.getAuthServiceName());
	}
	
	@Test
	public void testInvalidServiceNameOnAuthentication() {
		AuthInitialization authInitialization = new AuthInitialization();
		authInitialization.setAuthServiceName("this is an error");
		authInitialization.setEmailAddress(env.getEmailAddress());
		authInitialization.setState(AuthInitializationState.authenticating);
		
		httpSession.setAttribute(constants.getAuthInitializationKey(), 
				authInitialization);
		
		
		ICompleteAuthState authState = bootstrapper.bootstrap();
		
		assertEquals(AuthState.serverError, 
				authState.getAuthState());
	}
	
	@Test
	public void testAuthenticationWhenNotAuthenticated() {
		AuthInitialization authInitialization = new AuthInitialization();
		authInitialization.setAuthServiceName(AuthServiceName.googleAccounts);
		authInitialization.setEmailAddress(env.getEmailAddress());
		authInitialization.setState(AuthInitializationState.authenticating);
		
		httpSession.setAttribute(constants.getAuthInitializationKey(), 
				authInitialization);
		
		ICompleteAuthState completeAuthState = bootstrapper.bootstrap();
		
		assertNotNull(completeAuthState);
		assertEquals(AuthState.externalAuthenticationFailed, 
				completeAuthState.getAuthState());
		assertEquals(AuthServiceName.googleAccounts, 
				completeAuthState.getAuthServiceName());
	}
	
}
