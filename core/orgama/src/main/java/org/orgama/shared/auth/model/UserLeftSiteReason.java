/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orgama.shared.auth.model;

/**
 * enumeration of potential user left site reasons
 * @author kguthrie
 */
public enum UserLeftSiteReason {
    nil, 
    externalRegistration,
    externalAuthentication,
    deauthentication,
    redirect,
    pageClosed
}
