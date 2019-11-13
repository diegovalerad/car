package org.diego.tutorial.car.resources;

import static org.junit.Assert.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.diego.tutorial.car.model.Car;
import org.diego.tutorial.car.model.service.CarService;
import org.diego.tutorial.car.validations.CarValidator;
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
 * Set of unit tests for the {@link CarResource} class
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Validation.class, CarValidator.class})
public class CarResourceTest {
	@InjectMocks
	private CarResource carResource;
	@Mock
	private CarService carService;
	@Mock
	private UriInfo uriInfo;
	
	private UriBuilder uriBuilder;
	
	@Before
	public void setup() throws Exception {
		setupUriInfo();
		setupValidationErrors();
	}
	
	private void setupUriInfo() throws Exception {
		uriBuilder = Mockito.mock(UriBuilder.class);
		
		Mockito.when(uriInfo.getBaseUriBuilder()).thenReturn(uriBuilder);
        Mockito.when(uriBuilder.path(CarResource.class)).thenReturn(uriBuilder);
        Mockito.when(uriBuilder.path(Mockito.anyString())).thenReturn(uriBuilder);
        Mockito.when(uriBuilder.build()).thenReturn(new URI("www.abc.es"));
        Mockito.when(uriBuilder.toString()).thenReturn("http://www.prueba.es");
	}
	
	private void setupValidationErrors() throws Exception {
		List<String> validationErrors = new ArrayList<String>();
        ValidatorFactory validatorFactory = Mockito.mock(ValidatorFactory.class);
        Validator validator = Mockito.mock(Validator.class);
        
        PowerMockito.mockStatic(Validation.class);
        PowerMockito.when(Validation.class, "buildDefaultValidatorFactory")
        		.thenReturn(validatorFactory);
        PowerMockito.when(validatorFactory.getValidator())
        		.thenReturn(validator);
        
        PowerMockito.mockStatic(CarValidator.class);
        PowerMockito.when(CarValidator.class, "validateAddAndUpdate", Mockito.any(Car.class))
        		.thenReturn(validationErrors);
	}

	@Test
	public void testGetCars() {
		List<Car> cars = new ArrayList<Car>();
		Car car = Mockito.mock(Car.class);
		cars.add(car);
		
		Mockito.when(carService.getAllCars())
				.thenReturn(cars);
		
		assertEquals(cars, carResource.getCars(null).getEntity());
	}
	
	@Test
	public void testGetCarsFromCountry() {
		List<Car> cars = new ArrayList<Car>();
		Car car = Mockito.mock(Car.class);
		String country = "spain";
		car.setCountry(country);
		cars.add(car);
		
		Mockito.when(carService.getAllCarsFromCountry(country))
				.thenReturn(cars);
		
		assertEquals(cars, carResource.getCars(country).getEntity());
	}
	
	@Test
	public void testAddCar() throws Exception {
        Car car = Mockito.mock(Car.class);
		car.setId(8L);
		
		Mockito.when(uriInfo.getAbsolutePathBuilder()).thenReturn(uriBuilder);
        Mockito.when(uriBuilder.path(Mockito.anyString())).thenReturn(uriBuilder);
        
		Mockito.when(carService.addCar(car))
				.thenReturn(car);
		
		Response response = carResource.addCar(car);
		
		assertEquals(car, response.getEntity());
	}
	
	@Test
	public void testGetCar() throws Exception {
		Car car = Mockito.mock(Car.class);
		long carId = 8L;
		car.setId(carId);
		
		Mockito.when(carService.getCar(carId))
				.thenReturn(car);
		
		Response response = carResource.getCar(carId);
		
		assertEquals(car, response.getEntity());
	}
	
	@Test
	public void testUpdateCar() {
		Car car = Mockito.mock(Car.class);
		Long carId = 8L;
		car.setId(carId);
		
		Mockito.when(carService.updateCar(car))
				.thenReturn(car);
		
		Response response = carResource.updateCar(carId, car);
		
		assertEquals(car, response.getEntity());
	}
	
	@Test
	public void testDeleteCar() {
		Car car = Mockito.mock(Car.class);
		Long carId = 8L;
		car.setId(carId);
		
		Mockito.when(carService.softRemoveCar(carId))
				.thenReturn(car);
		
		Response response = carResource.deleteCar(carId);
		
		assertEquals(car, response.getEntity());
	}
	
}
