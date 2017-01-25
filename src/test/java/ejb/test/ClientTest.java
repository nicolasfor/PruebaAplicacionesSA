package ejb.test;

import static org.junit.Assert.*;
import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
import ejb.exception.ClientAddressSize;
import ejb.exception.ClientAlreadyExists;
import ejb.exception.ClientInvalidNameNumber;
import ejb.exception.ClientNameSize;
import ejb.exception.ClientNotFound;
import ejb.sessions.ClientEJB;

/**
 * @author nicolasfor
 *
 */
@RunWith(Arquillian.class)
public class ClientTest {
	
	// -----------------------------------------------------------------
	// Deploy
	// -----------------------------------------------------------------

	@Deployment
	public static Archive<?> createFileTest() {

		Archive<?> arquivoTest = ShrinkWrap.create(WebArchive.class, "applicationTest.war")
				.addPackage(Client.class.getPackage()).addPackage(ClientEJB.class.getPackage())
				.addPackage(ClientAlreadyExists.class.getPackage())
				.addClasses(ClientEJB.class, Client.class, Card.class)
				.addAsResource("addClient-expected.xml").addAsResource("META-INF/persistence.xml")
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
		return arquivoTest;
	}
	
	// -----------------------------------------------------------------
	// Atributos
	// -----------------------------------------------------------------


	@Inject
	private ClientEJB testBean;

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
		em.createQuery("delete from Client").executeUpdate();
		utx.commit();
	}

	private void insertData() throws Exception {
		utx.begin();
		em.joinTransaction();
		em.persist(new Client(999996, "Martin", "Cali"));
		em.persist(new Client(999995, "Nick", "Medellin"));
		em.persist(new Client(999994, "Jordan", "Bucaramanga"));
		em.persist(new Client(999993, "David", "Pereira"));
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
	 * Test del método createClient(Client newCliente)
	 * @throws Exception
	 */
	@Test
	@ShouldMatchDataSet(value = "addClient-expected.xml", orderBy = "id")
	public final void testCreateClient() throws Exception {
		Integer id = 999997;
		testBean.createClient(new Client(999997, "Scott", "Bogota"));
		Client c = em.find(Client.class, id);
		assertEquals(c.getId(), id);
	}

	/**
	 * Test del método removeClient(Integer clientID)
	 * @throws Exception
	 */
	@Test
	@UsingDataSet("addClient-expected.xml")
	@ShouldMatchDataSet(value = "removeClient-update.xml", orderBy = "id")
	public final void testRemoveClient() throws Exception {
		Integer id = 999996;
		testBean.removeClient(id);
		assertNull(em.find(Client.class, id));
	}

	/**
	 * Test del método List<Client> retrieveClients()
	 * @throws Exception
	 */
	@Test
	public final void testRetrieveClients() throws Exception {

		String fetchingAllClients = "select c from Client c order by c.id";
		List<Client> clientes = em.createQuery(fetchingAllClients, Client.class).getResultList();
		assertEquals(testBean.retrieveClients(), clientes);

	}

	/**
	 * Test del método Client getClient(final Integer id)
	 * @throws ClientNotFound
	 */
	@Test
	@UsingDataSet(value = "input.xml")
	public final void testGetClient() throws ClientNotFound {
		Integer id = 999993;
		String expected = "David";
		Client client = testBean.getClient(id);
		assertEquals(client.getNombre(), expected);

	}

	/**
	 * Test del método getClient(final Integer id) con cliente no registrado en la base de datos
	 * @throws ClientNotFound
	 */
	@Test(expected = ClientNotFound.class)
	public final void testGetClientException() throws ClientNotFound {
		Integer id = 111111;
		testBean.getClient(id);

	}

	/**
	 * Test del método createClient(Client newCliente) con cliente que ya se encuentra registrado por identificacion y nombre respectivamente
	 * @throws ClientAlreadyExists
	 */
	@Test(expected = ClientAlreadyExists.class)
	public final void testCreateClientException() throws Exception {
		testBean.createClient(new Client(999994, "Jack", "Bucaramanga"));
		testBean.createClient(new Client(999981, "Nick", "Medellin"));

	}
	

	/**
	 * Test del método createClient(Client newCliente) con el nombre de un cliente que contiene mas de 40 caracteres
	 * @throws Exception
	 */
	@Test(expected = ClientNameSize.class)
	public final void testCreateClientNameSizeException() throws Exception {
		
		String str = new String(new char[10]).replace("\0", "hello");
		testBean.createClient(new Client(999980, str, "Bucaramanga"));

	}
	
	/**
	 * Test del método createClient(Client newCliente) con la direccion de un cliente que contiene mas de 50 caracteres
	 * @throws Exception
	 */
	@Test(expected = ClientAddressSize.class)
	public final void testCreateClientAddessSizeException() throws Exception {
		
		String str = new String(new char[20]).replace("\0", "hello");
		testBean.createClient(new Client(999980, "Juan", str));

	}
	
	/**
	 * Test del método createClient(Client newCliente) con un nombre que posee numeros
	 * @throws Exception
	 */
	@Test(expected = ClientInvalidNameNumber.class)
	public final void testCreateClientNameInvalid() throws Exception {
		
		String str = "Pet3r";
		testBean.createClient(new Client(999980, str, "Calle 24"));

	}


}
