package ejb.sessions;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import ejb.entities.Client;
import ejb.exception.ClientAddressSize;
import ejb.exception.ClientAlreadyExists;
import ejb.exception.ClientInvalidNameNumber;
import ejb.exception.ClientNameSize;
import ejb.exception.ClientNotFound;

/**
 * Session Bean implementation class ClienteEJB
 */
@Stateless
@LocalBean
public class ClientEJB implements ClientManager {

	@PersistenceContext(unitName = "PruebaAplicacionesSA")
	EntityManager em;

	/**
	 * Default constructor.
	 */
	public ClientEJB() {

	}

	// -----------------------------------------------------------------
	// Métodos
	// -----------------------------------------------------------------

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.sessions.ClientManager#createClient(ejb.entities.Client)
	 */
	public void createClient(Client newCliente) throws ClientAlreadyExists, ClientNameSize, ClientAddressSize, ClientInvalidNameNumber {
		Query query = em.createQuery("select client from Client client where " + "client.id = :ID");

		query.setParameter("ID", newCliente.getId());

		Query query2 = em.createQuery("select client from Client client where " + "client.nombre = :NAME");

		query2.setParameter("NAME", newCliente.getNombre());

		if (!query.getResultList().isEmpty() || !query2.getResultList().isEmpty()) {
			throw new ClientAlreadyExists();
		} else {
			if (newCliente.getNombre().length() > 40) {
				throw new ClientNameSize();
			} else if (newCliente.getNombre().matches(".*\\d+.*")) {
				throw new ClientInvalidNameNumber();
			} else if (newCliente.getDireccion().length() > 50) {
				throw new ClientAddressSize();
			} else {
				em.persist(newCliente);
				em.flush();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.sessions.ClientManager#removeClient(java.lang.Integer)
	 */
	public void removeClient(Integer clientID) throws ClientNotFound {
		Client client = em.find(Client.class, clientID);

		if (client == null) {
			throw new ClientNotFound();
		} else {

			em.remove(client);
			em.flush();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.sessions.ClientManager#getClient(java.lang.Integer)
	 */
	public Client getClient(final Integer id) throws ClientNotFound {
		Client client = em.find(Client.class, id);
		if (client == null) {
			throw new ClientNotFound();
		}
		return client;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.sessions.ClientManager#retrieveClients()
	 */
	@Override
	public List<Client> retrieveClients() {
		Query query = em.createQuery("select client from Client client", Client.class);

		@SuppressWarnings("unchecked")
		List<Client> result = (List<Client>) query.getResultList();

		if (result == null) {
			return new ArrayList<Client>();
		}

		return result;
	}

}
