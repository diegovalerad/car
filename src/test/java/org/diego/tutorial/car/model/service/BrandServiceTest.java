package org.diego.tutorial.car.model.service;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.diego.tutorial.car.databases.jpa.JPAImplBrand;
import org.diego.tutorial.car.exceptions.BadRequestException;
import org.diego.tutorial.car.exceptions.DataAlreadyExistsException;
import org.diego.tutorial.car.exceptions.DataNotFoundException;
import org.diego.tutorial.car.model.Brand;
import org.diego.tutorial.car.model.Car;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Set of unit tests for the {@link BrandService} class
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class BrandServiceTest {
	@InjectMocks
	private BrandService brandService;
	@Mock
	private JPAImplBrand jpaImplBrand;
	@Mock
	private CarService carService;
	
	private long brandId;
	private String brandName;
	private String company;
	private Brand brand;
	private List<Brand> brands;
	
	@Before
	public void setup() {
		brandId = 1L;
		brandName = "brand";
		company = "company";
		brand = new Brand(brandId, brandName, company);
		brands = new ArrayList<Brand>();
		brands.add(brand);
	}

	@Test
	public void testGetAllBrands() {
		Mockito.when(jpaImplBrand.getAll(Brand.class))
				.thenReturn(brands);
		
		assertEquals(brands, brandService.getAllBrands());
	}
	
	@Test
	public void testGetAllBrandsFromCompany() {
		Mockito.when(jpaImplBrand.getAllBrandsFromCompany(company))
				.thenReturn(brands);
		
		assertEquals(brands, brandService.getAllBrandsFromCompany(company));
	}

	@Test
	public void testAddBrand() {
		Mockito.when(jpaImplBrand.brandNameAndCompanyExists(brandName, company))
				.thenReturn(false);
		Mockito.when(jpaImplBrand.add(brand))
				.thenReturn(brand);
		
		assertEquals(brand, brandService.addBrand(brand));
	}
	
	@Test (expected = DataAlreadyExistsException.class)
	public void testAddBrandThatExists() {
		Mockito.when(jpaImplBrand.brandNameAndCompanyExists(brandName, company))
				.thenReturn(true);
		
		brandService.addBrand(brand);
	}
	
	@Test
	public void testGetBrand() {
		Mockito.when(jpaImplBrand.get(Brand.class, brandId))
				.thenReturn(brand);
		
		assertEquals(brand, brandService.getBrand(brandId));
	}
	
	@Test (expected = DataNotFoundException.class)
	public void testGetBrandNotExists() {
		Mockito.when(jpaImplBrand.get(Brand.class, brandId))
				.thenReturn(null);
		
		brandService.getBrand(brandId);
	}

	@Test
	public void testUpdate() {
		Mockito.when(jpaImplBrand.get(Brand.class, brandId))
				.thenReturn(brand);
		Mockito.when(jpaImplBrand.update(brand))
				.thenReturn(brand);
		
		assertEquals(brand, brandService.updateBrand(brand));
	}
	
	@Test (expected = DataNotFoundException.class)
	public void testUpdateNotExists() {
		Mockito.when(jpaImplBrand.get(Brand.class, brandId))
				.thenReturn(null);
		
		brandService.updateBrand(brand);
	}
	
	@Test
	public void testRemove() {
		Mockito.when(jpaImplBrand.get(Brand.class, brandId))
				.thenReturn(brand);
		Mockito.when(carService.getAllCarsFromBrand(brandId))
				.thenReturn(new ArrayList<Car>());
		Mockito.when(jpaImplBrand.delete(brand))
				.thenReturn(brand);
		
		assertEquals(brand, brandService.removeBrand(brandId));
	}
	
	@Test (expected = DataNotFoundException.class)
	public void testRemoveNotExists() {
		Mockito.when(jpaImplBrand.get(Brand.class, brandId))
				.thenReturn(null);
		
		brandService.removeBrand(brandId);
	}
	
	@Test (expected = BadRequestException.class)
	public void testRemoveWithOthersUsingTheBrand() {
		Mockito.when(jpaImplBrand.get(Brand.class, brandId))
				.thenReturn(brand);
		
		Car car = Mockito.mock(Car.class);
		List<Car> cars = new ArrayList<Car>();
		cars.add(car);
		Mockito.when(carService.getAllCarsFromBrand(brandId))
				.thenReturn(cars);
		
		brandService.removeBrand(brandId);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
