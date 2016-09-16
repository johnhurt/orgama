package org.orgama.shared;

import com.allen_sauer.gwt.log.client.Log;

/**
 * logger singleton 
 * @author kguthrie
 */
public class Logger {
   
	public static boolean isLoggingEnabled() {
		return Log.isLoggingEnabled();
	}
	
	public static boolean isTraceEnabled() {
		return Log.isTraceEnabled();
	}
	
	public static boolean isDebugEnabled() {
		return Log.isDebugEnabled();
	}
	
	public static boolean isInfoEnabled() {
		return Log.isInfoEnabled();
	}
	
	public static boolean isWarnEnabled() {
		return Log.isWarnEnabled();
	}
	
	public static boolean isErrorEnabled() {
		return Log.isErrorEnabled();
	}
	
	public static boolean isFatalEnabled() {
		return Log.isFatalEnabled();
	}
	
    public static void trace(String msg) {
		if (isTraceEnabled()) {
			Log.trace(msg);
		}
    }

    public static void trace(String msg, Throwable ex) {
		if (isTraceEnabled()) {
			Log.trace(msg, ex);
		}
    }

    public static void debug(String msg) {
		if (isDebugEnabled()) {
			Log.debug(msg);
		}
    }

    public static void debug(String msg, Throwable ex) {
		if (isDebugEnabled()) {
			Log.debug(msg, ex);
		}
    }
 
    public static void info(String msg) {
		if (isInfoEnabled()) {
			Log.info(msg);
		}
    }

    public static void info(String msg, Throwable ex) {
		if (isInfoEnabled()) {
			Log.info(msg, ex);
		}
    }
 
    public static void warn(String msg) {
		if (isWarnEnabled()) {
			Log.warn(msg);
		}
    }

    public static void warn(String msg, Throwable ex) {
		if (isWarnEnabled()) {
			Log.warn(msg, ex);
		}
    }
 
    public static void error(String msg) {
		if (isErrorEnabled()) {
			Log.error(msg);
		}
    }

    public static void error(String msg, Throwable ex) {
		if (isErrorEnabled()) {
			Log.error(msg, ex);
		}
    }
     
    public static void fatal(String msg) {
		if (isFatalEnabled()) {
			Log.fatal(msg);
		}
    }

    public static void fatal(String msg, Throwable ex) {
		if (isFatalEnabled()) {
			Log.fatal(msg, ex);
		}
    }

}
