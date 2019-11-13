package org.diego.tutorial.car.model.service;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.diego.tutorial.car.databases.jpa.JPAImplCar;
import org.diego.tutorial.car.model.Brand;
import org.diego.tutorial.car.model.Car;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Set of unit tests for the {@link CarService} class
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class CarServiceTest {
	
	@InjectMocks
	private CarService carService;
	@Mock
	private JPAImplCar jpaImpl;
	@Mock
	private BrandService brandService;
	
	@Test
	public void testGetAllCars() {
		List<Car> cars = new ArrayList<Car>();
		
		Mockito.when(jpaImpl.getAll(Car.class))
				.thenReturn(cars);
		
		assertEquals(cars, carService.getAllCars());
	}
	
	@Test
	public void testGetCar() {
		long id = 1;
		Car car = Mockito.mock(Car.class);
		car.setId(id);
		
		Mockito.when(jpaImpl.get(Car.class, id))
				.thenReturn(car);
		
		assertEquals(car, carService.getCar(id));
	}
	
	@Test
	public void testGetAllCarsFromCountry() {
		List<Car> cars = new ArrayList<Car>();
		String countrySpain = "spain";
		String countryFrance = "france";
		
		Car carSpain = Mockito.mock(Car.class);
		carSpain.setCountry(countrySpain);
		Car carFrance = Mockito.mock(Car.class);
		carFrance.setCountry(countryFrance);
		
		cars.add(carSpain);
		cars.add(carFrance);
		
		List<Car> carsExpect = new ArrayList<Car>();
		carsExpect.add(carSpain);
		
		Mockito.when(jpaImpl.getAllCarsFromCountry(countrySpain))
				.thenReturn(carsExpect);
		
		assertEquals(carsExpect, carService.getAllCarsFromCountry(countrySpain));
	}
	
	@Test
	public void testAddCar() {
		Car car = new Car();
		Brand brand = Mockito.mock(Brand.class);
		car.setBrand(brand);
		
		Mockito.when(jpaImpl.add(car))
				.thenReturn(car);
		
		assertEquals(car, carService.addCar(car));
	}
	
	@Test
	public void testUpdateCar() {
		long id = 1;
		Car car = new Car();
		car.setId(id);
		Brand brand = Mockito.mock(Brand.class);
		car.setBrand(brand);
		
		Mockito.when(jpaImpl.get(Car.class, id))
				.thenReturn(car);
		Mockito.when(jpaImpl.update(car))
				.thenReturn(car);
		
		assertEquals(car, carService.updateCar(car));
	}
	
	@Test
	public void testSoftRemoveCar() {
		long id = 1;
		Car car = new Car();
		car.setId(id);
		
		Mockito.when(jpaImpl.get(Car.class, id))
				.thenReturn(car);
		
		assertEquals(car, carService.softRemoveCar(id));
		assertEquals(true, car.isSoftRemoved());
	}
	
	@Test
	public void testRemoveCar() {
		long id = 1;
		Car car = new Car();
		car.setId(id);
		
		Mockito.when(jpaImpl.get(Car.class, id))
				.thenReturn(car);
		Mockito.when(jpaImpl.delete(car))
				.thenReturn(car);
		
		assertEquals(car, carService.removeCar(car));
	}
}
