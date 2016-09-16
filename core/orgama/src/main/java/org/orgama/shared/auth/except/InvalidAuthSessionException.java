// <copyright file="InvalidAuthSessionException.java" company="RookAndPawn">
// Copyright (c) 2011 All Rights Reserved, http://www.rookandpawn.com/
package org.orgama.shared.auth.except;

/**
 * Exception indicating an invalid session Id
 * @author kguthrie
 */
public class InvalidAuthSessionException extends AuthException {
    public InvalidAuthSessionException() {
        super(AuthErrorCodes.invalidSession);
    }
}
