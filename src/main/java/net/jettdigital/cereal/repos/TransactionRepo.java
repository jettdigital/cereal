package net.jettdigital.cereal.repos;

import java.util.List;

import net.jettdigital.cereal.domain.Transaction;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepo extends CrudRepository<Transaction, Long>{
	
	public List<Transaction> findAllBySerialNum(String serialNum);
	public List<Transaction> findAllBySerialNumOrderByDetailExecutedOnDesc(String serialNum);
}
