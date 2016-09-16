/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orgama.server.auth;

import com.google.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import org.jukito.JukitoRunner;
import org.jukito.TestSingleton;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
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
public class HttpFacebookUserServiceFailureTest {

	private static final String applicationId = "TestApplicationId";
	private static final String fakeJSessionId = "FakeCookieId";
	private static final String accessToken = "TestAccessToken";
	private static final String applicationSecretId = "TestApplicationSecretId";
	private static final String fakeCode = "fakeCode";

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

	public HttpFacebookUserServiceFailureTest() {
	}

	@BeforeClass
	public static void setUpClass() {
	}

	@AfterClass
	public static void tearDownClass() {
	}

	@Before
	public void setUp() {
		resourceLoader = (MockResourceLoader)ifaceResourceLoader;
        requestResponse = ((MockServletRequestResponse.Request) ifaceRequest)
                .getParent();
		cookeiHandler.setValue("JSESSIONID", fakeJSessionId);
	}

	@After
	public void tearDown() {
	}

	@Test
	public void testAccessTokenFailures() {
		assertNull(userService.getAccessToken());

        requestResponse.addRequestQueryParam("code", fakeCode);

		assertNull(userService.getAccessToken());

		requestResponse.addRequestQueryParam("state", fakeJSessionId);

		String accessTokenUrl = String.format(
				"%s/r/facebook/access_token" +
						"?client_id=%s&redirect_uri=%s" +
						"&client_secret=%s&code=%s",
				Orgama.getDomain(),
				applicationId,
				"http%3A%2F%2F127.0.0.1%3A8888%2F",
				FacebookUserServiceViaHttp.DEFAULT_APP_SECRET_ID,
				fakeCode);

		assertNull(userService.getAccessToken());

		cookeiHandler.setValue("JSESSIONID", "notCorrect");

		assertNull(userService.getAccessToken());

		cookeiHandler.setValue("JSESSIONID", fakeJSessionId);

		resourceLoader.addUrlContent(accessTokenUrl,
				"something that has no accessToken");

		assertNull(userService.getAccessToken());
	}

	@Test
	public void testGetUserIdFailures() {
		assertNull(userService.getUserId(null));


		assertNull(userService.getUserId(accessToken));


		String meUrl = String.format("%s/r/facebook/me?access_token=%s",
				Orgama.getDomain(), accessToken);

		resourceLoader.addUrlContent(meUrl,
				"{something wrong\"}");


		assertNull(userService.getUserId(accessToken));


	}
}