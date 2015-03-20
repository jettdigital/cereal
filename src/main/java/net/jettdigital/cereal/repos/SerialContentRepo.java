package net.jettdigital.cereal.repos;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import net.jettdigital.cereal.domain.Serial;
import net.jettdigital.cereal.domain.SerialContent;

public interface SerialContentRepo extends CrudRepository<SerialContent, Long>{

	List<SerialContent> findBySerialOrderByCreatedOnDesc(Serial serial);
	SerialContent findOneBySerialOrderByCreatedOnDesc(Serial serial);
	
}
