package org.diego.tutorial.car.databases.jpa;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

import org.diego.tutorial.car.model.Car;

@Stateless
public class JPAImplCar extends JPAImpl {
	public List<Car> getAllCarsFromCountry(String country){
		String countryLowerCase = country.toLowerCase();
		
		String query = "SELECT car FROM Car car WHERE car.country='" + countryLowerCase + "'";
		TypedQuery<Car> createQuery = em.createQuery(query, Car.class);
		
		List<Car> carsFromCountry = createQuery.getResultList();
		return carsFromCountry;
	}
}
