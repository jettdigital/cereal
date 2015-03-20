package net.jettdigital.cereal.repos;

import net.jettdigital.cereal.domain.Organization;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface OrganizationRepo extends CrudRepository<Organization,Long>{

}
