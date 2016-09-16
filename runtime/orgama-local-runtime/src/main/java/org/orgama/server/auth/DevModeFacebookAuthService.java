package org.orgama.server.auth;

import com.google.gwt.thirdparty.guava.common.base.Strings;
import com.google.inject.Inject;
import java.net.URI;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.orgama.server.Orgama;

/**
 * 
 * @author kguthrie
 */
@Path("/facebook")
public class DevModeFacebookAuthService {

	private static final String cancelRedirectUrlPattern = 
			"%s?error_reason=user_denied" +
			"&error=access_denied" +
			"&error_description=The+user+denied+your+request" +
			"&state=%s";
	
	private final Map<String, User> usersByEmailAddress;
	private final Map<String, User> usersByCode;
	private final Map<String, User> usersByAccessToken;
	
	@Context
	HttpServletResponse response;
	
	@Inject
	public DevModeFacebookAuthService() {
		usersByEmailAddress = new ConcurrentHashMap<String, User>();
		usersByCode = new ConcurrentHashMap<String, User>();
		usersByAccessToken = new ConcurrentHashMap<String, User>();
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
	
	/**
	 * This is here to make sure everything is working with the restful service
	 * handler
	 * @return 
	 */
	@GET
	@Path("/")
	@Produces(MediaType.TEXT_PLAIN)
	public String defaultPath() {
		return "Hello, Fakebook";
	}
	
	/**
	 * Handle the /login uri.  This method will validate the client_id, 
	 * and return an html page for the user to enter their email address.
	 * @param clientId
	 * @param redirectUri
	 * @param state
	 * @return
	 * @throws Exception 
	 */
	@GET
	@Path("/login")
	@Produces(MediaType.TEXT_HTML)
	public String login(
			final @QueryParam("client_id") String clientId, 
			final @QueryParam("redirect_uri") String redirectUri,
			final @QueryParam("state") String state) 
					throws Exception {
		
		//Assert client id and redirect url are valid
		if (Strings.isNullOrEmpty(clientId)) {
			throw new RuntimeException("Client Id cannot be null or empty");
		}
		
		if (Strings.isNullOrEmpty(redirectUri)) {
			throw new RuntimeException(
					"Redirect Uri cannot be null or empty");
		}
		
		return Orgama.loadClassPathFile("html/login.html", 
				new HashMap<String, String>() {{
					put("client_id", clientId);
					put("redirect_uri", redirectUri);
					put("state", Strings.isNullOrEmpty(state) ? "null": state);
					put("cancel_redirect_url", 
							String.format(cancelRedirectUrlPattern, 
									redirectUri, state));
				}});
	}
	
	/**
	 * Mock up of the oauth system that returns a redirect 
	 * @param clientId
	 * @param redirectUrl
	 * @param state
	 * @param emailAddress
	 * @return
	 * @throws Exception 
	 */
	@GET
	@Path("/code")
	public Response code(
			final @QueryParam("client_id") String clientId, 
			final @QueryParam("redirect_uri") String redirectUri,
			final @QueryParam("state") String state,
			final @QueryParam("email_address") String emailAddress) 
					throws Exception {
		
		//Assert client id and redirect url are valid
		if (Strings.isNullOrEmpty(clientId)) {
			throw new RuntimeException("Client Id cannot be null or empty");
		}
		
		if (Strings.isNullOrEmpty(redirectUri)) {
			throw new RuntimeException(
					"Redirect Uri cannot be null or empty");
		}
		
		if (Strings.isNullOrEmpty(redirectUri)) {
			throw new RuntimeException(
					"Email address cannot be null or empty");
		}
		
		User user = new User();
		user.setCode(getRandomString(10));
		user.setEmailAddress(emailAddress);
		
		usersByEmailAddress.put(emailAddress, user);
		usersByCode.put(user.getCode(), user);
		
		String realRedirectUri;

		try {
			realRedirectUri = URLDecoder.decode(redirectUri, "UTF-8");
		}
		catch(Exception ex) {
			throw new RuntimeException("Error encoding redirect uri", ex);
		}

		
		String acturalRedirect = String.format(
				"%s?state=%s&code=%s", realRedirectUri, state, user.getCode());
		
		Response result = Response.temporaryRedirect(new URI(acturalRedirect)).build();
		
		return result;
	}
	
	/**
	 * Get the access token for the given code.  The code given must exist 
	 * already in the map of codes to users
	 * @param clientId
	 * @param redirectUri
	 * @param clientSecret
	 * @param code
	 * @return 
	 */
	@GET
	@Path("/access_token")
	@Produces(MediaType.TEXT_HTML)
	public String accessToken(
			final @QueryParam("client_id") String clientId,
			final @QueryParam("redirect_uri") String redirectUri,
			final @QueryParam("client_secret") String clientSecret, 
			final @QueryParam("code") String code) {
		
		
		//Assert client id and secret id are valid
		if (Strings.isNullOrEmpty(clientId)) {
			throw new RuntimeException("Client Id cannot be null or empty");
		}
		
		if (Strings.isNullOrEmpty(clientSecret)) {
			throw new RuntimeException("Secret Id cannot be null or empty");
		}
		
		if (Strings.isNullOrEmpty(code)) {
			throw new RuntimeException("code cannot be null or empty");
		}
		
		User user = usersByCode.get(code);
		
		if (user == null) {
			throw new RuntimeException("No user exists with the given code");
		}
		
		String accessToken = getRandomString(32);
		
		user.setAccessCode(accessToken);
		
		usersByAccessToken.put(accessToken, user);
		
		return String.format("access_token=%s&expires=%d", accessToken, 
				24*3600);
	}
	
	@GET
	@Path("/me")
	@Produces(MediaType.TEXT_HTML)
	public String me(final @QueryParam("access_token") String accessToken) 
			throws Exception {
		
		//Assert access token is valid
		if (Strings.isNullOrEmpty(accessToken)) {
			throw new RuntimeException("Access Token cannot be null or empty");
		}
		
		User user = usersByAccessToken.get(accessToken);
		
		if (user == null) {
			throw new RuntimeException(
					"No user exists with the given access token");
		}
		
		int hash = 56;
		
		for (int i = 0; i < user.getEmailAddress().length(); i++) {
			hash = (hash + user.getEmailAddress().charAt(i)*7) % 214413;
		}
		
		return String.format("{\"id\":\"%s\",\"emailAddress\":\"%s\"}", 
				Integer.toString(hash), user.getEmailAddress());
	}
	
	/**
	 * log the user that corresponds to the access code out of the fakebook
	 * @param next
	 * @param accessToken
	 * @return 
	 */
	@GET
	@Path("/logout")
	public Response logout(
			final @QueryParam("next") String next, 
			final @QueryParam("access_token") String accessToken)
					throws Exception {
	
		Response result;
		
		//Assert access token is valid
		if (Strings.isNullOrEmpty(accessToken)) {
			throw new RuntimeException("Access Token cannot be null or empty");
		}
		
		User user = usersByAccessToken.remove(accessToken);
		
		if (user == null) {
			throw new RuntimeException(
					"No user exists with the given access token");
		}
		
		usersByEmailAddress.remove(user.getEmailAddress());
		usersByCode.remove(user.getCode());
		
		result = Response.temporaryRedirect(new URI(next)).build();
		
		return result;
	}
	
	/**
	 * Storage for user
	 */
	public static class User {
		
		private String emailAddress;
		private String code;
		private String accessCode;
		
		public User() {
			
		}

		/**
		 * @return the emailAddress
		 */
		public String getEmailAddress() {
			return emailAddress;
		}

		/**
		 * @param emailAddress the emailAddress to set
		 */
		public void setEmailAddress(String emailAddress) {
			this.emailAddress = emailAddress;
		}

		/**
		 * @return the code
		 */
		public String getCode() {
			return code;
		}

		/**
		 * @param code the code to set
		 */
		public void setCode(String code) {
			this.code = code;
		}

		/**
		 * @return the accessCode
		 */
		public String getAccessCode() {
			return accessCode;
		}

		/**
		 * @param accessCode the accessCode to set
		 */
		public void setAccessCode(String accessCode) {
			this.accessCode = accessCode;
		}
		
	}
}
