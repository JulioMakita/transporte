package br.com.transporte.dao;

import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import br.com.transporte.config.GenericDao;
import br.com.transporte.model.ParcelaContrato;

@Repository
public class ParcelaContratoDao extends GenericDao<ParcelaContrato, Long>{

	public ParcelaContratoDao(){
		super(ParcelaContrato.class);
	}
	
	public List<ParcelaContrato> findGrid(final ParcelaContrato parcelaContrato){
		
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ParcelaContrato> cq = builder.createQuery(ParcelaContrato.class);
        Root<ParcelaContrato> from = cq.from(ParcelaContrato.class);
        Predicate predicate = builder.and();
        
        if(parcelaContrato.getQuantidadeParcela()  != null){
        	predicate = builder.equal(from.<String>get("quantidadeParcela"), parcelaContrato.getQuantidadeParcela());
        }
        
        if(parcelaContrato.getValorMensalParcela()  != null){
        	predicate = builder.equal(from.<String>get("valorMensalParcela"), parcelaContrato.getValorMensalParcela());
        }
        
        TypedQuery<ParcelaContrato> typedQuery = entityManager.createQuery(
        		cq.select(from).where(predicate).orderBy(builder.asc(from.get("quantidadeParcela"))));
        
        return typedQuery.getResultList();
	}
}
