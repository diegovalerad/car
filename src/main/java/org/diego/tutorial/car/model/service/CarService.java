package org.diego.tutorial.car.model.service;

import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.diego.tutorial.car.databases.jpa.JPAImplCar;
import org.diego.tutorial.car.exceptions.DataNotFoundException;
import org.diego.tutorial.car.model.Car;

/**
 * Class that represents the service of Cars, in charge of doing the operations involving cars, 
 * connecting the REST API and the JPA persistence. 
 *
 */
@Stateless
public class CarService {
	@EJB
	private JPAImplCar jpaImpl;
	
	private final static Logger LOGGER = Logger.getLogger(CarService.class);
	
	public CarService() {
		
	}
	
	/**
	 * Retrieves all cars from the database
	 * @return All cars.
	 */
	public List<Car> getAllCars() {
		LOGGER.info("Getting all the cars from the database.");
		List<Car> cars = jpaImpl.getAll(Car.class);
		LOGGER.info("All the cars retrieved from the database.");
		return cars;
	}
	
	/**
	 * Retrieves a requested car given by an identifier.
	 * @param id Identifier of the requested car
	 * @return Requested car
	 */
	public Car getCar(long id) {
		LOGGER.info("Getting the car with ID " + id + " from the database.");
		Car car = null;
		car = jpaImpl.get(Car.class, id);
		LOGGER.info("The car with ID " + id + " was retrieved from the database.");
		
		return car;
	}
	
	/**
	 * Retrieves all cars from an specific country
	 * @param country Requested country
	 * @return List of car objects, whose country field is the same as the requested.
	 */
	public List<Car> getAllCarsFromCountry(String country){
		LOGGER.info("Getting all the cars from the country '" + country + "'.");
		List<Car> carsForCountry = jpaImpl.getAllCarsFromCountry(country);
		LOGGER.info("All the cars from country '" + country + "' retrieved from the database.");
		
		return carsForCountry;
	}
	
	/**
	 * Method that adds a new car to the database
	 * @param car Car that should be added
	 * @return Car added
	 */
	public Car addCar(Car car) {
		car.setCreatedAt(new Date());
		car.setLastUpdated(new Date());
		car.setRegistration(new Date());
		LOGGER.info("Adding the car: " + car);
		
		Car carAdded = jpaImpl.add(car);
		
		LOGGER.info("Car " + car + " added to the database.");
		
		return carAdded;
	}
	
	/**
	 * Method that updates an existing car in the database. <p>
	 * If the car already exists, an {@link DataNotFoundException} exception is thrown.
	 * @param car Car object that should be updated
	 * @return Car updated
	 */
	public Car updateCar(Car car) {
		LOGGER.info("Updating the car: " + car);
		long idCar = car.getId();
		if (idCar <= 0 || !carAlreadyExists(idCar)) {
			LOGGER.warn("The car that it is trying to get updated does not exist.");
			throw new DataNotFoundException(createErrorMessageCarDoesNotExist("update", idCar));
		}
		Car carOld = getCar(idCar);
		car.setCreatedAt(carOld.getCreatedAt());
		car.setRegistration(carOld.getRegistration());
		car.setLastUpdated(new Date());
		LOGGER.info("Car " + car + " updated");
		return jpaImpl.update(car);
	}
	
	/**
	 * Method that removes an existing car from the database. <p>
	 * If the car already exists, an {@link DataNotFoundException} exception is thrown.
	 * @param id Identifier of the car that should be removed
	 * @return Car removed
	 */
	public Car removeCar(long id) {
		LOGGER.info("Removing the car with ID: " + id);
		if (id <= 0 || !carAlreadyExists(id)) {
			LOGGER.warn("The car that it is trying to get removed does not exist.");
			throw new DataNotFoundException(createErrorMessageCarDoesNotExist("remove", id));
		}
		
		Car carRemoved = jpaImpl.delete(getCar(id));
		LOGGER.info("The car with ID: " + id + " was removed from the database");
		return carRemoved;
	}
	
	/**
	 * Method that checks if a car given by an identifier exists in the database
	 * @param id Identifier of the car
	 * @return Boolean indicating if the car exists.
	 */
	private boolean carAlreadyExists(long id) {
		LOGGER.info("Checking if the car with ID: " + id + " exists.");
		try {
			jpaImpl.get(Car.class, id);
			LOGGER.info("The car with ID: " + id + " exists.");
			return true;
		} catch (DataNotFoundException e) {
			LOGGER.info("The car with ID: " + id + " does not exist.");
			return false;
		}
	}
	
	/**
	 * Method that creates a message error, indicating the operation and the ID of the car that
	 * produced that error.
	 * @param operation Operation (get/update/add/remove) that produced the error.
	 * @param id Identifier of the car that produced the error.
	 * @return String with a message error.
	 */
	private String createErrorMessageCarDoesNotExist(String operation, long id) {
		return "Trying to " + operation + " a car with ID: " + id + " that does not exist.";
	}
	
}
