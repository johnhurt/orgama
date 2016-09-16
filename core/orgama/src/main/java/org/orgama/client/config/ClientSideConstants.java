// <copyright file="ClientSideConstants.java" company="RookAndPawn">
// Copyright (c) 2011 All Rights Reserved, http://www.rookandpawn.com/

package org.orgama.client.config;

import com.google.inject.BindingAnnotation;
import com.google.inject.Inject;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;

/**
 * string constants for the entire app like the main title, keywords for
 * crawlers, and main description
 * @author kguthrie
 */
public class ClientSideConstants {


	@BindingAnnotation @Target({ FIELD, PARAMETER, METHOD }) @Retention(RUNTIME)
	public static @interface AppTitle {}
	
	@BindingAnnotation @Target({ FIELD, PARAMETER, METHOD }) @Retention(RUNTIME)
	public static @interface AppDescription {}
	
	@BindingAnnotation @Target({ FIELD, PARAMETER, METHOD }) @Retention(RUNTIME)
	public static @interface AppKeywords {}
	
	
	private static final String DEFAULT_APP_TITLE = "My Orgama App";
	private static final String DEFAULT_APP_KEYWORDS = "Orgama GWT AppEngine";
	private static final String DEFAULT_APP_DESCRIPTION = 
			"This is the default page description for an Orgama application";
	
    //Strings common to the entire app
    private String appTitle = DEFAULT_APP_TITLE;
    private String appDescription = DEFAULT_APP_DESCRIPTION;
    private String appKeywords = DEFAULT_APP_KEYWORDS;

    /**
     * @return the appTitle
     */
    public String getAppTitle() {
        return appTitle;
    }

    /**
     * @return the appDescription
     */
    public String getAppDescription() {
        return appDescription;
    }

    /**
     * @return the appKeywords
     */
    public String getAppKeywords() {
        return appKeywords;
    }

	/**
	 * @param appTitle the appTitle to set
	 */
	@Inject(optional = true)
	void setAppTitle(@AppTitle String appTitle) {
		this.appTitle = appTitle;
	}

	/**
	 * @param appDescription the appDescription to set
	 */
	@Inject(optional = true)
	void setAppDescription(@AppDescription String appDescription) {
		this.appDescription = appDescription;
	}

	/**
	 * @param appKeywords the appKeywords to set
	 */
	@Inject(optional = true)
	void setAppKeywords(@AppKeywords String appKeywords) {
		this.appKeywords = appKeywords;
	}

}
