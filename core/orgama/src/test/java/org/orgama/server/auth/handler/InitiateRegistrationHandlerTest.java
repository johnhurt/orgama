/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orgama.server.auth.handler;

import com.google.inject.Inject;
import com.gwtplatform.dispatch.shared.DispatchService;
import org.jukito.JukitoRunner;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.orgama.server.auth.AuthInitializationService;
import org.orgama.server.auth.AuthSessionService;
import org.orgama.server.auth.AuthUserService;
import org.orgama.server.auth.model.AuthInitialization;
import org.orgama.server.auth.model.AuthInitializationState;
import org.orgama.server.config.OrgamaTestEnv;
import org.orgama.server.config.OrgamaTstModule;
import org.orgama.shared.auth.action.InitiateRegistration;
import org.orgama.shared.auth.action.InitiateRegistrationResult;
import org.orgama.shared.auth.model.AuthUser;
import org.orgama.shared.auth.source.AuthServiceName;

/**
 *
 * @author kguthrie
 */
@RunWith(JukitoRunner.class)
public class InitiateRegistrationHandlerTest {
	
	public static class Module extends OrgamaTstModule {}
	
	@Inject DispatchService dispatch;
	@Inject AuthInitializationService authInitService;
	@Inject AuthUserService userService;
	@Inject AuthSessionService sessionService;
	@Inject OrgamaTestEnv env;
	
	
	String emailAddress1 = "test1@example1.com";
	String emailAddress2 = "test2@example@com";
	
	public InitiateRegistrationHandlerTest() {
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
	public void testInitiateRegistrationWithGoogleAccounts() throws Exception {
		InitiateRegistration registration = new InitiateRegistration();
		
		registration.setAuthResourceName(AuthServiceName.googleAccounts);
		registration.setEmailAddress(emailAddress1);
		
		InitiateRegistrationResult result = 
				(InitiateRegistrationResult)dispatch.execute(
						null, registration);
		
		assertNotNull(result);
		assertEquals("/_ah/login?continue=http%3A%2F%2F127.0.0.1%3A8888",
				result.getRedirectUrl());
		
		AuthInitialization authInit = authInitService.get();
		
		assertNotNull(authInit);
		
		assertEquals(emailAddress1, authInit.getEmailAddress());
		assertEquals(AuthServiceName.googleAccounts, 
				authInit.getAuthServiceName());
		assertEquals(AuthInitializationState.registering, authInit.getState());
		
	}
	
	@Test
	public void testSomeErrorConditions() {
		try {
			InitiateRegistration registration = new InitiateRegistration();

			registration.setAuthResourceName("Yo mama");
			registration.setEmailAddress(emailAddress1);

			dispatch.execute(null, registration);
			fail("This should have caused an error");
		}
		catch(Exception ex) {
			//this is expected
		}
		
		try {
			InitiateRegistration registration = new InitiateRegistration();

			registration.setAuthResourceName(AuthServiceName.googleAccounts);
			registration.setEmailAddress(null);

			dispatch.execute(null, registration);
			fail("This should have caused an error");
		}
		catch(Exception ex) {
			//this is expected
		}
		
		try {
			InitiateRegistration registration = new InitiateRegistration();

			registration.setAuthResourceName(AuthServiceName.googleAccounts);
			registration.setEmailAddress(emailAddress2);

			dispatch.execute(null, registration);
			fail("This should have caused an error");
		}
		catch(Exception ex) {
			//this is expected
		}
	}
	
	@Test
	public void testInitiateRegistrationAddressWhenAlreadyLoggedIn() {
		AuthInitialization authInit = new AuthInitialization();
		authInit.setAuthServiceName(AuthServiceName.googleAccounts);
		authInit.setEmailAddress(env.getEmailAddress());
		authInit.setServiceSpecificUserId(env.getEmailAddress());
		AuthUser user = userService.registerNewUser(authInit);
		sessionService.create(user, authInit);
		
		try {
			InitiateRegistration action = 
					new InitiateRegistration();
			action.setAuthResourceName(AuthServiceName.googleAccounts);
			action.setEmailAddress(env.getEmailAddress());
			dispatch.execute(null, action);

			fail("This should have thrown an exception");
		}
		catch(Exception ex) {
			//this is expected
		}
		
	}
	
	@Test
	public void testRegisteringUserThatIsAlreadyRegistered() {
		AuthInitialization authInit = new AuthInitialization();
		authInit.setAuthServiceName(AuthServiceName.googleAccounts);
		authInit.setEmailAddress(env.getEmailAddress());
		authInit.setServiceSpecificUserId(env.getEmailAddress());
		AuthUser user = userService.registerNewUser(authInit);
		
		try {
			InitiateRegistration action = 
					new InitiateRegistration();
			action.setAuthResourceName(AuthServiceName.googleAccounts);
			action.setEmailAddress(env.getEmailAddress());
			dispatch.execute(null, action);

			fail("This should have thrown an exception");
		}
		catch(Exception ex) {
			//this is expected
		}
		
	}
}