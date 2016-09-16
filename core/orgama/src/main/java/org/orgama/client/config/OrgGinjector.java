// <copyright file="OrgGinjector.java" company="RookAndPawn">
// Copyright (c) 2011 All Rights Reserved, http://www.rookandpawn.com/
package org.orgama.client.config;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.inject.client.Ginjector;
import com.gwtplatform.mvp.client.proxy.AsyncCallFailHandler;
import com.gwtplatform.mvp.client.proxy.PlaceManager;

/**
 *
 * @author kguthrie
 */
public interface OrgGinjector extends Ginjector {

    EventBus getEventBus();

    PlaceManager getPlaceManager();

    AsyncCallFailHandler getAsyncCallFailHandler();

    ClientSideConstants getAppStringConstants();
    
    
}
