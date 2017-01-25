package ejb.exception;

import javax.ejb.ApplicationException;

/**
 * @author nicolasfor
 *
 */
@SuppressWarnings("serial")
@ApplicationException(rollback = true)
public class OutOfBalance extends Exception {

	public OutOfBalance() {
		this.message = "Balance must be less than $5.000.000";
	}

	public OutOfBalance(String message) {
		this.message = message;
	}

	@Override
	public String getMessage() {
		return this.message;
	}

	private String message;
}
