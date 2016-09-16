// <copyright file="ErrorMessageRegistry.java" company="RookAndPawn">
// Copyright (c) 2011 All Rights Reserved, http://www.rookandpawn.com/
package org.orgama.client.except;

import org.orgama.shared.Logger;
import java.util.HashMap;

/**
 * Singleton class where sets of ErrorMessageLookups.  This class will be used 
 * by client side exceptions to translate error codes from modules.
 * @author kguthrie
 */
public class ErrorMessageRegistry {
    
    protected static boolean initialized = false;
    protected static HashMap<String, ErrorMessageLookup> lookups = null;
    
    /**
     * initialize the singleton.  Creates a new map
     */
    protected static void init() {
        
        if (initialized) {
            return;
        }
        
        lookups = new HashMap<String, ErrorMessageLookup>();
        
        initialized = true;
    }
    
    /**
     * adds another error message lookup to the list of those registered
     * @param lookup 
     */
    public static void registerLookup(ErrorMessageLookup lookup) {
        
        init();
        
        lookups.put(lookup.getModuleCode().toUpperCase(), lookup);
    }
    
    /**
     * get the localized error message for the given error code string.  This 
     * method will split up the given code from the 
     * @param code
     * @return 
     */
    public static String getMessageForCode(String code) {
        
        StringBuilder moduleCode = new StringBuilder();
        int errorNumber = 0;
        
        init();
        
        if ((code == null) || (code.length() < 3)) {
            Logger.error("Error code given: " + code + " is too short");
            return "Unknown Error: " + code;
        }
        
        for (char c : code.toCharArray()) {
            if (Character.isLetter(c)) {
                moduleCode.append(c);
            }
            else if (Character.isDigit(c)) {
                errorNumber *= 10;
                errorNumber += (int)(c - '0');
            }
        }
     
        return getMessageForCode(moduleCode.toString(), errorNumber);
    }
    
    /**
     * get the localized error message for the given module code and error 
     * number
     * @param moduleCode
     * @param errorNumber
     * @return 
     */
    public static String getMessageForCode(String moduleCode, int errorNumber) {
        String result = "";
        ErrorMessageLookup lookup = null;
        
        try {
            lookup = lookups.get(moduleCode.toUpperCase());
            result = lookup.getMessageForCode(errorNumber);
        }
        catch(Exception ex) {
            Logger.warn("Error getting message for error code " + moduleCode + 
                    errorNumber);
            result = "Error: " + moduleCode + errorNumber + 
                    ".  No localization found";
        }
        
        return result;
    }
    
}
