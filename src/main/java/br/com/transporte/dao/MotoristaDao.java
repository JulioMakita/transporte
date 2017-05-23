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
import br.com.transporte.model.Motorista;

@Repository
public class MotoristaDao extends GenericDao<Motorista, Long>{

	public MotoristaDao(){
		super(Motorista.class);
	}
	
	public List<Motorista> findGrid(final Motorista motorista){
		
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Motorista> cq = builder.createQuery(Motorista.class);
        Root<Motorista> from = cq.from(Motorista.class);
        Predicate predicate = builder.and();
        
        if(motorista.getNome() != null && StringUtils.isNotBlank(motorista.getNome())){
        	predicate = builder.and(predicate, 
        	        builder.like(from.<String>get("nome"), "%"+motorista.getNome()+"%"));
        }
        
        if(motorista.getCnh() != null && StringUtils.isNotBlank(motorista.getCnh())){
        	predicate = builder.equal(from.<String>get("cnh"), motorista.getCnh());
        }
        
        if(motorista.getSexo() != null && !motorista.getSexo().equals(EnumSexo.TODOS)){
        	predicate = builder.equal(from.<EnumSexo>get("sexo"), motorista.getSexo());
        }
        
        if(motorista.getRg() != null && StringUtils.isNotBlank(motorista.getRg())){
        	predicate = builder.equal(from.<String>get("rg"), motorista.getRg());
        }
        
        if(motorista.getCpf() != null && StringUtils.isNotBlank(motorista.getCpf())){
        	predicate = builder.equal(from.<String>get("cpf"), motorista.getCpf());
        }
        
        TypedQuery<Motorista> typedQuery = entityManager.createQuery(
        		cq.select(from).where(predicate).orderBy(builder.asc(from.get("nome"))));
        
        return typedQuery.getResultList();
	}
}
