// <copyright file="OrgGinModule.java" company="RookAndPawn">
// Copyright (c) 2011 All Rights Reserved, http://www.rookandpawn.com/

package org.orgama.client.config;

import com.google.inject.Singleton;
import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;
import com.gwtplatform.mvp.client.proxy.AsyncCallFailHandler;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.TokenFormatter;
import org.orgama.client.*;
import org.orgama.client.presenter.RootPresenter;
import org.orgama.client.view.BlockUi;
import org.orgama.client.view.RootView;
import org.orgama.client.auth.CompleteAuthState;
import org.orgama.client.presenter.AuthInfoWidgetPresenter;
import org.orgama.client.presenter.AuthSourceSelectionDialogPresenter;
import org.orgama.client.presenter.SignOutDialogPresenter;
import org.orgama.client.view.AuthInfoWidgetView;
import org.orgama.client.view.AuthSourceSelectionDialogView;
import org.orgama.client.view.SignOutDialogView;
import org.orgama.shared.IRedirector;
import org.orgama.shared.auth.model.ICompleteAuthState;

/**
 * Orgama gin module.  This binds all the components that the gwt client side
 * application uses.
 * @author kguthrie
 */
public class OrgGinModule extends AbstractPresenterModule {

    @Override
    protected final void configure() {
		
		bindConstants();
		bindLogger();
		
		bindPresenters();
		bindWidgets();
		
		bindGwtComponents();
		bindGwtpComponents();
		bindAuthComponents();
		
    }
	
	protected void bindConstants() {
		
	}
	
	protected void bindPresenters() {
		
        bind(RootPresenter.class).asEagerSingleton();
        bind(RootPresenter.Display.class).to(RootView.class).in(
                Singleton.class);
        
        bind(BlockUi.class).asEagerSingleton();
	}
	
	protected void bindWidgets() {
		
		bind(BlockUi.class).in(Singleton.class);
		
		bindPresenterWidget(
				AuthInfoWidgetPresenter.class,
				AuthInfoWidgetPresenter.Display.class, 
				AuthInfoWidgetView.class);
		
		bindPresenterWidget(
				AuthSourceSelectionDialogPresenter.class,
				AuthSourceSelectionDialogPresenter.Display.class,
				AuthSourceSelectionDialogView.class);
		
		bindPresenterWidget(
				SignOutDialogPresenter.class, 
				SignOutDialogPresenter.Display.class, 
				SignOutDialogView.class);
		
	}
	
	protected void bindLogger() {
		
	}
	
	protected void bindGwtComponents() {
		bind(IRedirector.class)
				.to(Redirector.class);
		
		bind(com.google.gwt.event.shared.EventBus.class)
				.toProvider(EventBus.Provider.class);
				
		
	}
	
	protected void bindGwtpComponents() {
		
        bind(TokenFormatter.class).to(BetterTokenFormatter.class).in(
                Singleton.class);
        
        
        bind(AsyncCallFailHandler.class).to(GwtpFailureHandler.class).in(
                Singleton.class);
        bind(PlaceManager.class).to(BasePlaceManager.class).in(Singleton.class);
        bind(Dispatch.class).asEagerSingleton();
	}
	
	protected void bindAuthComponents() {
		
        bind(ICompleteAuthState.class)
				.to(CompleteAuthState.class)
				.asEagerSingleton();
        
	}
	
}
