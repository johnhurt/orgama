package org.orgama.server.scope;

import com.google.inject.Provider;

/**
 * provider for an instance of a true singleton
 * @author kguthrie
 */
public class TrueSingletonProvider<T> implements Provider<T> {

	private T instance;
	private Provider<T> originalProvider;
	
	public TrueSingletonProvider(Provider<T> originalProvider) {
		instance = null;
		this.originalProvider = originalProvider;
	}
	
	@Override
	public T get() {
		if (instance != null) {
			return instance;
		}
		
		synchronized(this) {
			if (instance != null) {
				return instance;
			}
			
			instance = originalProvider.get();
			return instance;
		}
	}

}
