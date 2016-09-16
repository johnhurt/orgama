package org.orgama.server.config;

import com.google.inject.Singleton;
import com.google.inject.servlet.RequestScoped;
import com.google.inject.servlet.SessionScoped;
import com.gwtplatform.dispatch.server.guice.DispatchServiceImpl;
import com.gwtplatform.dispatch.shared.ActionImpl;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;
import org.orgama.server.DefaultResourceLoader;
import org.orgama.server.IResourceLoader;
import org.orgama.server.Orgama;
import org.orgama.server.ServerSideCookieHandler;
import org.orgama.server.annotation.MainLoggerName;
import org.orgama.server.dummy.DummyServlet;
import org.orgama.shared.Logger;
import org.orgama.server.scope.TrueSingletonScope;
import org.orgama.server.annotation.TrueSingleton;
import org.orgama.server.auth.AuthBootstrapper;
import org.orgama.server.auth.AuthInitializationService;
import org.orgama.server.auth.AuthSessionService;
import org.orgama.server.auth.AuthSystemCleaner;
import org.orgama.server.auth.AuthUserService;
import org.orgama.server.auth.ICoreSessionService;
import org.orgama.server.auth.ICoreUserService;
import org.orgama.server.auth.IFacebookUserService;
import org.orgama.server.auth.handler.InitiateRegistrationHandler;
import org.orgama.server.auth.handler.SignOutHandler;
import org.orgama.server.auth.handler.ValidateEmailAddressHandler;
import org.orgama.server.auth.model.AuthInitialization;
import org.orgama.server.auth.source.FacebookUserServiceViaHttp;
import org.orgama.shared.ICookieHandler;
import org.orgama.shared.auth.action.InitiateRegistration;
import org.orgama.shared.auth.action.SignOut;
import org.orgama.shared.auth.action.ValidateEmailAddress;

/**
 * Orgama extension module for the core of the orgama server side code base.  
 * Even though this is technically and extension, it is really the core of 
 * the system.
 * @author kguthrie
 */
public class OrgamaServerCoreModule extends BaseExtensionModule {

	/**
	 * Add the core handler registrations
	 */
	@Override
	protected void addHandlerBindings() {
		bindHandler(ValidateEmailAddress.class, 
				ValidateEmailAddressHandler.class);
		
		bindHandler(InitiateRegistration.class,
				InitiateRegistrationHandler.class);
		
		bindHandler(SignOut.class, 
				SignOutHandler.class);
	}

	/**
	 * Add the core server bindings
	 */
	@Override
	protected void addServerBindings() {
		
		bindScopes();
		bindServlets();
		
		bindConstants();
		bindLogger();
		
		bindServerCore();
		bindAuthSystem();
		
	}
	
	protected void bindConstants() {
		bind(ServerSideConstants.class).in(TrueSingleton.class);
	}
	
	protected void bindScopes() {
		//bind the true singleton scope to an instance created right here
		TrueSingletonScope trueSingletonScope = new TrueSingletonScope();
		bindScope(TrueSingleton.class, trueSingletonScope);
		bind(TrueSingletonScope.class).toInstance(trueSingletonScope);
	}

	protected void bindServlets() {
		
		//bind the dummy service 
        bind(DummyServlet.class);
        
        //This is a special case for app engine, but it shouldn't affect 
        //projects not running on app engine
        serve("/_ah/warmup").with(DummyServlet.class);
        
        serve("/r/*").with(GuiceContainer.class);
        serve("/" + ActionImpl.DEFAULT_SERVICE_NAME + "*").with(
                DispatchServiceImpl.class);
		
	} 
	
	protected void bindLogger() {
        //bind/configure the logger for serverside use
        bindConstant().annotatedWith(MainLoggerName.class).to("Main");
        bind(Logger.class).asEagerSingleton();
	}
	
	protected void bindAuthSystem() {
		
		bind(AuthInitialization.class)
				.toProvider(AuthInitializationService.class)
				.in(SessionScoped.class);
				
		bind(AuthInitializationService.class)
				.annotatedWith(AuthInitializationService.RequireNew.class)
				.to(AuthInitializationService.NewRequired.class)
				.in(RequestScoped.class);
		
		bind(AuthSystemCleaner.class)
				.in(RequestScoped.class);
		
		bind(AuthBootstrapper.class)
				.in(RequestScoped.class);
		
		bind(ICoreUserService.class)
				.to(AuthUserService.class)
				.in(TrueSingleton.class);
		
		bind(ICoreSessionService.class)
				.to(AuthSessionService.class)
				.in(TrueSingleton.class);
		
		bind(IFacebookUserService.class)
				.to(FacebookUserServiceViaHttp.class)
				.in(TrueSingleton.class);
	}
	
	protected void bindServerCore() {
		
		bind(Orgama.class).asEagerSingleton();
		
		bind(ICookieHandler.class)
				.to(ServerSideCookieHandler.class)
				.in(RequestScoped.class);
		
		bind(IResourceLoader.class)
				.to(DefaultResourceLoader.class)
				.in(Singleton.class);
	}
}
