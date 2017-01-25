package ejb.exception;

import javax.ejb.ApplicationException;

/**
 * @author nicolasfor
 *
 */
@SuppressWarnings("serial")
@ApplicationException(rollback = true)
public class ClientInvalidNameNumber extends Exception{

	public ClientInvalidNameNumber() {
		this.message = "Name must contain no numbers.";
	}

	public ClientInvalidNameNumber(String message) {
		this.message = message;
	}

	@Override
	public String getMessage() {
		return this.message;
	}

	private String message;
}
