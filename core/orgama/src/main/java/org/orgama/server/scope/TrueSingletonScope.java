package org.orgama.server.scope;

import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.Scope;
import java.util.HashMap;

/**
 * Scope that creates true singletons from injected objects
 * @author kguthrie
 */
public class TrueSingletonScope implements Scope {

	private static final Object syncLock = new Object();
	private static final HashMap<Key<?>, Provider<?>> providers = 
			new HashMap<Key<?>, Provider<?>>();
	
	private static <T> Provider<T> scopeImpl(final Key<T> key, 
			final Provider<T> prvdr) {
		synchronized (syncLock) {
			
			Provider<T> result = (Provider<T>)providers.get(key);
			
			if (result == null) {
				result = new TrueSingletonProvider<T>(prvdr);
				providers.put(key, result);
			}
			
			return result;
		}
	}
	
	@Override
	public <T> Provider<T> scope(Key<T> key, Provider<T> prvdr) {
		
		return scopeImpl(key, prvdr);
		
	}

}
