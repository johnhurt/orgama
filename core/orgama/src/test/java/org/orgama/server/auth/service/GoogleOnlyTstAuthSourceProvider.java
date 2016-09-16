package org.orgama.server.auth.service;

import com.google.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import org.orgama.server.auth.source.IAuthServiceProvider;
import org.orgama.server.auth.source.IAuthService;
import org.orgama.shared.auth.source.AuthServiceName;

/**
 * Auth source provider that only provides the test google accounts auth service
 * @author kguthrie
 */
public class GoogleOnlyTstAuthSourceProvider implements IAuthServiceProvider {

	private final Map<String, IAuthService> authServices;
	
	@Inject
	public GoogleOnlyTstAuthSourceProvider(
			GoogleAccountsTstAuthService googleService) {
		authServices = new HashMap<String, IAuthService>();
		authServices.put(AuthServiceName.googleAccounts, googleService);
	}

	@Override
	public Map<String, IAuthService> getAuthServices() {
		return authServices;
	}

	
}
