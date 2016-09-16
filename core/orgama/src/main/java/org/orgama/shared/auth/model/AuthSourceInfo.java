// <copyright file="AuthSourceInfo.java" company="RookAndPawn">
// Copyright (c) 2011 All Rights Reserved, http://www.rookandpawn.com/

package org.orgama.shared.auth.model;

import java.io.Serializable;

/**
 * storage class for a source of authentication.  This contains the name of the
 * service, the required fields for the authentication page url, a c# style
 * formatted string representing the url, and urls for the authenticating site's
 * main page and page describing their open authentication service.
 * @author kguthrie
 */
public class AuthSourceInfo implements Serializable {
    
    private String serviceName;
    private String resourceName;

    /**
     * Tests the given fields and values against the set of required set of 
     * Auth fields
     * @return 
     */
    public static boolean testAuthFieldsForCoverage(
            AuthField[] requiredAuthFields,
            AuthField[] givenAuthFields, 
            String[] authFieldValues) {
        
        boolean result;
        
        for (int i = 0; i < requiredAuthFields.length; i++)
        {
            result = false;
            for (int j = 0; j < givenAuthFields.length; j++)
            {
                if ((requiredAuthFields[i] == givenAuthFields[j]) &&
                    (authFieldValues[j] != null) &&
                    (authFieldValues[j].trim().length() > 0)) {
                    result = true;
                    break;
                }
            }
            
            if (result)
            {
                continue;
            }
            
            return false;
        }
        
        return true;
    }
    
    /**
     * for serialization
     */
    AuthSourceInfo() {
        
    }
	
    /**
     * create the auth source with everything needed
     * @param serviceName
     * @param resurceName used with resource bundle to get logo images
     * @param authFields
     * @param authUrlPatern
     * @param authSiteMainUrl
     * @param authSiteOpenAuthUrl
     */
    public AuthSourceInfo(String serviceName, String resourceName) {
        this.serviceName = serviceName;
        this.resourceName = resourceName;
    }

    /**
     * @return the serviceName
     */
    public String getServiceName() {
        return serviceName;
    }
    
    /**
     * @return the resourceName
     */
    public String getResourceName() {
        return resourceName;
    }

}
