package net.jettdigital.cereal.repos;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import net.jettdigital.cereal.domain.Organization;
import net.jettdigital.cereal.domain.State;
@Repository
public interface StateRepo extends CrudRepository<State,Long>{
	                                   
	public State findOneByIdAndOrganization(Long id, Organization organization);
	public State findOneByNameAndOrganization(String name, Organization organization);
	public List<State> findByOrganization(Organization organization);
}
