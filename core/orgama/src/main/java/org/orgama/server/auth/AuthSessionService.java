package org.orgama.server.auth;

import com.google.common.base.Strings;
import com.google.gdata.util.common.util.Base64;
import com.google.inject.Inject;
import com.google.inject.Provider;
import java.security.SecureRandom;
import java.util.Date;
import javax.servlet.http.HttpSession;
import org.orgama.server.Ofy;
import org.orgama.server.auth.model.AuthInitialization;
import org.orgama.shared.ICookieHandler;
import org.orgama.server.config.ServerSideConstants;
import org.orgama.shared.Logger;
import org.orgama.shared.auth.except.AuthException;
import org.orgama.shared.auth.except.AuthSessionExpiredException;
import org.orgama.shared.auth.except.InvalidAuthSessionException;
import org.orgama.shared.auth.model.AuthSession;
import org.orgama.shared.auth.model.AuthUser;
import org.orgama.shared.except.OrgamaCoreException;

/**
 * Provider for auth sessions
 * @author kguthrie
 */
public class AuthSessionService implements ICoreSessionService {

    //length of the new eession key strings that are created
    private static final int sessionKeyLength = 256;

	private final java.security.SecureRandom rand;
	
	private final Provider<HttpSession> sessionProvider;
	private final ServerSideConstants serverSideConstants;
	private final Provider<ICookieHandler> cookieHandlerProvider;
	
	@Inject
	public AuthSessionService(Provider<HttpSession> sessionProvider, 
			Provider<ICookieHandler> cookieHandlerProvider, 
			ServerSideConstants serverSideConstants) {
		
		Ofy.register(AuthSession.class);
		
		this.sessionProvider = sessionProvider;
		this.cookieHandlerProvider = cookieHandlerProvider;
		this.serverSideConstants = serverSideConstants;
		rand = new SecureRandom(new Date().toString().getBytes());
	}
	
	/**
	 * create a new auth session key
	 * @return 
	 */
    public synchronized String createSessionIdString() {
        byte[] sessionBytes = new byte[sessionKeyLength];
		rand.nextBytes(sessionBytes);
		try {
			return Base64.encode(sessionBytes);
		}
		catch (Exception ex) {
			throw new AuthException("Failed to create session string", ex);
		}
    }
    
	/**
	 * provides the AuthSession in a simple way.  If there is no auth session, 
	 * then null is returned.  
	 * @return 
	 */
	@Override
	public AuthSession get() {
		AuthSession result = getImpl();
		
		if (result != null) {
			validateAuthSession(result);
		}
		
		return result;
	}
	
	/**
	 * Get the instance of the auth session for the current user if there is 
	 * one.
	 */
	private AuthSession getImpl() {
		
		try {
			HttpSession httpSession = sessionProvider.get();

			AuthSession authSession = (AuthSession)httpSession.getAttribute(
					serverSideConstants.getAuthSessionKey());

			if (authSession != null) {
				return authSession;
			}

			ICookieHandler coookieHandler = cookieHandlerProvider.get();

			String authSessionId = coookieHandler.getValue(
					serverSideConstants.getSessionCookieName());

			if (Strings.isNullOrEmpty(authSessionId)) {
				return null;
			}

			authSession = getById(authSessionId);

			if (authSession == null) {
				return null;
			}

			httpSession.setAttribute(
					serverSideConstants.getAuthSessionKey(), authSession);

			return authSession;
		}
		catch(Exception ex) {
			throw new OrgamaCoreException(
					"Error getting Auth Session", ex);
		}
	}
	
	/**
	 * Validate the auth session to make sure the id matches what's in the 
	 * cookie and has not expired.  Any errors that occur during validation will
	 * raise exceptions with informational types
	 * @param authSession AuthSession to validate
	 */
	void validateAuthSession(AuthSession authSession) {
		
		HttpSession httpSession = sessionProvider.get();
		
		//Is the session expired?   If so close it with the manager and remove 
		//it from the httpSession
		if (authSession.isClosed()) {
			close(authSession);
			httpSession.removeAttribute(
					serverSideConstants.getSessionCookieName());
			throw new AuthSessionExpiredException();
		}
		
		String authSessionId = cookieHandlerProvider.get().getValue(
				serverSideConstants.getSessionCookieName());
		
		//Does the auth session's id match the cookie in the request?
		if ((authSessionId == null) ||
				(!authSessionId.equals(authSession.getSessionId()))) {
			close(authSession);
			throw new InvalidAuthSessionException();
		}
	}
	
	/**
	 * Create a new auth session for the given user
	 * @param user
	 * @return 
	 */
	@Override
	public AuthSession create(AuthUser user, AuthInitialization AuthInit) {
		
		AuthSession result = new AuthSession();
		Date expirationDate = new Date((new Date()).getTime() + 
				1000L * 3600L * 24L * 30L);
		
		result.setUserId(user.getUserId());
		result.setAuthServiceName(user.getAuthServiceName());
		result.setExpirationDate(expirationDate);
		result.setServiceSpecificSessionId(
				AuthInit.getServiceSpecificSessionId());
		
		boolean success = false;
		
		for (int i = 0; i < 10; i++) {
			result.setSessionId(createSessionIdString());
			try {
				Ofy.save().entity(result);
				success = true;
				break;
			}
			catch(Exception ex) {
				Logger.warn("Error saving auth session", ex);
				//happens on duplicate session id (not often)
			}
		}
		
		if (!success) {
			throw new AuthException("Failed to create auth session");
		}
		
		HttpSession session = sessionProvider.get();
		ICookieHandler cookieHandler = cookieHandlerProvider.get();
		session.setAttribute(serverSideConstants.getAuthSessionKey(), result);
		cookieHandler.setValue(serverSideConstants.getSessionCookieName(), 
				result.getSessionId(), expirationDate);
		
		return result;
	}
	
	/**
	 * Get the auth session corresponding to the given id.  If none exists, null
	 * is returned.  If the session exists, but the expiration date is passed, 
	 * then it is removed from the datastore and null is returned
	 * @param id
	 * @return 
	 */
	public AuthSession getById(String id) {
		return Ofy.load().type(AuthSession.class).id(id).get();
	}
	
	/**
	 * Close the given auth session by marking it as expired and deleting it
	 * from the datastore
	 * @param session 
	 */
	@Override
	public void close(AuthSession authSession) {
		
		Ofy.delete().entity(authSession).now(); 
		HttpSession session = sessionProvider.get();
		ICookieHandler cookieHandler = cookieHandlerProvider.get();
		session.removeAttribute(serverSideConstants.getAuthSessionKey());
		cookieHandler.deleteValue(serverSideConstants.getSessionCookieName());
		
	}

	/**
	 * Close the current session
	 */
	@Override
	public void closeCurrent() {
		AuthSession session = get();
		if (session != null) {
			close(session);
		}
	}
	
}
