package org.orgama.shared;

import java.util.Date;

/**
 * Interface for an entity that provides access to cookies.  This is intended to
 * unify the way cookies are used on server and client side
 * @author kguthrie
 */
public interface ICookieHandler {

	/**
	 * Get the value for the cookie with the given name.  Cookie names are not 
	 * case sensitive.  
	 * @param name
	 * @return a string containing the value of the cookie with the given name
	 * or null if the cookie with the given name is not found
	 */
	public String getValue(String name);
	
	/**
	 * Set the value of the cookie with the given name to the value given.  If 
	 * a cookie with the given name exists, the old value will be returned, if 
	 * no cookie with the given name existed, then null will be returned.  This
	 * method will apply the default cookie lifetime (session)
	 * @param name
	 * @param value
	 * @return 
	 */
	public String setValue(String name, String value);
	
	/**
	 * Set the value of the cookie with the given name to the value given.  If 
	 * a cookie with the given name exists, the old value will be returned, if 
	 * no cookie with the given name existed, then null will be returned.  This
	 * method will apply the default cookie lifetime (session)
	 * @param name
	 * @param value
	 * @return 
	 */
	public String setValue(String name, String value, Date expirationDate);
	
	/**
	 * Clear the value of the given cookie and set it to expire.  The value 
	 * returned is the value of the cookie before it was deleted
	 * @param name
	 * @return 
	 */
	public String deleteValue(String name);
}
