// <copyright file="Utilities.java" company="RookAndPawn">
// Copyright (c) 2011 All Rights Reserved, http://rookandpawn.com/

package org.orgama.client;

import com.google.gwt.core.client.Duration;

/**
 * Useful functions for client side applications
 * @author Kevin Guthrie
 */
public class Utilities {

    /**
     * returns the same thing as System.curretTimeMillis but as a double and
     * divided by 1000
     * @return
     */
    public static double getSysTimeSecs() {
        return ((double)System.currentTimeMillis()) / 1000;
    }

}
