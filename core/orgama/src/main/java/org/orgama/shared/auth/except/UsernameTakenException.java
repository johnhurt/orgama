// <copyright file="EmailAddressTakenException.java" company="RookAndPawn">
// Copyright (c) 2011 All Rights Reserved, http://www.rookandpawn.com/
package org.orgama.shared.auth.except;

/**
 * Indicates that the username specified in registration is already taken
 * @author kguthrie
 */
public class UsernameTakenException extends AuthException {
    
    public UsernameTakenException() {
        super(AuthErrorCodes.usernameTaken);
    }
    
    public UsernameTakenException(Throwable cause) {
        super(AuthErrorCodes.usernameTaken, cause);
    }
    
}
