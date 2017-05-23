package br.com.transporte.service;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.transporte.dao.ValorContratoDao;
import br.com.transporte.model.ValorContrato;

@Service
public class ValorContratoService {

	@Inject
	private ValorContratoDao valorContratoDao;
	
	@Transactional(propagation = Propagation.REQUIRED)
	public ValorContrato save(ValorContrato valorContrato){
		return this.valorContratoDao.save(valorContrato);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public ValorContrato update(ValorContrato valorContrato){
		return this.valorContratoDao.update(valorContrato);
	}
	
	public List<ValorContrato> findGrid(final ValorContrato valorContrato){
		return this.valorContratoDao.findGrid(valorContrato);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(final ValorContrato valorContrato) {
		this.valorContratoDao.remove(valorContrato);
	}
}
