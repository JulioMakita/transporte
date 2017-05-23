package br.com.transporte.service;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.transporte.dao.ParcelaContratoDao;
import br.com.transporte.model.ParcelaContrato;

@Service
public class ParcelaContratoService {

	@Inject
	private ParcelaContratoDao parcelaContratoDao;
	
	@Transactional(propagation = Propagation.REQUIRED)
	public ParcelaContrato save(ParcelaContrato parcelaContrato){
		return this.parcelaContratoDao.save(parcelaContrato);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public ParcelaContrato update(ParcelaContrato parcelaContrato){
		return this.parcelaContratoDao.update(parcelaContrato);
	}
	
	public List<ParcelaContrato> findGrid(final ParcelaContrato parcelaContrato){
		return this.parcelaContratoDao.findGrid(parcelaContrato);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(final ParcelaContrato parcelaContrato) {
		this.parcelaContratoDao.remove(parcelaContrato);
	}
}
