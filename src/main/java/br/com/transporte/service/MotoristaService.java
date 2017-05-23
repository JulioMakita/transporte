package br.com.transporte.service;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.transporte.dao.MotoristaDao;
import br.com.transporte.enumeration.EnumTipoCadastro;
import br.com.transporte.model.Motorista;

@Service
public class MotoristaService {

	@Inject
	private MotoristaDao motoristaDao;
	
	@Transactional(propagation = Propagation.REQUIRED)
	public Motorista save(final Motorista motorista){
		setDataCadastro(motorista);
		return this.motoristaDao.save(motorista);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public Motorista update(final Motorista motorista){
		setDataAlteracao(motorista);
		return this.motoristaDao.update(motorista);
	}
	
	public List<Motorista> findGrid(final Motorista motorista){
		return this.motoristaDao.findGrid(motorista);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(final Motorista motorista){
		this.motoristaDao.remove(motorista);
	}
	
	public Motorista findOne(final Long id){
		return this.motoristaDao.find(id);
	}
	
	private void setDataCadastro(final Motorista motorista){
		motorista.setDataCadastro(new Date());
		motorista.getEndereco().setTipoCadastro(EnumTipoCadastro.MOTORISTA);
		motorista.getEndereco().setDataCadastro(new Date());
	}
	
	private void setDataAlteracao(final Motorista motorista){
		motorista.setDataAlteracao(new Date());
		motorista.getEndereco().setDataAlteracao(new Date());
	}
}
