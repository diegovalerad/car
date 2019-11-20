package org.diego.tutorial.car.databases.jpa;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

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
	
	private String company;
	private String brandName;
	
	@Before
	public void setup() {
		brandName = "brandName";
		company = "company";
	}
	
	@Test
	public void testGetAllBrandsFromCompany() {
		String query = "SELECT brand FROM Brand brand WHERE brand.company='" + company + "'";
		List<Brand> brandsFromCompany = new ArrayList<Brand>();
		@SuppressWarnings("unchecked")
		TypedQuery<Brand> typedQuery = Mockito.mock(TypedQuery.class);
		
		Mockito.when(em.createQuery(query, Brand.class))
				.thenReturn(typedQuery);
		Mockito.when(typedQuery.getResultList())
				.thenReturn(brandsFromCompany);
		
		assertEquals(brandsFromCompany, jpaImplBrand.getAllBrandsFromCompany(company));
	}
	
	@Test 
	public void testBrandNameAndCompanyExists() {
		String query = "SELECT brand "
				+ "FROM Brand brand "
				+ "WHERE brand.company='" + company + "' "
						+ "AND brand.brand='" + brandName + "'";
		@SuppressWarnings("unchecked")
		TypedQuery<Brand> typedQuery = Mockito.mock(TypedQuery.class);
		
		Mockito.when(em.createQuery(query, Brand.class))
				.thenReturn(typedQuery);
		Mockito.when(typedQuery.getSingleResult())
				.thenReturn(new Brand());
		
		assertEquals(true, jpaImplBrand.brandNameAndCompanyExists(brandName, company));
	}
	
	@Test 
	public void testBrandNameAndCompanyNotExists() {
		String query = "SELECT brand "
				+ "FROM Brand brand "
				+ "WHERE brand.company='" + company + "' "
						+ "AND brand.brand='" + brandName + "'";
		@SuppressWarnings("unchecked")
		TypedQuery<Brand> typedQuery = Mockito.mock(TypedQuery.class);
		
		Mockito.when(em.createQuery(query, Brand.class))
				.thenReturn(typedQuery);
		Mockito.when(typedQuery.getSingleResult())
				.thenThrow(NoResultException.class);
		
		assertEquals(false, jpaImplBrand.brandNameAndCompanyExists(brandName, company));
	}
	
	
	
	
	
	
	
	
	
	
	

}
