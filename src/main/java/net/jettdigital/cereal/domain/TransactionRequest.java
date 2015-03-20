package net.jettdigital.cereal.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class TransactionRequest implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long requestedBy;
	private Long toState;
	private Long expectedNextState;
	private String serial;
	private String description;

	public Long getRequestedBy() {
		return requestedBy;
	}
	public void setRequestedBy(Long requestedBy) {
		this.requestedBy = requestedBy;
	}
	public Long getExpectedNextState() {
		return expectedNextState;
	}
	public void setExpectedNextState(Long expectedNextState) {
		this.expectedNextState = expectedNextState;
	}
	public Long getToState() {
		return toState;
	}
	public void setToState(Long toState) {
		this.toState = toState;
	}
	public String getSerial() {
		return serial;
	}
	public void setSerial(String serial) {
		this.serial = serial;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
	
}
