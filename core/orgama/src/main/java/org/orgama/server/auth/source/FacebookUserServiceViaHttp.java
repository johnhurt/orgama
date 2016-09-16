package org.orgama.server.auth.source;

import static org.orgama.server.auth.IFacebookUserService.*;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Provider;
import java.net.URLEncoder;
import javax.servlet.http.HttpServletRequest;
import org.orgama.server.Orgama;
import org.orgama.server.auth.IFacebookUserService;
import org.orgama.shared.ICookieHandler;
import org.orgama.shared.Logger;
import org.orgama.shared.auth.except.AuthException;
import org.orgama.shared.except.OrgException;

/**
 * Facebook user service for use in devmode and production.  This will use the 
 * known oauth and facebook url patterns to get the accessToken from the code
 * which is presumed to be in the url as a param.  
 * @author kguthrie
 */
public class FacebookUserServiceViaHttp implements IFacebookUserService {

	public static final String APP_SECRET_ID_FILE = 
			"META-INF/facebook-app-secret-id.txt";
	public static final String DEFAULT_APP_ID = "ApplicationIdPlaceHolder";
	public static final String DEFAULT_APP_SECRET_ID = 
			"ApplicationSecretIdPlaceHolder";
	
	private final Provider<HttpServletRequest> requestProvider;
	private final ICookieHandler cookieHandler;
	
	private String accessTokenUrlPattern;
	private String meUrlPattern;
	private String signInUrlPattern;
	private String signOutUrlPattern;
	
	private String applicationId;
	
	private boolean initialized;
	
	@Inject
	public FacebookUserServiceViaHttp(
			Provider<HttpServletRequest> requestProvider, 
			ICookieHandler cookieHander) {
		this.requestProvider = requestProvider;
		this.cookieHandler = cookieHander;
		
		applicationId = DEFAULT_APP_ID;
		
		initialized = false;
	}

	/**
	 * ensures the urls used by this service are initialized
	 */
	private void init() {
		
		if (initialized) {
			return;
		}
		
		initImpl();
		
		initialized = true;
	}
	
	/**
	 * synchronized implementation of the initializion code
	 */
	private synchronized void initImpl() {
		if (initialized) {
			return;
		}
		
		String applicationSecretId = Orgama.loadFileFromAppRoot(
				APP_SECRET_ID_FILE);
		
		if (applicationSecretId == null) {
			applicationSecretId = DEFAULT_APP_SECRET_ID;
		}
		
		//Create sign-in and sign-out url patterns based on whether this is
		//production or development.  The local mock facebook service is used
		//for development while the real facebook urls
		if (!Orgama.isProduction()) {
			accessTokenUrlPattern = String.format(
					"%s/r/facebook/access_token" +
							"?client_id=%s&redirect_uri=%%s" +
							"&client_secret=%s&code=%%s",
					Orgama.getDomain(),	applicationId, applicationSecretId);
			
			meUrlPattern = String.format("%s/r/facebook/me?access_token=%%s",
					Orgama.getDomain());
			
			signInUrlPattern = String.format(
					"../r/facebook/login?client_id=%s&" + 
							"redirect_uri=%%s&state=%%s",
					applicationId);
			signOutUrlPattern = "../r/facebook/logout?next=%s&access_token=%s";
		}
		else {
			accessTokenUrlPattern = String.format(
					"https://graph.facebook.com/oauth/access_token" +
							"?client_id=%s&redirect_uri=%%s" +
							"&client_secret=%s&code=%%s",
					applicationId, applicationSecretId);
			
			meUrlPattern = "https://graph.facebook.com/me?access_token=%s";
			
			signInUrlPattern = String.format(
					"https://www.facebook.com/dialog/oauth?client_id=%s&" + 
							"redirect_uri=%%s&state=%%s",
					applicationId);
			
			signOutUrlPattern = 
					"https://www.facebook.com/logout.php?next=%s&access_token=%s";
		}
	}
	
	/**
	 * Used to optionally inject the application id into the user service
	 * @param applicationId 
	 */
	@Inject(optional = true)
	public void setApplicationId(@ApplictionId String applicationId) {
		this.applicationId = applicationId;
	}
	
	/**
	 * Get the signInUrl for facebook
	 * @return 
	 */
	@Override
	public String getSignInUrl(String redirectUri) {
		init();
		
		if (!redirectUri.endsWith("/")) {
			redirectUri += "/";
		}
		
		try {
			redirectUri = URLEncoder.encode(redirectUri, "UTF-8");
		}
		catch(Exception ex) {
			throw new OrgException("Error encoding redirect uri", ex);
		}
			
		return String.format(signInUrlPattern, redirectUri, 
				cookieHandler.getValue("JSESSIONID"));
	}

	/**
	 * Get the sign out url for facebook
	 * @param accessToken
	 * @return 
	 */
	@Override
	public String getSignOutUrl(String redirectUri, String accessToken) {
		init();
		
		if (!redirectUri.endsWith("/")) {
			redirectUri += "/";
		}
		
		try {
			redirectUri = URLEncoder.encode(redirectUri, "UTF-8");
		}
		catch(Exception ex) {
			throw new OrgException("Error encoding redirect uri", ex);
		}
			
		return String.format(signOutUrlPattern, redirectUri, accessToken);
	}

	/**
	 * Get the access token by performing a url get on the configured url
	 * resource for exchanging code for access token.
	 * @return 
	 */
	@Override
	public String getAccessToken() {
		
		init();
		
		HttpServletRequest request = requestProvider.get();
		String code = request.getParameter("code");
		String state = request.getParameter("state");
		
		if (Strings.isNullOrEmpty(code)) {
			Logger.error(
					"No code parameter was found in the request url");
			return null;
		}
		
		if (Strings.isNullOrEmpty(state) 
				|| !state.equals(cookieHandler.getValue("JSESSIONID"))) {
			Logger.error("Mismatch in oauth state variable.  This " +
					"could be due to a XSRF attack");
			return null;
		}

		Logger.info("Making a request to facebook for accessToken");

		String redirectUri = Orgama.getDomain();

		if (!redirectUri.endsWith("/")) {
			redirectUri += "/";
		}

		try {
			redirectUri = URLEncoder.encode(redirectUri, "UTF-8");
		}
		catch(Exception ex) {
			throw new OrgException("Error encoding redirect uri", ex);
		}

		String result = Orgama.loadFromUrl(
				String.format(accessTokenUrlPattern, redirectUri, code));

		Logger.info("Request to facebook returned");

		if (Strings.isNullOrEmpty(result)) {
			Logger.error("Empty result when trying to get " +
					"access token from facebook");
			return null;
		}

		result = getStringBetween(result, "access_token=", "&");

		//No access token in response from server means user not logged in
		if (Strings.isNullOrEmpty(result)) {
			return null;
		}

		return result;
	}

	
	/**
	 * Get the id of the user from the facebook person service.  
	 * @param accessToken
	 * @return 
	 */
	@Override
	public String getUserId(String accessToken) {
		
		init();
		
		Logger.info("Making a request to facebook for user id");

		String result = Orgama.loadFromUrl(
				String.format(meUrlPattern, accessToken));

		Logger.info("Request to facebook returned");

		if (Strings.isNullOrEmpty(result)) {
			Logger.error("Empty result when trying to get " +
					"access token from facebook");
			return null;
		}

		result = getStringBetween(result, "\"id\":\"", "\"");

		//No user id in response from server means user not logged in
		if (Strings.isNullOrEmpty(result)) {
			return null;
		}

		return result;
	}

    
    /**
     * returns the string between the first occurrence of the before string 
     * and the first occurrence of the after string following the before string.
     * Null is returned if no instances of before are found.  If an instance
     * of before is found, but no instance of after is found, then all contents
     * following before are returned.  The same holds if after is null
     * @param toSearch
     * @param before
     * @param after
     * @return 
     */
    public String getStringBetween(String toSearch, 
            String before, String after) {
        int start;
        int end;
        String result;
        
        if (toSearch == null || before == null) {
            return null;
        }
        
        if ((start = toSearch.indexOf(before)) < 0) {
            return null;
        }
        
        start += before.length();
        
        if (start == toSearch.length()) {
            return "";
        }
        
        if (after == null || 
                ((end = toSearch.indexOf(after, start)) < 0)) {
            result = toSearch.substring(start);
        }
        else {
            result = toSearch.substring(start, end);
        }
        
        return result;
    }
    
}
