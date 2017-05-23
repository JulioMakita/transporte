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
import br.com.transporte.model.Veiculo;

@Repository
public class VeiculoDao extends GenericDao<Veiculo, Long>{

	public VeiculoDao(){
		super(Veiculo.class);
	}
	
	public List<Veiculo> findGrid(final Veiculo parcelaContrato){
		
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Veiculo> cq = builder.createQuery(Veiculo.class);
        Root<Veiculo> from = cq.from(Veiculo.class);
        Predicate predicate = builder.and();
        
        if(parcelaContrato.getChassi()  != null && StringUtils.isNotBlank(parcelaContrato.getChassi())){
        	predicate = builder.equal(from.<String>get("chassi"), parcelaContrato.getChassi());
        }
        
        if(parcelaContrato.getMarca()  != null  && StringUtils.isNotBlank(parcelaContrato.getMarca())){
        	predicate = builder.and(predicate, builder.like(from.<String>get("marca"), "%"+parcelaContrato.getMarca()+"%"));
        }
        
        if(parcelaContrato.getModelo()  != null  && StringUtils.isNotBlank(parcelaContrato.getModelo())){
        	predicate = builder.and(predicate, builder.like(from.<String>get("modelo"), "%"+parcelaContrato.getModelo()+"%"));
        }
        
        if(parcelaContrato.getAnoFabricacao() != null && parcelaContrato.getAnoFabricacao() > 0){
        	predicate = builder.equal(from.<Integer>get("anoFabricacao"), parcelaContrato.getAnoFabricacao());
        }
        
        TypedQuery<Veiculo> typedQuery = entityManager.createQuery(
        		cq.select(from).where(predicate).orderBy(builder.asc(from.get("anoFabricacao"))));
        
        return typedQuery.getResultList();
	}
}
