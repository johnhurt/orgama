// <copyright file="InvalidUsernameFormatException.java" company="RookAndPawn">
// Copyright (c) 2011 All Rights Reserved, http://www.rookandpawn.com/
package org.orgama.shared.auth.except;

/**
 * Exception indicating that the username given was not formatted correctly
 * @author kguthrie
 */
public class InvalidUsernameFormatException extends AuthException {
    
    public InvalidUsernameFormatException() {
        super(AuthErrorCodes.invalidUsernameFormat);
    }
    
    public InvalidUsernameFormatException(Throwable cause) {
        super(AuthErrorCodes.invalidUsernameFormat, cause);
    }
    
}
