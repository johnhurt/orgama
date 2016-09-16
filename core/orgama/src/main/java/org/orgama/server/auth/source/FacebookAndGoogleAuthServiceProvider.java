package org.orgama.server.auth.source;

import com.google.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import org.orgama.shared.auth.source.AuthServiceName;

/**
 * AuthService provider that provides auth services for google and facebook
 * @author kguthrie
 */
public class FacebookAndGoogleAuthServiceProvider 
		implements IAuthServiceProvider {

	private final Map<String, IAuthService> authServices;
	
	@Inject
	public FacebookAndGoogleAuthServiceProvider(
			FacebookAuthService facebook, 
			GoogleAccountsAuthService google) {
		authServices = new HashMap<String, IAuthService>();
		authServices.put(AuthServiceName.googleAccounts, google);
		authServices.put(AuthServiceName.facebook, facebook);
	}
	
	@Override
	public Map<String, IAuthService> getAuthServices() {
		return authServices;
	}

	
	
}
