package org.diego.tutorial.car.timers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.diego.tutorial.car.model.Car;
import org.diego.tutorial.car.model.service.CarService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Set of unit tests for the {@link CarsTimer} class
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class CarsTimerTest {

	@InjectMocks
	private CarsTimer carsTimer;
	@Mock
	private CarService carService;
	
	@Test
	public void testCheckSoftRemovedCars() {
		List<Car> softRemovedCars = new ArrayList<Car>();
		
		for (int i = 0; i < 10; i++) {
			long id = (long) i;
			String brand = "brand" + i;
			Date registration, createdAt, lastUpdated;
			registration = createdAt = lastUpdated = new Date();
			String country = "country" + i;
			Car car = new Car(id, brand, registration, country, createdAt, lastUpdated);
			softRemovedCars.add(car);
			
			Mockito.when(carService.removeCar(car))
					.thenReturn(car);
		}
		
		Mockito.when(carService.getAllSoftRemovedCars())
				.thenReturn(softRemovedCars);
		
		carsTimer.checkSoftRemovedCars();
		
	}

}
