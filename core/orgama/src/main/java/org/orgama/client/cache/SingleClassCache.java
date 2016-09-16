/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orgama.client.cache;

import org.orgama.shared.IHasId;
import java.util.*;

/**
 * Class that represents a cache of a single class of object
 * @author kguthrie
 */
public class SingleClassCache<K, T extends IHasId<K>>  {
    
    private Class<T> clazz;
    
    private HashMap<K, CacheWrapper<K, T>> soCache;
    
    private boolean complete;
    
    /**
     * create a single class cache or the given class type
     * @param clazz t
     */
    public SingleClassCache(Class<T> clazz) {
        this.clazz = clazz;
        this.soCache = new HashMap<K, CacheWrapper<K, T>>();
        complete = false;
    }
    
    /**
     * get the class type cached in this object
     */
    public Class<T> getClassType()
    {
        return clazz;
    }
    
    /**
     * returns the object that corresponds to the given id.  If one does not 
     * then null is returned
     * @param id
     * @return 
     */
    public T getForId(K id) {
        CacheWrapper<K, T> tempResult = soCache.get(id);
        
        if (tempResult == null) {
            return null;
        }
        
        return tempResult.get();
    }
    
    /**
     * This is method when multiple objects are requested.  The results for 
     * objects that are found and objects that are not found are placed in the
     * two output parameters
     */
    public void getForIds(LinkedList<K> ids, 
            LinkedList<T> found, LinkedList<K> notFoundIds) {
        if (ids == null || found == null || notFoundIds == null) {
            return;
        }
        
        for (K i : ids) {
            T tempResult = getForId(i);
            if (tempResult == null) {
                notFoundIds.add(i);
            }
            else {
                found.add(tempResult);
            }
        }
    }
    
    /**
     * cache the given object.  If the given object has an id that is already
     * stored in this cache, then this object will replace the previous one.
     * The returned value indicated whether the toCache object contained a new
     * Id.  If the result is false, the cached object replaced a previously 
     * cached object
     * @param iHasId 
     */
    public boolean put(T toCache) {
        CacheWrapper wrapped;
        
        if (toCache == null) {
            return false;
        }
        
        wrapped = new CacheWrapper<K,T>(toCache);
        
        try {
            soCache.put(toCache.getId(), wrapped);
            return true;
        }
        catch(Exception ex) { //Assume this is becuase the id already exists
            soCache.remove(toCache.getId());
            soCache.put(toCache.getId(), wrapped);
            return false;
        }
    }
    
    /**
     * put all the elements in the list into the cache.  The completeness of the
     * set is not affected
     * @param to 
     */
    public int putAll(ArrayList<T> toCache) {
        return putAll(toCache, complete);
    }
    
    /**
     * put all the objects given into the cache.  If the boolean complete set 
     * is true, then this single class cache is marked as containing all 
     * instances.  The value returned is the number of items that were newly
     * inserted
     * @param toCache 
     */
    public int putAll(ArrayList<T> toCache, boolean completeSet) {
        int result = 0;
        for (T i : toCache) {
            if (put(i)) {
                result ++;
            }
        }
        
        complete = completeSet;
        return result;
    }
    
    /**
     * get the number of cached instances
     * @return 
     */
    public int getSize() {
        return soCache.size();
    }
    
    /**
     * get all the elements in this cache and puts them in the provided array.
     * the boolean returned indicates whether or not the set of items contained
     * in this cache is complete
     * @return 
     */
    public boolean getAll(ArrayList<T> target) {
        Iterator<CacheWrapper<K, T>> it = soCache.values().iterator();
        
        while (it.hasNext()) {
            target.add(it.next().get());
        }
        
        return complete;
    }
    
    /**
     * clean up entries in the cache that have not been read since the given 
     * reference data
     */
    public CleanupResults cleanUp(double minTime, double referencetime, 
            boolean countOnly) {
        
        Iterator<Map.Entry<K, CacheWrapper<K, T>>> iterator = 
                soCache.entrySet().iterator();
        
        Map.Entry<K, CacheWrapper<K, T>> entry;
        CleanupResults result = new CleanupResults();
        double readAge;
        double readTime;
        
        while (iterator.hasNext()) {
            entry = iterator.next();
            readTime = entry.getValue().getLastReadTime();
            readAge = readTime - minTime;
            
            if ((!countOnly) && (readTime < referencetime)) {
                iterator.remove();
                result.numDiscarded++;
            }
            else {
                result.numKept++;
                result.remainingTotalReadAge += readAge;
                result.remainingMinAccessTime = 
                        (result.remainingMinAccessTime < readTime) ? 
                                (result.remainingMinAccessTime) : (readTime);
            }
        }
        
        return result;
    }
}
