package net.jettdigital.cereal.repos;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import net.jettdigital.cereal.domain.Serial;
import net.jettdigital.cereal.domain.State;

@Repository
public interface SerialRepo extends CrudRepository<Serial,Long>{

	public Serial findOneByNumAndCurrentState(String num, State state);
	public Serial findOneByNum(String num);
}
