package org.diego.tutorial.car.timers;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;

import org.apache.log4j.Logger;
import org.diego.tutorial.car.model.Car;
import org.diego.tutorial.car.model.service.CarService;

/**
 * Class that implements a timer for the operations related to the cars
 * */
@Singleton
public class CarsTimer {
	@EJB
	private CarService carService;
	
	private final static Logger LOGGER = Logger.getLogger(CarsTimer.class);
	
	/**
	 * Method executed every 10 minutes, that gets a list of all the 
	 * non-checked cars, and them checks them.
	 * */
	@Schedule(minute = "*/10", hour = "*", dayOfMonth = "*")
	public void checkedField() {
		LOGGER.info("Checking the cars");
		List<Car> nonCheckedCars = carService.getAllNonCheckedCars();
		
		for (Car car : nonCheckedCars) {
			car.setChecked(true);
			carService.updateCar(car);
			LOGGER.info("Checking the car: " + car);
		}
		
		LOGGER.info("All the cars are checked");
	}
}
