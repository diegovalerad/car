package org.diego.tutorial.car.model.service;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.diego.tutorial.car.databases.jpa.JPAImplCar;
import org.diego.tutorial.car.exceptions.BadRequestException;
import org.diego.tutorial.car.exceptions.DataNotFoundException;
import org.diego.tutorial.car.model.Brand;
import org.diego.tutorial.car.model.Car;
import org.diego.tutorial.car.model.Country;
import org.diego.tutorial.car.validations.GeneralValidator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Set of unit tests for the {@link CarService} class
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Validation.class, GeneralValidator.class})
public class CarServiceTest {
	
	@InjectMocks
	private CarService carService;
	@Mock
	private JPAImplCar jpaImpl;
	@Mock
	private BrandService brandService;
	@Mock
	private CountryService countryService;
	
	private void mockValidationErrors() throws Exception {
		List<String> validationErrors = new ArrayList<String>();
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
	
	@Test
	public void testGetAllCars() {
		List<Car> cars = new ArrayList<Car>();
		
		Mockito.when(jpaImpl.getAll(Car.class))
				.thenReturn(cars);
		
		assertEquals(cars, carService.getAllCars());
	}
	
	@Test
	public void testGetCar() {
		long id = 1;
		Car car = Mockito.mock(Car.class);
		car.setId(id);
		
		Mockito.when(jpaImpl.get(Car.class, id))
				.thenReturn(car);
		
		assertEquals(car, carService.getCar(id));
	}
	
	@Test
	public void testGetAllCarsFromCountry() {
		List<Car> cars = new ArrayList<Car>();
		Country countrySpain = new Country(1L, "spain", "esp");
		Country countryFrance = new Country(2L, "france", "fr");
		
		Car carSpain = Mockito.mock(Car.class);
		carSpain.setCountry(countrySpain);
		Car carFrance = Mockito.mock(Car.class);
		carFrance.setCountry(countryFrance);
		
		cars.add(carSpain);
		cars.add(carFrance);
		
		List<Car> carsExpect = new ArrayList<Car>();
		carsExpect.add(carSpain);
		
		Mockito.when(jpaImpl.getAllCarsFromCountry(countrySpain.getCountryName()))
				.thenReturn(carsExpect);
		
		assertEquals(carsExpect, carService.getAllCarsFromCountry(countrySpain.getCountryName()));
	}
	
	@Test
	public void testAddCar() throws Exception {
		Car car = new Car();
		Brand brand = Mockito.mock(Brand.class);
		car.setBrand(brand);
		Country country = Mockito.mock(Country.class);
		car.setCountry(country);
		
		mockValidationErrors();
		
		Mockito.when(jpaImpl.add(car))
				.thenReturn(car);
		
		assertEquals(car, carService.addCar(car));
	}
	
	@Test (expected = BadRequestException.class)
	public void testAddCarWithoutBrand() {
		Car car = new Car();
		
		carService.addCar(car);
	}
	
	@Test (expected = BadRequestException.class)
	public void testAddCarWithNonValidBrand() throws Exception {
		Car car = new Car();
		Brand brand = Mockito.mock(Brand.class);
		car.setBrand(brand);
		
		mockValidationErrors();
		List<String> validationErrors = new ArrayList<String>();
		validationErrors.add("error");
		PowerMockito.when(GeneralValidator.class, "validateObject", Mockito.any(Object.class))
					.thenReturn(validationErrors);
		
		carService.addCar(car);
	}
	
	@Test (expected = DataNotFoundException.class)
	public void testAddCarWithNonExistingBrand() throws Exception {
		Car car = new Car();
		Brand brand = Mockito.mock(Brand.class);
		car.setBrand(brand);
		
		mockValidationErrors();
		Mockito.when(brandService.getBrand(brand.getId()))
				.thenThrow(DataNotFoundException.class);
		
		carService.addCar(car);
	}
	
	@Test
	public void testUpdateCar() throws Exception {
		long id = 1;
		Car car = new Car();
		car.setId(id);
		Brand brand = Mockito.mock(Brand.class);
		car.setBrand(brand);
		Country country = Mockito.mock(Country.class);
		car.setCountry(country);
		
		Mockito.when(jpaImpl.get(Car.class, id))
				.thenReturn(car);
		
		mockValidationErrors();
		
		Mockito.when(jpaImpl.update(car))
				.thenReturn(car);
		
		assertEquals(car, carService.updateCar(car));
	}
	
	@Test
	public void testSoftRemoveCar() {
		long id = 1;
		Car car = new Car();
		car.setId(id);
		
		Mockito.when(jpaImpl.get(Car.class, id))
				.thenReturn(car);
		
		assertEquals(car, carService.softRemoveCar(id));
		assertEquals(true, car.isSoftRemoved());
	}
	
	@Test
	public void testRemoveCar() {
		long id = 1;
		Car car = new Car();
		car.setId(id);
		
		Mockito.when(jpaImpl.get(Car.class, id))
				.thenReturn(car);
		Mockito.when(jpaImpl.delete(car))
				.thenReturn(car);
		
		assertEquals(car, carService.removeCar(car));
	}
}
