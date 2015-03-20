package net.jettdigital.cereal.repos;

import java.util.List;

import net.jettdigital.cereal.domain.TransactionDetail;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionDetailRepo extends CrudRepository<TransactionDetail, Long>{
	
}
