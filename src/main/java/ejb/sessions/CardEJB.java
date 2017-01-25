package ejb.sessions;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import ejb.entities.Card;
import ejb.entities.Client;
import ejb.exception.BalanceCannotBeNegative;
import ejb.exception.CardNotFound;
import ejb.exception.ClientNotFound;
import ejb.exception.MaximumCardsAllowed;
import ejb.exception.OutOfBalance;
import ejb.exception.OutOfSizeCardNumber;

/**
 * Session Bean implementation class CardEJB
 */
@Stateless
@LocalBean
public class CardEJB implements CardManager {

	@PersistenceContext(unitName = "PruebaAplicacionesSA")
	EntityManager em;

	// -----------------------------------------------------------------
	// Constructor
	// -----------------------------------------------------------------

	public CardEJB() {

	}

	// -----------------------------------------------------------------
	// Métodos
	// -----------------------------------------------------------------

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.sessions.CardManager#createCard(ejb.entities.Card,
	 * ejb.entities.Client)
	 */
	@Override
	public void createCard(Card newCard, Client c) throws ClientNotFound, MaximumCardsAllowed, CardNotFound,
			BalanceCannotBeNegative, OutOfSizeCardNumber, OutOfBalance {

		Client client = em.find(Client.class, c.getId());
		if (client == null) {
			throw new ClientNotFound();
		} else {
			List<Card> cards = getCardsFromClient(c.getId());
			if (cards.size() < 2) {

				long s = (long) Math.pow(10, 17);

				if (newCard.getValor() < 0) {
					throw new BalanceCannotBeNegative();
				} else if (newCard.getNumero() > s) {
					throw new OutOfSizeCardNumber();
				}

				else if (newCard.getValor() > 5000000) {
					throw new OutOfBalance();
				}

				else {
					newCard.setOwner(c);
					em.persist(newCard);
					em.flush();
				}

			} else {
				throw new MaximumCardsAllowed();
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.sessions.CardManager#removeCard(java.lang.Integer)
	 */
	@Override
	public void removeCard(Long cardID) throws CardNotFound {

		Card card = em.find(Card.class, cardID);

		if (card == null) {
			throw new CardNotFound();
		} else {

			em.remove(card);
			em.flush();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.sessions.CardManager#getCardsFromClient(java.lang.Integer)
	 */
	@Override
	public List<Card> getCardsFromClient(Integer id) {
		Query query = em.createQuery("select card from Card card where " + " card.owner.id= :ID", Card.class);

		query.setParameter("ID", id);
		@SuppressWarnings("unchecked")
		List<Card> result = (List<Card>) query.getResultList();

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.sessions.CardManager#retrieveCards()
	 */
	@Override
	public List<Card> retrieveCards() throws CardNotFound {
		Query query = em.createQuery("select card from Card card order by card.numero", Card.class);

		@SuppressWarnings("unchecked")
		List<Card> result = (List<Card>) query.getResultList();

		if (result == null) {
			return new ArrayList<>();
		}

		return result;
	}
}
