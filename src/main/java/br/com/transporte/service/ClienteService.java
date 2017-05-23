
package br.com.transporte.service;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.transporte.dao.ClienteDao;
import br.com.transporte.enumeration.EnumTipoCadastro;
import br.com.transporte.model.Cliente;

@Service
public class ClienteService {

	@Inject
	private ClienteDao clienteDao;
	
	@Transactional(propagation = Propagation.REQUIRED)
	public Cliente save(final Cliente cliente){
		setDataCadastro(cliente);
		return this.clienteDao.save(cliente);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public Cliente update(Cliente cliente){
		setDataAlteracao(cliente);
		return this.clienteDao.update(cliente);
	}
	
	public List<Cliente> findGrid(final Cliente cliente){
		return this.clienteDao.findGrid(cliente);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(final Cliente cliente){
		this.clienteDao.remove(cliente);
	}
	
	public Cliente findOne(final Long idCliente){
		return this.clienteDao.find(idCliente);
	}
	
	private void setDataCadastro(final Cliente cliente){
		cliente.setDataCadastro(new Date());
		cliente.getEndereco().setDataCadastro(new Date());
		cliente.getEndereco().setTipoCadastro(EnumTipoCadastro.CLIENTE);
		cliente.getContrato().setDataCadastro(new Date());
	}
	
	private void setDataAlteracao(final Cliente cliente){
		cliente.setDataAlteracao(new Date());
		cliente.getEndereco().setDataAlteracao(new Date());
		cliente.getContrato().setDataAlteracao(new Date());
	}
}
