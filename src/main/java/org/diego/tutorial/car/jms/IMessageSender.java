package org.diego.tutorial.car.jms;

import javax.ejb.Local;

import org.diego.tutorial.car.model.Car;

/**
 * Interface of the sending bean
 *
 */
@Local
public interface IMessageSender {
	/**
	 * Method that sends a message to the queue, to create a new car
	 * @param car Car that should be created
	 */
	public void sendCreateCar(Car car);
	
	/**
	 * Method that sends a message to the queue, to update a car
	 * @param car Car that should be updated
	 */
	public void sendUpdateCar(Car car);
	
	/**
	 * Method that sends a message to the queue, to remove a car
	 * @param car Car that should be removed
	 */
	public void sendRemoveCar(Car car);
}
