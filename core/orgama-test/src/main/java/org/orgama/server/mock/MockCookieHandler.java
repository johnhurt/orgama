package org.orgama.server.mock;

import com.google.inject.Inject;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.orgama.shared.ICookieHandler;

/**
 *
 * @author kguthrie
 */
public class MockCookieHandler implements ICookieHandler {

	private Map<String,String> storage;
	private boolean throwOnGet;
	
	@Inject
	public MockCookieHandler() {
		storage = new HashMap<String, String>();
		throwOnGet = false;
	}
	
	@Override
	public String getValue(String name) {
		if (throwOnGet) {
			throw new RuntimeException(
					"This is an deliberate exception from the "
					+ "mock cookie handler");
		}
		
		return storage.get(name);
	}

	@Override
	public String setValue(String name, String value) {
		return storage.put(name, value);
	}

	/**
	 * set whether or not an exception will be thrown on getting from the
	 * cookies.  This is a way of introducing errors into some server processes
	 * @param throwErrorOnGet 
	 */
	public void setThrowErrorOnGet(boolean throwOnGet) {
		this.throwOnGet = throwOnGet;
	}

	@Override
	public String setValue(String name, String value, Date expirationDate) {
		return setValue(name, value);
	}

	@Override
	public String deleteValue(String name) {
		return storage.remove(name);
	}
	
}
