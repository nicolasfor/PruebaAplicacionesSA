package ejb.exception;

import javax.ejb.ApplicationException;

/**
 * @author nicolasfor
 *
 */
@SuppressWarnings("serial")
@ApplicationException(rollback=true)
public class CardNotFound extends Exception{

	public CardNotFound () {
        this.message = "Card not found in database";
    }
    
    public CardNotFound(String message) {
        this.message = message;
    }
    
    @Override
    public String getMessage() {
        return this.message; 
    }
    
    private String message;

}
