package org.diego.tutorial.car.validations;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.log4j.Logger;

/**
 * Class that validates {@link Brand} objects
 *
 */
public class GeneralValidator {
	private static ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	private static Validator validator = factory.getValidator();
	private final static Logger LOGGER = Logger.getLogger(GeneralValidator.class);
	
	/**
	 * Method that validates an Object
	 * @param object Object to validate
	 * @return List of validation errors
	 */
	protected static List<String> validateObject(Object object) {
		LOGGER.info("Validating the object: " + object);
		
		Set<ConstraintViolation<Object>> violations = validator.validate(object);
		List<String> validationErrors = new ArrayList<String>();
		for (ConstraintViolation<Object> constraintViolation : violations) {
			String validationError = constraintViolation.getMessage();
			validationErrors.add(validationError);
			LOGGER.info("Error validating brand: " + validationError);
		}
		
		LOGGER.info("Validation of object '" + object + "' finished with " + validationErrors.size() + " errors");
		
		return validationErrors;
	}
}
