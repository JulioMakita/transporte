package br.com.transporte.dao;

import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import br.com.transporte.config.GenericDao;
import br.com.transporte.enumeration.EnumSexo;
import br.com.transporte.model.Cliente;

@Repository
public class ClienteDao extends GenericDao<Cliente, Long>{

	public ClienteDao() {
		super(Cliente.class);
	}
	
	public List<Cliente> findGrid(final Cliente cliente){
		
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Cliente> cq = builder.createQuery(Cliente.class);
        Root<Cliente> from = cq.from(Cliente.class);
        Predicate predicate = builder.and();
        
        if(cliente.getNome() != null && StringUtils.isNotBlank(cliente.getNome())){
        	predicate = builder.and(predicate, 
        	        builder.like(from.<String>get("nome"), "%"+cliente.getNome()+"%"));
        }
        
        if(cliente.getSexo() != null && !cliente.getSexo().equals(EnumSexo.TODOS)){
        	predicate = builder.equal(from.<EnumSexo>get("sexo"), cliente.getSexo());
        }
        
        if(cliente.getRg() != null && StringUtils.isNotBlank(cliente.getRg())){
        	predicate = builder.equal(from.<String>get("rg"), cliente.getRg());
        }
        
        if(cliente.getCpf() != null && StringUtils.isNotBlank(cliente.getCpf())){
        	predicate = builder.equal(from.<String>get("cpf"), cliente.getCpf());
        }
        
        TypedQuery<Cliente> typedQuery = entityManager.createQuery(
        		cq.select(from).where(predicate).orderBy(builder.asc(from.get("nome"))));
        
        return typedQuery.getResultList();
	}
}
