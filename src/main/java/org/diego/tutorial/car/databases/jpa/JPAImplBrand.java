package org.diego.tutorial.car.databases.jpa;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.diego.tutorial.car.model.Brand;

/**
 * Implementation of the JPA persistence with specific
 * brand methods
 */
@Stateless
public class JPAImplBrand extends JPAImpl {
	
	/**
	 * Gets all brands whose company is the one specified
	 * @param company Company of the brands
	 * @return List of brands
	 */
	public List<Brand> getAllBrandsFromCompany(String company){
		String query = "SELECT brand FROM Brand brand WHERE brand.company='" + company + "'";
		TypedQuery<Brand> createQuery = em.createQuery(query, Brand.class);
		
		List<Brand> brandsFromCompany = createQuery.getResultList();
		return brandsFromCompany;
	}

	/**
	 * Checks if exists a brand with the brand name and company specified
	 * @param brand Name of the brand
	 * @param company Company of the brand
	 * @return Boolean
	 */
	public boolean brandNameAndCompanyExists(String brand, String company) {
		String query = "SELECT brand "
						+ "FROM Brand brand "
						+ "WHERE brand.company='" + company + "' "
								+ "AND brand.brand='" + brand + "'";
		TypedQuery<Brand> createQuery = em.createQuery(query, Brand.class);
	
		try {
			createQuery.getSingleResult();
			return true;
		} catch (NoResultException e) {
			return false;
		}
	}
}
