package org.diego.tutorial.car.resources;

import java.net.URI;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.log4j.Logger;
import org.diego.tutorial.car.exceptions.BadRequestException;
import org.diego.tutorial.car.model.Country;
import org.diego.tutorial.car.model.service.CountryService;
import org.diego.tutorial.car.validations.GeneralValidationErrorsChecker;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

/**
 * Endpoint of our REST service, that provides all the necessary methods
 * regarding countries.
 *
 */
@Path("/countries")
@Consumes(value = { MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
@Produces(value = { MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
@Stateless
public class CountryResource {
	@EJB
	private CountryService countryService;
	
	private @Context UriInfo uriInfo;
	
	private final static Logger LOGGER = Logger.getLogger(CountryResource.class);
	
	/**
	 * Retrieves all the countries from the system
	 * @return List of countries
	 */
	@GET
	@Operation(summary = "Get all the countries",
				description = "Retrieves all the countries from the system",
				responses = {
					@ApiResponse(
							description = "Countries retrieved",
							responseCode = "200",
							content = @Content(
									array = @ArraySchema(
											schema = @Schema(implementation = Country.class)
									)
				            )
					),
				}
	)
	public Response getCountries() {
		LOGGER.info("Trying to retrieve all the countries from the system");
		List<Country> countries = countryService.getAllCountries();
		LOGGER.info("All the countries have been retrieved");
		
		GenericEntity<List<Country>> countriesGeneric = new GenericEntity<List<Country>>(countries) {};
		return Response.ok()
					.entity(countriesGeneric)
					.build();
	}
	
	@POST
	@Operation(summary = "Tries to add a country",
				description = "Adds a country to the system",
				responses = {
					@ApiResponse(
							description = "Country added",
							responseCode = "201",
							content = @Content(
									array = @ArraySchema(
											schema = @Schema(implementation = Country.class)
									)
				            )
					),
					@ApiResponse(
							description = "Trying to add a country with invalid fields",
							responseCode = "400"
					),
				}
	)
	public Response addCountry(@Parameter(name = "country", description = "country that should be added", required = true)
								Country country) {
		LOGGER.info("Trying to add the country: " + country);
		GeneralValidationErrorsChecker.checkValidationErrors(country, "create");
		LOGGER.info("Validation errors passed");
		
		Country addedCountry = countryService.addCountry(country);
		LOGGER.info("Country added: " + addedCountry);
		
		String newId = String.valueOf(addedCountry.getId());
		URI uri = uriInfo.getAbsolutePathBuilder()
					.path(newId)
					.build();
		
		String urlSelf = getUriForSelf(addedCountry.getId());
		addedCountry.addLink(urlSelf, "self");
		
		return Response.created(uri)
					.entity(addedCountry)
					.build();
	}
	
	@GET
	@Path("/{countryId}")
	@Operation(summary = "Tries to get a country",
				description = "Gets a country from the system",
				responses = {
					@ApiResponse(
							description = "Country retrieved",
							responseCode = "200",
							content = @Content(
									array = @ArraySchema(
											schema = @Schema(implementation = Country.class)
									)
				            )
					),
					@ApiResponse(
							description = "Country not found",
							responseCode = "404"
					),
					@ApiResponse(
							description = "Non valid ID",
							responseCode = "400"
							)
				}
	)
	public Response getCountry(@Parameter(name = "country", description = "ID of the country that should be retrieved", required = true)
								@PathParam("countryId") long countryId) {
		LOGGER.info("Trying to get the country with ID: " + countryId);
		if (countryId <= 0) {
			String message = "The ID of the country cannot be 0 or less";
			LOGGER.info(message);
			throw new BadRequestException(message);
		}
		
		Country country = countryService.getCountry(countryId);
		LOGGER.info("Country with ID '" + countryId + "' retrieved: " + country);
		
		String urlSelf = getUriForSelf(countryId);
		country.addLink(urlSelf, "self");
		
		return Response.ok()
				.entity(country)
				.build();
	}
	
	@PUT
	@Path("/{countryId}")
	@Operation(summary = "Tries to update a country",
				description = "Updates a country from the system",
				responses = {
					@ApiResponse(
							description = "Country updated",
							responseCode = "200",
							content = @Content(
									array = @ArraySchema(
											schema = @Schema(implementation = Country.class)
									)
				            )
					),
					@ApiResponse(
							description = "Country not found",
							responseCode = "404"
					),
					@ApiResponse(
							description = "Non valid fields",
							responseCode = "400"
							)
				}
	)
	public Response updateCountry(@Parameter(name = "countryId", description = "Identifier of the country")
									@PathParam("countryId") long countryId,
								@Parameter(name = "country", description = "Country with the updated information")
									Country country) {
		LOGGER.info("Trying to update the country with ID: " + countryId + " with the new information: " + country);
		GeneralValidationErrorsChecker.checkValidationErrors(country, "update");
		LOGGER.info("Validations passed");
		
		country.setId(countryId);
		Country updatedCountry = countryService.updateCountry(country);
		LOGGER.info("Country updated: " + updatedCountry);
		
		String urlSelf = getUriForSelf(countryId);
		updatedCountry.addLink(urlSelf, "self");
		
		return Response.ok()
				.entity(updatedCountry)
				.build();
	}
	
	/**
	 * Method that gets the URI of the car with the given ID.
	 * @param id Identifier of the car
	 * @return String that contains the URI of the given car. 
	 */
	private String getUriForSelf(long id) {
		String urlSelf = uriInfo.getBaseUriBuilder()
				.path(CountryResource.class)
				.path(String.valueOf(id))
				.build()
				.toString();
		return urlSelf;
	}
	
}
