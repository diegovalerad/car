package org.diego.tutorial.car.resources;

import java.net.URI;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.log4j.Logger;
import org.diego.tutorial.car.exceptions.BadRequestException;
import org.diego.tutorial.car.model.Car;
import org.diego.tutorial.car.model.service.CarService;
import org.diego.tutorial.car.validations.car.CarValidator;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

/**
 * Endpoint of our REST service, that provides all the necessary methods
 * regarding cars.
 *
 */
@Path("/cars")
@Consumes(value = { MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
@Produces(value = { MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
@Stateless
public class CarResource {
	@EJB
	private CarService carService;
	private @Context UriInfo uriInfo;
	
	private final static Logger LOGGER = Logger.getLogger(CarResource.class);
	
	/**
	 * Method that retrieves all the cars from the database. <p>
	 * Optionally, the country param can be provided.
	 * @param country Country of the cars
	 * @return List of cars retrieved
	 */
	@GET
	@Operation(summary = "Get all the cars",
			description = "Retrieves all the cars from the system",
			responses = {
					@ApiResponse(
							description = "Cars retrieved",
							responseCode = "200",
							content = @Content(
									array = @ArraySchema(schema = @Schema(implementation = Car.class))
				            )),
			})
	public Response getCars(@QueryParam("country") String country){
		List<Car> cars = null;
		if (country != null && !country.isEmpty()) { // If "country" in the query
			LOGGER.info("Trying to get all the cars from the country '" + country + "'");
			cars = carService.getAllCarsFromCountry(country);
		}else{
			LOGGER.info("Trying to get all the cars");
			cars = carService.getAllCars();
		}
		LOGGER.info("All the cars retrieved");
		// Mapping the List in a generic entity to be able to return it
		GenericEntity<List<Car>> carsGeneric = new GenericEntity<List<Car>>(cars) {};
		return Response.ok()
				.entity(carsGeneric)
				.build();
	}
	
	/**
	 * Method that adds a new car to the database.
	 * @param car Car that needs to be added.
	 * @return Car added
	 */
	@POST
	@Operation(summary = "Create a car",
			description = "Create a car and store it in the system",
			responses = {
					@ApiResponse(
						description = "Car created", 
						responseCode = "201",
						content = @Content(
									schema = @Schema(implementation = Car.class)
								)
						),
					@ApiResponse( 
						description = "Creating a car with non valid fields",
						responseCode = "400"
					),
					@ApiResponse( 
						description = "Not found the brand of the car",
						responseCode = "404"
					)
			}
	)
	public Response addCar(@Parameter(description = "new car object", required = true) Car car) {
		LOGGER.info("Trying to create a car: " + car);
		List<String> validationErrors = CarValidator.validateAddAndUpdate(car);
		String errorMessage = "Request to add a car with non valid fields";
		checkValidationErrors(validationErrors, errorMessage);
		
		Car carAdded = carService.addCar(car);
		String newId = String.valueOf(carAdded.getId());
		URI uri = uriInfo.getAbsolutePathBuilder().path(newId).build();
		
		addAllLinks(carAdded);
		
		return Response.created(uri)
					.entity(carAdded)
					.build();
	}
	
	/**
	 * Method that gets an specific car from the database
	 * @param id Identifier of the requested car
	 * @return Car requested
	 */
	@GET
	@Path("/{id}")
	@Operation(summary = "Get a car",
			description = "Get a car given by an ID from the system",
			responses = {
				@ApiResponse(
						description = "Car retrieved", 
						responseCode = "200",
						content = @Content(
								schema = @Schema(implementation = Car.class)
			            )
				),
				@ApiResponse(
						description = "Car not found",
						responseCode = "404"
				)
			}
	)
	public Response getCar(@Parameter(description = "id of the car that should be retrieved", required = true) 
							@PathParam("id") long id) {
		LOGGER.info("Trying to get the car with ID '" + id + "'");
		String errorMessage = "Request to get a car with non valid ID: " + id;
		checkValidationErrors(id, errorMessage);
		
		Car car = carService.getCar(id);
		addAllLinks(car);
		
		return Response.ok()
						.entity(car)
						.build();
	}
	
	/**
	 * Method that updates the information related to an specific car
	 * @param id Identifier of the car that should be updated
	 * @param car Car object with the new information
	 * @return Car updated
	 */
	@PUT
	@Path("/{id}")
	@Operation(summary = "Update a car",
			description = "Update a car from the system",
			responses = {
				@ApiResponse(
						description = "Car updated", 
						responseCode = "200",
						content = @Content(
								schema = @Schema(implementation = Car.class)
			            )
				),
				@ApiResponse(
						description = "Car not found",
						responseCode = "404"
				),
				@ApiResponse(
						description = "Non valid fields",
						responseCode = "400"
				)
			}
	)
	public Response updateCar(@Parameter(description = "id of the car that should be updated", required = true) 
								@PathParam("id") long id,
							@Parameter(description = "updated car object", required = true) 
								Car car) {
		car.setId(id);
		
		LOGGER.info("Trying to update the car with ID '" + id + "' with the info: " + car);
		
		List<String> validationErrors = CarValidator.validateAddAndUpdate(car);
		String errorMessage = "Request to update car with non valid fields";
		checkValidationErrors(validationErrors, errorMessage);
		
		Car carUpdated = carService.updateCar(car);
		
		addAllLinks(carUpdated);
		
		return Response.ok()
					.entity(carUpdated)
					.build();
	}
	
	/**
	 * Method that soft-removes a car from the database, that is,
	 * the car is set with a flag, and it will be removed
	 * in a period of time.
	 * @param id Identifier of the car that should be removed
	 * @return Car removed
	 */
	@DELETE
	@Path("/{id}")
	@Operation(summary = "Delete a car",
			description = "Soft-delete a car given by an ID from the system. The car will be completely "
						+ "removed from the system after a certain period of time.",
			responses = {
					@ApiResponse(
							description = "Car", 
							responseCode = "200",
							content = @Content(
									schema = @Schema(implementation = Car.class)
				            )
					),
					@ApiResponse(
							description = "Car not found", 
							responseCode = "404"
					),
					@ApiResponse(
							description = "Non valid parameters", 
							responseCode = "400"
					)
			}
	)
	public Response deleteCar(@Parameter(description = "id of the car that should be removed", required = true) @PathParam("id") long id) {
		String errorMessage = "Request to soft-delete a car with non valid ID: " + id;
		
		LOGGER.info("Trying to remove the car with ID '" + id + "'");
		
		checkValidationErrors(id, errorMessage);
		
		Car car = carService.softRemoveCar(id);
		
		addAllLinks(car);
		
		return Response.ok()
					.entity(car)
					.build();
	}
	
	/**
	 * Method that adds all the links related to a car
	 * @param car Car that should have the links added.
	 */
	private void addAllLinks(Car car) {
		String urlSelf = getUriForSelf(car.getId());
		car.addLink(urlSelf, "self");
		String urlBrand = getUriForBrand(car.getBrand().getId());
		car.addLink(urlBrand, "brand");
	}
	
	/**
	 * Method that gets the URI of the car with the given ID.
	 * @param id Identifier of the car
	 * @return String that contains the URI of the given car. 
	 */
	private String getUriForSelf(long id) {
		String urlSelf = uriInfo.getBaseUriBuilder()
				.path(CarResource.class)
				.path(String.valueOf(id))
				.build()
				.toString();
		return urlSelf;
	}
	
	/**
	 * Method that gets the URI of the brand of the car with the given brand.
	 * @param brandName Name of the brand
	 * @return String that contains the URI of the given brand. 
	 */
	private String getUriForBrand(long id) {
		String urlSelf = uriInfo.getBaseUriBuilder()
				.path(BrandResource.class)
				.path(String.valueOf(id))
				.build()
				.toString();
		return urlSelf;
	}
	
	/**
	 * Method that loops though a list of validation errors, throwing a {@link BadRequestException} exception
	 * in case there is one or more validation errors. This exception includes the list of errors and a 
	 * descriptive error message.
	 * @param validationErrors List of possible validation errors
	 * @param errorMessage Descriptive error message thrown in the {@link BadRequestException} exception next to the list of errors. 
	 */
	private void checkValidationErrors(List<String> validationErrors, String errorMessage) {
		LOGGER.info("Checking validation errors");
		if (!validationErrors.isEmpty()) {
			String message = errorMessage + ": ";
			for (String validationError : validationErrors) {
				LOGGER.info("Validation error: " + validationError);
				message += validationError + " - ";
			}
			throw new BadRequestException(message);
		}
		LOGGER.info("Validation errors checked without errors");
	}
	
	/**
	 * Alternative method of checking validation errors, where it is only checked the ID of the car that is being
	 * checked. If the ID is less or equal to zero, a {@link BadRequestException} exception is thrown, including an
	 * error message.
	 * @param id Identifier of the car
	 * @param errorMessage Descriptive error message thrown in the {@link BadRequestException} exception. 
	 */
	private void checkValidationErrors(long id, String errorMessage) {
		LOGGER.info("Checking validation errors");
		if (id <= 0) {
			LOGGER.info("ID is less or equal to 0");
			throw new BadRequestException(errorMessage);
		}
	}
}
