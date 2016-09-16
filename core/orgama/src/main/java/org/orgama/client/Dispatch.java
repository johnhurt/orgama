// <copyright file="Dispatch.java" company="RookAndPawn">
// Copyright (c) 2011 All Rights Reserved, http://www.rookandpawn.com/
package org.orgama.client;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.shared.Action;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.dispatch.shared.Result;
import org.orgama.client.cache.ObjectCache;
import org.orgama.client.except.ClientSideException;
import org.orgama.shared.IHasId;
import org.orgama.shared.action.*;
import java.util.ArrayList;
import java.util.HashMap;
import org.orgama.shared.IRedirector;
import org.orgama.shared.Logger;

/**
 * Singleton for handling dispatcher requests from different sources
 * @author kguthrie
 */
public class Dispatch {
    
    private static DispatchAsync dispatcher;
	private static IRedirector redirector;
    
	private static final HashMap<String, Timer> timersByOverride = 
			new HashMap<String, Timer>();
	
    /**
     * Constructor for eager singleton creation
     * @param dispatcher 
     */
    @Inject
    public Dispatch(DispatchAsync dispatcher, IRedirector redirector) {
        Dispatch.dispatcher = dispatcher;
		Dispatch.redirector = redirector;
    }
    
    /**
     * make a dispatch call using the given action that will execute the given
     * callback upon completion
     */
    public static <A extends Action<R>, R extends Result>void dispatch(
            A action, AsyncCallback<R> callback) {
		if (action instanceof AbstractAction) {
			dispatchAbstract((AbstractAction)action, 
					(AsyncCallback<AbstractResult>)callback);
		}
		else {
			dispatcher.execute(action, callback);
		}
    }
    
	/**
	 * private dispatch method that will be used if a given action is 
	 * an abstract action and thus the result is an abstract result.  
	 * @param <A>
	 * @param <R>
	 * @param action
	 * @param callback 
	 */
	private static <A extends AbstractAction<R>, R extends AbstractResult>
			void dispatchAbstract(final A action, final AsyncCallback<R> callback) {
		
		OrgAsyncCallback<R> orgCallback = new OrgAsyncCallback<R>() {

			@Override
			public void onFailure(ClientSideException ex) {
				action.onResponse();
				callback.onFailure(ex);
			}

			@Override
			public void onSuccess(R result) {
				//Handle automatic redirects
				if (result.getRedirectUrl() != null) {
					Logger.info("Automatically redirecting to " + 
							result.getRedirectUrl());
					redirector.redirect(result.getRedirectUrl());
				}
				else {
					action.onResponse();
					callback.onSuccess(result);
				}
			}
		};
		
		dispatcher.execute(action, orgCallback);
		action.onDispatch();
	}
	
    /**
     * Get instance of the given class from the server with the given id
     */
    public static <K, T extends IHasId<K>> void getInstance(
            final Class<T> clazz, K id, final AsyncCallback<T> callback) {
        T result = (T)ObjectCache.get(clazz, id);
        
        if (result != null) {
            callback.onSuccess(result);
            return;
        }
        
        dispatch(new GetInstance(((Class)clazz), id), 
                new OrgAsyncCallback<GetInstanceResult>() {

            @Override
            public void onFailure(ClientSideException ex) {
                callback.onFailure(ex);
            }

            @Override
            public void onSuccess(GetInstanceResult result) {
                T data = (T)result.getData();
                ObjectCache.put(clazz, data);
                callback.onSuccess(data);
            }
        });
    }
    
    /**
     * Dispatch a call to the server to get all instances of the given class
     * type
     * @param <K>
     * @param <T>
     * @param clazz
     * @param callback 
     */
    public static <K, T extends IHasId<K>> void getAllInstances(
            final Class<T> clazz, 
            final AsyncCallback<ArrayList<T>> callback) {
        
        final ArrayList<T> cached = new ArrayList<T>();
        boolean complete = ObjectCache.getAll(clazz, cached);
        ArrayList<K> cachedIds;
        
        if (complete) {
            callback.onSuccess(cached);
            return;
        }
        
        cachedIds = new ArrayList<K>();
        
        for (T obj : cached) {
            cachedIds.add(obj.getId());
        }
        
        dispatch(new GetAllInstances((Class)clazz, cachedIds), 
                new OrgAsyncCallback<GetAllInstancesResult>() {

            @Override
            public void onFailure(ClientSideException ex) {
                callback.onFailure(ex);
            }

            @Override
            public void onSuccess(GetAllInstancesResult result) {
                ArrayList<T> typedResult = (ArrayList)result.getResults();
                ObjectCache.putAll(clazz, typedResult, true);
                cached.addAll(typedResult);
                callback.onSuccess(cached);
            }
        });
    }
    
    /**
     * dispatch a call to put the given instance into persistent storage
     * @param <K>
     * @param <T>
     * @param instance
     * @param callback 
     */
    public static <K, T extends IHasId<K>> void putInsance(final T instance, 
            final AsyncCallback<T> callback) {
        dispatch(new PutInstance(instance), 
                new OrgAsyncCallback<PutInstanceResult>() {

            @Override
            public void onFailure(ClientSideException ex) {
                callback.onFailure(ex);
            }

            @Override
            public void onSuccess(PutInstanceResult result) {
                ObjectCache.put((Class<T>)instance.getClass(), instance);
                callback.onSuccess((T)result.getResult());
            }
        });
    }
}
