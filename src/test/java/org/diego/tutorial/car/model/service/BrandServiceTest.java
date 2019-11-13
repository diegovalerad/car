package org.diego.tutorial.car.model.service;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.diego.tutorial.car.databases.jpa.JPAImplBrand;
import org.diego.tutorial.car.model.Brand;
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
	
	private String brandName;
	private String company;
	private Brand brand;
	
	@Before
	public void setUp() {
		brandName = "brandName";
		company = "company";
		brand = new Brand(brandName, company);
	}
	
	@Test
	public void testGetAllBrands() {
		List<Brand> brands = new ArrayList<Brand>();
		
		Mockito.when(jpaImplBrand.getAll(Brand.class))
				.thenReturn(brands);
		
		assertEquals(brands, brandService.getAllBrands());
	}

	@Test
	public void testGetAllBrandsFromCompany() {
		List<Brand> brands = new ArrayList<Brand>();
		
		Mockito.when(jpaImplBrand.getAllBrandsFromCompany(company))
				.thenReturn(brands);
		
		assertEquals(brands, brandService.getAllBrandsFromCompany(company));
	}
	
	@Test
	public void testGetBrand() {
		Mockito.when(jpaImplBrand.get(Brand.class, brandName))
				.thenReturn(brand);
		
		assertEquals(brand, brandService.getBrand(brandName));
	}
	
	@Test
	public void testAddBrand() {
		Mockito.when(jpaImplBrand.get(Brand.class, brandName.toLowerCase()))
				.thenReturn(null);
		
		Mockito.when(jpaImplBrand.add(brand))
				.thenReturn(brand);
		
		assertEquals(brand, brandService.addBrand(brand));
	}
	
	@Test
	public void testUpdateBrand() {
		Mockito.when(jpaImplBrand.get(Brand.class, brandName.toLowerCase()))
				.thenReturn(brand);
		Mockito.when(jpaImplBrand.update(brand))
				.thenReturn(brand);
		
		assertEquals(brand, brandService.updateBrand(brand));
	}
	
	@Test
	public void testRemoveBrand() {
		Mockito.when(jpaImplBrand.get(Brand.class, brandName.toLowerCase()))
				.thenReturn(brand);
		Mockito.when(jpaImplBrand.delete(brand))
				.thenReturn(brand);
		
		assertEquals(brand, brandService.removeBrand(brandName));
	}
	
	
	
}
