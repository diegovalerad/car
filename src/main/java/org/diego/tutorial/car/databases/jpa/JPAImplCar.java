package org.diego.tutorial.car.databases.jpa;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.diego.tutorial.car.model.Car;
import org.diego.tutorial.car.model.Country;

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
		/*
		SELECT car 
		FROM Car car
		WHERE car.country = (SELECT country
							FROM Country country
							WHERE country.countryName = 'country')
		*/
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Car> cq = cb.createQuery(Car.class);
		
		Root<Car> cars = cq.from(Car.class);
		Root<Country> countries = cq.from(Country.class);
		
		ParameterExpression<String> paramCountry = cb.parameter(String.class);
		Predicate countryRestriction = cb.and(
				cb.equal(countries.get("countryName"), paramCountry),
				cb.equal(cars.get("country").get("id"), countries.get("id"))
		);
		
		cq.select(cars)
			.where(countryRestriction);
		
		TypedQuery<Car> createQuery = em.createQuery(cq);
		createQuery.setParameter(paramCountry, country);
		
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
	 * @param id ID of the brand
	 * @return List of cars
	 */
	public List<Car> getAllCarsFromBrand(long id) {
		// SELECT car FROM Car car WHERE car.brand='id'
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Car> cq = cb.createQuery(Car.class);
		
		Root<Car> cars = cq.from(Car.class);
		
		ParameterExpression<Long> paramId = cb.parameter(Long.class);
		Predicate predId = cb.equal(cars.get("brand").get("id"), paramId);
		
		cq.select(cars)
			.where(predId);
		
		TypedQuery<Car> typedQuery = em.createQuery(cq);
		typedQuery.setParameter(paramId, id);
		
		List<Car> carsFromBrand = typedQuery.getResultList();
		return carsFromBrand;
	}
}
