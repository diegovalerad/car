package org.diego.tutorial.car.validations.brand;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.log4j.Logger;
import org.diego.tutorial.car.model.Brand;

/**
 * Class that validates {@link Brand} objects
 *
 */
public class BrandValidator {
	
	private static ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	private static Validator validator = factory.getValidator();
	private final static Logger LOGGER = Logger.getLogger(BrandValidator.class);
	
	/**
	 * Method that validates a {@link Brand} object
	 * @param brand Object to validate
	 * @return List of validation errors
	 */
	public static List<String> validateBrand(Brand brand) {
		LOGGER.info("Validating a brand: " + brand);
		
		Set<ConstraintViolation<Brand>> violations = validator.validate(brand);
		List<String> validationErrors = new ArrayList<String>();
		for (ConstraintViolation<Brand> constraintViolation : violations) {
			String validationError = constraintViolation.getMessage();
			validationErrors.add(validationError);
			LOGGER.debug("Error validating brand: " + validationError);
		}
		
		LOGGER.info("Validation of brand '" + brand + "' finished with " + validationErrors.size() + " errors");
		
		return validationErrors;
	}
}
