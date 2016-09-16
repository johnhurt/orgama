// <copyright file="ValidateEmailAddressResult.java" company="RookAndPawn">
// Copyright (c) 2011 All Rights Reserved, http://www.rookandpawn.com/
package org.orgama.shared.auth.action;

import java.util.ArrayList;
import org.orgama.shared.action.AbstractResult;
import org.orgama.shared.auth.model.AuthSourceInfo;

/**
 * Result from validating the email address. This result will come back with
 * either the auth source for the existing username or a list of auth sources
 * for a non-existent username
 *
 * @author kguthrie
 */
public class ValidateEmailAddressResult extends AbstractResult {

	/**
	 * @return the responseCode
	 */
	public Code getResponseCode() {
		return responseCode;
	}

	/**
	 * Enumeration of response codes from email address validation. This does
	 * not include error responses. Error responses are returned as raised
	 * exceptions so the client can handle them as errors
	 */
	public static enum Code {

		nil,
		unknownEmailAddress,
		redirect
	}
	private String emailAddress;
	private ArrayList<AuthSourceInfo> authSourceList;
	private Code responseCode;

	/**
	 * for serialization
	 */
	ValidateEmailAddressResult() {
		responseCode = Code.nil;
	}

	/**
	 * create the result from the action
	 *
	 * @param action
	 */
	public ValidateEmailAddressResult(String emailAddress,
			ArrayList<AuthSourceInfo> authSources) {
		this.emailAddress = emailAddress;
		authSourceList = authSources;
		responseCode = Code.unknownEmailAddress;
	}

	/**
	 * Creates a validate email address response that simply indicates were the
	 * browser is being redirected to. The redirection is done via temporary
	 * server redirect within the httpResponse, so the client does not need to
	 * act upon this result in any way
	 *
	 * @param redirectUrl
	 */
	public ValidateEmailAddressResult(String redirectUrl) {
		setRedirectUrl(redirectUrl);
		responseCode = Code.redirect;
	}

	/**
	 * @return the authSourceList
	 */
	public ArrayList<AuthSourceInfo> getAuthSourceList() {
		return authSourceList;
	}

	/**
	 * @param authSourceList the authSourceList to set
	 */
	public void setAuthSourceList(ArrayList<AuthSourceInfo> authSourceList) {
		this.authSourceList = authSourceList;
	}

	/**
	 * @return the emailAddress
	 */
	public String getEmailAddress() {
		return emailAddress;
	}
}
