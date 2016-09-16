/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orgama.client.cache;

import org.orgama.client.Utilities;
import org.orgama.shared.IHasId;

/**
 * Class that wraps a cached object.  It provides things like last read time, 
 * write time, etc
 * @author kguthrie
 */
public class CacheWrapper<K, T extends IHasId<K>>  {
    
    private T payload;
    private double lastReadTime;
    
    public CacheWrapper(T payload)
    {
        lastReadTime = Utilities.getSysTimeSecs();
        ObjectCache.updateReadTime(lastReadTime);
        this.payload = payload;
    }
    
    /**
     * get the wrapped object
     * @return 
     */
    public T get() {
        lastReadTime = Utilities.getSysTimeSecs();
        ObjectCache.updateReadTime(lastReadTime);
        return payload;
    }
    
    /**
     * get the last time this data was read
     * @return 
     */
    public double getLastReadTime() {
        return lastReadTime;
    }
}
