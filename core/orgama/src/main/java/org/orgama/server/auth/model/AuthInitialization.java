package org.orgama.server.auth.model;

import java.io.Serializable;

/**
 *
 * @author kguthrie
 */
public class AuthInitialization implements Serializable {

	private AuthInitializationState state;
	private String emailAddress;
	private String authServiceName;
	private String serviceSpecificUserId;
	private String serviceSpecificSessionId;
	
	/** For serialization */
	public AuthInitialization() {
		state = AuthInitializationState.nil;
	}

	/**
	 * @return the state
	 */
	public AuthInitializationState getState() {
		return state;
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

	/**
	 * @return the authSourceId
	 */
	public String getAuthServiceName() {
		return authServiceName;
	}

	/**
	 * @param authServiceName the authSourceId to set
	 */
	public void setAuthServiceName(String authServiceName) {
		this.authServiceName = authServiceName;
	}

	/**
	 * Set the authInitialization state
	 * @param state 
	 */
	public void setState(AuthInitializationState state) {
		this.state = state;
	}

	/**
	 * @return the externalUserId
	 */
	public String getServiceSpecificUserId() {
		return serviceSpecificUserId;
	}

	/**
	 * @param serviceSpecificUserId the externalUserId to set
	 */
	public void setServiceSpecificUserId(String serviceSpecificUserId) {
		this.serviceSpecificUserId = serviceSpecificUserId;
	}

	/**
	 * @return the serviceSpecificSessionId
	 */
	public String getServiceSpecificSessionId() {
		return serviceSpecificSessionId;
	}

	/**
	 * @param serviceSpecificSessionId the serviceSpecificSessionId to set
	 */
	public void setServiceSpecificSessionId(String serviceSpecificSessionId) {
		this.serviceSpecificSessionId = serviceSpecificSessionId;
	}
	
}
