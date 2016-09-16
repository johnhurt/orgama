package org.orgama.server.auth.service;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.inject.Inject;
import com.google.inject.Provider;
import javax.servlet.http.HttpServletRequest;
import org.orgama.server.auth.source.GoogleAccountsAuthService;

/**
 * Basically the same as the google accounts provider, but provides the user's
 * email address as it's external user id.
 * @author kguthrie
 */
public class GoogleAccountsTstAuthService 
		extends GoogleAccountsAuthService {

	@Inject
	public GoogleAccountsTstAuthService(
			Provider<HttpServletRequest> requestProvider) {
		super(requestProvider);
	}

	/**
	 * Returns null if the user is not logged in, but returns the email
	 * address (of the account) if the user is logged in
	 * @return 
	 */
	@Override
	public String getServiceSpecificUserId(String notUsed) {
		
		UserService userService = getUserService();
		
		if (!userService.isUserLoggedIn()) {
			return null;
		}
		
		String result = super.getServiceSpecificUserId(notUsed);
		
		if (result == null) {
			User user = userService.getCurrentUser();
		
			result = user.getEmail();
		}
		
		return result;
	}

}
