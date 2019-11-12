package org.diego.tutorial.car.jms;

import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.apache.log4j.Logger;
import org.diego.tutorial.car.model.Car;
import org.diego.tutorial.car.model.service.CarService;

/**
 * Message driven that it is invoked by the EJB
 * container when there is a new message in the queue
 *
 */
@MessageDriven(
		mappedName = "jms/carsQueue"
)
public class JMSReceiver implements MessageListener {
	@EJB
	private CarService carService;
	
	private final static Logger LOGGER = Logger.getLogger(JMSReceiver.class);
	
	/**
	 * Method that reads a message from the queue, and then call the appropriate method in the 
	 * {@link CarService} class
	 */
	@Override
	public void onMessage(Message message) {
		ObjectMessage objectMessage = (ObjectMessage) message;
		try {
			String operationString = objectMessage.getStringProperty(JMSGroups.GLOBAL.toString());
			JMSOperations operation = JMSOperations.valueOf(operationString); 
			Car car = (Car) objectMessage.getObject();
			
			LOGGER.info("Received message, Operation: " + operationString + ", Car: " + car);
			
			switch (operation) {
				case CREATE:
					carService.addCar(car);
					LOGGER.info("Car added: " + car);
					break;
				case UPDATE:
					carService.updateCar(car);
					LOGGER.info("Car updated: " + car);
					break;
				case DELETE:
					carService.removeCar(car.getId());
					LOGGER.info("Car removed: " + car);
					break;
			}
		} catch (JMSException e) {
			LOGGER.warn("Error in the receiver JMS: " + e.getMessage());
		}
	}

}
