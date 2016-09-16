package org.orgama.shared.except;

/**
 * Class that implements throwable, so it can be treated as a normal exception, 
 * but doesn't extend exception and won't cause type explosions when serialized
 * @author kguthrie
 */
public class CondensedThrowable extends Throwable {
	
	/**
	 * Create a condensed throwable from a normal throwable
	 * @param toClone
	 * @return 
	 */
	public static CondensedThrowable from(Throwable toClone) {
		if (toClone == null) {
			return null;
		}
		
		return new CondensedThrowable(toClone);
	}
	
	private String message;
	private CondensedThrowable cause;
	
	/**
	 * Create a condensed throwable that wraps the given throwable
	 * @param toWrap 
	 */
	public CondensedThrowable(Throwable toWrap) {
		String[] classNameParts = toWrap.getClass().getName().split(".");
		this.message = "";
		
		if (classNameParts.length > 0) {
			this.message = classNameParts[classNameParts.length - 1] + " - ";
		}
		
		this.message += toWrap.getMessage();
		this.setStackTrace(toWrap.getStackTrace());
		this.cause = CondensedThrowable.from(toWrap.getCause());
	}

	@Override
	public synchronized Throwable getCause() {
		return cause;
	}
	
	@Override
	public String getMessage() {
		return message;
	}
}
