package org.diego.tutorial.car.databases.jpa;

import static org.junit.Assert.*;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

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
	@Mock
	private CriteriaQuery<Country> cq;
	
	private String countryName;
	
	@Before
	public void setup() {
		countryName = "countryName";

		setupCriteriaBuilder();
	}
	
	@SuppressWarnings("unchecked")
	private void setupCriteriaBuilder() {
		CriteriaBuilder cb = Mockito.mock(CriteriaBuilder.class);
		Root<Country> rootBrands = Mockito.mock(Root.class);
		ParameterExpression<String> paramString = Mockito.mock(ParameterExpression.class);
		Predicate predicate = Mockito.mock(Predicate.class);
		
		Mockito.when(em.getCriteriaBuilder())
				.thenReturn(cb);
		Mockito.when(cb.createQuery(Country.class))
				.thenReturn(cq);
		Mockito.when(cq.from(Country.class))
				.thenReturn(rootBrands);
		Mockito.when(cb.parameter(String.class))
				.thenReturn(paramString);
		Mockito.when(cb.equal(Mockito.any(), Mockito.any()))
				.thenReturn(predicate);
		Mockito.when(cq.select(rootBrands))
				.thenReturn(cq);
		Mockito.when(cq.where(predicate))
				.thenReturn(cq);
	}
	
	@Test
	public void testCountryAlreadyExists() {
		Country country = Mockito.mock(Country.class);
		
		@SuppressWarnings("unchecked")
		TypedQuery<Country> typedQuery = Mockito.mock(TypedQuery.class);
		
		Mockito.when(em.createQuery(cq))
				.thenReturn(typedQuery);
		Mockito.when(typedQuery.getSingleResult())
				.thenReturn(country);
		
		assertEquals(true, jpaImplCountry.countryAlreadyExists(countryName));
		Mockito.verify(em).createQuery(cq);
	}
	
	@Test
	public void testCountryAlreadyExistsFalse() {
		
		@SuppressWarnings("unchecked")
		TypedQuery<Country> typedQuery = Mockito.mock(TypedQuery.class);
		
		Mockito.when(em.createQuery(cq))
				.thenReturn(typedQuery);
		Mockito.when(typedQuery.getSingleResult())
				.thenThrow(NoResultException.class);
		
		assertEquals(false, jpaImplCountry.countryAlreadyExists(countryName));
		Mockito.verify(em).createQuery(cq);
	}

}
