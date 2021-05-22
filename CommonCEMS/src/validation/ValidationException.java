package validation;

/**
 * class for validation errors
 * @author Or Man
 * @version 1.1
 * @since 21/12/2020
 */
public class ValidationException extends Exception{

	/**
	 * serialId for serialization
	 */
	private static final long serialVersionUID = -8767430358076562072L;
	private String validatedString = "";

	/**
	 * @see Exception#Exception()
	 * @param received the string that his validation caused this exception
	 */
	public ValidationException(String received) {
		super(generateMsg(received));
		validatedString = received;
	}

	/**
	 * @see Exception#Exception(String, Throwable, boolean, boolean)
	 * @param received the string that his validation caused this exception
	 */
	public ValidationException(String message,String received, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(generateMsg(received) + message, cause, enableSuppression, writableStackTrace);
		validatedString = received;
	}

	/**
	 * @see Exception#Exception(String, Throwable)
	 * @param received the string that his validation caused this exception
	 */
	public ValidationException(String message,String received, Throwable cause) {
		super(generateMsg(received) + message, cause);
		validatedString = received;
	}

	/**
	 * @see Exception#Exception(String)
	 * @param received the string that his validation caused this exception
	 */
	public ValidationException(String message,String received) {
		super(generateMsg(received) + message);
		validatedString = received;
	}
	
	/**
	 * @see Exception#Exception(Throwable)
	 */
	public ValidationException(Throwable cause) {
		super(cause);
	}
	
	
	private static String generateMsg(String received) {
		return "String checked: \""+received +"\" ";
	}
	
	/**
	 * getter
	 * @return the string that his validation caused this exception
	 */
	public String getValidatedString() {
		return validatedString;
	}
}
