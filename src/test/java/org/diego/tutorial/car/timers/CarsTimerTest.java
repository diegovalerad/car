package org.diego.tutorial.car.timers;

import static org.junit.Assert.*;

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

@RunWith(MockitoJUnitRunner.class)
public class CarsTimerTest {

	@InjectMocks
	private CarsTimer carsTimer;
	@Mock
	private CarService carService;
	
	@Test
	public void testCheckedField() {
		List<Car> nonCheckedCars = new ArrayList<Car>();
		
		for (int i = 0; i < 10; i++) {
			long id = (long) i;
			String brand = "brand" + i;
			Date registration, createdAt, lastUpdated;
			registration = createdAt = lastUpdated = new Date();
			String country = "country" + i;
			Car car = new Car(id, brand, registration, country, createdAt, lastUpdated);
			nonCheckedCars.add(car);
		}
		
		Mockito.when(carService.getAllNonCheckedCars())
				.thenReturn(nonCheckedCars);
		
		carsTimer.checkedField();
		for (Car car : nonCheckedCars) {
			assertEquals(true, car.isChecked());
		}
	}

}
