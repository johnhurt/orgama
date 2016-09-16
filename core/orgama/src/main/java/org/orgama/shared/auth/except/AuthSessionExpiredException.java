// <copyright file="InvalidSessionException.java" company="RookAndPawn">
// Copyright (c) 2011 All Rights Reserved, http://www.rookandpawn.com/
package org.orgama.shared.auth.except;

/**
 * Exception indicating an invalid session Id
 * @author kguthrie
 */
public class AuthSessionExpiredException extends AuthException {
    public AuthSessionExpiredException() {
        super(AuthErrorCodes.sessionExpired);
    }
}
