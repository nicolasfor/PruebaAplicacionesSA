package ejb.exception;

import javax.ejb.ApplicationException;

/**
 * @author nicolasfor
 *
 */
@SuppressWarnings("serial")
@ApplicationException(rollback = true)
public class ClientNameSize extends Exception{

	public ClientNameSize() {
		this.message = "Name size must be less than 40 characters.";
	}

	public ClientNameSize(String message) {
		this.message = message;
	}

	@Override
	public String getMessage() {
		return this.message;
	}

	private String message;

}
