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

import org.diego.tutorial.car.model.Brand;
import org.diego.tutorial.car.model.service.BrandService;
import org.diego.tutorial.car.validations.brand.BrandValidator;
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
 * Set of unit tests for the {@link BrandResource} class
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Validation.class, BrandValidator.class})
public class BrandResourceTest {
	
	@InjectMocks
	private BrandResource brandResource;
	@Mock
	private BrandService brandService; 
	@Mock
	private UriInfo uriInfo;
	private UriBuilder uriBuilder;
	
	private String brandName;
	private String company;
	private Brand brand;
	
	@Before
	public void setUp() throws Exception {
		setupUriInfo();
		setupValidationErrors();
		
		brandName = "brandName";
		company = "company";
		brand = new Brand(brandName, company);
	}
	
	private void setupUriInfo() throws Exception {
		uriBuilder = Mockito.mock(UriBuilder.class);
		
		Mockito.when(uriInfo.getBaseUriBuilder()).thenReturn(uriBuilder);
        Mockito.when(uriBuilder.path(BrandResource.class)).thenReturn(uriBuilder);
        Mockito.when(uriBuilder.path(Mockito.anyString())).thenReturn(uriBuilder);
        Mockito.when(uriBuilder.build()).thenReturn(new URI("www.abc.es"));
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
        
        PowerMockito.mockStatic(BrandValidator.class);
        PowerMockito.when(BrandValidator.class, "validateBrand", Mockito.any(Brand.class))
        		.thenReturn(validationErrors);
	}

	@Test
	public void testGetBrands() {
		List<Brand> brands = new ArrayList<Brand>();
		
		Mockito.when(brandService.getAllBrands())
				.thenReturn(brands);
		
		assertEquals(brands, brandResource.getBrands(null).getEntity());
	}
	
	@Test
	public void testGetBrandsFromCountry() {
		List<Brand> brandsFromCountry = new ArrayList<Brand>();
		
		Mockito.when(brandService.getAllBrandsFromCompany(company))
				.thenReturn(brandsFromCountry);
		
		assertEquals(brandsFromCountry, brandResource.getBrands(company).getEntity());
	}
	
	@Test
	public void testAddBrand() {
		Mockito.when(brandService.addBrand(brand))
				.thenReturn(brand);
		
		Mockito.when(uriInfo.getAbsolutePathBuilder()).thenReturn(uriBuilder);
		
		assertEquals(brand, brandResource.addBrand(brand).getEntity());
	}
	
	@Test
	public void testGetBrand() {
		Mockito.when(brandService.getBrand(brandName))
				.thenReturn(brand);
		
		assertEquals(brand, brandResource.getBrand(brandName).getEntity());
	}
	
	@Test
	public void testUpdateBrand() {
		Mockito.when(brandService.updateBrand(brand))
				.thenReturn(brand);
		
		assertEquals(brand, brandResource.updateBrand(brandName, brand).getEntity());
	}
	
	@Test
	public void testRemoveBrand() {
		Mockito.when(brandService.removeBrand(brandName))
				.thenReturn(brand);
		
		assertEquals(brand, brandResource.removeBrand(brandName).getEntity());
	}
}
