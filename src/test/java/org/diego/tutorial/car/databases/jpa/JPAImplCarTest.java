package org.diego.tutorial.car.databases.jpa;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.diego.tutorial.car.model.Car;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class JPAImplCarTest {
	@InjectMocks
	private JPAImplCar jpaImplCar;
	@Mock
	private EntityManager em; 

	@Test
	public void testGetAllNonCheckedCars() {
		String query = "SELECT car FROM Car car WHERE car.checked='false'";
		List<Car> carsNonChecked = new ArrayList<Car>();
		
		@SuppressWarnings("unchecked")
		TypedQuery<Car> typedQuery = Mockito.mock(TypedQuery.class);
		
		Mockito.when(em.createQuery(query, Car.class))
				.thenReturn(typedQuery);
		Mockito.when(typedQuery.getResultList())
				.thenReturn(carsNonChecked);
		
		assertEquals(carsNonChecked, jpaImplCar.getAllNonCheckedCars());
	}

}
