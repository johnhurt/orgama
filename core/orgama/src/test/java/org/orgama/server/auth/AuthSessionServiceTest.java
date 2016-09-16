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
import org.orgama.server.config.OrgamaTstModule;
import org.orgama.server.config.ServerSideConstants;
import org.orgama.shared.ICookieHandler;
import org.orgama.shared.auth.except.AuthSessionExpiredException;
import org.orgama.shared.auth.except.InvalidAuthSessionException;
import org.orgama.shared.auth.model.AuthSession;

/**
 *
 * @author kguthrie
 */
@RunWith(JukitoRunner.class)
public class AuthSessionServiceTest {

	//Boiler plate to make injections work using OrgamaTstModule
	public static class Module extends OrgamaTstModule {
	}
	@Inject
	AuthSessionService asp;
	@Inject
	HttpSession httpSession;
	@Inject
	ICookieHandler cookieHandler;
	@Inject
	ServerSideConstants constants;

	public AuthSessionServiceTest() {
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

	/**
	 * Test trying to get an auth session and getting null because none have
	 * been initialized
	 */
	@Test
	public void testFailingToGetAnAuthSession() {

		assertNull(asp.get());

	}

	@Test
	public void testGettingAnAuthSessionDirectlyFromHttpSession() {

		assertNull(asp.get());
		String tempSessionId = "Hello";

		{
			AuthSession tempSession = new AuthSession();
			tempSession.setSessionId(tempSessionId);
			tempSession.setExpirationDate(new Date(
					new Date().getTime() + 1000000));
			cookieHandler.setValue(constants.getSessionCookieName(),
					tempSessionId);
			httpSession.setAttribute(
					constants.getAuthSessionKey(), tempSession);
		}

		//Verify that getting the auth session from the http session returns
		//an equivalent object.  Because the http session serializes, the 
		//pointers will not be the same
		AuthSession authSession = asp.get();
		assertNotNull(authSession);
		assertEquals(tempSessionId, authSession.getSessionId());

	}

	@Test
	public void testGettingAnAuthSessionFromDatastore() {
		assertNull(asp.get());
		String tempSessionId = "YoYoYo";

		{
			AuthSession tempSession = new AuthSession();
			tempSession.setSessionId(tempSessionId);
			tempSession.setExpirationDate(new Date(
					new Date().getTime() + 1000000));
			Ofy.save().entity(tempSession).now();
			cookieHandler.setValue(constants.getSessionCookieName(),
					tempSessionId);
		}

		//Verify that getting the auth session from the http session returns
		//an equivalent object.  Because the http session serializes, the 
		//pointers will not be the same
		AuthSession authSession = asp.get();
		assertNotNull(authSession);
		assertEquals(tempSessionId, authSession.getSessionId());

		//Reset the auth session provider so that it will not just return the 
		//same sessions.  and remove the session from the datastore.  This will 
		//test whether the same session once retrieved from the datastore can
		//be retrieved again from the session.
		Ofy.delete().entity(authSession).now();

		AuthSession authSessionFromHttpSession = asp.get();
		assertNotNull(authSessionFromHttpSession);
		assertNotSame(authSession, authSessionFromHttpSession);
		assertEquals(tempSessionId, authSessionFromHttpSession.getSessionId());

	}

	@Test
	public void testGettingAnExpiredAuthSessionFromHttpSession() {

		assertNull(asp.get());
		String tempSessionId = "Hello";

		{
			AuthSession tempSession = new AuthSession();
			tempSession.setSessionId(tempSessionId);
			tempSession.setExpirationDate(new Date(
					new Date().getTime() - 1000000));
			cookieHandler.setValue(constants.getSessionCookieName(),
					tempSessionId);
			httpSession.setAttribute(
					constants.getAuthSessionKey(), tempSession);
		}

		try {
			asp.get();
			fail("Retrieving an expired session should cause and exception.");
		} catch (AuthSessionExpiredException asx) {
			//This is expected
		}

	}

	@Test
	public void testGettingAnExpiredSessionFromDatastore() {

		assertNull(asp.get());
		String tempSessionId = "Helllfldfld";

		{
			AuthSession tempSession = new AuthSession();
			tempSession.setSessionId(tempSessionId);
			tempSession.setExpirationDate(
					new Date(new Date().getTime() - 1000000));

			cookieHandler.setValue(constants.getSessionCookieName(),
					tempSessionId);
			Ofy.save().entity(tempSession).now();
		}

		try {
			asp.get();
			fail("Retrieving an expired session should cause and exception.");
		} catch (AuthSessionExpiredException asx) {
			//This is expected
		}

	}

	@Test
	public void testGettingNullReturnFromAuthSessionManager() {

		assertNull(asp.get());
		String tempSessionId = "asdfasdfasdf";

		{
			AuthSession tempSession = new AuthSession();
			tempSession.setSessionId(tempSessionId);
			tempSession.setExpirationDate(
					new Date(new Date().getTime() - 1000000));

			cookieHandler.setValue(constants.getSessionCookieName(),
					tempSessionId);
			//Don't save the auth session in the datastore, so the session id
			//in the cookie points nowhere
		}

		assertNull(asp.get());
	}

	@Test
	public void testGettingMismatchedSessionIdsFromHttpSession() {

		assertNull(asp.get());
		String tempSessionId = "ThisIsRight";
		String wrongSessionId = "ThisIsWrong";

		{
			AuthSession tempSession = new AuthSession();
			tempSession.setSessionId(tempSessionId);
			tempSession.setExpirationDate(
					new Date(new Date().getTime() + 1000000));

			cookieHandler.setValue(constants.getSessionCookieName(),
					wrongSessionId);
			httpSession.setAttribute(constants.getAuthSessionKey(),
					tempSession);
		}

		try {
			asp.get();
			fail("Retrieving a session with wrong id in cookie should "
					+ "cause and exception.");
		} catch (InvalidAuthSessionException iax) {
			//This is expected
		}
	}

	@Test
	public void testValidatingNullSessionId() {
		try {
			asp.validateAuthSession(null);
			fail("Validating null auth session id should be null");
		} catch (Exception ex) {
			//this is exprected
		}
	}
}
