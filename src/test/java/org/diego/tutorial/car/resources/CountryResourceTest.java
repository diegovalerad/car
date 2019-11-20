package org.diego.tutorial.car.resources;

import static org.junit.Assert.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.diego.tutorial.car.exceptions.BadRequestException;
import org.diego.tutorial.car.exceptions.DataAlreadyExistsException;
import org.diego.tutorial.car.exceptions.DataNotFoundException;
import org.diego.tutorial.car.model.Country;
import org.diego.tutorial.car.model.service.CountryService;
import org.diego.tutorial.car.validations.GeneralValidator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Set of unit tests for the {@link CountryResource} class
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Validation.class, GeneralValidator.class})
public class CountryResourceTest {
	@InjectMocks
	private CountryResource countryResource;
	@Mock
	private CountryService countryService;
	@Mock
	private UriInfo uriInfo;
	
	private UriBuilder uriBuilder;
	
	private long countryId;
	private String countryName;
	private String countryAbbreviation;
	private Country country;
	private List<String> validationErrors;
	
	@Before
	public void setup() throws Exception {
		setupUriInfo();
		setupValidationErrors();
		setupCountryInfo();
	}

	private void setupUriInfo() throws Exception {
		uriBuilder = Mockito.mock(UriBuilder.class);
		
		Mockito.when(uriInfo.getBaseUriBuilder()).thenReturn(uriBuilder);
        Mockito.when(uriBuilder.path(CountryResource.class)).thenReturn(uriBuilder);
        Mockito.when(uriBuilder.path(Mockito.anyString())).thenReturn(uriBuilder);
        Mockito.when(uriBuilder.build()).thenReturn(new URI("www.abc.es"));
        Mockito.when(uriBuilder.toString()).thenReturn("http://www.prueba.es");
	}
	
	private void setupValidationErrors() throws Exception {
		validationErrors = new ArrayList<String>();
        ValidatorFactory validatorFactory = Mockito.mock(ValidatorFactory.class);
        Validator validator = Mockito.mock(Validator.class);
        
        PowerMockito.mockStatic(Validation.class);
        PowerMockito.when(Validation.class, "buildDefaultValidatorFactory")
        		.thenReturn(validatorFactory);
        PowerMockito.when(validatorFactory.getValidator())
        		.thenReturn(validator);
        
        PowerMockito.mockStatic(GeneralValidator.class);
        PowerMockito.when(GeneralValidator.class, "validateObject", Mockito.any(Object.class))
        		.thenReturn(validationErrors);
	}
	
	private void setupCountryInfo() {
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
		Mockito.when(countryService.getAllCountries())
				.thenReturn(countries);
		
		assertEquals(countries, countryResource.getCountries().getEntity());
	}
	
	@Test
	public void testAddCountry() {
		Mockito.when(uriInfo.getAbsolutePathBuilder())
				.thenReturn(uriBuilder);
		Mockito.when(countryService.addCountry(country))
				.thenReturn(country);
		
		assertEquals(country, countryResource.addCountry(country).getEntity());
	}
	
	@Test (expected = BadRequestException.class)
	public void testAddCountryWithValidationErrors() throws Exception {
		validationErrors.add("error");
		PowerMockito.when(GeneralValidator.class, "validateObject", Mockito.any(Object.class))
					.thenReturn(validationErrors);
		
		countryResource.addCountry(country);
	}
	
	@Test (expected = DataAlreadyExistsException.class)
	public void testAddCountryExisting() {
		Mockito.when(uriInfo.getAbsolutePathBuilder())
				.thenReturn(uriBuilder);
		Mockito.when(countryService.addCountry(country))
				.thenThrow(DataAlreadyExistsException.class);
		countryResource.addCountry(country);
	}
	
	@Test
	public void testGetCountry() {
		Mockito.when(countryService.getCountry(countryId))
				.thenReturn(country);
		
		assertEquals(country, countryResource.getCountry(countryId).getEntity());
	}
	
	@Test (expected = BadRequestException.class)
	public void testGetCountryInvalidId() {
		countryResource.getCountry(0);
	}
	
	@Test (expected = DataNotFoundException.class)
	public void testGetCountryNotExist() {
		Mockito.when(countryService.getCountry(countryId))
				.thenThrow(DataNotFoundException.class);
		countryResource.getCountry(countryId);
	}
	
	@Test
	public void testUpdateCountry() {
		Mockito.when(countryService.updateCountry(country))
				.thenReturn(country);
		
		assertEquals(country, countryResource.updateCountry(countryId, country).getEntity());
	}
	
	@Test (expected = BadRequestException.class)
	public void testUpdateCountryInvalidId() {
		countryResource.updateCountry(0, country);
	}
	
	@Test (expected = BadRequestException.class)
	public void testUpdateCountryWithValidationErrors() throws Exception {
		validationErrors.add("error");
		PowerMockito.when(GeneralValidator.class, "validateObject", Mockito.any(Object.class))
					.thenReturn(validationErrors);
		
		countryResource.updateCountry(countryId, country);
	}
	
	@Test (expected = DataNotFoundException.class)
	public void testUpdateCountryNotFound() {
		Mockito.when(countryService.updateCountry(country))
				.thenThrow(DataNotFoundException.class);
		
		countryResource.updateCountry(countryId, country);
	}
	
	@Test
	public void testRemoveCountry() {
		Mockito.when(countryService.removeCountry(countryId))
				.thenReturn(country);
		
		assertEquals(country, countryResource.removeCountry(countryId).getEntity());
	}
	
	@Test (expected = BadRequestException.class)
	public void testRemoveCountryInvalidId() {
		countryResource.removeCountry(0);
	}
	
	@Test (expected = DataNotFoundException.class)
	public void testRemoveCountryNotFound() {
		Mockito.when(countryService.removeCountry(countryId))
				.thenThrow(DataNotFoundException.class);
		
		countryResource.removeCountry(countryId);
	}

}
