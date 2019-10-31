package org.diego.tutorial.car.databases.jpa;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import org.diego.tutorial.car.databases.IJPA;
import org.diego.tutorial.car.exceptions.DataNotFoundException;

@Stateless
public class JPAImpl implements IJPA {
	@PersistenceContext(unitName = "postg")
	protected EntityManager em;

	@Override
	public <T> List<T> getAll(Class<T> type) {
		String classType = type.getName();
		String query = "SELECT d FROM " + classType + " d";
		TypedQuery<T> createQuery = em.createQuery(query, type);
		
		List<T> dataList = createQuery.getResultList();
		return dataList;
	}

	@Override
	public <T> T get(Class<T> type, long id){
		T t = em.find(type, id);
		
		if (t == null)
			throw new DataNotFoundException("Trying to get an object that does not exists");
		return t;
	}

	@Override
	public <T> T add(T entity) {
		try {
			em.persist(entity);
		} catch (PersistenceException e) {
			throw new DataNotFoundException("Trying to add an object that already exists");
		}
		
		return entity;
	}

	@Override
	public <T> T update(T entity){
		return em.merge(entity);
	}

	@Override
	public <T> T delete(T entity) {
		em.remove(entity);
		return entity;
	}

}
