package org.orgama.server.auth.source;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.inject.Inject;
import com.google.inject.Provider;
import javax.servlet.http.HttpServletRequest;
import org.orgama.server.Orgama;
import org.orgama.shared.auth.model.AuthSourceInfo;
import org.orgama.shared.auth.source.AuthServiceName;

/**
 * contains the methods used to log into or query a user's google account. 
 * @author kguthrie
 */
public class GoogleAccountsAuthService implements IAuthService {
    
	private final Provider<HttpServletRequest> requestProvider;
	private final AuthSourceInfo authSourceInfo;
	
	@Inject
	public GoogleAccountsAuthService(
			Provider<HttpServletRequest> requestProvider) {
		this.requestProvider = requestProvider;
		authSourceInfo = new AuthSourceInfo("Google Accounts", 
				AuthServiceName.googleAccounts);
	}
	
    /**
     * gets an instance of the UserServiceFactory
     * @return 
     */
    public UserService getUserService() {
        return UserServiceFactory.getUserService();
    }
    
    /**
     * creates a login url from the given request URI.  This method creates a 
     * new instance of USerService on each use.
     * @param requestUri
     * @return 
     */
	@Override
    public String getSignInUrl() {
        return getUserService().createLoginURL(Orgama.getDomain());
    }
    
    /**
     * creates a logout url using the given userService from the given 
     * requestURI
     * @param requestUri
     * @param userService
     * @return 
     */
	@Override
    public String getSignOutUrl(String notUsed) {
        return getUserService().createLogoutURL(Orgama.getDomain());
    }

	@Override
	public AuthSourceInfo getInfo() {
		return authSourceInfo;
	}

	/**
	 * Determines from the Google User Service whether or not the user is 
	 * authenticated and returns null if the user is not authenticated and
	 * the user's google accounts email address if they are 
	 * @return 
	 */
	@Override
	public String getServiceSpecificUserId(String notUsed) {
		
		UserService userService = getUserService();
		
		if (!userService.isUserLoggedIn()) {
			return null;
		}
		
		User user = userService.getCurrentUser();
		
		return user.getUserId();
	}

	@Override
	public String getServiceSpecificSessionId() {
		return "NotUsed";
	}

}
