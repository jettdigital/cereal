package net.jettdigital.cereal.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class TransactionDetail implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue
	private Long id;
	@ManyToOne
	private Organization requestedByOrg;
	private String description;
	private Date executedOn;
	@ManyToOne
	private State expectedNextState;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Organization getRequestedByOrg() {
		return requestedByOrg;
	}
	public void setRequestedByOrg(Organization requestedByOrg) {
		this.requestedByOrg = requestedByOrg;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public Date getExecutedOn() {
		return executedOn;
	}
	public void setExecutedOn(Date executedOn) {
		this.executedOn = executedOn;
	}
	
	public State getExpectedNextState() {
		return expectedNextState;
	}
	public void setExpectedNextState(State expectedNextState) {
		this.expectedNextState = expectedNextState;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TransactionDetail other = (TransactionDetail) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
}
