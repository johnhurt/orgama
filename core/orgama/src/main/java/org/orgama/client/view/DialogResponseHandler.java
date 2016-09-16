// <copyright file="DialogResponseHandler.java" company="RookAndPawn">
// Copyright (c) 2011 All Rights Reserved, http://www.rookandpawn.com/
package org.orgama.client.view;

import org.orgama.shared.enm.DialogResult;

/**
 * interface for an object that handles a dialog result 
 * @author kguthrie
 */
public interface DialogResponseHandler {
    public void handleResponse(DialogResult result);
}
