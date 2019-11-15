package org.diego.tutorial.car.client;

import java.util.Date;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.diego.tutorial.car.model.Brand;
import org.diego.tutorial.car.model.Car;

public class RestApiClient {

	public static void main(String[] args) {
		Client client = ClientBuilder.newClient();

		WebTarget baseTarget = client.target("http://localhost:8080/car/webapi/");
		WebTarget carsTarget = baseTarget.path("cars");
		WebTarget singleCarTarget = carsTarget.path("{messageId}");

		getCar(singleCarTarget, 1);

		Brand brand = new Brand("brand", "company");
		
		Car carPost = new Car(4, brand, new Date(), "countryClient", new Date(), new Date());
		addCar(carsTarget, carPost);
		
		getCarsByCountry(carsTarget, "countryClient");
	}
	
	public static void getCarsByCountry(WebTarget target, String country) {
		List<Car> carsByCountry = target
						.queryParam("country", country)
						.request(MediaType.APPLICATION_JSON)
						.get(new GenericType<List<Car>>() { });
		System.out.println(carsByCountry);
	}

	public static void getCar(WebTarget target, int id) {
		Car car1 = target
				.resolveTemplate("messageId", id)
				.request(MediaType.APPLICATION_JSON)
				.get(Car.class);

		System.out.println(car1.getCreatedAt());
	}

	public static void addCar(WebTarget target, Car car) {
		Response postResponse = target.request().post(Entity.json(car));

		if (postResponse.getStatus() != Status.CREATED.getStatusCode()) {
			System.out.println("car not created");
		} else {
			Car createdCar = postResponse.readEntity(Car.class);
			System.out.println("Car with ID: " + createdCar.getId());
		}
	}

}
