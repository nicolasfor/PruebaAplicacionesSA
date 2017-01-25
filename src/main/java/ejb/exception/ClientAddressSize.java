package ejb.exception;

import javax.ejb.ApplicationException;

/**
 * @author nicolasfor
 *
 */
@SuppressWarnings("serial")
@ApplicationException(rollback = true)
public class ClientAddressSize extends Exception {

	public ClientAddressSize() {
		this.message = "Address size must be less than 50 characters.";
	}

	public ClientAddressSize(String message) {
		this.message = message;
	}

	@Override
	public String getMessage() {
		return this.message;
	}

	private String message;

}
