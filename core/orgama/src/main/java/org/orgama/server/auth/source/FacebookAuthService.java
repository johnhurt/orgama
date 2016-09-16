package org.orgama.server.auth.source;

import static org.orgama.server.auth.IFacebookUserService.*;

import com.google.inject.Inject;
import com.google.inject.Provider;
import javax.servlet.http.HttpServletRequest;
import org.orgama.server.Orgama;
import org.orgama.server.auth.IFacebookUserService;
import org.orgama.shared.auth.model.AuthSourceInfo;
import org.orgama.shared.auth.source.AuthServiceName;

/**
 * Auth service for using facebook oauth 2 service
 * @author kguthrie
 */
public class FacebookAuthService implements IAuthService {

	private final Provider<HttpServletRequest> requestProvider;
	private final AuthSourceInfo authSourceInfo;
	private final IFacebookUserService userService;
	
	@Inject
	public FacebookAuthService(
			Provider<HttpServletRequest> requestProvider, 
			IFacebookUserService userService) {
		this.requestProvider = requestProvider;
		authSourceInfo = new AuthSourceInfo("Facebook", 
				AuthServiceName.facebook);
		
		this.userService = userService;
		
	}
	
	@Override
	public AuthSourceInfo getInfo() {
		return authSourceInfo;
	}

	/**
	 * Get the signInUrl for facebook
	 * @return 
	 */
	@Override
	public String getSignInUrl() {
		return userService.getSignInUrl(Orgama.getDomain());
	}

	/**
	 * Get the sign out url for facebook
	 * @param accessToken
	 * @return 
	 */
	@Override
	public String getSignOutUrl(String accessToken) {
		return userService.getSignOutUrl(Orgama.getDomain(), accessToken);
	}

	/**
	 * Get the userId for the current user based on the given accessToken
	 * @param accessToken
	 * @return 
	 */
	@Override
	public String getServiceSpecificUserId(String accessToken) {
		return userService.getUserId(accessToken);
	}
	
	/**
	 * Get the sessionId specific to Facebook.  This is called the access token
	 * @return 
	 */
	@Override
	public String getServiceSpecificSessionId() {
		return userService.getAccessToken();
	}

}
