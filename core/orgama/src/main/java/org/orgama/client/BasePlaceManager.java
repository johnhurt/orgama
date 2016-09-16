// <copyright file="BasePlaceManager.java" company="RookAndPawn">
// Copyright (c) 2011 All Rights Reserved, http://www.rookandpawn.com/
package org.orgama.client;

import org.orgama.shared.Logger;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.History;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.proxy.PlaceManagerImpl;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.TokenFormatter;
import org.orgama.client.annotation.DefaultPlace;

/**
 * This is a simple base place manager for gwt apps.  the constructor is
 * injected and the default place revealed is assumed to be the page named
 * "main"
 * @author kguthrie
 */
public class BasePlaceManager extends PlaceManagerImpl {

    PlaceRequest defaultPlace;
    
    @Inject
    public BasePlaceManager(EventBus eventBus, TokenFormatter tokenFormatter, 
            @DefaultPlace String defaultPlace) {
        super(eventBus, tokenFormatter);
        
        this.defaultPlace = new PlaceRequest(defaultPlace);
        
    }

    @Override
    public void revealDefaultPlace() {
        revealPlace(defaultPlace);
    }
    
    @Override
    public void revealErrorPlace(String invlidToken) {
        Logger.debug("error loading presenter for token " + invlidToken);
    }

	@Override
    public void revealCurrentPlace() {
        String currToken = History.getToken();
        while (currToken.endsWith("&")) {
            currToken = currToken.substring(0, currToken.length() - 1);
        }
        if (currToken.equals(History.getToken())) {
			History.fireCurrentHistoryState();
		}
		else {
			History.newItem(currToken, true);
		}
    }
    
}
