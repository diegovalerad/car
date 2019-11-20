package org.diego.tutorial.car.databases.jpa;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.diego.tutorial.car.model.Brand;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Set of units for the {@link JPAImplBrand} class
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class JPAImplBrandTest {
	@InjectMocks
	private JPAImplBrand jpaImplBrand;
	@Mock
	private EntityManager em;
	@Mock
	private CriteriaQuery<Brand> cq;
	
	private String company;
	private String brandName;
	
	
	@Before
	public void setup() {
		brandName = "brandName";
		company = "company";
		
		setupCriteriaBuilder();
	}
	
	@SuppressWarnings("unchecked")
	private void setupCriteriaBuilder() {
		CriteriaBuilder cb = Mockito.mock(CriteriaBuilder.class);
		Root<Brand> rootBrands = Mockito.mock(Root.class);
		ParameterExpression<String> paramString = Mockito.mock(ParameterExpression.class);
		Predicate predicate = Mockito.mock(Predicate.class);
		
		Mockito.when(em.getCriteriaBuilder())
				.thenReturn(cb);
		Mockito.when(cb.createQuery(Brand.class))
				.thenReturn(cq);
		Mockito.when(cq.from(Brand.class))
				.thenReturn(rootBrands);
		Mockito.when(cb.parameter(String.class))
				.thenReturn(paramString);
		Mockito.when(cb.equal(Mockito.any(), Mockito.any()))
				.thenReturn(predicate);
		Mockito.when(cb.and(Mockito.any(), Mockito.any()))
				.thenReturn(predicate);
		Mockito.when(cq.select(rootBrands))
				.thenReturn(cq);
		Mockito.when(cq.where(predicate))
				.thenReturn(cq);
	}
	
	@Test
	public void testGetAllBrandsFromCompany() {
		List<Brand> brandsFromCompany = new ArrayList<Brand>();
		
		@SuppressWarnings("unchecked")
		TypedQuery<Brand> typedQuery = Mockito.mock(TypedQuery.class);
		
		Mockito.when(em.createQuery(cq))
				.thenReturn(typedQuery);
		Mockito.when(typedQuery.getResultList())
				.thenReturn(brandsFromCompany);
		
		assertEquals(brandsFromCompany, jpaImplBrand.getAllBrandsFromCompany(company));
		Mockito.verify(em).createQuery(cq);
	}
	
	@Test 
	public void testBrandNameAndCompanyExists() {
		@SuppressWarnings("unchecked")
		TypedQuery<Brand> typedQuery = Mockito.mock(TypedQuery.class);
		
		Mockito.when(em.createQuery(cq))
				.thenReturn(typedQuery);
		Mockito.when(typedQuery.getSingleResult())
				.thenReturn(new Brand());
		
		assertEquals(true, jpaImplBrand.brandNameAndCompanyExists(brandName, company));
		Mockito.verify(em).createQuery(cq);
	}
	
	@Test 
	public void testBrandNameAndCompanyNotExists() {
		@SuppressWarnings("unchecked")
		TypedQuery<Brand> typedQuery = Mockito.mock(TypedQuery.class);
		
		Mockito.when(em.createQuery(cq))
				.thenReturn(typedQuery);
		Mockito.when(typedQuery.getSingleResult())
				.thenThrow(NoResultException.class);
		
		assertEquals(false, jpaImplBrand.brandNameAndCompanyExists(brandName, company));
		Mockito.verify(em).createQuery(cq);
	}

}
