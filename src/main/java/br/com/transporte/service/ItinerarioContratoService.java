package br.com.transporte.service;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.transporte.dao.ItinerarioContratoDao;
import br.com.transporte.model.ItinerarioContrato;

@Service
public class ItinerarioContratoService {

	@Inject
	private ItinerarioContratoDao itinerarioContratoDao;
	
	@Transactional(propagation = Propagation.REQUIRED)
	public ItinerarioContrato save(ItinerarioContrato itinerarioContrato){
		return this.itinerarioContratoDao.save(itinerarioContrato);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public ItinerarioContrato update(ItinerarioContrato itinerarioContrato){
		return this.itinerarioContratoDao.update(itinerarioContrato);
	}
	
	public List<ItinerarioContrato> findGrid(final ItinerarioContrato itinerarioContrato){
		return this.itinerarioContratoDao.findGrid(itinerarioContrato);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(final ItinerarioContrato itinerarioContrato) {
		this.itinerarioContratoDao.remove(itinerarioContrato);
	}
}
