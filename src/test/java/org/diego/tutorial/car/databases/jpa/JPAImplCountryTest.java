package org.diego.tutorial.car.databases.jpa;

import static org.junit.Assert.*;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.diego.tutorial.car.model.Country;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Set of tests for the {@link JPAImplCountry} class
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class JPAImplCountryTest {
	@InjectMocks
	private JPAImplCountry jpaImplCountry;
	@Mock
	private EntityManager em; 
	
	private String countryName;
	
	@Before
	public void setup() {
		countryName = "countryName";
	}
	
	@Test
	public void testCountryAlreadyExists() {
		String query = "SELECT country "
				+ "FROM Country country "
				+ "WHERE country.countryName='" + countryName + "'";
		Country country = Mockito.mock(Country.class);
		
		@SuppressWarnings("unchecked")
		TypedQuery<Country> typedQuery = Mockito.mock(TypedQuery.class);
		
		Mockito.when(em.createQuery(query, Country.class))
				.thenReturn(typedQuery);
		Mockito.when(typedQuery.getSingleResult())
				.thenReturn(country);
		
		assertEquals(true, jpaImplCountry.countryAlreadyExists(countryName));
	}
	
	@Test
	public void testCountryAlreadyExistsFalse() {
		String query = "SELECT country "
				+ "FROM Country country "
				+ "WHERE country.countryName='" + countryName + "'";
		
		@SuppressWarnings("unchecked")
		TypedQuery<Country> typedQuery = Mockito.mock(TypedQuery.class);
		
		Mockito.when(em.createQuery(query, Country.class))
				.thenReturn(typedQuery);
		Mockito.when(typedQuery.getSingleResult())
				.thenThrow(NoResultException.class);
		
		assertEquals(false, jpaImplCountry.countryAlreadyExists(countryName));
	}

}
