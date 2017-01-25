package ejb.exception;

import javax.ejb.ApplicationException;

/**
 * @author nicolasfor
 *
 */
@SuppressWarnings("serial")
@ApplicationException(rollback = true)
public class BalanceCannotBeNegative extends Exception {

	public BalanceCannotBeNegative() {
		this.message = "Balance cannot be negative";
	}

	public BalanceCannotBeNegative(String message) {
		this.message = message;
	}

	@Override
	public String getMessage() {
		return this.message;
	}

	private String message;
}
