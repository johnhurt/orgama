// <copyright file="OrgamaTestEnv.java" company="RookAndPawn">
// Copyright (c) 2011 All Rights Reserved, http://www.rookandpawn.com/
package org.orgama.server.config;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.inject.BindingAnnotation;
import com.google.inject.Inject;
import com.googlecode.objectify.ObjectifyFilter;
import com.googlecode.objectify.ObjectifyService;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.METHOD;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;

/**
 * Static singleton class for setting up the testing environment
 *
 * @author kguthrie
 */
public class OrgamaTestEnv {


	@BindingAnnotation
	@Target({PARAMETER, METHOD})
	@Retention(RUNTIME)
	public static @interface EmailAddress {
	}

	@BindingAnnotation
	@Target({PARAMETER, METHOD})
	@Retention(RUNTIME)
	public static @interface Authenticated {
	}

	@BindingAnnotation
	@Target({PARAMETER, METHOD})
	@Retention(RUNTIME)
	public static @interface AuthServiceName {
	}

	@BindingAnnotation
	@Target({PARAMETER, METHOD})
	@Retention(RUNTIME)
	public static @interface Administrator {
	}
	
	private LocalServiceTestHelper appEngine = null;
	private boolean admin = false;
	private boolean authenticated = false;
	private String authServiceName = null;
	private String emailAddress = "test@unit.com";
	private String nickname = "TestUnit";
	private String fullName = "Test Unit";
	private String username = "testunit";
	private String domainName = "TestDomain";

	@Inject
	public OrgamaTestEnv(@EmailAddress String emailAddress,
			@Authenticated boolean authenticated,
			@AuthServiceName String authServiceName,
			@Administrator boolean admin) {

		this.authenticated = authenticated;
		this.admin = admin;
		this.emailAddress = emailAddress;
		this.authServiceName = authServiceName;

		String googleAccounts = 
				org.orgama.shared.auth.source.AuthServiceName.googleAccounts;
		
		boolean authenticatedWithGoogle = 
				authenticated && 
				authServiceName != null && 
				authServiceName.equals(googleAccounts);
				
		
		if (appEngine != null) {
			ObjectifyService.ofy().clear();
			ObjectifyFilter.complete();
			appEngine.tearDown();
		}

		appEngine = new LocalServiceTestHelper(
				new LocalDatastoreServiceTestConfig())
				.setEnvEmail(emailAddress)
				.setEnvIsLoggedIn(authenticatedWithGoogle)
				.setEnvAuthDomain(domainName)
				.setEnvIsAdmin(admin)
				.setSimulateProdLatencies(true);

		appEngine.setUp();
	}


	/**
	 * @return the admin
	 */
	public boolean isAdmin() {
		return admin;
	}

	/**
	 * @return the authenticated
	 */
	public boolean isAuthenticated() {
		return authenticated;
	}

	/**
	 * @return the emailAddress
	 */
	public String getEmailAddress() {
		return emailAddress;
	}
	
	/**
	 * @return the authServiceName
	 */
	public String getAuthServiceName() {
		return authServiceName;
	}
}
