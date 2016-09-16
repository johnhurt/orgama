// <copyright file="ExceptionHandler.java" company="RookAndPawn">
// Copyright (c) 2011 All Rights Reserved, http://rookandpawn.com/

package org.orgama.shared.except;

import org.orgama.shared.Logger;

/**
 * Error handler/reporter
 * @author Kevin Guthrie
 */
public class ErrorHandler {

    public void onFailure(Throwable e) {
        Logger.error(e.getMessage());
    }

}
