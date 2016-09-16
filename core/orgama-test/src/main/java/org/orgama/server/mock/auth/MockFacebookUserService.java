package org.orgama.server.mock.auth;

import com.google.inject.Inject;
import java.net.URLEncoder;
import java.util.Random;
import org.orgama.server.auth.IFacebookUserService;
import org.orgama.server.config.OrgamaTestEnv;
import org.orgama.shared.auth.source.AuthServiceName;

/**
 *
 * @author kguthrie
 */
public class MockFacebookUserService implements IFacebookUserService {

	private static final String APPLICATION_ID = "TestApplicationId";
	private static final String XSRF_TOKEN = "TestXsrfToken";
	
	private final String accessToken;
	private final String userId;
	
	private final String signInUrlPattern;
	private final String signOutUrlPattern;
	
	@Inject
	public MockFacebookUserService(OrgamaTestEnv env) {
		if (env.isAuthenticated() &&
				env.getAuthServiceName() != null &&
				env.getAuthServiceName().equals(AuthServiceName.facebook)) {
			accessToken = getRandomString(32);
			
			int hash = 56;

			for (int i = 0; i < env.getEmailAddress().length(); i++) {
				hash = (hash + env.getEmailAddress().charAt(i)*7) % 214413;
			}

			userId = Integer.toString(hash);
		}
		else {
			accessToken = null;
			userId = null;
		}
			
		signInUrlPattern = String.format(
				"../r/facebook/login?client_id=%s&" + 
						"redirect_uri=%%s&state=%%s",
				APPLICATION_ID);
		
		signOutUrlPattern = "../r/facebook/logout?next=%s&access_token=%s";
	}
	
	public String getSignInUrl(String redirectUri) {
		try {
			redirectUri = URLEncoder.encode(redirectUri, "UTF-8");
		}
		catch(Exception ex) {
			throw new RuntimeException("Url Encode exception", ex);
		}
		
		return String.format(signInUrlPattern, redirectUri, XSRF_TOKEN);
	}

	public String getSignOutUrl(String redirectUri, String accessToken) {
		try {
			redirectUri = URLEncoder.encode(redirectUri, "UTF-8");
		}
		catch(Exception ex) {
			throw new RuntimeException("Url Encode exception", ex);
		}
		
		return String.format(signOutUrlPattern, redirectUri, accessToken);
	}

	public String getAccessToken() {
		return accessToken;
	}

	public String getUserId(String accessToken) {
		if (this.accessToken != null && this.accessToken.equals(accessToken)) {
			return userId;
		}
		
		return null;
	}

	/**
	 * get a string of random characters of the given length
	 * @return 
	 */
	private String getRandomString(int length) {
		Random rand = new Random(System.currentTimeMillis());
		StringBuilder result = new StringBuilder();
		
		for (int i = 0; i < length; i++) {
			switch(rand.nextInt(3)) {
				case 0: {
					result.append((char)(rand.nextInt(26) + 'a'));
					break;
				}
				case 1: {
					result.append((char)(rand.nextInt(26) + 'A'));
					break;
				}
				case 2: {
					result.append(rand.nextInt(10));
					break;
				}
			}
		}
		
		return result.toString();
	}
	
}
