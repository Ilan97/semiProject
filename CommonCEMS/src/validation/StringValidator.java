package validation;

/**
 * class for checking strings
 * @author Or Man
 * @version 1.2
 * @since 21/01/2021
 */
public class StringValidator {

	/**
	 * constructor for non-instantiable class 
	 */
	private StringValidator() {}
	
	/**<pre>check if string is in format of email
	 * before '@' has to be string with letters from this group {@literal [a-z or A-Z or 0-9 or .!#$%&'*+/=?^_`{|}~- ]}
	 * then after '@' need to be word from the group [a-z or A-Z or 0-9 or -]
	 * then need to be at least one appereance of " '.' and than word from the group [a-z or A-Z or 0-9 or -]
	 * 
	 * for example user@host.ext</pre>
	 * @param email the string to check
	 * @return if all of these rules applies 
	 */
	public static boolean isValidEmail(String email) {
		String emailFormat = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)+$";
		return email.matches(emailFormat);
	}
	
	/**
	 * check if String can be parsed into int <br><br>
	 * the validation uses the throw of {@link Integer#parseInt(String)}
	 * @param num the string to check
	 * @return if <b>num</b> can be parsed into int
	 *  
	 */
	public static boolean isValidInt(String num) {
		try {
			Integer.parseInt(num);
			return true;
		}catch(NumberFormatException e) {
			return false;
		}
	}
	
	/**
	 * check if String can be parsed into double <br><br>
	 * the validation uses the throw of {@link Double#parseDouble(String)}
	 * @param num the string to check
	 * @return if <b>num</b> can be parsed into double
	 * 
	 */
	public static boolean isValidDouble(String num) {
		try {
			Double.parseDouble(num);
			return true;
		}catch(NumberFormatException e) {
			return false;
		}
	}
	
	/**<pre>
	 * check string for valid Israeli ID,
	 * ID length must be 9 digit long
	 * </pre>
	 * @param id the string to check
	 * @return if the string is valid Israeli ID( by the check digit)
	 * @throws ValidationException if the length is lower than 9 characters
	 * @throws ValidationException if the string has characters that dont represent digits ['0' - '9']
	 */
	public static boolean isValidIsraelyId(String id) throws ValidationException {
			if(id.length()!=9)
				throw new ValidationException("Length must be 9 digits", id);
		return checkDigitVerification(id);
	}
	/**
	 * check string for valid credit card,
	 * need to contain only numbers and '-'
	 * @param cardNumber the string to check
	 * @return if the string is valid cardNumber( by the check digit)
	 * @throws ValidationException if the string has characters that don't represent digits ['0' - '9'] or the separator '-'
	 */
	public static boolean isValidCreditCard(String cardNumber) throws ValidationException {
		String digitAndSeperatorFormat = "(^[0-9-]+)";
		if(!cardNumber.matches(digitAndSeperatorFormat))
			throw new ValidationException("Id must contain only digits(0-9) and seperator '-'", cardNumber);
	return checkDigitVerification(cardNumber.replaceAll("-", ""));
}

	
	/**
	 * check string for valid check digit
	 * @param number the string to check
	 * @return if the string check digit is valid
	 * @throws ValidationException if the string has characters that dont represent digits ['0' - '9']
	 */
	public static boolean checkDigitVerification(String number) throws ValidationException {
		String onlyDigitFormat = "(^[0-9]+)";
		if(!number.matches(onlyDigitFormat))
			throw new ValidationException("Id must contain only digits(0-9)", number);
		boolean even = false;
		int sum = 0;
		for(int i = number.length()-1 ;i>=0;i--) {
			int val = number.charAt(i)-'0';
			val = (even)?val*2:val;
			sum+=(val/10)+(val%10);
			even = !even;
		}

		return sum%10 == 0;
	}
}
