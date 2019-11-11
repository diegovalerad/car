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
	 * Method executed every minute, that gets a list of all the 
	 * non-checked cars, and then checks them.
	 * */
	@Schedule(hour = "*", minute = "*/1", persistent = false)
	public void checkedField() {
		LOGGER.info("Checking the cars");
		List<Car> nonCheckedCars = carService.getAllNonCheckedCars();
		
		for (Car car : nonCheckedCars) {
			LOGGER.info("Checking the car: " + car);
			car.setChecked(true);
		}
		
		LOGGER.info("All the cars are checked");
	}
}
