// <copyright file="AuthErrorCodes.java" company="RookAndPawn">
// Copyright (c) 2011 All Rights Reserved, http://www.rookandpawn.com/
package org.orgama.shared.auth.except;

/**
 * Singleton containing an enumeration of all the auth errors
 * @author kguthrie
 */
public class AuthErrorCodes {
    
    public static final String moduleCode = "AUTH";
    public static final int moduleCodeStringLength = moduleCode.length();
    
    public static final int unknownError = 0;
    public static final int invalidSession = 1;
    public static final int invalidApiKey = 2;
    public static final int emailAddressTaken = 3;
    public static final int usernameTaken = 4;
    public static final int invalidUsernameFormat = 5;
    public static final int invalidEmailAddressFormat = 6;
    public static final int invalidDomain = 7;
    public static final int domainMismatch = 8;
    public static final int cookieCreationFailure = 9;
    public static final int invalidAuthSource = 10;
    public static final int missingAuthField = 11;
    public static final int externalAuthAccountClaimed = 12;
    public static final int unauthorized = 13;
	public static final int sessionExpired = 14;
	public static final int alreadyAuthenticated = 15;
    
    /**
     * test the given full error code to see if it matches the auth module
     * error number given
     * @param fullErrorCode
     * @param errorNumber
     * @return 
     */
    public static boolean compare(String fullErrorCode, int errorNumber)
    {
        if (fullErrorCode == null)
        {
            return false;
        }
        
        if (fullErrorCode.length() < (moduleCodeStringLength + 1)) {
            return false;
        }
        
        return fullErrorCode.equalsIgnoreCase(moduleCode + errorNumber);
    }
    
}
