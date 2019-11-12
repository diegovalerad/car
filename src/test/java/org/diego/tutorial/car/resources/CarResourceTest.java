package org.diego.tutorial.car.resources;

import static org.junit.Assert.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriBuilderException;
import javax.ws.rs.core.UriInfo;

import org.diego.tutorial.car.model.Car;
import org.diego.tutorial.car.model.service.CarService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CarResourceTest {
	
	@InjectMocks
	private CarResource carResource;
	@Mock
	private CarService carService;
	@Mock
	private UriInfo uriInfo;
	
	private UriBuilder uriBuilder;

	@Test
	public void testGetCars() {
		List<Car> cars = new ArrayList<Car>();
		Car car = Mockito.mock(Car.class);
		cars.add(car);
		
		Mockito.when(carService.getAllCars())
				.thenReturn(cars);
		
		GenericEntity<List<Car>> carsGeneric = new GenericEntity<List<Car>>(cars) {};
		
		assertEquals(carsGeneric, carResource.getCars(null).getEntity());
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
		
		GenericEntity<List<Car>> carsGeneric = new GenericEntity<List<Car>>(cars) {};
		
		assertEquals(carsGeneric, carResource.getCars(country).getEntity());
	}
	
	@Before
	public void setup() throws IllegalArgumentException, UriBuilderException, URISyntaxException {
		uriBuilder = Mockito.mock(UriBuilder.class);
		
		Mockito.when(uriInfo.getBaseUriBuilder()).thenReturn(uriBuilder);
        Mockito.when(uriBuilder.path(CarResource.class)).thenReturn(uriBuilder);
        Mockito.when(uriBuilder.build()).thenReturn(new URI("www.abc.es"));
        Mockito.when(uriBuilder.toString()).thenReturn("http://www.prueba.es");
	}
	
	@Test
	public void testAddCar() throws IllegalArgumentException, UriBuilderException, URISyntaxException {
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
	public void testGetCar() {
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
		
		Mockito.when(carService.removeCar(carId))
				.thenReturn(car);
		
		Response response = carResource.deleteCar(carId);
		
		assertEquals(car, response.getEntity());
	}
	
}
