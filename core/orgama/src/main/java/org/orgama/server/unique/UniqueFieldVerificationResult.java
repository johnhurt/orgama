package org.orgama.server.unique;

/**
 * Storage class for the response returned from trying to store an object with 
 * unique fields
 * @author kguthrie
 */
public class UniqueFieldVerificationResult {

	public static UniqueFieldVerificationResult failed(FieldValueLock lock) {
		UniqueFieldVerificationResult result = 
				new UniqueFieldVerificationResult();
		result.errorFieldDescription = lock.toString();
		result.success = false;
		return result;
	}
	
	public static UniqueFieldVerificationResult success() {
		UniqueFieldVerificationResult result = 
				new UniqueFieldVerificationResult();
		result.success = true;
		return result;
	}
	
	private String errorFieldDescription;
	private boolean success;
	
	
	private UniqueFieldVerificationResult() {
		
	}
	
	/**
	 * @return the errorFieldDescription
	 */
	public String getErrorFieldDescription() {
		return errorFieldDescription;
	}

	/**
	 * @return the success
	 */
	public boolean isSuccess() {
		return success;
	}
	
}
