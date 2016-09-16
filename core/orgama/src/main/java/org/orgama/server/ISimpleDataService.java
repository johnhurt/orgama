/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orgama.server;

import org.orgama.shared.action.PutInstanceResponse;
import org.orgama.shared.IHasId;
import java.util.ArrayList;

/**
 * Interface for a class that can get and put data into a persistent store.
 * @author kguthrie
 */
public interface ISimpleDataService {
    public IHasId<?> get(String className, Object id);
    public ArrayList<IHasId<?>> getAll(String className, 
            ArrayList<?> presentIds);
    public <T extends IHasId<?>> PutInstanceResponse<T> put(T toPut);
}
