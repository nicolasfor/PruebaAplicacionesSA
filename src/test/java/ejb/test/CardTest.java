package ejb.test;

import static org.junit.Assert.*;
import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.UserTransaction;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.persistence.ShouldMatchDataSet;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import ejb.entities.Card;
import ejb.entities.Client;
import ejb.exception.BalanceCannotBeNegative;
import ejb.exception.CardAlreadyExists;
import ejb.exception.CardNotFound;
import ejb.exception.ClientNotFound;
import ejb.exception.MaximumCardsAllowed;
import ejb.exception.OutOfBalance;
import ejb.exception.OutOfSizeCardNumber;
import ejb.sessions.CardEJB;
import ejb.sessions.ClientEJB;

/**
 * @author nicolasfor
 *
 */
@RunWith(Arquillian.class)
public class CardTest {

	// -----------------------------------------------------------------
	// Deploy
	// -----------------------------------------------------------------

	@Deployment
	public static Archive<?> createFileTest() {

		Archive<?> arquivoTest = ShrinkWrap.create(WebArchive.class, "applicationTest.war")
				.addPackage(Card.class.getPackage()).addPackage(ClientEJB.class.getPackage())
				.addPackage(CardAlreadyExists.class.getPackage()).addClasses(CardEJB.class, Card.class, Client.class)
				.addAsResource("META-INF/persistence.xml").addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
		return arquivoTest;
	}

	// -----------------------------------------------------------------
	// Atributos
	// -----------------------------------------------------------------

	@Inject
	private CardEJB testBean;

	@PersistenceContext
	private EntityManager em;

	@Inject
	UserTransaction utx;

	// -----------------------------------------------------------------
	// Configuración
	// -----------------------------------------------------------------

	@Before
	public void preparePersistenceTest() throws Exception {
		clearData();
		insertData();
		startTransaction();
	}

	private void clearData() throws Exception {
		utx.begin();
		em.joinTransaction();
		System.out.println("Dumping old records...");
		em.createQuery("delete from Card").executeUpdate();
		utx.commit();
	}

	private void insertData() throws Exception {
		utx.begin();
		em.joinTransaction();
		Integer id = 999995;
		Client client = em.find(Client.class, id);
		Card c = new Card((long) 999999, 200000);
		c.setOwner(client);
		Integer id2 = 999994;
		Client client2 = em.find(Client.class, id2);
		Card c2 = new Card((long) 999992, 50000);
		c2.setOwner(client2);
		Integer id3 = 999995;
		Client client3 = em.find(Client.class, id3);
		Card c3 = new Card((long) 999991, 60000);
		c3.setOwner(client3);
		em.persist(c);
		em.persist(c2);
		em.persist(c3);
		utx.commit();
		em.clear();
	}

	private void startTransaction() throws Exception {
		utx.begin();
		em.joinTransaction();
	}

	@After
	public void commitTransaction() throws Exception {
		utx.commit();
	}

	// -----------------------------------------------------------------
	// Pruebas
	// -----------------------------------------------------------------

	/**
	 * Test del método createCard(Card newCard, Client c)
	 * 
	 * @throws Exception
	 */
	@Test
	@UsingDataSet("input.xml")
	@ShouldMatchDataSet(value = "addCard-expected.xml", orderBy = "id")
	public final void testCreateCard() throws Exception {
		Integer id = 999994;
		Long idCard = (long) 999998;
		Client client = em.find(Client.class, id);
		Card c = new Card(idCard, 100000);
		testBean.createCard(c, client);
		Card testCard = em.find(Card.class, idCard);
		assertEquals(testCard.getOwner().getId(), c.getOwner().getId());
	}

	/**
	 * Test del método removeCard(Integer cardID)
	 * 
	 * @throws Exception
	 */
	@Test
	@UsingDataSet("addCard-expected.xml")
	@ShouldMatchDataSet(value = "removeCard-update.xml", orderBy = "id")
	public final void testRemoveCard() throws Exception {
		Long id = (long) 999992;
		testBean.removeCard(id);
		assertNull(em.find(Card.class, id));
	}

	/**
	 * Test del método getCardsFromClient(Integer id)
	 * 
	 * @throws Exception
	 */
	@Test
	@UsingDataSet("removeCard-update.xml")
	public final void testRetrieveCardsFromClient() throws Exception {
		Integer id = 999995;
		String fetchingAllCards = "select card from Card card where card.owner.id=" + id;
		Query query = em.createQuery(fetchingAllCards, Card.class);
		@SuppressWarnings("unchecked")
		List<Card> cards = query.getResultList();
		assertEquals(testBean.getCardsFromClient(id).size(), cards.size());
		assertEquals(testBean.getCardsFromClient(id).get(0).getOwner().getNombre(), "Nick");

	}

	/**
	 * Test del método retrieveCards()
	 * 
	 * @throws Exception
	 */
	@Test
	@UsingDataSet("input.xml")
	public final void testRetrieveCards() throws Exception {

		String fetchingAllCards = "select c from Card c order by c.numero";
		List<Card> cards = em.createQuery(fetchingAllCards, Card.class).getResultList();
		assertEquals(testBean.retrieveCards(), cards);

	}

	/**
	 * Test del método createCard(Card newCard, Client c) con un cliente que
	 * posee 2 tarjetas
	 * 
	 * @throws MaximumCardsAllowed
	 * @throws ClientNotFound
	 * @throws CardNotFound
	 * @throws BalanceCannotBeNegative
	 */
	@Test(expected = MaximumCardsAllowed.class)
	public final void testCreateCardMaximumException() throws Exception {
		Long idCard = (long) 999990;
		Integer id = 999995;
		Client client = em.find(Client.class, id);
		Card c = new Card(idCard, 80000);
		testBean.createCard(c, client);

	}

	/**
	 * Test del método createCard(Card newCard, Client c) que contiene un numero
	 * negativo
	 * 
	 * @throws Exception
	 */
	@Test(expected = BalanceCannotBeNegative.class)
	public final void testCreateCardNegativeBalance() throws Exception {
		Long idCard = (long) 999989;
		Integer id = 999993;
		Client client = em.find(Client.class, id);
		Card c = new Card(idCard, -500);
		testBean.createCard(c, client);

	}

	/**
	 * Test del método createCard(Card newCard, Client c) que contiene un numero
	 * superior a 16 numeros
	 * 
	 * @throws Exception
	 */
	@Test(expected = OutOfSizeCardNumber.class)
	public final void testCreateOutOfSize() throws Exception {
		Long idCard = (long) (9 * Math.pow(10, 17));
		Integer id = 999993;
		Client client = em.find(Client.class, id);
		Card c = new Card(idCard, 500);
		testBean.createCard(c, client);

	}

	/**
	 * Test del método createCard(Card newCard, Client c) que contiene un
	 * balance superior a $5.000.000
	 * 
	 * @throws Exception
	 */
	@Test(expected = OutOfBalance.class)
	public final void testCreateOutOfBalance() throws Exception {
		Long idCard = (long) 999988;
		Integer id = 999997;
		Client client = em.find(Client.class, id);
		Card c = new Card(idCard, 5000001);
		testBean.createCard(c, client);

	}

}
