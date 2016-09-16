/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orgama.server;

import org.orgama.shared.action.PutInstanceResponse;
import com.google.inject.Inject;
import com.google.inject.Provider;
import org.orgama.shared.IHasId;
import java.util.ArrayList;

/**
 * Static class that contains the methods for getting and putting instances of
 * classes
 * @author kguthrie
 */
public class SimpleDataServices {
    
    private static ISimpleDataService dataService;
    private static Object syncLock = new Object();
    private static boolean initialized = false;
    
    /**
     * creates a new instance of the data provider
     * @param dataService 
     */
    @Inject
    public SimpleDataServices(Provider<ISimpleDataService> dataService) {
        
        if (initialized) {
            return;
        }
        
        synchronized(syncLock) {
            
            if (initialized) {
                return;
            }

            SimpleDataServices.dataService = dataService.get();
            
            initialized = true;
        }
    }
    
    /**
     * get an instance of the given object
     * @param clazz
     * @param id
     * @return 
     */
    public static IHasId<?> get(String className, Object id) {
        return dataService.get(className, id);
    }
    
    /**
     * get all instances of the given type except the ones in the given set of
     * ids
     * @param clazz
     * @param presentIds
     * @return 
     */
    public static ArrayList<IHasId<?>> getAll(String className, 
            ArrayList<?> presentIds) {
        return dataService.getAll(className, presentIds);
    }
    
    public static <T extends IHasId<?>> PutInstanceResponse<T> put(T toPut) {
        return dataService.put(toPut);
    }
}
