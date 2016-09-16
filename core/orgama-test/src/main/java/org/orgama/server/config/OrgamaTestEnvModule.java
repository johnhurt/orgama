package org.orgama.server.config;

import com.google.inject.AbstractModule;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.jukito.TestEagerSingleton;
import org.jukito.TestSingleton;
import org.orgama.server.IResourceLoader;
import org.orgama.server.auth.IFacebookUserService;
import org.orgama.server.mock.MockCookieHandler;
import org.orgama.server.mock.MockResourceLoader;
import org.orgama.server.mock.MockSerializingHttpSession;
import org.orgama.server.mock.MockServletRequestResponse;
import org.orgama.server.mock.auth.MockFacebookUserService;
import org.orgama.shared.ICookieHandler;

/**
 * Test Module for binding the Orgama test environment in one shot.  This
 * includes the app engine datastore mock system as well as the mock auth
 * systems for google, and facebook.  This also binds the mocks for servlet
 * components
 * @author kguthrie
 */
public class OrgamaTestEnvModule extends AbstractModule {

	private final String emailAddress;
	private final String authServiceName;
	private final boolean authenticated;
	private final boolean admin;

	public OrgamaTestEnvModule(String emailAddress,
			String authServiceName,
			boolean authenticated,
			boolean admin) {
		this.emailAddress = emailAddress == null ? "" : emailAddress;
		this.authServiceName = authServiceName == null ? "" : authServiceName;
		this.authenticated = authenticated;
		this.admin = admin;
	}

	/**
	 * bind the elements of the test environment
	 */
	@Override
	protected void configure() {

		bind(OrgamaTestEnv.class).in(TestEagerSingleton.class);

		bindTestEnvConfigs();

		bindMocks();
	}

	/**
	 * Bind constants and other configurations for specific environments.  This
	 * Method should be overridden in extensions of the OrgamaTestEnvModule to
	 * produce different types of environments
	 */
	protected void bindTestEnvConfigs() {
		bindConstant()
				.annotatedWith(OrgamaTestEnv.Authenticated.class)
				.to(authenticated);

		bindConstant()
				.annotatedWith(OrgamaTestEnv.EmailAddress.class)
				.to(emailAddress);

		bindConstant()
				.annotatedWith(OrgamaTestEnv.Administrator.class)
				.to(admin);

		bindConstant()
				.annotatedWith(OrgamaTestEnv.AuthServiceName.class)
				.to(authServiceName);
	}

	protected void bindFacebookUserService() {

		bind(IFacebookUserService.class)
				.to(MockFacebookUserService.class)
				.in(TestSingleton.class);

	}

	/**
	 * bind all the mocked objects used in the tests
	 */
	protected void bindMocks() {

		bindCookieHandler();

		bind(HttpSession.class)
				.to(MockSerializingHttpSession.class)
				.in(TestSingleton.class);

        bind(MockServletRequestResponse.class)
                .in(TestSingleton.class);

        bind(HttpServletRequest.class)
                .to(MockServletRequestResponse.Request.class)
                .in(TestSingleton.class);

        bind(HttpServletResponse.class)
                .to(MockServletRequestResponse.Response.class)
                .in(TestSingleton.class);

		bind(IResourceLoader.class)
				.to(MockResourceLoader.class)
				.in(TestSingleton.class);

		bindFacebookUserService();
	}

	/**
	 * explicitely bind the cookie handler so that it can be overridden easily
	 */
	protected void bindCookieHandler() {

		bind(ICookieHandler.class)
				.to(MockCookieHandler.class)
				.in(TestSingleton.class);

	}

}
