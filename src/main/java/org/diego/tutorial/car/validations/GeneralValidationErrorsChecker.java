package org.diego.tutorial.car.validations;

import java.util.List;

import org.diego.tutorial.car.exceptions.BadRequestException;

/**
 * Class that checks the validation errors of objects
 *
 */
public class GeneralValidationErrorsChecker {
	
	/**
	 * Method that checks the validation errors of an object, and throws an {@link BadRequestException} in
	 * case there are any of them.
	 * @param object Object to be validated
	 * @param operation Operation of the object
	 */
	public static void checkValidationErrors(Object object, String operation) {
		List<String> validationErrors = GeneralValidator.validateObject(object);
		String errorMessage = "Request to " + operation + " an object with non-valid fields";
		
		if (!validationErrors.isEmpty()) {
			String message = errorMessage + ": ";
			for (String validationError : validationErrors) {
				message += validationError + " - ";
			}
			throw new BadRequestException(message);
		}
	}
}
