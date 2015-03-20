package net.jettdigital.cereal.services;

import java.util.List;

import net.jettdigital.cereal.domain.Serial;
import net.jettdigital.cereal.domain.SerialContent;

public interface SerialContentService {

	public List<SerialContent> history(Serial serial);
	public SerialContent current(Serial serial);
	public SerialContent persist(char[] data, Serial serial);
}
