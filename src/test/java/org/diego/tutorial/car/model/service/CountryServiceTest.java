package org.diego.tutorial.car.model.service;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.diego.tutorial.car.databases.jpa.JPAImplCountry;
import org.diego.tutorial.car.exceptions.BadRequestException;
import org.diego.tutorial.car.exceptions.DataAlreadyExistsException;
import org.diego.tutorial.car.exceptions.DataNotFoundException;
import org.diego.tutorial.car.model.Car;
import org.diego.tutorial.car.model.Country;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Set of unit tests for the {@link CountryService} class
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class CountryServiceTest {
	@InjectMocks
	private CountryService countryService;
	@Mock
	private JPAImplCountry jpaImplCountry;
	@Mock
	private CarService carService;
	
	private long countryId;
	private String countryName;
	private String countryAbbreviation;
	private Country country;
	
	@Before
	public void setup() {
		countryId = 1L;
		countryName = "countryName";
		countryAbbreviation = "countryAbbrev";
		country = new Country();
		country.setId(countryId);
		country.setCountryName(countryName);
		country.setCountryAbbreviation(countryAbbreviation);
	}

	@Test
	public void testGetAllCountries() {
		List<Country> countries = new ArrayList<Country>();
		
		Mockito.when(jpaImplCountry.getAll(Country.class))
				.thenReturn(countries);
		
		assertEquals(countries, countryService.getAllCountries());
	}
	
	@Test
	public void testAddCountry() {
		Mockito.when(jpaImplCountry.countryAlreadyExists(countryName))
				.thenReturn(false);
		Mockito.when(jpaImplCountry.add(country))
				.thenReturn(country);
		
		assertEquals(country, countryService.addCountry(country));
	}
	
	@Test (expected = DataAlreadyExistsException.class)
	public void testAddCountryAlreadyExists() {
		Mockito.when(jpaImplCountry.countryAlreadyExists(countryName))
				.thenReturn(true);
		
		countryService.addCountry(country);
	}
	
	@Test
	public void testGetCountry() {
		Mockito.when(jpaImplCountry.get(Country.class, countryId))
				.thenReturn(country);
		
		assertEquals(country, countryService.getCountry(countryId));
	}
	
	@Test (expected = DataNotFoundException.class)
	public void testGetCountryNotExists() {
		Mockito.when(jpaImplCountry.get(Country.class, countryId))
				.thenReturn(null);

		countryService.getCountry(countryId);
	}
	
	@Test
	public void testUpdateCountry() {
		Mockito.when(jpaImplCountry.get(Country.class, countryId))
				.thenReturn(country);
		Mockito.when(jpaImplCountry.update(country))
				.thenReturn(country);
		
		assertEquals(country, countryService.updateCountry(country));
	}
	
	@Test (expected = DataNotFoundException.class)
	public void testUpdateCountryNotFound() {
		Mockito.when(jpaImplCountry.get(Country.class, countryId))
				.thenReturn(null);
		
		countryService.updateCountry(country);
	}
	
	@Test
	public void testRemoveCountry() {
		Mockito.when(jpaImplCountry.get(Country.class, countryId))
				.thenReturn(country);
		Mockito.when(carService.getAllCarsFromCountry(countryName))
				.thenReturn(new ArrayList<Car>());
		Mockito.when(jpaImplCountry.delete(country))
				.thenReturn(country);
		
		assertEquals(country, countryService.removeCountry(countryId));
	}
	
	@Test (expected = DataNotFoundException.class)
	public void testRemoveCountryNotExists() {
		Mockito.when(jpaImplCountry.get(Country.class, countryId))
				.thenReturn(null);

		countryService.removeCountry(countryId);
	}
	
	@Test (expected = BadRequestException.class)
	public void testRemoveCountryBeingUsed() {
		List<Car> cars = new ArrayList<Car>();
		cars.add(new Car());
		Mockito.when(jpaImplCountry.get(Country.class, countryId))
				.thenReturn(country);
		Mockito.when(carService.getAllCarsFromCountry(countryName))
				.thenReturn(cars);
		
		countryService.removeCountry(countryId);
	}
	
	
	
	

}
