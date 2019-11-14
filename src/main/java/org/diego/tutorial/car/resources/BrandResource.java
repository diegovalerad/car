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
import org.diego.tutorial.car.model.Brand;
import org.diego.tutorial.car.model.service.BrandService;
import org.diego.tutorial.car.validations.brand.BrandValidator;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

/**
 * Endpoint of our REST service, that provides all the necessary methods
 * regarding brands.
 *
 */
@Path("/brands")
@Consumes(value = { MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
@Produces(value = { MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
@Stateless
public class BrandResource {
	@EJB
	private BrandService brandService;
	private @Context UriInfo uriInfo;
	
	private final static Logger LOGGER = Logger.getLogger(BrandResource.class);
	
	/**
	 * Method that retrieves all the brands from the system. <p>
	 * Optionally, a company param can be provided, in order to
	 * retrieve all the cars from that company.
	 * @param company Company of the brands.
	 * @return List of brands
	 */
	@GET
	@Operation(summary = "Get all the brands",
			description = "Retrieves all the brands from the system",
			responses = {
					@ApiResponse(
							description = "All brands",
							responseCode = "200",
							content = @Content(
									array = @ArraySchema(schema = @Schema(implementation = Brand.class))
				            )
					),
			}
	)
	public Response getBrands(@QueryParam("company") String company) {
		List<Brand> brands = null;
		if (company != null && !company.isEmpty()) {
			LOGGER.info("Retrieving all the brands from the company '" + company + "'");
			brands = brandService.getAllBrandsFromCompany(company);
		}else {
			LOGGER.info("Retrieving all the brands");
			brands = brandService.getAllBrands();
		}
		LOGGER.info("All the brands retrieved");
		GenericEntity<List<Brand>> brandGeneric = new GenericEntity<List<Brand>>(brands) {};
		return Response.ok()
				.entity(brandGeneric)
				.build();
	}
	
	/**
	 * Created a new brand in the system
	 * @param brand Brand that should be created
	 * @return Created brand
	 */
	@POST
	@Operation(summary = "Create a brand",
			description = "Create a brand and store it in the system",
			responses = {
					@ApiResponse(
						description = "Brand created", 
						responseCode = "201",
						content = @Content(
								schema = @Schema(implementation = Brand.class)
						)
		            ),
					@ApiResponse(
						description ="Brand already exists",
						responseCode = "409"
					),
					@ApiResponse(
						description = "Trying to create a brand with non-valid fields",
						responseCode = "400"
					)
	})
	public Response addBrand(@Parameter(description = "new brand", required = true) Brand brand) {
		LOGGER.info("Trying to create a new brand: " + brand);
		
		List<String> validationErrors = BrandValidator.validateBrand(brand);
		String errorMessage = "Request to create a new brand with non-valid fields";
		checkValidationErrors(validationErrors, errorMessage);
		
		Brand addedBrand = brandService.addBrand(brand);
		LOGGER.info("Brand created: " + brand);
		
		URI uri = uriInfo.getAbsolutePathBuilder().path(addedBrand.getBrand()).build();
		
		String urlSelf = getUriForSelf(addedBrand.getBrand());
		addedBrand.addLink(urlSelf, "self");
		
		return Response.created(uri)
				.entity(brand)
				.build();
	}
	
	/**
	 * Gets a brand from the database
	 * @param brandName Name of the brand
	 * @return Brand
	 */
	@GET
	@Path("/{brandName}")
	@Operation(summary = "Get an existing brand",
			description = "Get a brand from the database given by its name",
			responses = {
					@ApiResponse(
						description = "Brand retrieved",
						responseCode = "200",
						content = @Content(
								schema = @Schema(implementation = Brand.class)
						)
					),
					@ApiResponse(
						description = "Brand does not exist",
						responseCode = "404"
					)
			}
	)			
	public Response getBrand(@Parameter(description = "name of the brand", required = true) 
								@PathParam("brandName") String brandName) {
		LOGGER.info("Trying to get the brand '" + brandName + "'");
		
		Brand brand = brandService.getBrand(brandName);
		LOGGER.info("Brand '" + brandName + "' retrieved");
		
		String urlSelf = getUriForSelf(brand.getBrand());
		brand.addLink(urlSelf, "self");
		
		return Response.ok()
				.entity(brand)
				.build();
	}
	
	/**
	 * Updates a brand in the database
	 * @param brandName Name of the brand that should be updated
	 * @param brand Brand with new data
	 * @return Updated brand
	 */
	@PUT
	@Path("/{brandName}")
	@Operation(summary = "Update a brand",
			description = "Update an existing brand",
			responses = {
					@ApiResponse(
						description = "Brand updated",
						responseCode = "200",
						content = @Content(
								schema = @Schema(implementation = Brand.class)
						)
					),
					@ApiResponse(
						description = "Brand does not exist",
						responseCode = "404"
					),
					@ApiResponse(
						description = "Trying to update a brand with non-valid fields",
						responseCode = "400"
					)
					
			}
	)
	public Response updateBrand(@Parameter(description = "Name of the brand that should be updated", required = true) 
									@PathParam("brandName") String brandName,
								@Parameter(description = "Brand that should be updated", required = true) 
									Brand brand){
		LOGGER.info("Trying to update the brand '" + brand + "'");
		
		List<String> validationErrors = BrandValidator.validateBrand(brand);
		String errorMessage = "Request to update a brand with non-valid fields";
		checkValidationErrors(validationErrors, errorMessage);		
		
		Brand updatedBrand = brandService.updateBrand(brandName, brand);
		LOGGER.info("Updated brand '" + brand + "'");
		
		String urlSelf = getUriForSelf(updatedBrand.getBrand());
		updatedBrand.addLink(urlSelf, "self");
		
		return Response.ok()
				.entity(brand)
				.build();
	}
	
	/**
	 * Removes a brand from the database
	 * @param brandName Name of the brand
	 * @return Response with the removed brand
	 */
	@DELETE
	@Path("/{brandName}")
	@Operation(summary = "Delete a brand",
			description = "Delete a brand from the database",
			responses = {
					@ApiResponse(
						description = "Brand removed",
						responseCode = "200",
						content = @Content(
								schema = @Schema(implementation = Brand.class)
						)
					),
					@ApiResponse(
						description = "Brand does not exist",
						responseCode = "404"
					)
			}
	)
	public Response removeBrand(@Parameter(description = "Name of the brand that should be removed", required = true)
									@PathParam("brandName") String brandName) {
		LOGGER.info("Trying to remove the brand '" + brandName + "'");
		
		Brand removedBrand = brandService.removeBrand(brandName);
		LOGGER.info("Removed brand '" + brandName + "'");
		
		String urlSelf = getUriForSelf(removedBrand.getBrand());
		removedBrand.addLink(urlSelf, "self");
		
		return Response.ok()
				.entity(removedBrand)
				.build();
	}
	
	/**
	 * Method that gets the URI of the brand with the given brand name.
	 * @param brandName Name of the brand
	 * @return String that contains the URI of the given brand. 
	 */
	private String getUriForSelf(String brandName) {
		String urlSelf = uriInfo.getBaseUriBuilder()
				.path(BrandResource.class)
				.path(brandName)
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
		if (!validationErrors.isEmpty()) {
			String message = errorMessage + ": ";
			for (String validationError : validationErrors) {
				message += validationError + " - ";
			}
			throw new BadRequestException(message);
		}
	}
	
}
