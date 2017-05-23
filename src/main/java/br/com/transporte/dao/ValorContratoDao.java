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
import br.com.transporte.model.ValorContrato;

@Repository
public class ValorContratoDao extends GenericDao<ValorContrato, Long>{

	public ValorContratoDao(){
		super(ValorContrato.class);
	}
	
	public List<ValorContrato> findGrid(final ValorContrato valorContrato){
		
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ValorContrato> cq = builder.createQuery(ValorContrato.class);
        Root<ValorContrato> from = cq.from(ValorContrato.class);
        Predicate predicate = builder.and();
        
        if(valorContrato.getValorContrato() != null){
        	predicate = builder.equal(from.<String>get("valorContrato"), valorContrato.getValorContrato());
        }
        
        if(valorContrato.getValorContratoExtenso()  != null && StringUtils.isNotBlank(valorContrato.getValorContratoExtenso())){
        	predicate = builder.equal(from.<String>get("valorContratoExtenso"), valorContrato.getValorContratoExtenso());
        }
        
        TypedQuery<ValorContrato> typedQuery = entityManager.createQuery(
        		cq.select(from).where(predicate).orderBy(builder.asc(from.get("valorContrato"))));
        
        return typedQuery.getResultList();
	}
}
