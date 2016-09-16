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
import org.orgama.server.auth.AuthSessionService;
import org.orgama.server.auth.AuthUserService;
import org.orgama.server.auth.model.AuthInitialization;
import org.orgama.server.config.OrgamaTestEnv;
import org.orgama.server.config.OrgamaTstModule;
import org.orgama.shared.auth.action.ValidateEmailAddress;
import org.orgama.shared.auth.action.ValidateEmailAddressResult;
import org.orgama.shared.auth.model.AuthUser;
import org.orgama.shared.auth.source.AuthServiceName;

/**
 *
 * @author kguthrie
 */
@RunWith(JukitoRunner.class)
public class ValidateEmailAddressHandlerTest {
	
	public static class Module extends OrgamaTstModule {}
	
	@Inject DispatchService dispatch;
	@Inject AuthUserService userService;
	@Inject AuthSessionService sessionService;
	@Inject OrgamaTestEnv env;
	
	String emailAddress = "test@example.com";
	
	public ValidateEmailAddressHandlerTest() {
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
	
	/**
	 * This tests how the validate email address handler will handle a simple 
	 * and correct validate email address action when nothing has been 
	 * @throws Exception 
	 */
	@Test
	public void testValidationFromScratch() throws Exception {
		ValidateEmailAddress action = new ValidateEmailAddress(
				emailAddress);
		ValidateEmailAddressResult result = 
				(ValidateEmailAddressResult)dispatch.execute(null, action);
		
		assertNotNull(result);
		assertEquals(emailAddress, result.getEmailAddress());
		assertNotNull(result.getAuthSourceList());
		assertTrue(result.getAuthSourceList().size() > 0);
		assertNull(result.getRedirectUrl());
		assertEquals(ValidateEmailAddressResult.Code.unknownEmailAddress, 
				result.getResponseCode());
	}
	
	@Test
	public void testSomeErrorConditions() {
		
		try {
			ValidateEmailAddress action = 
					new ValidateEmailAddress(null);
			dispatch.execute(null, action);

			fail("This should have thrown an exception");
		}
		catch(Exception ex) {
			//this is expected
		}
		
		try {
			ValidateEmailAddress action = 
					new ValidateEmailAddress("adsfasf@asdfa@asdfa.com");
			dispatch.execute(null, action);

			fail("This should have thrown an exception");
		}
		catch(Exception ex) {
			//this is expected
		}
	}
	
	@Test
	public void testValidateEmailAddressWhenAlreadyLoggedIn() {
		AuthInitialization authInit = new AuthInitialization();
		authInit.setAuthServiceName(AuthServiceName.googleAccounts);
		authInit.setEmailAddress(env.getEmailAddress());
		authInit.setServiceSpecificUserId(env.getEmailAddress());
		AuthUser user = userService.registerNewUser(authInit);
		sessionService.create(user, authInit);
		
		try {
			ValidateEmailAddress action = 
					new ValidateEmailAddress("adsfasf@asdfa.com");
			dispatch.execute(null, action);

			fail("This should have thrown an exception");
		}
		catch(Exception ex) {
			//this is expected
		}
		
	}
	
	@Test
	public void testValidateEmailAddressWithExistingUser() throws Exception {
		AuthInitialization authInit = new AuthInitialization();
		authInit.setAuthServiceName(AuthServiceName.googleAccounts);
		authInit.setEmailAddress(env.getEmailAddress());
		authInit.setServiceSpecificUserId(env.getEmailAddress());
		AuthUser user = userService.registerNewUser(authInit);
		
		ValidateEmailAddress action = 
				new ValidateEmailAddress(env.getEmailAddress());
		ValidateEmailAddressResult result = 
				(ValidateEmailAddressResult)dispatch.execute(null, action);
		
		assertNotNull(result);
		assertNotNull(result.getRedirectUrl());
		assertEquals(ValidateEmailAddressResult.Code.redirect, 
				result.getResponseCode());
		assertEquals("/_ah/login?continue=http%3A%2F%2F127.0.0.1%3A8888", 
				result.getRedirectUrl());
	}
}
