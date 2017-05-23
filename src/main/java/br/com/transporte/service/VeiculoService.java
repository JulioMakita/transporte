package br.com.transporte.service;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.transporte.dao.VeiculoDao;
import br.com.transporte.model.Veiculo;

@Service 
public class VeiculoService {
	
	@Inject
	private VeiculoDao veiculoDao;
	
	@Transactional(propagation = Propagation.REQUIRED)
	public Veiculo save(final Veiculo veiculo){
		setDataCadastro(veiculo);
		return this.veiculoDao.save(veiculo);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public Veiculo update(final Veiculo veiculo){
		setDataAlteracao(veiculo);
		return this.veiculoDao.update(veiculo);
	}

	public List<Veiculo> findGrid(final Veiculo veiculo){
		return this.veiculoDao.findGrid(veiculo);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(final Veiculo veiculo){
		this.veiculoDao.remove(veiculo);
	}
	
	public Veiculo findOne(final Long id){
		return this.veiculoDao.find(id);
	}
	
	private void setDataCadastro(Veiculo veiculo) {
		veiculo.setDataCadastro(new Date());
		
	}

	private void setDataAlteracao(Veiculo veiculo) {
		veiculo.setDataAlteracao(new Date());
	}
}
