package br.com.transporte.dao;

import org.springframework.stereotype.Repository;

import br.com.transporte.config.GenericDao;
import br.com.transporte.model.Contrato;

@Repository
public class ContratoDao extends GenericDao<Contrato, Long>{

	public ContratoDao(){
		super(Contrato.class);
	}
}
