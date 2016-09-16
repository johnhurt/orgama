// <copyright file="GwtpFailureHandler.java" company="RookAndPawn">
// Copyright (c) 2011 All Rights Reserved, http://www.rookandpawn.com/
package org.orgama.client;

import com.gwtplatform.mvp.client.proxy.AsyncCallFailEvent;
import com.gwtplatform.mvp.client.proxy.AsyncCallFailHandler;
import org.orgama.shared.Logger;

/**
 * gwt-platform implementation of the proxy or async call handler
 * @author kguthrie
 */
public class GwtpFailureHandler implements AsyncCallFailHandler {

    /**
     * log the error
     * @param asyncCallFailEvent 
     */
    @Override
    public void onAsyncCallFail(AsyncCallFailEvent asyncCallFailEvent) {
        Throwable caught = asyncCallFailEvent.getCaught();
        Logger.error("Gwtp Proxy Error", caught);
    }
    
}
