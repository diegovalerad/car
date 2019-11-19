package org.diego.tutorial.car.model.service;

import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.diego.tutorial.car.databases.jpa.JPAImplCar;
import org.diego.tutorial.car.exceptions.BadRequestException;
import org.diego.tutorial.car.exceptions.DataNotFoundException;
import org.diego.tutorial.car.model.Brand;
import org.diego.tutorial.car.model.Car;
import org.diego.tutorial.car.model.Country;

/**
 * Class that represents the service of Cars, in charge of doing the operations involving cars, 
 * connecting the REST API and the JPA persistence. 
 *
 */
@Stateless
public class CarService {
	@EJB
	private JPAImplCar jpaImpl;
	
	@EJB
	private BrandService brandService;
	@EJB
	private CountryService countryService;
	
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
	 * Retrieves a requested car given by an identifier. <p>
	 * Throws a {@link DataNotFoundException} if the car does not exist
	 * @param id Identifier of the requested car
	 * @return Requested car
	 */
	public Car getCar(long id) {
		LOGGER.info("Getting the car with ID " + id + " from the database.");
		Car car = null;
		
		car = jpaImpl.get(Car.class, id);
		if (car == null || (car.isSoftRemoved()))
			throw new DataNotFoundException("Trying to get a car with ID '" + id + "' that does not exists");
		
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
	 * Gets all the cars from a brand
	 * @param id Identifier of the brand
	 * @return List of cars
	 */
	public List<Car> getAllCarsFromBrand(long id){
		LOGGER.info("Getting all the cars from the brand '" + id + "'");
		List<Car> carsFromBrand = jpaImpl.getAllCarsFromBrand(id);
		LOGGER.info("Retrieved " + carsFromBrand.size() + " cars from the brand '" + id + "");
		return carsFromBrand;
	}
	
	/**
	 * Gets all the soft removed cars from the system
	 * @return Soft removed cars
	 */
	public List<Car> getAllSoftRemovedCars(){
		LOGGER.info("Getting all the soft removed cars");
		List<Car> carsSoftRemoved = jpaImpl.getAllSoftRemovedCars();
		LOGGER.info("All the soft removed cars have been retrieved");
		return carsSoftRemoved;
	}
	
	/**
	 * Method that adds a new car to the database. It throws:
	 * <p>
	 * <ul>
	 * <li> {@link BadRequestException} if the brand of the car was not in the request message or if it was, but in an incorrect format
	 * <li> {@link DataNotFoundException} if the brand of the car does not exist
	 * </ul>
	 * @param car Car that should be added
	 * @return Car added 
	 */
	public Car addCar(Car car) {
		LOGGER.info("Adding the car: " + car);
		Brand brand = checkBrand(car.getBrand());
		car.setBrand(brand);
		Country country = checkCountry(car.getCountry());
		car.setCountry(country);
		
		car.setCreatedAt(new Date());
		car.setLastUpdated(new Date());
		car.setRegistration(new Date());
		
		Car carAdded = jpaImpl.add(car);
		
		LOGGER.info("Car " + car + " added to the database.");
		
		return carAdded;
	}
	
	/**
	 * Method that updates an existing car in the database. It throws: <p>
	 * <ul>
	 * <li>{@link DataNotFoundException} if the car or the brand of the car does not exist</li>
	 * <li> {@link BadRequestException} if the brand of the car is not valid
	 * </ul> 
	 * @param car Car object that should be updated
	 * @return Car updated
	 */
	public Car updateCar(Car car) {
		LOGGER.info("Updating the car: " + car);
		long idCar = car.getId();
		
		checkBrand(car.getBrand());
		checkCountry(car.getCountry());
		
		Car carOld = getCar(idCar);
		car.setCreatedAt(carOld.getCreatedAt());
		car.setRegistration(carOld.getRegistration());
		car.setLastUpdated(new Date());
		LOGGER.info("Car " + car + " updated");
		return jpaImpl.update(car);
	}
	
	/**
	 * Method that soft-removes an existing car from the database. It throws<p>
	 * <ul>
	 *  <li> {@link DataNotFoundException} if the car does not exist.</li>
	 *  <li> {@link BadRequestException} if the ID of the car is non valid (zero or less) </li>
	 * </ul>
	 * @param id Identifier of the car that should be removed
	 * @return Car soft-removed
	 */
	public Car softRemoveCar(long id) {
		LOGGER.info("Soft-removing the car with ID: " + id);
		
		Car carSoftRemoved = getCar(id);
		carSoftRemoved.setSoftRemoved(true);
		LOGGER.info("The car with ID: " + id + " was soft-removed from the database");
		return carSoftRemoved;
	}
	
	/**
	 * Method that completely removes an existing car from the database. It throws<p>
	 * <ul>
	 *  <li> {@link DataNotFoundException} if the car does not exist.</li>
	 * </ul>
	 * @param car Car object that should be removed
	 * @return Car removed
	 */
	public Car removeCar(Car car) {
		long id = car.getId();
		LOGGER.info("Removing the car with ID: " + id);
		
		Car carRemoved = jpaImpl.delete(car);
		LOGGER.info("The car with ID: " + id + " was removed from the database");
		return carRemoved;
	}

	/**
	 * Method that checks a brand in the request. It throws <p>
	 * <ul>
	 * <li> {@link BadRequestException} if the brand was not in the request message or it was, but in an incorrect format
	 * <li> {@link DataNotFoundException} if the brand does not exist 
	 * </ul>
	 * @param brand Given brand
	 * @return brand from the database
	 */
	private Brand checkBrand(Brand brand) {
		LOGGER.info("Checking the brand: " + brand);
		String message = null;
		if (brand == null) {
			message = "The brand is a required field";
			throw new BadRequestException(message);
		}
		
		Brand brandRetrieved = brandService.getBrand(brand.getId());
		
		LOGGER.info("Checking of the brand " + brand + " finished");
		return brandRetrieved;
	}
	
	/**
	 * Method that checks a country in the request. It throws: <p>
	 * <ul>
	 * <li> {@link BadRequestException} if the country was not in the request message or it was, but in an incorrect format
	 * <li> {@link DataNotFoundException} if the country does not exist 
	 * </ul>
	 * @param country Given country
	 */
	private Country checkCountry(Country country) {
		LOGGER.info("Checking the country: " + country);
		if (country == null) {
			String message = "The country is a required field";
			throw new BadRequestException(message);
		}
		
		Country countryRetrieved = countryService.getCountry(country.getId());
		
		LOGGER.info("Checking of the country '" + country + "' finished");
		
		return countryRetrieved;
	}

	
	
	
	
}
