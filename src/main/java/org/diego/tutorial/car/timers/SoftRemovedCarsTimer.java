package org.diego.tutorial.car.timers;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;

import org.apache.log4j.Logger;
import org.diego.tutorial.car.model.Car;
import org.diego.tutorial.car.model.service.CarService;

/**
 * Class that implements a timer that retrieves all the cars with a remove flag set to true,
 * removing them from the system
 * */
@Singleton
public class SoftRemovedCarsTimer {
	@EJB
	private CarService carService;
	
	private final static Logger LOGGER = Logger.getLogger(SoftRemovedCarsTimer.class);
	
	/**
	 * Method executed every minute, that gets a list of all the 
	 * soft removed cars, and then completely removes them.
	 * */
	@Schedule(hour = "*", minute = "*/1", persistent = false)
	public void check() {
		LOGGER.info("Checking the soft-removed cars");
		List<Car> softRemovedCars = carService.getAllSoftRemovedCars();
		
		for (Car car : softRemovedCars) {
			carService.removeCar(car);
		}
		
		LOGGER.info("All the cars are removed from the database");
	}
}
