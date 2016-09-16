// <copyright file="GetTypedInstanceResult.java" company="RookAndPawn">
// Copyright (c) 2011 All Rights Reserved, http://www.rookandpawn.com/

package org.orgama.shared.action;

import org.orgama.shared.IHasId;
import java.io.Serializable;

/**
 * base class for responding to the get data action
 * @author kguthrie
 */
public class GetTypedInstanceResult<K, T extends IHasId<K>> extends AbstractResult 
        implements Serializable {

    //data to be returned
    protected T data;

    //key used to identify current data
    private K id;

    /**
     * empty constructor for serialization
     */
    GetTypedInstanceResult() {

    }

    /**
     * create a get data result from the given action
     * @param <A>
     * @param action
     */
    public <A extends GetTypedInstance<K, T>> GetTypedInstanceResult(A action) {
        this.id = action.getKey();
    }

    /**
     * @param data the data to set
     */
    public void setData(T data) {
        this.data = data;
    }

    /**
     * @return the data
     */
    public T getData() {
        return data;
    }

    /**
     * @return the key
     */
    public K getKey() {
        return id;
    }

    /**
     * @param key the key to set
     */
    public void setKey(K id) {
        this.id = id;
    }

}
