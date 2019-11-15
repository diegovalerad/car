package org.diego.tutorial.car.databases.jpa;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.diego.tutorial.car.model.Country;

/**
 * Implementation of the JPA persistence with specific
 * country methods
 */
public class JPAImplCountry extends JPAImpl {

	/**
	 * Checks if a country exists
	 * @param countryName Name of the country to check
	 * @return Boolean
	 */
	public boolean countryAlreadyExists(String countryName) {
		String query = "SELECT country "
					+ "FROM Country country "
					+ "WHERE country.countryName='" + countryName + "'";
		TypedQuery<Country> createQuery = em.createQuery(query, Country.class);
		
		try {
			createQuery.getSingleResult();
			return true;
		} catch (NoResultException e) {
			return false;
		}
	}

}
