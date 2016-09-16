// <copyright file="PutDataAction.java" company="RookAndPawn">
// Copyright (c) 2011 All Rights Reserved, http://www.rookandpawn.com/

package org.orgama.shared.action;

import java.io.Serializable;

/**
 * base action for putting data into the cache and pushing it up to the server
 * @author kguthrie
 */
public abstract class PutDataAction<T extends Serializable>
        extends AbstractAction<PutDataResult<T>> {

    private T data;

    public abstract String getKey();

    /**
     * @return the data
     */
    public T getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(T data) {
        this.data = data;
    }

}
