package org.diego.tutorial.car.validations.car;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.log4j.Logger;
import org.diego.tutorial.car.model.Car;

/**
 * Class that validates {@link Car} objects
 *
 */
public class CarValidator {

	private static ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	private static Validator validator = factory.getValidator();
	private final static Logger LOGGER = Logger.getLogger(CarValidator.class);
	
	/**
	 * Method that validates a car. 
	 * @param car Car that should be validated
	 * @return Boolean indicating whether there is any validation error or not.
	 */
	public static List<String> validateCar(Car car) {
		LOGGER.info("Validating a car: " + car);
		
		Set<ConstraintViolation<Car>> violations = validator.validate(car);
		List<String> validationErrors = new ArrayList<String>();
		for (ConstraintViolation<Car> constraintViolation : violations) {
			validationErrors.add(constraintViolation.getMessage());
			LOGGER.debug("error validating car: " + constraintViolation.getMessage());
		}
		
		LOGGER.info("End of the validation of the car: " + car);
		return validationErrors;
	}
	
	public static List<String> validateAddAndUpdate(Car car) {
		LOGGER.info("Validating a car: " + car);
		
		Set<ConstraintViolation<Car>> violations = validator.validate(car, AddAndUpdateChecks.class);
		List<String> validationErrors = new ArrayList<String>();
		for (ConstraintViolation<Car> constraintViolation : violations) {
			validationErrors.add(constraintViolation.getMessage());
			LOGGER.debug("error validating car: " + constraintViolation.getMessage());
		}
		
		LOGGER.info("End of the validation of the car: " + car);
		return validationErrors;
	}
}
