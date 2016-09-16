// <copyright file="OrgamaTstModule.java" company="RookAndPawn">
// Copyright (c) 2011 All Rights Reserved, http://www.rookandpawn.com/
package org.orgama.server.config;


import com.gwtplatform.dispatch.server.guice.DispatchServiceImpl;
import com.gwtplatform.dispatch.server.guice.HandlerModule;
import com.gwtplatform.dispatch.shared.DispatchService;
import static org.orgama.client.config.ClientSideConstants.*;

import com.gwtplatform.mvp.client.proxy.TokenFormatter;
import org.jukito.TestEagerSingleton;
import org.jukito.TestModule;
import org.jukito.TestSingleton;
import org.orgama.client.BetterTokenFormatter;
import org.orgama.client.annotation.DefaultPlace;
import org.orgama.server.Orgama;
import org.orgama.server.auth.ICoreSessionService;
import org.orgama.server.scope.TrueSingletonScope;
import org.orgama.server.annotation.TrueSingleton;
import org.orgama.server.auth.AuthBootstrapper;
import org.orgama.server.auth.AuthSessionService;
import org.orgama.server.auth.model.AuthInitialization;
import org.orgama.server.auth.AuthInitializationService;
import org.orgama.server.auth.AuthSystemCleaner;
import org.orgama.server.auth.AuthUserService;
import org.orgama.server.auth.ICoreUserService;
import org.orgama.server.auth.handler.InitiateRegistrationHandler;
import org.orgama.server.auth.handler.SignOutHandler;
import org.orgama.server.auth.handler.ValidateEmailAddressHandler;
import org.orgama.server.auth.service.FacebookGoogleTstAuthServicesProvider;
import org.orgama.server.auth.service.GoogleOnlyTstAuthSourceProvider;
import org.orgama.server.auth.source.IAuthServiceProvider;
import org.orgama.shared.auth.action.InitiateRegistration;
import org.orgama.shared.auth.action.SignOut;
import org.orgama.shared.auth.action.ValidateEmailAddress;
import org.orgama.shared.auth.model.AuthSession;



/**
 * Guice module for the test environment of the gwt auth lib
 * @author kguthrie
 */
public class OrgamaTstModule extends TestModule {

    public static final String title = "Unit Test Title";
    public static final String description = "Unit Test Description";
    public static final String keywords = "Unit Test Keywords";
    public static final String defaultPlace = "default";
    
    @Override
    protected void configureTest() {
		
		bindConstants();
		bindCommonComponents();
		bindAuthComponents();
		
		bindActionHandlers();
		
		bindTestEnvironment();
    }
    
	/**
	 * get the email address to use within the auth system
	 */
	protected String getEmailAddress() {
		return "test@unit.com";
	}
	
	/**
	 * get whether the user is authenticated in the test environment
	 * @return 
	 */
	protected boolean isAuthenticated() {
		return false;
	}
	
	/**
	 * returns whether the given user is authenticated as an 
	 * @return 
	 */
	protected boolean isAdmin() {
		return false;
	}
	
	/**
	 * get the name of the auth service that the user is authenticated with
	 */
	protected String getAuthServiceName() {
		return null;
	}
	
	/**
	 * Bind classes and systems specific to the testing environment.  
	 */
	protected void bindTestEnvironment() {
		
		//Bind the Tst logger so that the static Logger will use it
		
		install(new OrgamaTestEnvModule(
				getEmailAddress(), 
				getAuthServiceName(), 
				isAuthenticated(), 
				isAdmin()));
		
	}
	
	/**
	 * Bind the components of the auth system
	 */
	protected void bindAuthComponents() {
		
		bind(AuthSession.class)
				.toProvider(AuthSessionService.class)
				.in(TestSingleton.class);
		
		bind(AuthInitialization.class)
				.toProvider(AuthInitializationService.class)
				.in(TestSingleton.class);
		
		bind(AuthInitializationService.class)
				.annotatedWith(AuthInitializationService.RequireNew.class)
				.to(AuthInitializationService.NewRequired.class)
				.in(TestSingleton.class);
		
		bind(ICoreUserService.class)
				.to(AuthUserService.class)
				.in(TestSingleton.class);
		
		bind(ICoreSessionService.class)
				.to(AuthSessionService.class)
				.in(TestSingleton.class);
		
		bind(AuthSystemCleaner.class)
				.in(TestSingleton.class);
		
		bind(AuthBootstrapper.class)
				.in(TestSingleton.class);
		
		bindAuthSourceProvider();
	}
	
	/**
	 * single overridable method for testing against a certain auth source
	 * provider.  The default is google and facebook
	 */
	protected void bindAuthSourceProvider() {
		bind(IAuthServiceProvider.class)
				.to(FacebookGoogleTstAuthServicesProvider.class);
	}
	
	/**
	 * bind the components that are common to almost all testing environments
	 */
	protected void bindCommonComponents() {
		
		bind(Orgama.class).in(TestEagerSingleton.class);
		
		//bind the true singleton scope to an instance created right here
		TrueSingletonScope trueSingletonScope = new TrueSingletonScope();
		bindScope(TrueSingleton.class, trueSingletonScope);
		bind(TrueSingletonScope.class).toInstance(trueSingletonScope);
		
		//bind a token formatter
        bind(TokenFormatter.class).to(BetterTokenFormatter.class)
                .in(TestSingleton.class);
		
	}
	
	/**
	 * bind the constants needed for testing and some for testing the constant
	 * binding process
	 */
	protected void bindConstants() {
		
        //Constants
        bindConstant().annotatedWith(AppTitle.class).to(title);
        bindConstant().annotatedWith(AppDescription.class).to(description);
        bindConstant().annotatedWith(AppKeywords.class).to(keywords);
        bindConstant().annotatedWith(DefaultPlace.class).to(defaultPlace);
	}
	
	/**
	 * Bind the handlers needed for testing.  
	 */
	protected void bindActionHandlers() {
		
		bind(DispatchService.class).to(DispatchServiceImpl.class);
		
		install(new HandlerModule() {

			@Override
			protected void configureHandlers() {
				bindHandler(
						ValidateEmailAddress.class, 
						ValidateEmailAddressHandler.class);
				
				bindHandler(
						InitiateRegistration.class, 
						InitiateRegistrationHandler.class);
				
				bindHandler(
						SignOut.class, 
						SignOutHandler.class);
			}
		});
		
	}

}
