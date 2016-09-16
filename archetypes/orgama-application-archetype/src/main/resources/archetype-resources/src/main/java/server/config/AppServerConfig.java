package ${package}.server.config;

import org.orgama.server.auth.IFacebookUserService;
import org.orgama.server.auth.source.FacebookAndGoogleAuthServiceProvider;
import org.orgama.server.auth.source.IAuthServiceProvider;
import org.orgama.server.config.BaseExtensionModule;

/**
 * Configuration for the server side dependency injections.  Two methods are
 *
 */
public class AppServerConfig extends BaseExtensionModule {

	@Override
	protected void addHandlerBindings() {
		
	}

	@Override
	protected void addServerBindings() {
		bindAuthSourceProvider();
	}
	
	/**
	 * single overridable method for testing against a certain auth source
	 * provider
	 */
	protected void bindAuthSourceProvider() {
		bind(IAuthServiceProvider.class)
				.to(FacebookAndGoogleAuthServiceProvider.class);
		
		bindConstant()
				.annotatedWith(IFacebookUserService.ApplictionId.class)
				.to("${facebook-app-id}");
	}
	
}
