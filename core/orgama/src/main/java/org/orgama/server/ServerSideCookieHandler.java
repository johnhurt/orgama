package org.orgama.server;

import com.google.inject.Inject;
import com.google.inject.Provider;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.orgama.shared.ICookieHandler;
import org.orgama.shared.except.OrgException;
import org.orgama.shared.except.OrgamaCoreException;

/**
 * Cookie handler for server side operations.  This class reads and updates the
 * cookies in the servlet request and response
 * @author kguthrie
 */
public class ServerSideCookieHandler implements ICookieHandler {

	private Provider<HttpServletRequest> requestProvider;
	private Provider<HttpServletResponse> responseProvider;
	private Map<String, Cookie> updatedCookies;
	
	@Inject
	public ServerSideCookieHandler(
			Provider<HttpServletRequest> requestProvider,
			Provider<HttpServletResponse> responseProvider) {
		this.requestProvider = requestProvider;
		this.responseProvider = responseProvider;
		updatedCookies = new HashMap<String, Cookie>();
	}
	
	/**
	 * Get the string value of the cookie stored in the request
	 * @param name
	 * @return 
	 */
	@Override
	public String getValue(String name) {
		Cookie resultCookie = getCookieFromRequest(name);
		if (resultCookie == null) {
			return null;
		}
		
		return resultCookie.getValue();
	}

	/**
	 * Set the value of the cookie with the given name to the given value, and
	 * set its expiration time to the default (session)
	 * @param name
	 * @param value
	 * @return 
	 */
	@Override
	public String setValue(String name, String value) {
		return setValue(name, value, null);
	}

	/**
	 * gets the actual cookie from the request based on the name.  If no cookie
	 * exists with the given name, then null is returned
	 * @return 
	 */
	private Cookie getCookieFromRequest(String name) {
		try {
			Cookie result = updatedCookies.get(name);
			
			if (result != null) {
				return result;
			}
			
			HttpServletRequest request = requestProvider.get();

			Cookie[] cookies = request.getCookies();
			
			if (cookies == null) {
				return null;
			}
			
			for(Cookie cookie : cookies) {
				if (cookie.getName().equalsIgnoreCase(name)) {
					return cookie;
				}
			}
			
			return null;
		}
		catch(Exception ex) {
			throw new OrgamaCoreException("Failed to get value for cookie: " +
					name, ex);
		}
	}

	/**
	 * set the value and expiration time of the cookie as given.  If the 
	 * expiration date is null, then the default will be used
	 * @param name
	 * @param value
	 * @param expirationDate
	 * @return 
	 */
	@Override
	public String setValue(String name, String value, Date expirationDate) {
		
		try {
			String result = null;
			Cookie cookie = null;
			HttpServletResponse response = responseProvider.get();

			if (response.isCommitted()) {
				throw new OrgamaCoreException(
						"Cookies cannot be added to the response after the " +
						"response has been committed");
			}
			
			Cookie existingCookie = getCookieFromRequest(name);
			
			if (existingCookie == null) {
				cookie = new Cookie(name, value);
			}
			else {
				result = existingCookie.getValue();
				cookie = (Cookie)existingCookie.clone();
				cookie.setValue(value);
			}
			
			if (expirationDate != null) {
				cookie.setMaxAge((int)((expirationDate.getTime() - 
						new Date().getTime()) / 1000L));
			}
			
			updatedCookies.put(name, cookie);
			response.addCookie(cookie);
			
			return result;
		}
		catch(OrgException ox) {
			throw ox;
		}
		catch(Exception ex) {
			throw new OrgamaCoreException(
					"Failed to set the value of cookie: " + name, ex);
		}
	}

	/**
	 * Set the value of the cookie with the given name to null and set it to 
	 * expire immediately 
	 * @param name
	 * @return 
	 */
	@Override
	public String deleteValue(String name) {
		return setValue(name, null, new Date());
	}
}
