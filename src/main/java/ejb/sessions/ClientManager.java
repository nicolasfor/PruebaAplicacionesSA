package ejb.sessions;

import java.util.List;
import javax.ejb.Local;
import ejb.entities.Client;
import ejb.exception.ClientAddressSize;
import ejb.exception.ClientAlreadyExists;
import ejb.exception.ClientInvalidNameNumber;
import ejb.exception.ClientNameSize;
import ejb.exception.ClientNotFound;

/**
 * Session Bean implementation class ClientManager
 */
@Local
public interface ClientManager {
	// -----------------------------------------------------------------
	// Métodos
	// -----------------------------------------------------------------

	/**
	 * Éste método crea un cliente con los parámetros correspondientes
	 * @param newCliente corresponde al nuevo cliente a registrar en la base de datos 
	 * @throws ClientAlreadyExists en caso de que ya exista un cliente con el mismo id
	 * @throws ClientNameSize en caso de que el nombre del cliente exceda 40 caracteres
	 * @throws ClientAddressSize en caso de que la direccion del cliente exceda 50 caracteres
	 * @throws ClientInvalidNameNumber en caso de que el nombre del cliente contenga un numero
	 */
	public void createClient(Client newCliente) throws ClientAlreadyExists, ClientNameSize, ClientAddressSize, ClientInvalidNameNumber;

	/**
	 * Éste método elimina el cliente correspondiente al id del parámetro
	 * @param clientID corresponde al id del cliente a eliminar
	 * @throws ClientNotFound en caso de que el cliente con id pasado por parámetro no exista
	 */
	public void removeClient(Integer clientID) throws ClientNotFound;

	/** Éste método retorna todos los clientes registradas en la base de datos
	 * @return una lista con todos los clientes
	 */
	public List<Client> retrieveClients();

	/**
	 * Éste método retorna un cliente dado su id por parámetro
	 * @param id el identificador del cliente a buscar
	 * @return el cliente asociado al id
	 * @throws ClientNotFound en caso de que el cliente no exista en la base de datos.
	 */
	public Client getClient(final Integer id) throws ClientNotFound;
}
