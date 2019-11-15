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
	 * Retrieves all the brands that are part of a company
	 * @param company Company to look for
	 * @return List of brands
	 */
	public List<Brand> getAllBrandsFromCompany(String company) {
		LOGGER.info("Getting all the brands from the company '" + company + "'");
		List<Brand> brands = jpaImplBrand.getAllBrandsFromCompany(company);
		LOGGER.info("All the brands from the company '" + company + "' retrieved");
		return brands;
	}
	
	/**
	 * Adds a brand to the system. It throws: <p>
	 * {@link DataAlreadyExistsException} if the brand already exists (if there is a brand 
	 * with the same brand and company names)
	 * @param brand Brand that should be added
	 * @return Brand added
	 */
	public Brand addBrand(Brand brand) {
		LOGGER.info("Adding the brand '" + brand + "' to the system");
		
		boolean brandExists = jpaImplBrand.brandNameAndCompanyExists(brand.getBrand(), brand.getCompany());
		if (brandExists) {
			throw new DataAlreadyExistsException("Trying to add a brand that already exists");
		}
		
		Brand addedBrand = jpaImplBrand.add(brand);
		LOGGER.info("Brand retrieved: " + addedBrand);
		return addedBrand;
	}
	
	/**
	 * Retrieves a brand from the database. It throws: <p>
	 * {@link DataNotFoundException} if the brand does not exist
	 * @param id Identifier of the brand
	 * @return Brand
	 */
	public Brand getBrand(long id) {
		LOGGER.info("Getting the brand with ID '" + id + "'");
		Brand brand = jpaImplBrand.get(Brand.class, id);
		if (brand == null) {
			LOGGER.info("The brand with ID '" + id + "' does not exist");
			throw new DataNotFoundException("Trying to get a brand with ID '" + id + "' that does not exist");
		}
		LOGGER.info("The brand with ID '" + id + "' exists");
		return brand;
	}
	
	/**
	 * Updates a brand with new information. It throws: <p>
	 * {@link DataNotFoundException} if the brand does not exist
	 * @param brand Object with the new info
	 * @return Updated brand
	 */
	public Brand updateBrand(Brand brand) {
		LOGGER.info("Trying to update the brand: " + brand);
		
		getBrand(brand.getId());
		
		Brand updatedBrand = jpaImplBrand.update(brand);
		LOGGER.info("Brand updated: " + updatedBrand);
		return updatedBrand;
	}
	
	/**
	 * Removes a brand. It throws: <p>
	 * <ul>
	 * <li> {@link DataNotFoundException} if the brand does not exist </li>
	 * <li> {@link BadRequestException} if the brand has others objects that use it
	 * </ul>
	 * @param id Identifier of the brand
	 * @return Removed brand
	 */
	public Brand removeBrand(long id) {
		LOGGER.info("Trying to remove the brand with ID '" + id + "'");
		
		Brand brand = getBrand(id);
		
		if (brandIsBeingUsed(id)) {
			throw new BadRequestException("Trying to remove a brand with ID '" + id + "' that has other objects using it");
		}
		
		Brand removedBrand = jpaImplBrand.delete(brand);
		
		return removedBrand;
	}

	/**
	 * Method that checks if a brand is being used by another entity
	 * @param brandId Identifier of the brand that should be checked
	 * @return Boolean
	 */
	private boolean brandIsBeingUsed(long brandId) {
		List<Car> carsUsedByBrand = carService.getAllCarsFromBrand(brandId);
		
		return !carsUsedByBrand.isEmpty();
	}
	
	
	
	
}
