package org.orgama.server.auth.service;

import com.google.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import org.orgama.server.auth.source.FacebookAuthService;
import org.orgama.server.auth.source.IAuthService;
import org.orgama.server.auth.source.IAuthServiceProvider;
import org.orgama.shared.auth.source.AuthServiceName;

/**
 * 
 * @author kguthrie
 */
public class FacebookGoogleTstAuthServicesProvider
		implements IAuthServiceProvider {

	private final Map<String, IAuthService> services;

	@Inject
	public FacebookGoogleTstAuthServicesProvider(
			FacebookAuthService facebook, 
			GoogleAccountsTstAuthService google) {
		services = new HashMap<String, IAuthService>();
		services.put(AuthServiceName.googleAccounts, google);
		services.put(AuthServiceName.facebook, facebook);
	}
	
	
	@Override
	public Map<String, IAuthService> getAuthServices() {
		return services;
	}

}
