// <copyright file="AbstractResult.java" company="RookAndPawn">
// Copyright (c) 2011 All Rights Reserved, http://rookandpawn.com/

package org.orgama.shared.action;

import com.gwtplatform.dispatch.shared.Result;
import java.io.Serializable;

/**
 * enhanced version of the gwt dispatch result
 * @author Kevin Guthrie
 */
public abstract class AbstractResult implements Result, Serializable {

    //id of the corresponding action
    private long id;
	
	private String redirectUrl;

    /**
     * empty constructor for serialization
     */
    public AbstractResult() {}

    public long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(long id) {
        this.id = id;
    }

	/**
	 * @return the redirectUrl
	 */
	public String getRedirectUrl() {
		return redirectUrl;
	}

	/**
	 * @param redirectUrl the redirectUrl to set
	 */
	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

}
