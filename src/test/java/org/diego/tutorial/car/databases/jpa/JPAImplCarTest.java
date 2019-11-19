package org.diego.tutorial.car.databases.jpa;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.diego.tutorial.car.model.Car;
import org.diego.tutorial.car.model.Country;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Set of unit tests for the {@link JPAImplCar} class
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class JPAImplCarTest {
	@InjectMocks
	private JPAImplCar jpaImplCar;
	@Mock
	private EntityManager em; 
	@Mock
	private CriteriaQuery<Car> cq;
	
	@Before
	public void setup() {
		setupCriteriaBuilder();
	}
	
	@SuppressWarnings("unchecked")
	private void setupCriteriaBuilder() {
		CriteriaBuilder cb = Mockito.mock(CriteriaBuilder.class);
		Root<Car> rootsCars = Mockito.mock(Root.class);
		Root<Country> rootsCountries = Mockito.mock(Root.class);
		ParameterExpression<String> paramString = Mockito.mock(ParameterExpression.class);
		Predicate predicate = Mockito.mock(Predicate.class);
		Path<Object> path = Mockito.mock(Path.class);
		
		Mockito.when(em.getCriteriaBuilder())
				.thenReturn(cb);
		Mockito.when(cb.createQuery(Car.class))
				.thenReturn(cq);
		Mockito.when(cq.from(Car.class))
				.thenReturn(rootsCars);
		Mockito.when(cq.from(Country.class))
				.thenReturn(rootsCountries);
		
		Mockito.when(rootsCars.get(Mockito.anyString()))
				.thenReturn(path);
		Mockito.when(rootsCountries.get(Mockito.anyString()))
				.thenReturn(path);
		Mockito.when(cb.parameter(String.class))
				.thenReturn(paramString);
		Mockito.when(cb.equal(Mockito.any(), Mockito.any()))
				.thenReturn(predicate);
		Mockito.when(cq.select(rootsCars))
				.thenReturn(cq);
	}
	
	@Test
	public void testGetAllCarsFromCountry() {
		List<Car> carsFromCountry = new ArrayList<Car>();
		
		@SuppressWarnings("unchecked")
		TypedQuery<Car> typedQuery = Mockito.mock(TypedQuery.class);
		
		Mockito.when(em.createQuery(cq))
				.thenReturn(typedQuery);
		Mockito.when(typedQuery.getResultList())
				.thenReturn(carsFromCountry);
		
		assertEquals(carsFromCountry, jpaImplCar.getAllCarsFromCountry("country"));
	}

	@Test
	public void testGetAllSoftRemovedCars() {
		String query = "SELECT car FROM Car car WHERE car.softRemoved='true'";
		List<Car> carsSoftRemoved = new ArrayList<Car>();
		
		@SuppressWarnings("unchecked")
		TypedQuery<Car> typedQuery = Mockito.mock(TypedQuery.class);
		
		Mockito.when(em.createQuery(query, Car.class))
				.thenReturn(typedQuery);
		Mockito.when(typedQuery.getResultList())
				.thenReturn(carsSoftRemoved);
		
		assertEquals(carsSoftRemoved, jpaImplCar.getAllSoftRemovedCars());
	} 
	
	@Test
	public void testGetAllCarsFromBrand() {
		List<Car> carsFromBrand = new ArrayList<Car>();
		
		@SuppressWarnings("unchecked")
		TypedQuery<Car> typedQuery = Mockito.mock(TypedQuery.class);
		
		Mockito.when(em.createQuery(cq))
				.thenReturn(typedQuery);
		Mockito.when(typedQuery.getResultList())
				.thenReturn(carsFromBrand);
		
		assertEquals(carsFromBrand, jpaImplCar.getAllCarsFromBrand(1L));
	}

}
