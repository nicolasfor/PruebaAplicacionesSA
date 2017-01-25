package presentation;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import ejb.entities.Card;
import ejb.entities.Client;
import ejb.sessions.CardManager;




@Named
@ViewScoped
public class UserAddBacking extends BaseBacking implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6806367959840697221L;

	@EJB
    private CardManager cardManager;
	
	@Named
    @Produces
    @RequestScoped
    private Card newCard = new Card();
	
	private String infoMessage;
    
    public String getInfoMessage() {
        return infoMessage;
    }

    public void setInfoMessage(String infoMessage) {
        this.infoMessage = infoMessage;
    }    
    
    public String registerUser() {         
    	System.out.println("ENTRA Backing bean");
    	
        try {
        	Client  newClient = new Client(998896, "Martin", "Cali");  
        	cardManager.createCard(newCard, newClient);;
            infoMessage = "User saved successfully";
            
        } 
        catch (Exception ex) {
            Logger.getLogger(UserAddBacking.class.getName()).log(Level.SEVERE, null, ex);
            getContext().addMessage(null, new FacesMessage("An error occurs while registering user"));             
        }
        
        return null;
    }
}
