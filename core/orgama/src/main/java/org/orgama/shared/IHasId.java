/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orgama.shared;

import java.io.Serializable;

/**
 * Interface for any class that has an id
 * @author kguthrie
 */
public interface IHasId<T> extends Serializable {
    public T getId();
}
