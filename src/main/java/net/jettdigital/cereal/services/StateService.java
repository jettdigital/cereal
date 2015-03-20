package net.jettdigital.cereal.services;


import net.jettdigital.cereal.domain.TransactionRequest;

public interface StateService {

	public void changeState(TransactionRequest req);

}
