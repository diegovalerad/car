package org.diego.tutorial.car.databases.jpa;

import java.util.List;

import javax.ejb.Stateless;
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
}
