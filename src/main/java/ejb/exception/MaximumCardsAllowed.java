package ejb.exception;

import javax.ejb.ApplicationException;

/**
 * @author nicolasfor
 *
 */
@SuppressWarnings("serial")
@ApplicationException(rollback=true)
public class MaximumCardsAllowed  extends Exception{

	public MaximumCardsAllowed () {
        this.message = "Maximum cards allowed per client is 2";
    }
    
    public MaximumCardsAllowed(String message) {
        this.message = message;
    }
    
    @Override
    public String getMessage() {
        return this.message; 
    }
    
    private String message;

}
