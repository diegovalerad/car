package org.diego.tutorial.car.databases.jpa;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.diego.tutorial.car.exceptions.DataNotFoundException;
import org.diego.tutorial.car.model.Car;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Set of unit tests for the {@link JPAImpl} class
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class JPAImplTest {

	@InjectMocks
	private JPAImpl jpaImpl;
	@Mock
	private EntityManager em; 
	
	@Test
	public void testGetAll() {
		List<Object> cars = new ArrayList<Object>();
		Car car = Mockito.mock(Car.class);
		cars.add(car);
		
		@SuppressWarnings("unchecked")
		TypedQuery<Object> typedQuery = Mockito.mock(TypedQuery.class);
		Mockito.when(em.createQuery(Mockito.anyString(), Mockito.any()))
				.thenReturn(typedQuery);
		Mockito.when(typedQuery.getResultList())
				.thenReturn(cars);
		
		assertEquals(cars, jpaImpl.getAll(Car.class));
	}
	
	@Test
	public void testGet() {
		Car car = Mockito.mock(Car.class);
		
		Mockito.when(em.find(Mockito.any(), Mockito.anyLong()))
				.thenReturn(car);
		
		assertEquals(car, jpaImpl.get(Car.class, 0L));
	}
	
	@Test
	public void testAdd() {
		Car carExpected = Mockito.mock(Car.class);
		
		Mockito.doNothing()
				.when(em)
				.persist(Mockito.any());
		
		Car carPersisted = jpaImpl.add(carExpected);
		
		Mockito.verify(em).persist(carExpected);
		
		assertEquals(carExpected, carPersisted);
	}
	
	@Test(expected = DataNotFoundException.class)
	public void testAddDataThatAlreadyExists() {
		Car carExpected = Mockito.mock(Car.class);
		
		Mockito.doNothing()
				.doThrow(DataNotFoundException.class)
				.when(em)
				.persist(carExpected);
		
		jpaImpl.add(carExpected);
		jpaImpl.add(carExpected);
	}
	
	@Test
	public void testUpdate() {
		Car carExpected = Mockito.mock(Car.class);
		Mockito.when(em.merge(Mockito.any()))
				.thenReturn(carExpected);
		
		Car carActual = jpaImpl.update(carExpected);
		
		Mockito.verify(em)
				.merge(carExpected);
		
		assertEquals(carExpected, carActual);
	}
	
	@Test
	public void testRemove() {
		Car carExpected = Mockito.mock(Car.class);
		Mockito.doNothing().when(em).remove(carExpected);
		
		Car carActual = jpaImpl.delete(carExpected);
		Mockito.verify(em)
				.remove(carExpected);
		
		assertEquals(carExpected, carActual);
	}

}
