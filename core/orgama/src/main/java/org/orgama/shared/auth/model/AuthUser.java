// <copyright file="AuthUser.java" company="RookAndPawn">
// Copyright (c) 2011 All Rights Reserved, http://www.rookandpawn.com/
package org.orgama.shared.auth.model;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import java.io.Serializable;
import org.orgama.server.annotation.Unique;
import org.orgama.shared.auth.AuthUtils;
import org.orgama.shared.unique.HasIdAndUniqueFields;

/**
 * Data storage object for Users
 * @author kguthrie
 */
@Entity
public class AuthUser extends HasIdAndUniqueFields implements Serializable {
    
    @Id
    private Long userId;
    private String originalEmailAddress;
	
	@Unique @Index
    private String sanitizedEmailAddress;
    
	@Unique @Index
    private String serviceSpecificUserId;
    private String authServiceName;
    
    /**
     * Empty constructor 
     */
    public AuthUser() {
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
     * @return the emailAddress
     */
    public String getSanitizedEmailAddress() {
        return sanitizedEmailAddress;
    }

    /**
     * @return the emailAddress
     */
    public String getOriginalEmailAddress() {
        return originalEmailAddress;
    }
    
    /**
     * @param emailAddress the emailAddress to set
     */
    public void setEmailAddress(String emailAddress) {
        sanitizedEmailAddress = AuthUtils.sanitizeEmailAddress(emailAddress);
        originalEmailAddress = emailAddress.trim();
    }
    
    /**
     * @return the externalAuthEmailAddress
     */
    public String getServiceSpecificUserId() {
        return serviceSpecificUserId;
    }

    /**
     * @param serviceSpecificUserId the externalAuthEmailAddress to set
     */
    public void setServiceSpecificUserId(String serviceSpecificUserId) {
        this.serviceSpecificUserId = serviceSpecificUserId;
    }

    /**
     * @return the authSourceResName
     */
    public String getAuthServiceName() {
        return authServiceName;
    }

    /**
     * @param authServiceName the authSourceResName to set
     */
    public void setAuthServiceName(String authServiceName) {
        this.authServiceName = authServiceName;
    }

	@Override
	public Object getId() {
		return userId;
	}
}
