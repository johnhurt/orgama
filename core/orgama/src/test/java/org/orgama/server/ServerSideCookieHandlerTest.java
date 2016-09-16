/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orgama.server;

import com.google.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jukito.JukitoRunner;
import org.jukito.TestSingleton;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.orgama.server.config.OrgamaTestEnvModule;
import org.orgama.server.config.OrgamaTstModule;
import org.orgama.server.mock.MockServletRequestResponse;
import org.orgama.shared.ICookieHandler;
import org.orgama.shared.except.OrgamaCoreException;

/**
 * Test the implementation of the cookie handler for the server side
 * @author kguthrie
 */
@RunWith(JukitoRunner.class)
public class ServerSideCookieHandlerTest {

	//Boiler plate to make injections work using OrgamaTstEnvModule
    public static class Module extends OrgamaTstModule {

		@Override
		protected void bindTestEnvironment() {



			install(new OrgamaTestEnvModule(
					getEmailAddress(),
					getAuthServiceName(),
					isAuthenticated(),
					isAdmin()) {

							@Override
							protected void bindCookieHandler() {
								bind(ICookieHandler.class)
										.to(ServerSideCookieHandler.class)
										.in(TestSingleton.class);

							}

					});

		}

	}

	@Inject ICookieHandler cookieHandler;
	@Inject HttpServletRequest rawRequest;
	@Inject HttpServletResponse rawResponse;

	private MockServletRequestResponse request;
	private MockServletRequestResponse response;

	public ServerSideCookieHandlerTest() {
	}

	@BeforeClass
	public static void setUpClass() {
	}

	@AfterClass
	public static void tearDownClass() {
	}

	@Before
	public void setUp() {
        request = ((MockServletRequestResponse.Request) rawRequest).getParent();
        response = ((MockServletRequestResponse.Response) rawResponse).getParent();
	}

	@After
	public void tearDown() {
	}

	@Test
	public void testGettingNonexistantCookie() {
		assertNull(cookieHandler.getValue("asdfasfa"));
	}


	@Test
	public void testGettingExistingCookie() {
		String cookieName = "cName";
		String cookieValue = "cValue";

		request.addRequestCookie(new Cookie(cookieName, cookieValue));

		assertEquals(cookieValue, cookieHandler.getValue(cookieName));
	}

	@Test
	public void testSavingCookieValue() {
		String name = "name";
		String value = "Value";

		cookieHandler.setValue(name, value);

		assertNotNull(response.getResponseCookies());
		assertEquals(1, response.getResponseCookies().length);

		Cookie cookie = response.getResponseCookies()[0];

		assertNotNull(cookie);
		assertEquals(name, cookie.getName());
		assertEquals(value, cookie.getValue());
	}

	@Test
	public void testUpdatingCookieValue() {
		String name = "name";
		String value1 = "value1asdfasdf";
		String value2 = "value2strtert";
		String domain = "domain";

		{
			Cookie testCookie = new Cookie(name, value1);
			testCookie.setDomain(domain);
			request.addRequestCookie(testCookie);
		}

		String testValue1 = cookieHandler.getValue(name);

		assertEquals(value1, testValue1);

		cookieHandler.setValue(name, value2);

		assertNotNull(response.getResponseCookies());
		assertEquals(1, response.getResponseCookies().length);

		Cookie cookie = response.getResponseCookies()[0];

		assertNotNull(cookie);
		assertEquals(name, cookie.getName());
		assertEquals(value2, cookie.getValue());
		assertEquals(domain, cookie.getDomain());

		String testValue2 = cookieHandler.getValue(name);

		assertEquals(value2, testValue2);
	}

	@Test
	public void testAlreadyCommittedException() {
		response.setCommitted();
		try {
			cookieHandler.setValue("name", "value");
			fail("adding or modifying a cookie value after the response has "
					+ "been committed should cause an execption");
		}
		catch (OrgamaCoreException ox) {
			//This is expected
		}
	}
}
