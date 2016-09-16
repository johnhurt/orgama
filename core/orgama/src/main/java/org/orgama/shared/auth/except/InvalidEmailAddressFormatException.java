// <copyright file="InvalidUsernameFormatException.java" company="RookAndPawn">
// Copyright (c) 2011 All Rights Reserved, http://www.rookandpawn.com/
package org.orgama.shared.auth.except;

/**
 * Exception indicating the format of the given email address was invalid
 * @author kguthrie
 */
public class InvalidEmailAddressFormatException extends AuthException {
    
    public InvalidEmailAddressFormatException() {
        super(AuthErrorCodes.invalidEmailAddressFormat);
    }
    
    public InvalidEmailAddressFormatException(Throwable cause) {
        super(AuthErrorCodes.invalidEmailAddressFormat, cause);
    }
    
}
