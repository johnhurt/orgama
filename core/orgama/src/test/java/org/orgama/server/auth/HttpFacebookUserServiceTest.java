/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orgama.server.auth;

import com.google.appengine.api.utils.SystemProperty;
import com.google.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import org.jukito.JukitoRunner;
import org.jukito.TestSingleton;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.orgama.server.IResourceLoader;
import org.orgama.server.Orgama;
import org.orgama.server.auth.source.FacebookUserServiceViaHttp;
import org.orgama.server.config.OrgamaTestEnvModule;
import org.orgama.server.config.OrgamaTstModule;
import org.orgama.server.mock.MockResourceLoader;
import org.orgama.server.mock.MockServletRequestResponse;
import org.orgama.shared.ICookieHandler;

/**
 *
 * @author kguthrie
 */
@RunWith(JukitoRunner.class)
public class HttpFacebookUserServiceTest {

	private static final String applicationId = "TestApplicationId";
	private static final String fakeJSessionId = "FakeCookieId";
	private static final String accessToken = "TestAccessToken";
	private static final String applicationSecretId = "TestApplicationSecretId";
	private static final String fakeCode = "fakeCode";
	private static final String fakeUserId = "fakeUserId";


	public static class Module extends OrgamaTstModule {

		@Override
		protected void bindTestEnvironment() {

			bindConstant()
					.annotatedWith(IFacebookUserService.ApplictionId.class)
					.to(applicationId);

			OrgamaTestEnvModule module = new OrgamaTestEnvModule(
					getEmailAddress(),
					getAuthServiceName(),
					isAuthenticated(),
					isAdmin()) {

				@Override
				protected void bindFacebookUserService() {
					bind(IFacebookUserService.class)
							.to(FacebookUserServiceViaHttp.class)
							.in(TestSingleton.class);
				}
			};

			install(module);

		}
	}

	@Inject IResourceLoader ifaceResourceLoader;
	@Inject IFacebookUserService userService;
	@Inject ICookieHandler cookeiHandler;
	@Inject HttpServletRequest ifaceRequest;

	private MockServletRequestResponse requestResponse;
	private MockResourceLoader resourceLoader;

	public HttpFacebookUserServiceTest() {
	}

	@BeforeClass
	public static void setUpClass() {
	}

	@AfterClass
	public static void tearDownClass() {

		SystemProperty.environment.set(
				SystemProperty.Environment.Value.Development);

	}

	@Before
	public void setUp() throws Exception {
		resourceLoader = (MockResourceLoader)ifaceResourceLoader;
        requestResponse
                = ((MockServletRequestResponse.Request) ifaceRequest).getParent();
		cookeiHandler.setValue("JSESSIONID", fakeJSessionId);
	}

	@After
	public void tearDown() {
	}

	@Test
	public void testGetSignInUrlDev() {

		SystemProperty.environment.set(
				SystemProperty.Environment.Value.Development);

		String redirectUrl = userService.getSignInUrl("http://localhost:8888");

		assertNotNull(redirectUrl);

		assertEquals(redirectUrl,
				"../r/facebook/login?client_id=" +
				applicationId + "&" +
				"redirect_uri=http%3A%2F%2Flocalhost%3A8888%2F&" +
				"state=" + fakeJSessionId);


	}

	@Test
	public void testGetSignInUrlProd() {

		SystemProperty.environment.set(
				SystemProperty.Environment.Value.Production);

		String redirectUrl = userService.getSignInUrl("http://localhost:8888/");

		assertNotNull(redirectUrl);

		assertEquals(redirectUrl,
				"https://www.facebook.com/dialog/oauth?client_id=" +
				applicationId + "&" +
				"redirect_uri=http%3A%2F%2Flocalhost%3A8888%2F" +
				"&state=" + fakeJSessionId);
	}

	@Test
	public void testGetSignOutUrlDev() {

		SystemProperty.environment.set(
				SystemProperty.Environment.Value.Development);

		String redirectUrl = userService.getSignOutUrl(
				"http://localhost:8888", accessToken);

		assertNotNull(redirectUrl);

		assertEquals(redirectUrl, String.format(
				"../r/facebook/logout?next=%s&access_token=%s",
				"http%3A%2F%2Flocalhost%3A8888%2F", accessToken));
	}

	@Test
	public void testGetSignOutUrlProd() {

		SystemProperty.environment.set(
				SystemProperty.Environment.Value.Production);

		String redirectUrl = userService.getSignOutUrl(
				"http://localhost:8888/", accessToken);

		assertNotNull(redirectUrl);

		assertEquals(redirectUrl, String.format(
				"https://www.facebook.com/logout.php?next=%s&access_token=%s",
				"http%3A%2F%2Flocalhost%3A8888%2F", accessToken));
	}

	@Test
	public void testGetAccessTokenDev() throws Exception {

		SystemProperty.environment.set(
				SystemProperty.Environment.Value.Development);

		String accessTokenUrl = String.format(
					"%s/r/facebook/access_token" +
							"?client_id=%s&redirect_uri=%s" +
							"&client_secret=%s&code=%s",
					Orgama.getDomain(),
					applicationId,
					"http%3A%2F%2F127.0.0.1%3A8888%2F",
					FacebookUserServiceViaHttp.DEFAULT_APP_SECRET_ID,
					fakeCode);

		resourceLoader.addUrlContent(accessTokenUrl,
				"access_token=" + accessToken + "&expires=1342515");

		requestResponse.addRequestQueryParam("code", fakeCode);
		requestResponse.addRequestQueryParam("state", fakeJSessionId);

		String accessToken = userService.getAccessToken();

		assertNotNull(accessToken);
		assertEquals(accessToken, HttpFacebookUserServiceTest.accessToken);

	}

	@Test
	public void testGetAccessTokenProd() throws Exception {

		SystemProperty.environment.set(
				SystemProperty.Environment.Value.Production);

		String accessTokenUrl = String.format(
					"https://graph.facebook.com/oauth/access_token" +
							"?client_id=%s&redirect_uri=%s" +
							"&client_secret=%s&code=%s",
					applicationId,
					"http%3A%2F%2F127.0.0.1%3A8888%2F",
					applicationSecretId,
					fakeCode);

		resourceLoader.addAppRootContent(
				FacebookUserServiceViaHttp.APP_SECRET_ID_FILE,
				applicationSecretId);

		resourceLoader.addUrlContent(accessTokenUrl,
				"access_token=" + accessToken + "&expires=1342515");

		requestResponse.addRequestQueryParam("code", fakeCode);
		requestResponse.addRequestQueryParam("state", fakeJSessionId);

		requestResponse.setRequestUri("dispatch/");

		String accessToken = userService.getAccessToken();

		assertNotNull(accessToken);
		assertEquals(accessToken, HttpFacebookUserServiceTest.accessToken);

	}

	@Test
	public void testGetUserIdDev() throws Exception {

		SystemProperty.environment.set(
				SystemProperty.Environment.Value.Development);

		String meUrl = String.format("%s/r/facebook/me?access_token=%s",
				Orgama.getDomain(), accessToken);

		resourceLoader.addUrlContent(meUrl,
				"{\"id\":\"" + fakeUserId + "\"}");

		String userId = userService.getUserId(accessToken);

		assertNotNull(userId);
		assertEquals(fakeUserId, userId);
	}

	@Test
	public void testGetStringBetween() {

		FacebookUserServiceViaHttp userService =
				(FacebookUserServiceViaHttp)this.userService;

		assertNull(userService.getStringBetween("", null	, null));
		assertNull(userService.getStringBetween(null, ""	, null));

		assertEquals("", userService.getStringBetween("abcdef", "f", "a"));

		assertEquals("c", userService.getStringBetween("abc", "b", null));

		assertEquals("c", userService.getStringBetween("abc", "b", "not here"));
	}
}