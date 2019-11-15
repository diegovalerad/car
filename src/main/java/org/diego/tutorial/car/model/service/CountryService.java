package org.diego.tutorial.car.model.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.diego.tutorial.car.databases.jpa.JPAImplCountry;
import org.diego.tutorial.car.exceptions.DataAlreadyExistsException;
import org.diego.tutorial.car.exceptions.DataNotFoundException;
import org.diego.tutorial.car.model.Country;

/**
 * Class that represents the service of countries, in charge of doing the operations involving cars, 
 * connecting the REST API and the JPA persistence. 
 *
 */
@Stateless
public class CountryService {
	@EJB
	private JPAImplCountry jpaImplCountry;
	
	private final static Logger LOGGER = Logger.getLogger(CountryService.class);
	
	public CountryService() {
	
	}
	
	/**
	 * Retrieves all the countries from the database
	 * @return List of countries
	 */
	public List<Country> getAllCountries(){
		LOGGER.info("Trying to retrieve all the countries from the database");
		List<Country> countries = jpaImplCountry.getAll(Country.class);
		LOGGER.info("All the countries retrieved");
		return countries;
	}
	
	/**
	 * Tries to add a country to the system. It throws:<p>
	 * {@link DataAlreadyExistsException} if the country already exists
	 * @param country Country to add
	 * @return Added country
	 */
	public Country addCountry(Country country) {
		LOGGER.info("Trying to create a country: " + country);
		
		if (jpaImplCountry.countryAlreadyExists(country.getCountryName())) {
			String message ="The country that it is trying to be created already exists";
			LOGGER.info(message);
			throw new DataAlreadyExistsException(message);
		}
		
		Country addedCountry = jpaImplCountry.add(country);
		LOGGER.info("Added country: " + addedCountry);
		
		return addedCountry;
	}

	/**
	 * Tries to retrieve a country from the database. It throws: <p>
	 * {@link DataNotFoundException} if the country does not exist.
	 * @param countryId Identifier of the country
	 * @return Country
	 */
	public Country getCountry(long countryId) {
		LOGGER.info("Trying to get the country with ID: " + countryId);
		
		Country country = jpaImplCountry.get(Country.class, countryId);
		if (country == null) {
			String message = "The country with ID '" + countryId + "' does not exist.";
			LOGGER.info(message);
			throw new DataNotFoundException(message);
		}
		LOGGER.info("Country retrieved: " + country);
		return country;
	}

	public Country updateCountry(Country country) {
		LOGGER.info("Trying to update the country: " + country);
		
		getCountry(country.getId());
	}
	
	
	
	
	
	
	
	
}
