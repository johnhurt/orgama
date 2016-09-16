// <copyright file="ObjectCache.java" company="RookAndPawn">
// Copyright (c) 2011 All Rights Reserved, http://www.rookandpawn.com/

package org.orgama.client.cache;

import org.orgama.client.Utilities;
import org.orgama.shared.IHasId;
import org.orgama.shared.except.OrgException;
import org.orgama.shared.except.ObjectCacheException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * This class will contain a hashtable of all the data objects for the site have
 * been been loaded from the server (Up to a certain limit).  All data requests
 * from the server will be routed through this class first to see if the object
 * requested has already been loaded.  Some of the math in this class seems sort
 * of complicated and superstitious, and it might be.  
 * @author Kevin Guthrie
 */
public class ObjectCache {
    private static int maxSize = 250;
    private static double minAccessTime = 0;
    private static double averageAccessTime = 0;
    private static int numAccessTimeSamples = 0;
    private static int size;

    private static HashMap<Class<?>, SingleClassCache> cache;

    static {init();}
    
    /**
     * initialize the object cache with a size limit of 250
     */
    public static void init() {
        init(maxSize);
    }

    /**
     * initialize the object cache with a given maximum size
     * @param maxSize
     */
    public static void init(int maxSize) {
        size = 0;
        minAccessTime = Utilities.getSysTimeSecs();
        averageAccessTime = 0;
        numAccessTimeSamples = 1;
        ObjectCache.maxSize = maxSize;
        cache = new HashMap<Class<?>, SingleClassCache>();
    }

    /**
     * get the minimum access time
     * @return
     */
    public static double getMinAccessTime() {
        return minAccessTime;
    }

    /**
     * get the average access time
     * @return
     */
    public static double getAverageAccessTime() {
        return averageAccessTime;
    }

    /**
     * return the number of sampled access times
     * @return
     */
    public static int getNumAccessTimeSamples() {
        return numAccessTimeSamples;
    }

    /**
     * return the number of objects in the cache
     * @return
     */
    public static int getSize() {
        return size;
    }

    
    public static int getCachedClassCount() {
        return cache.size();
    }
    
    /**
     * get the object stored at
     * @param <T>
     * @param key
     * @return
     */
    public static <K, T extends IHasId<K>> T get(Class<T> clazz, K id) {
        T result = null;
        SingleClassCache<K, T> soCache = cache.get(clazz);
        
        //No cache of this class
        if (soCache == null) {
            return null;
        }
        else {
            result = (T)soCache.getForId(id);
        }

        return result;
    }

    /**
     * insert the given cacheable object into the cache.  This method will
     * call the method that cleans up the object cache based on the number of
     * cached objects and their relative ages
     * @param <T>
     * @param toCache
     * @throws ObjectCacheException
     */
    public static <K, T extends IHasId<K>> void put(
            Class<T> clazz, T toCache)
            throws ObjectCacheException {

        SingleClassCache<K, T> currCache;
        
        try {
            currCache = cache.get(clazz);
            
            if (currCache == null)
            {
                currCache = new SingleClassCache(clazz);
                cache.put(clazz, currCache);
            }
            
            if (currCache.put(toCache)) {
                size++;
            }
            
            checkAndThin();
        }
        catch(Exception ex) {
            throw new ObjectCacheException(ex.getMessage());
        }
    }

    /**
     * Get all the elements of the given type and adds them to the provided 
     * array list.  the value returned indicates whether the gotten items are
     * known to be the complete set
     * object
     * @param <K>
     * @param <T>
     * @param clazz
     * @return 
     */
    public static <K, T extends IHasId<K>> boolean getAll(
            Class<T> clazz, ArrayList<T> target) {
        boolean result;
        SingleClassCache currCache = cache.get(clazz);
        
        if (currCache == null) {
            return false;
        }
        
        result = currCache.getAll(target);
        
        return result;
    }
    
    /**
     * put all the given items into the cache.  the variable complete indicates 
     * whether the given set of elements is a complete set of the type T
     * @param <K>
     * @param <T> 
     */
    public static <K, T extends IHasId<K>> void putAll(Class<T> clazz, 
            ArrayList<T> toCache, boolean complete) {
        
        SingleClassCache<K, T> currCache;
        
        try {
            currCache = cache.get(clazz);
            
            if (currCache == null)
            {
                currCache = new SingleClassCache(clazz);
                cache.put(clazz, currCache);
            }
            
            size += currCache.putAll(toCache);
            
            checkAndThin();
        }
        catch(Exception ex) {
            throw new ObjectCacheException(ex.getMessage());
        }
    }
    
    /**
     * locally used method to update this singletons rolling average and
     * count of samples used in the average
     * @return
     */
    private static double updateRollingAverage(double currValue) {
        double result = updateRollingAverage(minAccessTime, averageAccessTime,
                currValue, numAccessTimeSamples);
        numAccessTimeSamples++;
        return result;
    }

    /**
     * update a rolling average by adding one value to the existing rolling
     * average.  The inputs are the current minimum value used in the rolling
     * average, the current value of the average, the next value to be averaged,
     * and the previous count of averages.  The rolling average in this method
     * is the difference between the actual average and the current minimum
     * @param currAverge
     * @param currValue
     * @param prevCount
     * @return
     */
    public static double updateRollingAverage(double currMin,
            double currAverge, double currValue, int prevCount) {
        double result = currAverge;
        double currDelta = currValue - currMin;

        result *= prevCount;
        result += currDelta;
        result /= (prevCount + 1);

        return result;
    }

    /**
     * check the size of the cache and if it is larger than the limit, iterate
     * through the items in the cache and remove any that are older than the
     * average.  This method will update the singleton's rolling average,
     * minimum access time and sample count
     */
    private static void checkAndThin() {
        Iterator<Map.Entry<Class<?>, SingleClassCache>> it;
        double tempSum;
        int keptCount;
        int deletedCount;
        double tempMin;
        SingleClassCache curr;
        double currAccessTime;
        double referenceTime;
        int halfMaxSize;
        CleanupResults cr;

        if (size <= maxSize) {
            return;
        }

        deletedCount = 0;
        keptCount = 0;
        tempSum = 0;
        tempMin = Double.MAX_VALUE;
        currAccessTime = 0;
        referenceTime = minAccessTime + averageAccessTime;
        halfMaxSize = maxSize /2;

        it = cache.entrySet().iterator();

        while (it.hasNext()) {
            curr = it.next().getValue();
            
            //If we have delete over half, then quit
            if (deletedCount < halfMaxSize) {
                cr = curr.cleanUp(minAccessTime, referenceTime, false);
            }
            else {
                cr = curr.cleanUp(minAccessTime, referenceTime, true);
            }
            
            deletedCount += cr.numDiscarded;
            keptCount += cr.numKept;

            //sum up the remaining items' access times and subtract
            //the current minAccessTime to prevent overflows
            tempSum += cr.remainingTotalReadAge;

            //find the minimum remaining time
            tempMin = ((tempMin < cr.remainingMinAccessTime)?
                    (tempMin):(cr.remainingMinAccessTime));
            
            
        }

        //find the new average of access time offsets from the new minimum
        tempSum = tempSum /= ((double)keptCount);
        tempSum = tempSum - (tempMin - minAccessTime);

        //update the singleton's values
        averageAccessTime = tempSum;
        minAccessTime = tempMin;
        numAccessTimeSamples = keptCount;
        size = keptCount;
    }
    
    /**
     * updates the 
     * @param readTime 
     */
    public static void updateReadTime(double readTime) {
        averageAccessTime = updateRollingAverage(readTime);
    }
}
