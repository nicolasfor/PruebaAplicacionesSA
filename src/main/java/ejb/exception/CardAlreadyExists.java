package ejb.exception;

import javax.ejb.ApplicationException;

/**
 * @author nicolasfor
 *
 */
@SuppressWarnings("serial")
@ApplicationException(rollback=true)
public class CardAlreadyExists extends Exception{
	
	public CardAlreadyExists () {
        this.message = "Card already exists";
    }
    
    public CardAlreadyExists(String message) {
        this.message = message;
    }
    
    @Override
    public String getMessage() {
        return this.message; 
    }
    
    private String message;
}
