package ejb.entities;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * Entity implementation class for Entity: Card
 *
 */

@Entity
@Table(name = "card")
public class Card implements Serializable {

	// -----------------------------------------------------------------
	// Atributos
	// -----------------------------------------------------------------

	@Id
	@Column(name = "NUMERO", length = 16)
	private Long numero;

	@Column(name = "VALOR")
	@Max(value = 5000000, message = "The field must be less or equals than 5000000 numbers")
	@Min(value = 0, message = "The field must be greater or equals than 0")
	private int valor;

	@ManyToOne
	@JoinColumns({ @JoinColumn(name = "ID_CLIENT", referencedColumnName = "ID") })
	private Client owner;

	private static final long serialVersionUID = 1L;

	// -----------------------------------------------------------------
	// Constructores
	// -----------------------------------------------------------------

	public Card() {
	}

	public Card(Long numero, int valor) {
		super();
		this.numero = numero;
		this.valor = valor;
	}

	// -----------------------------------------------------------------
	// Métodos
	// -----------------------------------------------------------------

	/**
	 * @return
	 */
	public Long getNumero() {
		return this.numero;
	}

	public void setNumero(Long numero) {
		this.numero = numero;
	}

	public int getValor() {
		return this.valor;
	}

	public void setValor(int valor) {
		this.valor = valor;
	}

	public Client getOwner() {
		return owner;
	}

	public void setOwner(Client owner) {
		this.owner = owner;
	}

}
