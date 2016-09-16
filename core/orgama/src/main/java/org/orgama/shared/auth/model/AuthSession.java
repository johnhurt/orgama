// <copyright file="AuthSession.java" company="RookAndPawn">
// Copyright (c) 2011 All Rights Reserved, http://www.rookandpawn.com/
package org.orgama.shared.auth.model;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * AuthSession for serialized storage object
 * @author kguthrie
 */
@Entity
public class AuthSession implements Serializable {
    
    @Id
    private String sessionId;
    private Long userId;
    private Date creationDate;
    private Date expirationDate;
	private String authServiceName;
	private String serviceSpecificSessionId;
	
    /**
     * For Serialization 
     */
    public AuthSession() {
        creationDate = new Date();
    }

    /**
     * @return the creationDate
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * @return the expirationDate
     */
    public Date getExpirationDate() {
        return expirationDate;
    }

    /**
     * @return the sessionKey
     */
    public String getSessionId() {
        return sessionId;
    }

    /**
     * @param sessionKey the sessionKey to set
     */
    public void setSessionId(String sessionKey) {
        this.sessionId = sessionKey;
    }

    /**
     * @return the userId
     */
    public long getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(long userId) {
        this.userId = userId;
    }

    /**
     * Sets the expiration date of this session to now.  This session will
     * be expired
     */
    public void markAsExpired() {
        this.setExpirationDate(new Date());
    }
	
	/**
	 * returns whether or not this sessions expiration date has passed
	 * @return 
	 */
	public boolean isClosed() {
		return expirationDate.before(new Date());
	}

	/**
	 * @param expirationDate the expirationDate to set
	 */
	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	/**
	 * @return the authServiceName
	 */
	public String getAuthServiceName() {
		return authServiceName;
	}

	/**
	 * @param authServiceName the authServiceName to set
	 */
	public void setAuthServiceName(String authServiceName) {
		this.authServiceName = authServiceName;
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
