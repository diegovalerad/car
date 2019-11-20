package org.diego.tutorial.car.timers;

import java.util.ArrayList;
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
 * Set of unit tests for the {@link SoftRemovedCarsTimer} class
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class SoftRemovedCarsTimerTest {

	@InjectMocks
	private SoftRemovedCarsTimer carsTimer;
	@Mock
	private CarService carService;
	
	@Test
	public void testCheck() {
		List<Car> softRemovedCars = new ArrayList<Car>();
		
		for (int i = 0; i < 10; i++) {
			Car car = Mockito.mock(Car.class);
			softRemovedCars.add(car);
			
			Mockito.when(carService.removeCar(car))
					.thenReturn(car);
		}
		
		Mockito.when(carService.getAllSoftRemovedCars())
				.thenReturn(softRemovedCars);
		
		carsTimer.check();
		
		for (Car car : softRemovedCars) {
			Mockito.verify(carService).removeCar(car);
		}
	}

}
