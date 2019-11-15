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
import org.diego.tutorial.car.model.Brand;
import org.diego.tutorial.car.model.service.BrandService;
import org.diego.tutorial.car.validations.GeneralValidationErrorsChecker;

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
	@Operation(summary = "Gets all the brands",
			description = "Retrieves all the brands from the system",
			responses = {
					@ApiResponse(
							description = "All brands retrieved",
							responseCode = "200",
							content = @Content(
									array = @ArraySchema(
												schema = @Schema(implementation = Brand.class)
											)
				            )
					),
			}
	)
	public Response getBrands(@Parameter(name = "company", description = "Company of which the brands are part", required = true) 
								@QueryParam("company") String company) {
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
	@Operation(summary = "Creates a brand",
			description = "Creates a brand and store it in the system",
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
	public Response addBrand(@Parameter(name = "brand", description = "new brand", required = true) 
								Brand brand) {
		LOGGER.info("Trying to create a new brand: " + brand);
		
		GeneralValidationErrorsChecker.checkValidationErrors(brand, "create");
		LOGGER.info("Validation errors passed");
		
		Brand addedBrand = brandService.addBrand(brand);
		LOGGER.info("Brand created: " + brand);
		
		URI uri = uriInfo.getAbsolutePathBuilder()
					.path(String.valueOf(addedBrand.getId()))
					.build();
		
		String urlSelf = getUriForSelf(String.valueOf(addedBrand.getId()));
		addedBrand.addLink(urlSelf, "self");
		
		return Response.created(uri)
				.entity(addedBrand)
				.build();
	}
	
	/**
	 * Gets a brand from the database
	 * @param brandName Name of the brand
	 * @return Brand
	 */
	@GET
	@Path("/{brandId}")
	@Operation(summary = "Get an existing brand",
			description = "Get a brand from the database given by its ID",
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
								@PathParam("brandId") long brandId) {
		LOGGER.info("Trying to get the brand with ID " + brandId);
		
		Brand brand = brandService.getBrand(brandId);
		LOGGER.info("Brand with ID " + brandId + " retrieved");
		
		String urlSelf = getUriForSelf(String.valueOf(brand.getId()));
		brand.addLink(urlSelf, "self");
		
		return Response.ok()
				.entity(brand)
				.build();
	}
	
	/**
	 * Updates a brand in the database
	 * @param brandId ID of the brand that should be updated
	 * @param brand Brand with new data
	 * @return Updated brand
	 */
	@PUT
	@Path("/{brandId}")
	@Operation(summary = "Updates a brand",
			description = "Updates an existing brand",
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
	public Response updateBrand(@Parameter(description = "ID of the brand that should be updated", required = true) 
									@PathParam("brandId") long brandId,
								@Parameter(description = "Brand that should be updated", required = true) 
									Brand brand){
		LOGGER.info("Trying to update the brand with ID '" + brandId + "' with the info: " + brand);
		
		GeneralValidationErrorsChecker.checkValidationErrors(brand, "update");	
		
		brand.setId(brandId);
		Brand updatedBrand = brandService.updateBrand(brand);
		LOGGER.info("Updated brand '" + brand + "'");
		
		String urlSelf = getUriForSelf(String.valueOf(updatedBrand.getId()));
		updatedBrand.addLink(urlSelf, "self");
		
		return Response.ok()
				.entity(updatedBrand)
				.build();
	}
	
	/**
	 * Removes a brand from the database
	 * @param brandId ID of the brand
	 * @return Response with the removed brand
	 */
	@DELETE
	@Path("/{brandId}")
	@Operation(summary = "Deletes a brand",
			description = "Deletes a brand from the database",
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
					),
					@ApiResponse(
						description = "Brand has other objects using it",
						responseCode = "400"
					)
			}
	)
	public Response removeBrand(@Parameter(description = "ID of the brand that should be removed", required = true)
									@PathParam("brandId") long brandId) {
		LOGGER.info("Trying to remove the brand with ID " + brandId);
		
		Brand removedBrand = brandService.removeBrand(brandId);
		LOGGER.info("Removed brand with ID " + brandId);
		
		String urlSelf = getUriForSelf(String.valueOf(removedBrand.getId()));
		removedBrand.addLink(urlSelf, "self");
		
		return Response.ok()
				.entity(removedBrand)
				.build();
	}
	
	/**
	 * Method that gets the URI of the brand with the given brand ID.
	 * @param brandName ID of the brand
	 * @return String that contains the URI of the given brand. 
	 */
	private String getUriForSelf(String brandId) {
		// TODO refactor method
		String urlSelf = uriInfo.getBaseUriBuilder()
				.path(BrandResource.class)
				.path(brandId)
				.build()
				.toString();
		return urlSelf;
	}
	
}
