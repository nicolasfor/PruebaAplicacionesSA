package ejb.sessions;

import java.util.List;
import javax.ejb.Local;
import ejb.entities.Card;
import ejb.entities.Client;
import ejb.exception.BalanceCannotBeNegative;
import ejb.exception.CardNotFound;
import ejb.exception.ClientNotFound;
import ejb.exception.MaximumCardsAllowed;
import ejb.exception.OutOfBalance;
import ejb.exception.OutOfSizeCardNumber;

/**
 * Session Bean implementation class CardManager
 */
@Local
public interface CardManager {
	// -----------------------------------------------------------------
	// Métodos
	// -----------------------------------------------------------------

	/**
	 * Éste método crea una tarjeta con los parámetros correspondientes
	 * @param newCard corresponde a la tarjeta que se le asignará al cliente
	 * @param c corresponde al cliente que se asignara la tarjeta correspondiente newCard
	 * @throws ClientNotFound en caso de que el cliente no exista
	 * @throws MaximumCardsAllowed en caso de que el cliente ya tenga 2 tarjetas asignadas
	 * @throws CardNotFound en caso de que la tarjeta no exista en la base de datos
	 * @throws BalanceCannotBeNegative en caso de que el saldo sea negativo
	 * @throws OutOfSizeCardNumber en caso de que el numero de la tarjeta sea mayor a 16
	 * @throws OutOfBalance en caso de que el balance supere el valor de $5.000.000
	 */
	public void createCard(Card newCard, Client c) throws ClientNotFound, MaximumCardsAllowed, CardNotFound, BalanceCannotBeNegative, OutOfSizeCardNumber, OutOfBalance;

	
	/**
	 * Éste método elimina la tarjeta correspondiente al id del plarámetro
	 * @param cardID corresponde al id de la tarjeta que se va a elminar
	 * @throws CardNotFound en caso de que la tarjeta no exista en la base de datos
	 */
	public void removeCard(Long cardID) throws CardNotFound;

	/**
	 * Éste método retorna todas las tarjetas asociadas a un cliente dado su id
	 * @param id corresponde al id del cliente
	 * @return una lista de todas las tarjetas asociadas al cliente
	 * @throws CardNotFound en caso de que la tarjeta no exista
	 */
	List<Card> getCardsFromClient(Integer id) throws CardNotFound;

	/**
	 * Éste método retorna todas las tarjetas registradas en la base de datos
	 * @return una lista con todas las tarjetas
	 * @throws CardNotFound en caso de que no hayan tarjetas
	 */
	List<Card> retrieveCards() throws CardNotFound;
}
