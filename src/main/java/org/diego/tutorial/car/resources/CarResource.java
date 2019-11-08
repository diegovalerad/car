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
	
	/**
	 * Method that retrieves all the cars from the database. <p>
	 * Optionally, the country param can be provided.
	 * @param country Country of the cars
	 * @return List of cars retrieved
	 */
	@GET
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
	
	/**
	 * Method that adds a new car to the database.
	 * @param car Car that needs to be added.
	 * @return Car added
	 */
	@POST
	public Response addCar(Car car) {
		Car carAdded = carService.addCar(car);
		String newId = String.valueOf(carAdded.getId());
		URI uri = uriInfo.getAbsolutePathBuilder().path(newId).build();
		
		String urlSelf = getUriForSelf(carAdded.getId());
		car.addLink(urlSelf, "self");
		
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
	public Response getCar(@PathParam("id") long id) {
		Car car = carService.getCar(id);
		String urlSelf = getUriForSelf(id);
		car.addLink(urlSelf, "self");
		
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
	public Response updateCar(@PathParam("id") long id,
							Car car) {
		car.setId(id);
		Car carUpdated = carService.updateCar(car);
		
		String urlSelf = getUriForSelf(id);
		car.addLink(urlSelf, "self");
		
		return Response.ok()
					.entity(carUpdated)
					.build();
	}
	
	/**
	 * Method that removes a car from the database
	 * @param id Identifier of the car that should be removed
	 * @return Car removed
	 */
	@DELETE
	@Path("/{id}")
	public Response deleteCar(@PathParam("id") long id) {
		Car car = carService.removeCar(id);
		
		String urlSelf = getUriForSelf(id);
		car.addLink(urlSelf, "self");
		
		return Response.ok()
					.entity(car)
					.build();
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
}
