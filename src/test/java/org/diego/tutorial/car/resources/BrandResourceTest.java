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
import org.diego.tutorial.car.exceptions.DataNotFoundException;
import org.diego.tutorial.car.model.Brand;
import org.diego.tutorial.car.model.service.BrandService;
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
 * Set of tests for the {@link BrandResource} class
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Validation.class, GeneralValidator.class})
public class BrandResourceTest {
	@InjectMocks 
	private BrandResource brandResource;
	@Mock
	private BrandService brandService;
	@Mock
	private UriInfo uriInfo;
	
	private UriBuilder uriBuilder;
	
	private long brandId;
	private String brandName;
	private String company;
	private Brand brand;
	private List<Brand> brands;

	@Before
	public void setup() throws Exception {
		setupUriInfo();
		setupValidationErrors();
		
		brandId = 1L;
		brandName = "brand";
		company = "company";
		brand = new Brand(brandId, brandName, company);
		brands = new ArrayList<Brand>();
		brands.add(brand);
	}
	
	private void setupUriInfo() throws Exception {
		uriBuilder = Mockito.mock(UriBuilder.class);
		
		Mockito.when(uriInfo.getBaseUriBuilder()).thenReturn(uriBuilder);
        Mockito.when(uriBuilder.path(BrandResource.class)).thenReturn(uriBuilder);
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
        
        PowerMockito.mockStatic(GeneralValidator.class);
        PowerMockito.when(GeneralValidator.class, "validateObject", Mockito.any(Object.class))
        		.thenReturn(validationErrors);
	}
	
	@Test
	public void testGetBrands() {
		Mockito.when(brandService.getAllBrands())
				.thenReturn(brands);
		
		assertEquals(brands, brandResource.getBrands(null).getEntity());
	}
	
	@Test
	public void testGetBrandsFromCompany() {
		Mockito.when(brandService.getAllBrandsFromCompany(company))
				.thenReturn(brands);
		
		assertEquals(brands, brandResource.getBrands(company).getEntity());
	}
	
	@Test
	public void testAddBrand() {
		Mockito.when(uriInfo.getAbsolutePathBuilder())
				.thenReturn(uriBuilder);
		
		Mockito.when(brandService.addBrand(brand))
				.thenReturn(brand);
		
		assertEquals(brand, brandResource.addBrand(brand).getEntity());
	}
	
	@Test (expected = BadRequestException.class)
	public void testAddBrandWithInvalidFields() throws Exception {
		Mockito.when(uriInfo.getAbsolutePathBuilder())
				.thenReturn(uriBuilder);

		List<String> validationErrors = new ArrayList<String>();
		validationErrors.add("error");
		PowerMockito.when(GeneralValidator.class, "validateObject", Mockito.any(Object.class))
					.thenReturn(validationErrors);
		
		brandResource.addBrand(brand);
	}
	
	@Test
	public void testGetBrand() {
		Mockito.when(brandService.getBrand(brandId))
				.thenReturn(brand);
		
		assertEquals(brand, brandResource.getBrand(brandId).getEntity());
	}
	
	@Test (expected = DataNotFoundException.class)
	public void testGetBrandNonExisting() {
		Mockito.when(brandService.getBrand(brandId))
				.thenThrow(DataNotFoundException.class);
		
		brandResource.getBrand(brandId);
	}
	
	@Test
	public void testUpdate() {
		Mockito.when(brandService.getBrand(brandId))
				.thenReturn(brand);
		Mockito.when(brandService.updateBrand(brand))
				.thenReturn(brand);
		
		assertEquals(brand, brandResource.updateBrand(brandId, brand).getEntity());
	}
	
	@Test (expected = BadRequestException.class)
	public void testUpdateNotValid() throws Exception {
		List<String> validationErrors = new ArrayList<String>();
		validationErrors.add("error");
		PowerMockito.when(GeneralValidator.class, "validateObject", Mockito.any(Object.class))
					.thenReturn(validationErrors);
		
		brandResource.updateBrand(brandId, brand);
	}
	
	@Test (expected = DataNotFoundException.class)
	public void testUpdateNonExisting() {
		Mockito.when(brandService.updateBrand(brand))
				.thenThrow(DataNotFoundException.class);
		
		brandResource.updateBrand(brandId, brand);
	}
	
	@Test
	public void testRemoveBrand() {
		Mockito.when(brandService.getBrand(brandId))
				.thenReturn(brand);
		Mockito.when(brandService.removeBrand(brandId))
				.thenReturn(brand);
		
		assertEquals(brand, brandResource.removeBrand(brandId).getEntity());
	}
	
	@Test (expected = DataNotFoundException.class)
	public void testRemoveBrandNonExisting() {
		Mockito.when(brandService.removeBrand(brandId))
				.thenThrow(DataNotFoundException.class);

		brandResource.removeBrand(brandId);
	}

}
