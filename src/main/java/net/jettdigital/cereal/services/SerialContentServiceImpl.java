package net.jettdigital.cereal.services;

import java.util.List;

import net.jettdigital.cereal.domain.Serial;
import net.jettdigital.cereal.domain.SerialContent;
import net.jettdigital.cereal.repos.SerialContentRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SerialContentServiceImpl implements SerialContentService {

	@Autowired
	private SerialContentRepo serialContentRepo;
	
	@Override
	public List<SerialContent> history(Serial serial) {
		return serialContentRepo.findBySerialOrderByCreatedOnDesc(serial);
	}

	@Override
	public SerialContent current(Serial serial) {
		return serialContentRepo.findOneBySerialOrderByCreatedOnDesc(serial);
	}

	@Override
	public SerialContent persist(char[] data, Serial serial) {
		
		SerialContent content = new SerialContent();
		content.setData(data);
		content.setSerial(serial);
		content.setAssociatedState(serial.getCurrentState());
		return serialContentRepo.save(content);
		
	}

}
