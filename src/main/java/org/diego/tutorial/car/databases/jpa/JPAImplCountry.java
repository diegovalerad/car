package org.diego.tutorial.car.databases.jpa;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.diego.tutorial.car.model.Country;

/**
 * Implementation of the JPA persistence with specific
 * country methods
 */
@Stateless
public class JPAImplCountry extends JPAImpl {
	
	/**
	 * Checks if a country exists
	 * @param countryName Name of the country to check
	 * @return Boolean
	 */
	public boolean countryAlreadyExists(String countryName) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Country> query = cb.createQuery(Country.class);
		Root<Country> countries = query.from(Country.class);
		
		ParameterExpression<String> p = cb.parameter(String.class);
		Predicate predicateCountryName = cb.equal(countries.get("countryName"), p);
		
		query.select(countries)
				.where(predicateCountryName);
		
		TypedQuery<Country> typedQuery = em.createQuery(query);
		typedQuery.setParameter(p, countryName);
		
		try {
			typedQuery.getSingleResult();
			return true;
		} catch (NoResultException e) {
			return false;
		}
	}

}
