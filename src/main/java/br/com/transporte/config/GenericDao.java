package br.com.transporte.config;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

public abstract class GenericDao<T, I extends Serializable> {

   @PersistenceContext
   protected EntityManager entityManager;

   private Class<T> persistedClass;

   protected GenericDao() {}

   protected GenericDao(Class<T> persistedClass) {
       this();
       this.persistedClass = persistedClass;
   }
   
   public T save(T entity) {
	   this.entityManager.persist(entity);
       return entity;
   }

   public T update(T entity) {
	   return this.entityManager.merge(entity);
   }

   public void remove(T entity) { 
	   entity = this.entityManager.merge(entity);
       this.entityManager.remove(entity);
   }

   public List<T> getList() {
       CriteriaBuilder builder = entityManager.getCriteriaBuilder();
       CriteriaQuery<T> query = builder.createQuery(persistedClass);
       query.from(persistedClass);
       return entityManager.createQuery(query).getResultList();
   }

   public T find(I id) {
       return entityManager.find(persistedClass, id);
   }
}