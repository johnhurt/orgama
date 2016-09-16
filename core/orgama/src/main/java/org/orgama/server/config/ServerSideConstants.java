package org.orgama.server.config;


import com.google.inject.BindingAnnotation;
import com.google.inject.Inject;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;

/**
 * Constants 
 * @author kguthrie
 */
public class ServerSideConstants {
	
	/************ Binding annotations for application configurations *********/
	
	@BindingAnnotation @Target({ FIELD, PARAMETER, METHOD }) @Retention(RUNTIME)
	public static @interface SessionCookieName {}
	
	@BindingAnnotation @Target({ FIELD, PARAMETER, METHOD }) @Retention(RUNTIME)
	public static @interface AuthSessionKey {}
	
	@BindingAnnotation @Target({ FIELD, PARAMETER, METHOD }) @Retention(RUNTIME)
	public static @interface AuthInitializationKey {}
	
	@BindingAnnotation @Target({ FIELD, PARAMETER, METHOD }) @Retention(RUNTIME)
	public static @interface SessionLifetimeMillis {}
	
	/************* Default constants for Auth System ****************/
	private static final String DEFAULT_SESSION_COOKIE_NAME = "OGX";
	private static final String DEFAULT_AUTH_SESSION_KEY = "auth-session";
	private static final String DEFAULT_AUTH_INITIALIZATION_KEY = 
			"auth-initialization";
	private static final Long DEFAULT_SESSION_LIFETIME_MILLIS = 
			30L * 24L * 60L * 60L * 1000L; //30 days
	
	private String sessionCookieName = DEFAULT_SESSION_COOKIE_NAME;
	private String authSessionKey = DEFAULT_AUTH_SESSION_KEY;
	private String authInitializationKey = DEFAULT_AUTH_INITIALIZATION_KEY;
	private Long sessionLifetimeMillis = DEFAULT_SESSION_LIFETIME_MILLIS;
	
	/**
	 * @return the sessionCookieName
	 */
	public String getSessionCookieName() {
		return sessionCookieName;
	}

	/**
	 * @param sessionCookieName the sessionCookieName to set
	 */
	@Inject(optional = true)
	void setSessionCookieName(@SessionCookieName String sessionCookieName) {
		this.sessionCookieName = sessionCookieName;
	}

	/**
	 * @return the authSessionKey
	 */
	public String getAuthSessionKey() {
		return authSessionKey;
	}

	/**
	 * @param authSessionKey the authSessionKey to set
	 */
	@Inject(optional = true)
	void setAuthSessionKey(@AuthSessionKey String authSessionKey) {
		this.authSessionKey = authSessionKey;
	}

	/**
	 * @return the authInitializationKey
	 */
	public String getAuthInitializationKey() {
		return authInitializationKey;
	}

	/**
	 * @param authInitializationKey the authInitializationKey to set
	 */
	@Inject(optional = true)
	void setAuthInitializationKey(
			@AuthInitializationKey String authInitializationKey) {
		this.authInitializationKey = authInitializationKey;
	}

	/**
	 * @return the sessionLifetimeMillis
	 */
	public Long getSessionLifetimeMillis() {
		return sessionLifetimeMillis;
	}

	/**
	 * @param sessionLifetimeMillis the sessionLifetimeMillis to set
	 */
	@Inject(optional = true)
	void setSessionLifetimeMillis(
			@SessionLifetimeMillis Long sessionLifetimeMillis) {
		this.sessionLifetimeMillis = sessionLifetimeMillis;
	}
}
