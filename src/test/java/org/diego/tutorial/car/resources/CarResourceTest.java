package org.diego.tutorial.car.resources;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;

import org.diego.tutorial.car.model.Car;
import org.diego.tutorial.car.model.service.CarService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CarResourceTest {
	
	@InjectMocks
	private CarResource carResource;
	@Mock
	private CarService carService;

	@Test
	public void testGetCars() {
		List<Car> cars = new ArrayList<Car>();
		Car car = new Car();
		car.setCountry("sp");
		car.setBrand("brand");
		car.setCreatedAt(new Date());
		car.setLastUpdated(new Date());
		car.setRegistration(new Date());
		cars.add(car);
		
		Mockito.when(carService.getAllCars())
				.thenReturn(cars);
		
		GenericEntity<List<Car>> carsGeneric = new GenericEntity<List<Car>>(cars) {};
		
		assertEquals(carsGeneric, carResource.getCars(null).getEntity());
	}
	
	

}
