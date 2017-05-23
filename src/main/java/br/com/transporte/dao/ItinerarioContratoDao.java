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
import br.com.transporte.model.ItinerarioContrato;

@Repository
public class ItinerarioContratoDao extends GenericDao<ItinerarioContrato, Long>{

	public ItinerarioContratoDao(){
		super(ItinerarioContrato.class);
	}
	
	public List<ItinerarioContrato> findGrid(final ItinerarioContrato itinerarioContrato){
		
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ItinerarioContrato> cq = builder.createQuery(ItinerarioContrato.class);
        Root<ItinerarioContrato> from = cq.from(ItinerarioContrato.class);
        Predicate predicate = builder.and();
       
        if(itinerarioContrato.getLocalSaida() != null && StringUtils.isNotBlank(itinerarioContrato.getLocalSaida())){
        	predicate = builder.and(predicate,  builder.like(from.<String>get("localSaida"), "%"+itinerarioContrato.getLocalSaida()+"%"));
        }
        
        if(itinerarioContrato.getLocalRetorno() != null && StringUtils.isNotBlank(itinerarioContrato.getLocalRetorno())){
        	predicate = builder.and(predicate,  builder.like(from.<String>get("localRetorno"), "%"+itinerarioContrato.getLocalRetorno()+"%"));
        }
        
        if(itinerarioContrato.getHoraSaida() != null){
        	predicate = builder.equal(from.<String>get("horaSaida"), itinerarioContrato.getHoraSaida());
        }
        
        if(itinerarioContrato.getHoraRetorno() != null){
        	predicate = builder.equal(from.<String>get("horaRetorno"), itinerarioContrato.getHoraRetorno());
        }
        
        TypedQuery<ItinerarioContrato> typedQuery = entityManager.createQuery(
        		cq.select(from).where(predicate).orderBy(builder.asc(from.get("horaSaida"))));
        
        return typedQuery.getResultList();
	}
}
