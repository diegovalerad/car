package org.diego.tutorial.car.jms;

import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;
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
	
	private final static Logger LOGGER = Logger.getLogger(JMSReceiver.class);
	
	/**
	 * Method that reads a message from the queue, and then call the appropiate method in the 
	 * {@link CarService} class
	 */
	@Override
	public void onMessage(Message message) {
		LOGGER.info("Received message: " + message);
		TextMessage textMessage = (TextMessage) message;
		try {
			String text = textMessage.getText();
			LOGGER.info("\n\nReceived text: " + text + "\n\n");
		} catch (JMSException e) {
			LOGGER.info("Exception retrieving message content: " + e.getMessage());
		}
	}

}
