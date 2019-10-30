package org.diego.tutorial.car.model.service;

import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.diego.tutorial.car.databases.jpa.JPAImplCar;
import org.diego.tutorial.car.exceptions.DAOException;
import org.diego.tutorial.car.exceptions.DataNotFoundException;
import org.diego.tutorial.car.model.Car;

/**
 * Class that represents the service of Cars, in charge of doing the operations involving cars.
 * @author dvalerad
 *
 */

@Stateless
public class CarService {
	@EJB
	private JPAImplCar jpaImpl;
	
	public CarService() {
		
	}
	
	public List<Car> getAllCars() {
		List<Car> cars = jpaImpl.getAll(Car.class);
		return cars;
	}
	
	public Car getCar(long id) {
		Car car = null;
		try {
			car = jpaImpl.get(Car.class, id);
		} catch (DAOException e) {
			return null;
		}
		
		return car;
	}
	
	public List<Car> getAllCarsForCountry(String country){
		List<Car> carsForCountry = jpaImpl.getAllCarsForCountry(country);
		
		return carsForCountry;
	}
	
	public Car addCar(Car car) {
		Car carAdded = null;
		try {
			car.setCreatedAt(new Date());
			car.setLastUpdated(new Date());
			car.setRegistration(new Date());
			carAdded = jpaImpl.add(car);
		} catch (DAOException e) {
			return null;
		}
		return carAdded;
	}
	
	public Car updateCar(Car car) {
		long idCar = car.getId();
		if (idCar <= 0 || !carAlreadyExists(idCar)) {
			throw new DataNotFoundException(createErrorMessageCarDoesNotExist("update", idCar));
		}
		Car carOld = getCar(idCar);
		car.setCreatedAt(carOld.getCreatedAt());
		car.setRegistration(carOld.getRegistration());
		car.setLastUpdated(new Date());
		return jpaImpl.update(car);
	}
	
	public Car removeCar(long id) {
		if (id <= 0 || !carAlreadyExists(id))
			throw new DataNotFoundException(createErrorMessageCarDoesNotExist("remove", id));
		
		Car car = getCar(id);
		return jpaImpl.delete(car);
	}
	
	private boolean carAlreadyExists(long id) {
		try {
			jpaImpl.get(Car.class, id);
			return true;
		} catch (DAOException e) {
			return false;
		}
	}
	
	private String createErrorMessageCarDoesNotExist(String operation, long id) {
		return "Trying to " + operation + " a car with ID: " + id + " that does not exist";
	}
	
}
