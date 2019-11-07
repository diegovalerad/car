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

import org.diego.tutorial.car.model.Car;
import org.diego.tutorial.car.model.service.CarService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@Path("/cars")
@Consumes(value = { MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
@Produces(value = { MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
@Stateless
public class CarResource {
	@EJB
	private CarService carService;
	private @Context UriInfo uriInfo;
	
	@GET
	@Operation(summary = "Get all the cars",
			description = "Retrieves all the cars from the system",
			responses = {
					@ApiResponse(
							description = "Cars",
							responseCode = "200",
							content = @Content(
									array = @ArraySchema(schema = @Schema(implementation = Car.class))
				            )),
			})
	public Response getCars(@QueryParam("country") String country){
		List<Car> cars = null;
		if (country != null && !country.isEmpty()) { // If "country" in the query
			cars = carService.getAllCarsFromCountry(country);
		}else{
			cars = carService.getAllCars();
		}
		// Mapping the List in a generic entity to be able to return it
		GenericEntity<List<Car>> carsGeneric = new GenericEntity<List<Car>>(cars) {};
		return Response.ok()
					.entity(carsGeneric)
					.build();
	}
	
	@POST
	@Operation(summary = "Create a car",
	description = "Create a car and store it in the system",
	responses = {
			@ApiResponse(
					description = "Car", 
					responseCode = "201",
					content = @Content(
                    schema = @Schema(implementation = Car.class)
		            )),
			@ApiResponse(responseCode = "409", description = "Car already exists"),
	})
	public Response addCar(@Parameter(description = "updated car object", required = true) Car car) {
		Car carAdded = carService.addCar(car);
		String newId = String.valueOf(carAdded.getId());
		URI uri = uriInfo.getAbsolutePathBuilder().path(newId).build();
		
		String urlSelf = getUriForSelf(carAdded.getId());
		car.addLink(urlSelf, "self");
		
		return Response.created(uri)
					.entity(carAdded)
					.build();
	}
	
	@GET
	@Path("/{id}")
	@Operation(summary = "Get a car",
	description = "Get a car given by an ID from the system",
	responses = {
			@ApiResponse(
					description = "Car", 
					responseCode = "200",
					content = @Content(
                    schema = @Schema(implementation = Car.class)
		            )),
			@ApiResponse(responseCode = "404", description = "Car not found"),
	})
	public Response getCar(@Parameter(description = "id of the car that should be retrieved", required = true) @PathParam("id") long id) {
		Car car = carService.getCar(id);
		String urlSelf = getUriForSelf(id);
		car.addLink(urlSelf, "self");
		
		return Response.ok()
						.entity(car)
						.build();
	}
	
	@PUT
	@Path("/{id}")
	@Operation(summary = "Update a car",
	description = "Update a car from the system",
	responses = {
			@ApiResponse(
					description = "Car", 
					responseCode = "200",
					content = @Content(
                    schema = @Schema(implementation = Car.class)
		            )),
			@ApiResponse(responseCode = "404", description = "Car not found"),
	})
	public Response updateCar(@Parameter(description = "id of the car that should be updated", required = true) @PathParam("id") long id,
			@Parameter(description = "updated car object", required = true) Car car) {
		car.setId(id);
		Car carUpdated = carService.updateCar(car);
		
		String urlSelf = getUriForSelf(id);
		car.addLink(urlSelf, "self");
		
		return Response.ok()
					.entity(carUpdated)
					.build();
	}
	
	@DELETE
	@Path("/{id}")
	@Operation(summary = "Delete a car",
	description = "Delete a car given by an ID from the system",
	responses = {
			@ApiResponse(
					description = "Car", 
					responseCode = "200",
					content = @Content(
                    schema = @Schema(implementation = Car.class)
		            )),
			@ApiResponse(responseCode = "404", description = "Car not found"),
	})
	public Response deleteCar(@Parameter(description = "id of the car that should be removed", required = true) @PathParam("id") long id) {
		Car car = carService.removeCar(id);
		
		String urlSelf = getUriForSelf(id);
		car.addLink(urlSelf, "self");
		
		return Response.ok()
					.entity(car)
					.build();
	}
	
	private String getUriForSelf(long id) {
		String urlSelf = uriInfo.getBaseUriBuilder()
				.path(CarResource.class)
				.path(String.valueOf(id))
				.build()
				.toString();
		return urlSelf;
	}
}
