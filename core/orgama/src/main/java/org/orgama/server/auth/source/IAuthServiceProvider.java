// <copyright file="IAuthServiceProvider.java" company="RookAndPawn">
// Copyright (c) 2011 All Rights Reserved, http://www.rookandpawn.com/

package org.orgama.server.auth.source;

import java.util.Map;


/**
 * provider of a list of auth sources
 * @author kguthrie
 */
public interface IAuthServiceProvider {
	
	Map<String, IAuthService> getAuthServices();
	
}
