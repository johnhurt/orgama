// <copyright file="ValidateEmailAddress.java" company="RookAndPawn">
// Copyright (c) 2011 All Rights Reserved, http://www.rookandpawn.com/
package org.orgama.shared.auth.action;

import org.orgama.shared.action.AbstractAction;

/**
 * Action to validate the given email address for the first part of the 
 * auth process
 * @author kguthrie
 */
public class ValidateEmailAddress 
        extends AbstractAction<ValidateEmailAddressResult> {
    
    private String emailAddress;

    /**
     * for serialization damnit
     */
    ValidateEmailAddress() {
    }
    
    public ValidateEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    /**
     * @return the emailAddress
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * @param emailAddress the emailAddress to set
     */
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

}
