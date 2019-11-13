package org.diego.tutorial.car.databases.jpa;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.diego.tutorial.car.model.Brand;
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
	
	@Test
	public void testGetAllBrandsFromCompany() {
		String company = "company";
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
	
	
	
	
	
	
	
	
	
	
	

}
