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
import org.orgama.server.config.OrgamaTstModule;
import org.orgama.server.config.ServerSideConstants;
import org.orgama.shared.ICookieHandler;
import org.orgama.shared.auth.model.AuthSession;

/**
 *
 * @author kguthrie
 */
@RunWith(JukitoRunner.class)
public class AuthSystemCleanerTest {

	public static class Module extends OrgamaTstModule {}
	
	@Inject AuthSystemCleaner authSystemCleaner;
	@Inject AuthInitializationService authInitializationProvider;
	@Inject AuthSessionService authSessionProvider;
	@Inject HttpSession httpSession;
	@Inject ICookieHandler cookieHandler;
	@Inject ServerSideConstants constants;
	
	public AuthSystemCleanerTest() {
	}

	@BeforeClass
	public static void setUpClass() {
		Ofy.register(AuthSession.class);
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
	public void testAuthSystemCleanupWithNothingToDo() {
		
		authSystemCleaner.clean();
		
	}
	
	@Test
	public void testAuthSystemCleanup() {
		
		String sessionId = "asdfasdfasdfasdfa";
		
		{
			AuthInitialization ai = authInitializationProvider.get();
			AuthSession as = new AuthSession();

			as.setExpirationDate(new Date(new Date().getTime() + 1000000));
			as.setSessionId(sessionId);
			as.setUserId(1);

			Ofy.save().entity(as).now();
			
			cookieHandler.setValue(constants.getSessionCookieName(), sessionId);
			
			authSessionProvider.get();
		}
		
		
		//At this point there is an auth initialization in the http session
		//and an authSession in both the datastore and the http session
		
		assertNotNull(httpSession.getAttribute(
				constants.getAuthSessionKey()));
		assertNotNull(httpSession.getAttribute(
				constants.getAuthInitializationKey()));
		assertNotNull(cookieHandler.getValue(
				constants.getSessionCookieName()));
		assertTrue(cookieHandler.getValue(
				constants.getSessionCookieName()).length() > 0);
		assertNotNull(Ofy.load().type(AuthSession.class).id(sessionId).get());
		
		authSystemCleaner.clean();
		
		//At this point there should be no auth initializations or auth sessions
		//anywhere and no valid cookie value for session id
		
		assertNull(httpSession.getAttribute(
				constants.getAuthSessionKey()));
		assertNull(httpSession.getAttribute(
				constants.getAuthInitializationKey()));
		assertNull(cookieHandler.getValue(
				constants.getSessionCookieName()));
		
		String sessionIdFromCookie = cookieHandler.getValue(
				constants.getSessionCookieName());
		
		assertTrue(sessionIdFromCookie == null 
				|| sessionIdFromCookie.length() == 0);
		
		assertNull(Ofy.load().type(AuthSession.class).id(sessionId).get());
		
	}
	
}
