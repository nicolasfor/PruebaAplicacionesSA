package ejb.exception;

import javax.ejb.ApplicationException;

/**
 * @author nicolasfor
 *
 */
@SuppressWarnings("serial")
@ApplicationException(rollback = true)
public class OutOfSizeCardNumber extends Exception {

	public OutOfSizeCardNumber() {
		this.message = "Digits length must be less than 16.";
	}

	public OutOfSizeCardNumber(String message) {
		this.message = message;
	}

	@Override
	public String getMessage() {
		return this.message;
	}

	private String message;
}
