package org.diego.tutorial.car.model.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.diego.tutorial.car.databases.jpa.JPAImplBrand;
import org.diego.tutorial.car.exceptions.BadRequestException;
import org.diego.tutorial.car.exceptions.DataAlreadyExistsException;
import org.diego.tutorial.car.exceptions.DataNotFoundException;
import org.diego.tutorial.car.model.Brand;
import org.diego.tutorial.car.model.Car;

/**
 * Class that represents the service of Brands, in charge of doing the operations involving brands, 
 * connecting the REST API and the JPA persistence. 
 *
 */
@Stateless
public class BrandService {
	@EJB
	private JPAImplBrand jpaImplBrand;
	
	@EJB
	private CarService carService;
	
	private final static Logger LOGGER = Logger.getLogger(BrandService.class);
	
	public BrandService() {
		
	}

	/**
	 * Retrieves all the brands from the database
	 * @return List of brands
	 */
	public List<Brand> getAllBrands() {
		LOGGER.info("Getting all the brands from the database");
		List<Brand> brands = jpaImplBrand.getAll(Brand.class);
		LOGGER.info("All the brands retrieved from the database.");
		return brands;
	}
	
	/**
	 * Retrieves all the brands from a certain company
	 * @param company Company which brands are part of.
	 * @return List of brands
	 */
	public List<Brand> getAllBrandsFromCompany(String company) {
		String companyLowerCase = company.toLowerCase();
		LOGGER.info("Getting all the brands from the company '" + companyLowerCase + "'");
		List<Brand> brands = jpaImplBrand.getAllBrandsFromCompany(companyLowerCase);
		LOGGER.info("All the brands retrieved from the database.");
		return brands;
	}
	
	/**
	 * Retrieves a requested brand given by its name
	 * @param brandName Name of the brand
	 * @return Brand
	 */
	public Brand getBrand(String brandName) {
		brandName = brandName.toLowerCase();
		LOGGER.info("Getting the brand '" + brandName + "'");
		Brand brand = jpaImplBrand.get(Brand.class, brandName);
		if (brand == null)
			throw new DataNotFoundException("Trying to get a brand which name '" + brandName + "' does not exist");
		LOGGER.info("Brand retrieved: " + brand);
		return brand;
	}

	/**
	 * Creates a new brand in the system
	 * @param brand Brand object that should be created
	 * @return Created brand
	 */
	public Brand addBrand(Brand brand) {
		String brandName = brand.getBrand().toLowerCase();
		brand.setBrand(brandName);
		if (brandAlreadyExists(brand.getBrand())) {
			String message = "Trying to add a brand with name: '" + brand.getBrand() + "' that already exists.";
			LOGGER.info(message);
			throw new DataAlreadyExistsException(message);
		}
		Brand addedBrand = jpaImplBrand.add(brand);
		return addedBrand;
	}
	
	/**
	 * Tries to update an existing brand
	 * @param brand name of the brand that should be updated
	 * @param updateBrand object with the new info for the brand
	 * @return Updated brand
	 */
	public Brand updateBrand(String brand, Brand updateBrand) {
		String brandNameLowerCase = updateBrand.getBrand().toLowerCase();
		String brandName2LowerCase = brand.toLowerCase();
		
		if (!brandNameLowerCase.equals(brandName2LowerCase)) {
			String message = "The brand name cannot be updated";
			LOGGER.info(message);
			throw new BadRequestException(message);
		}
		
		updateBrand.setBrand(brandNameLowerCase);
		if (!brandAlreadyExists(updateBrand.getBrand())) {
			String message = createErrorMessageBrandDoesNotExist("update", updateBrand.getBrand());
			LOGGER.info(message);
			throw new DataNotFoundException(message);
		}
		Brand updatedBrand = jpaImplBrand.update(updateBrand);
		return updatedBrand;
	}
	
	/**
	 * Tries to remove an existing brand
	 * @param brandName Brand that should be removed
	 * @return Removed brand
	 */
	public Brand removeBrand(String brandName) {
		brandName = brandName.toLowerCase();
		if (!brandAlreadyExists(brandName)) {
			String message = createErrorMessageBrandDoesNotExist("delete", brandName);
			LOGGER.info(message);
			throw new DataNotFoundException(message);
		}
		
		Brand brand = jpaImplBrand.get(Brand.class, brandName);
		if (brand == null)
			throw new DataNotFoundException("Trying to get a brand with name '" + brandName + "' that does not exists");
		
		if (brandHasCars(brandName)) {
			throw new BadRequestException("Trying to remove the brand '" + brandName + "' that has cars");
		}
		
		Brand removedBrand = jpaImplBrand.delete(brand);
		return removedBrand;
	}
	
	/**
	 * Checks if a brand already exists
	 * @param brandName Name of the brand
	 * @return Boolean
	 */
	private boolean brandAlreadyExists(String brandName) {
		LOGGER.info("Checking if the brand '" + brandName + "' exists");
		
		Brand brand = jpaImplBrand.get(Brand.class, brandName); 
		if (brand == null) {
			LOGGER.info("The brand '" + brandName + "' does not exist");
			return false;
		}
		LOGGER.info("The brand '" + brandName + "' exists");
		return true;
	}
	
	/**
	 * Method that creates a message error, indicating the operation and the name of the brand that
	 * produced that error.
	 * @param operation Operation (get/update/add/remove) that produced the error.
	 * @param brand Name of the brand that produced the error.
	 * @return String with a message error.
	 */
	private String createErrorMessageBrandDoesNotExist(String operation, String brand) {
		return "Trying to " + operation + " a brand with name '" + brand + "' that does not exist.";
	}
	
	private boolean brandHasCars(String brandName) {
		LOGGER.info("Checking the number of cars of the brand '" + brandName + "'");
		List<Car> carsOfBrand = carService.getAllCarsFromBrand(brandName);
		LOGGER.info("The brand '" + brandName + "' has " + carsOfBrand.size() + " cars");
		return !carsOfBrand.isEmpty();
	}
	
	
}
