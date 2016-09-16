// <copyright file="AuthException.java" company="RookAndPawn">
// Copyright (c) 2011 All Rights Reserved, http://www.rookandpawn.com/

package org.orgama.shared.auth.except;

import org.orgama.shared.except.ExceptionByErrorCode;

/**
 * exception specific to mvp auth lib
 * @author kguthrie
 */
public class AuthException extends ExceptionByErrorCode {

    /**
     * create an Auth Exception with a message and cause
     * @param message
     * @param cause 
     */
    public AuthException(int errorCode, Throwable cause) {
        super(AuthErrorCodes.moduleCode, errorCode, cause);
    }

    /**
     * create an AuthException with just a message
     * @param message 
     */
    public AuthException(int errorCode) {
        super(AuthErrorCodes.moduleCode, errorCode);
    }
    
    /**
     * create a new unknown auth exception with this essage
     * @param message 
     */
    public AuthException(String message) {
        super(AuthErrorCodes.moduleCode, AuthErrorCodes.unknownError, 
                message);
    }
    
    /**
     * create a new 
     * @param message
     * @param cause 
     */
    public AuthException(String message, Throwable cause) {
        super(AuthErrorCodes.moduleCode, AuthErrorCodes.unknownError, 
                message, cause);
    }

}
