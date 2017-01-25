package ejb.entities;

import java.io.Serializable;
import java.lang.String;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.Size;

/**
 * Entity implementation class for Entity: Client
 *
 */
@Entity
@Table(name = "client")
public class Client implements Serializable {

	// -----------------------------------------------------------------
	// Atributos
	// -----------------------------------------------------------------

	@Id
	@Column(name = "ID")
	private Integer id;

	@Column(name = "NOMBRE")
	@Size(max = 40, message = "The field must be less or equals than 40 characters")
	private String nombre;

	@Column(name = "DIRECCION")
	@Size(max = 50, message = "The field must be less or equals than 50 characters")
	private String direccion;

	@OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
	private List<Card> phones;

	private static final long serialVersionUID = 1L;

	// -----------------------------------------------------------------
	// Constructores
	// -----------------------------------------------------------------

	public Client(Integer id, String nombre, String direccion) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.direccion = direccion;
	}

	public Client() {

	}

	// -----------------------------------------------------------------
	// Métodos
	// -----------------------------------------------------------------

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDireccion() {
		return this.direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

}
