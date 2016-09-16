// <copyright file="AuthUtils.java" company="RookAndPawn">
// Copyright (c) 2011 All Rights Reserved, http://www.rookandpawn.com/
package org.orgama.shared.auth;

import com.google.gwt.regexp.shared.RegExp;
import org.orgama.shared.auth.except.InvalidEmailAddressFormatException;
import org.orgama.shared.auth.except.InvalidUsernameFormatException;

/**
 * Utility functions for the auth service
 * @author kguthrie
 */
public class AuthUtils {
    
    private static boolean isInitialized = false;;
    private static final Object syncLock = new Object();
    
    /**
     * this pattern allows only strings that are 6-32 characters long that have
     * a letter as the first character, and the rest either a letter or number
     */
    private static RegExp prettyUsernamePattern = null;
    
    /**
     * this pattern allows any valid email address
     */
    private static RegExp prettyEmailAddressPattern = null;
    
    /**
     * patterns for email addresses that cause problems when tested
     */
    private static RegExp problematicEmailPatterns = null;
    
    /**
     * thread-safe method for singleton initialization 
     */
    private static void init() {
        if (isInitialized) {
            return;
        }
        
        synchronized(syncLock) {
            if (isInitialized) {
                return;
            }
            
            initImpl();
            isInitialized = true;
        }
    }
    
    /**
     * non-threadsafe method for initializing this singleton.  This will
     * create the regular expressions used in these utility functions
     */
    private static void initImpl() {
        prettyUsernamePattern = RegExp.compile(
                "\\A\\p{Alnum}\\w{5,31}\\z");
        
        prettyEmailAddressPattern = RegExp.compile(
                "^(?:(?:[a-zA-Z0-9_-])+\\.?)+" + 
                "(?:\\+(?:(?:[a-zA-Z0-9_-])+\\.?)+)?@" + 
                "(?:(?:[a-zA-Z0-9_-])||(?:\\.))+\\.(?:[a-zA-Z]{2,5}){1,25}$");
        
        problematicEmailPatterns = RegExp.compile("^(?:\\p{Alnum}+)$");
    }
    
    /**
     * returns whether or not the string given can be used as a username
     * @param toBeValidated
     * @return 
     */
    public static boolean validateUsernameString(String toBeValidated) {
        init();
        
        return prettyUsernamePattern.test(toBeValidated);
    }
    
    /**
     * returns whether or not the email address given is valid
     * @param toBeValidated
     * @return 
     */
    public static boolean validateEmailAddress(String toBeValidated) {
        boolean result = false;
        
        init();
        
        result = !problematicEmailPatterns.test(toBeValidated);
        
        if (result) {
            result = prettyEmailAddressPattern.test(toBeValidated);
        }
        
        if (result) {
            result = toBeValidated.length() <= 128;
        }
        
        return result;
    }
    
    /**
     * takes a string, and returns a sanitized copy of the same string.  This 
     * means trimmed and  all lowercase.  If there are any special characters 
     * or any internal spaces or white space, an AuthException will be thrown
     * @param toSanitize
     * @return 
     */
    public static String sanitizeStringForUsername(String toSanitize) {
        
        String result = toSanitize;
        
        
        //check for null input
        if (result == null) {
            throw new InvalidUsernameFormatException();
        }
        
        result = result.trim();
        
        //check that the string length is not too long
        if (result.length() < 6) {
            throw new InvalidUsernameFormatException();
        }
        
        //check that the string is not too long
        if (result.length() > 32) {
            throw new InvalidUsernameFormatException();
        }
        
        //check to see if the string can be used for a username.  If it can't
        //throw an auth exception 
        if (!validateUsernameString(result)) {
            throw new InvalidUsernameFormatException();
        }
        
        result = result.toLowerCase();
        
        return result;
    }
    
    /**
     * Takes a string and if it is valid, it will be changed to all lower case
     * with and trims it.
     * @return 
     */
    public static String sanitizeEmailAddress(String toSanitize) {
        
        String result = toSanitize;
        
        //check for null input
        if (result == null) {
            throw new InvalidEmailAddressFormatException();
        }
        
        result = result.trim();
        
        //check that the string is not too long
        if (result.length() > 128) {
            throw new InvalidEmailAddressFormatException();
        }
        
        //check to see if the string can be used for a username.  If it can't
        //throw an auth exception 
        if (!validateEmailAddress(result)) {
            throw new InvalidEmailAddressFormatException();
        }
        
        result = result.toLowerCase();
        
        return result;
    }
    
}
