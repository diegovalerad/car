package org.diego.tutorial.car.databases.jpa;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

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
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Brand> cq = cb.createQuery(Brand.class);
		
		Root<Brand> brands = cq.from(Brand.class);
		ParameterExpression<String> paramCompany = cb.parameter(String.class);
		Predicate predCompany = cb.equal(brands.get("company"), paramCompany);
		
		cq.select(brands)
			.where(predCompany);
		
		TypedQuery<Brand> typedQuery = em.createQuery(cq);
		typedQuery.setParameter(paramCompany, company);
		List<Brand> brandsFromCompany = typedQuery.getResultList();
		return brandsFromCompany;
	}
	
	/**
	 * Checks if exists a brand with the brand name and company specified
	 * @param brand Name of the brand
	 * @param company Company of the brand
	 * @return Boolean
	 */
	public boolean brandNameAndCompanyExists(String brand, String company) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Brand> cq = cb.createQuery(Brand.class);
		
		Root<Brand> brands = cq.from(Brand.class);
		
		ParameterExpression<String> paramCompany = cb.parameter(String.class);
		Predicate predCompany = cb.equal(brands.get("company"), paramCompany);
		ParameterExpression<String> paramBrand = cb.parameter(String.class);
		Predicate predBrand = cb.equal(brands.get("brand"), paramBrand);
		
		Predicate predCompanyAndBrand = cb.and(predCompany, predBrand);
		
		cq.select(brands)
			.where(predCompanyAndBrand);
		
		TypedQuery<Brand> typedQuery = em.createQuery(cq);
		typedQuery.setParameter(paramCompany, company);
		typedQuery.setParameter(paramBrand, brand);

		try {
			typedQuery.getSingleResult();
			return true;
		} catch (NoResultException e) {
			return false;
		}
	}
}
