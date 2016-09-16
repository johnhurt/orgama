// <copyright file="ErrorMessageLookup.java" company="RookAndPawn">
// Copyright (c) 2011 All Rights Reserved, http://www.rookandpawn.com/
package org.orgama.client.except;

/**
 * This interface should be implemented on a per module basis.  instances will
 * be registered in the 
 * @author kguthrie
 */
public interface ErrorMessageLookup {
    public String getModuleCode();
    public String getMessageForCode(int errorNum);
}
