// <copyright file="OrgExceptionMessages.java" company="RookAndPawn">
// Copyright (c) 2011 All Rights Reserved, http://www.rookandpawn.com/
package org.orgama.client.i18n;

import com.google.gwt.i18n.client.Constants;

/**
 *
 * @author kguthrie
 */
public interface OrgExceptionMessages extends Constants {
    
    @DefaultStringValue("An error occurred while accessing data on the server")
    public String dataAccessError();
    
    @DefaultStringValue("An unkown error occurred on the server")
    public String unknownError();
	
    @DefaultStringValue("The session given was invalid or expired")
    public String invalidOrExpiredSession();
    
    @DefaultStringValue("The api key given was invalid or expired")
    public String invalidApiKey();
    
    @DefaultStringValue("The email address given is already in use")
    public String emailAddressAlreadyExists();
    
    @DefaultStringValue("The username specified already exists")
    public String usernameAlreadyExists();
    
    @DefaultStringValue("The email address given was invalid")
    public String invalidEmailAddressFormat();
    
    @DefaultStringValue("The username given was invalid")
    public String invalidUsernameFormat();
    
    @DefaultStringValue("Failed to create cookie")
    public String cookieCreationFailure();
    
    @DefaultStringValue("The domain given was invalid")
    public String invalidDomain();
    
    @DefaultStringValue("The authentication source given was invalid")
    public String invalidAuthSource();
    
    @DefaultStringValue("A field for required for authentication was missing")
    public String missingAuthField();
    
    @DefaultStringValue("The selected external account is already claimed by " + 
            "an existing user")
    public String externalAuthAccountClaimed();
    
    @DefaultStringValue("You are not authorized to perform this action")
    public String youAreNotAuthorizedToPerformThisAction();
}
