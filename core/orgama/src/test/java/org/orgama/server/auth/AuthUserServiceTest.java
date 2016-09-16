/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orgama.server.auth;

import com.google.inject.Inject;
import org.jukito.JukitoRunner;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.orgama.server.auth.model.AuthInitialization;
import org.orgama.server.config.OrgamaTestEnv;
import org.orgama.server.config.OrgamaTstModule;
import org.orgama.shared.auth.except.AuthException;
import org.orgama.shared.auth.model.AuthUser;
import org.orgama.shared.auth.source.AuthServiceName;

/**
 *
 * @author kguthrie
 */
@RunWith(JukitoRunner.class)
public class AuthUserServiceTest {
	
	public static class Module extends OrgamaTstModule {
		
	}
	
	@Inject AuthUserService userService;
	@Inject OrgamaTestEnv env;
	
	AuthInitialization authInit = new AuthInitialization();
	AuthUser user = new AuthUser();
	
	public AuthUserServiceTest() {
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
	public void testCreateUserWithMissingInformation() {
		
		
		try {
			authInit.setAuthServiceName(AuthServiceName.googleAccounts);
			authInit.setEmailAddress(env.getEmailAddress());
			authInit.setServiceSpecificUserId(null);//env.getEmailAddress());
			userService.registerNewUser(authInit);
			fail("Missing field in auth initialzation should cause exception");
		}
		catch(AuthException ex) {
			//expected
		}
		
		try {
			user.setAuthServiceName(AuthServiceName.googleAccounts);
			user.setServiceSpecificUserId(env.getEmailAddress());
			userService.registerUser(user);
			fail("Missing field in auth initialzation should cause exception");
		}
		catch(AuthException ex) {
			//expected
		}
		
		try {
			authInit.setAuthServiceName(null);//AuthServiceName.googleAccounts);
			authInit.setEmailAddress(env.getEmailAddress());
			authInit.setServiceSpecificUserId(env.getEmailAddress());
			userService.registerNewUser(authInit);
			fail("Missing field in auth initialzation should cause exception");
		}
		catch(AuthException ex) {
			//expected
		}
	}
}