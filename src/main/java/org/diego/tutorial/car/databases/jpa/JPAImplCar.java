package org.diego.tutorial.car.databases.jpa;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

import org.diego.tutorial.car.model.Car;

/**
 * Implementation of the JPA persistence with specific
 * car methods
 */
@Stateless
public class JPAImplCar extends JPAImpl {
	/**
	 * Method that retrieves all the car objects from the database, that are
	 * from a certain country.
	 * @param country Country searched
	 * @return List of cars from the country searched
	 */
	public List<Car> getAllCarsFromCountry(String country){
		String query = "SELECT car FROM Car car WHERE car.country='" + country + "'";
		TypedQuery<Car> createQuery = em.createQuery(query, Car.class);
		
		List<Car> carsFromCountry = createQuery.getResultList();
		return carsFromCountry;
	}

	/**
	 * Method that queries the database and retrieves all the soft removed cars. A 
	 * soft removed car is a car with a flag that the car should be removed.
	 * @return List of soft removed cars.
	 */
	public List<Car> getAllSoftRemovedCars() {
		String query = "SELECT car FROM Car car WHERE car.softRemoved='true'";
		
		TypedQuery<Car> createQuery = em.createQuery(query, Car.class);
		List<Car> softRemovedCars = createQuery.getResultList();
		return softRemovedCars;
	}

	/**
	 * Method that queries the database and retrieves all the cars from
	 * a certain brand
	 * @param brand Name of the brand
	 * @return List of cars
	 */
	public List<Car> getAllCarsFromBrand(String brand) {
		String query = "SELECT car FROM Car car WHERE car.brand='" + brand + "'";
		
		TypedQuery<Car> createQuery = em.createQuery(query, Car.class);
		List<Car> softRemovedCars = createQuery.getResultList();
		return softRemovedCars;
	}
}
