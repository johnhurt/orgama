// <copyright file="InvalidApiKeyException.java" company="RookAndPawn">
// Copyright (c) 2011 All Rights Reserved, http://www.rookandpawn.com/
package org.orgama.shared.auth.except;

/**
 * Exception indicating an invalid api key
 * @author kguthrie
 */
public class InvalidApiKeyException extends AuthException {
    public InvalidApiKeyException() {
        super(AuthErrorCodes.invalidApiKey);
    }
    
    
    public InvalidApiKeyException(Throwable cause) {
        super(AuthErrorCodes.invalidApiKey, cause);
    }
}
