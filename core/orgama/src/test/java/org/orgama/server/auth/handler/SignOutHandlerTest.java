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
import org.orgama.shared.auth.action.SignOut;
import org.orgama.shared.auth.action.SignOutResult;
import org.orgama.shared.auth.model.AuthSession;
import org.orgama.shared.auth.model.AuthUser;
import org.orgama.shared.auth.source.AuthServiceName;

/**
 *
 * @author kguthrie
 */
@RunWith(JukitoRunner.class)
public class SignOutHandlerTest {
	
	public static class Module extends OrgamaTstModule {}
	
	@Inject DispatchService dispatch;
	@Inject AuthUserService userService;
	@Inject AuthSessionService sessionService;
	@Inject OrgamaTestEnv env;
	
	
	public SignOutHandlerTest() {
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
	public void testSignOutWhenNotSignedIn() throws Exception {
		SignOut action = new SignOut();
		action.setAuthServiceName(AuthServiceName.googleAccounts);
		action.setSignOutOfApp(true);
		action.setSignOutOfExternalService(false);
		
		SignOutResult result = (SignOutResult)dispatch.execute(null, action);
	
		assertNotNull(result);
		assertNotNull(result.getRedirectUrl());
		assertEquals("http://127.0.0.1:8888", result.getRedirectUrl());
		
		action = new SignOut();
		action.setAuthServiceName(AuthServiceName.googleAccounts);
		action.setSignOutOfApp(false);
		action.setSignOutOfExternalService(true);
		
		result = (SignOutResult)dispatch.execute(null, action);
	
		assertNotNull(result);
		assertNotNull(result.getRedirectUrl());
		assertEquals("/_ah/logout?continue=http%3A%2F%2F127.0.0.1%3A8888", 
				result.getRedirectUrl());
		
		action = new SignOut();
		action.setAuthServiceName(AuthServiceName.googleAccounts);
		action.setSignOutOfApp(true);
		action.setSignOutOfExternalService(true);
		
		result = (SignOutResult)dispatch.execute(null, action);
	
		assertNotNull(result);
		assertNotNull(result.getRedirectUrl());
		assertEquals("/_ah/logout?continue=http%3A%2F%2F127.0.0.1%3A8888", 
				result.getRedirectUrl());
		
	}
	
	@Test
	public void testSignOutOfAppOnly() throws Exception {
		AuthInitialization authInit = new AuthInitialization();
		authInit.setAuthServiceName(AuthServiceName.googleAccounts);
		authInit.setEmailAddress(env.getEmailAddress());
		authInit.setServiceSpecificUserId(env.getEmailAddress());
		AuthUser user = userService.registerNewUser(authInit);
		sessionService.create(user, authInit);
		
		SignOut action = new SignOut();
		action.setAuthServiceName(AuthServiceName.googleAccounts);
		action.setSignOutOfApp(true);
		action.setSignOutOfExternalService(false);
		
		SignOutResult result = (SignOutResult)dispatch.execute(null, action);
	
		assertNotNull(result);
		assertNotNull(result.getRedirectUrl());
		assertEquals("http://127.0.0.1:8888", result.getRedirectUrl());
		
		AuthSession session = sessionService.get();
		
		assertNull(session);
	}
	
	@Test
	public void testSignOutOfAppAndExternalService() throws Exception {
		AuthInitialization authInit = new AuthInitialization();
		authInit.setAuthServiceName(AuthServiceName.googleAccounts);
		authInit.setEmailAddress(env.getEmailAddress());
		authInit.setServiceSpecificUserId(env.getEmailAddress());
		AuthUser user = userService.registerNewUser(authInit);
		sessionService.create(user, authInit);
		
		SignOut action = new SignOut();
		action.setAuthServiceName(AuthServiceName.googleAccounts);
		action.setSignOutOfApp(true);
		action.setSignOutOfExternalService(true);
		
		SignOutResult result = (SignOutResult)dispatch.execute(null, action);
	
		assertNotNull(result);
		assertNotNull(result.getRedirectUrl());
		assertEquals("/_ah/logout?continue=http%3A%2F%2F127.0.0.1%3A8888", 
				result.getRedirectUrl());
		
		AuthSession session = sessionService.get();
		
		assertNull(session);
	}
}